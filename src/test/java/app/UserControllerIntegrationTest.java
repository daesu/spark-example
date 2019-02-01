package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.Assert.*;
import static spark.Spark.awaitInitialization;

import org.junit.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.user.PojoUser;
import spark.Spark;
import spark.utils.IOUtils;

/***
 *  Basic Tests for UserController
 */
public class UserControllerIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		Application.main(null);
		awaitInitialization();
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Test
	public void aNewUserShouldBeCreated() {
		HttpURLConnection connection = getConnection("POST", "/users");

		String postBody = "{\"id\": 4,\"name\": \"jon\"}";
		TestResponse res = postRequest(connection, postBody);

		// Retrieve result from POST and try to parse to User POJO
		Gson gson = new GsonBuilder().create();
		PojoUser pjUser = gson.fromJson(res.body, PojoUser.class);
		assertEquals(200, res.status);
		assertEquals("jon", pjUser.getName());
		assertEquals(4, pjUser.getId());
	}

	@Test
	public void aNewUserShouldNotBeCreatedEmptyBody() {
		HttpURLConnection connection = getConnection("POST", "/users");
		String postBody = "";
		TestResponse res = postRequest(connection, postBody);
		assertEquals(400, res.status);
	}

	@Test
	public void aNewUserShouldNotBeCreatedInvalidBody() {
		HttpURLConnection connection = getConnection("POST", "/users");
		String postBody = "{\"name\": \"jon\"}";
		TestResponse res = postRequest(connection, postBody);
		assertEquals(400, res.status);
	}

	@Test
	public void canGetUserList() {
		HttpURLConnection connection = getConnection("GET", "/users");
		TestResponse res = getRequest(connection);
		assertEquals(200, res.status);

		// Retrieve result from GET and try to parse to []User POJO
		Gson gson = new GsonBuilder().create();
		PojoUser[] pjUsers = gson.fromJson(res.body, PojoUser[].class);

		assertEquals(200, res.status);
		assertTrue(pjUsers.length > 0);
	}

	// Method to accept a HttpURLConnection, json formatted string and make post request
	private TestResponse postRequest(HttpURLConnection connection, String postBody) {
		try {
			// Send json post body to create new user
			OutputStream os = connection.getOutputStream();
			os.write(postBody.getBytes());
			os.flush();

			String body;
			if (connection.getResponseCode() < 400) {
				body = IOUtils.toString(connection.getInputStream());
			} else {
				body = IOUtils.toString(connection.getErrorStream());
			}

			System.out.println("response from server");
			System.out.println(body);

			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	// Method to accept a HttpURLConnection and make get request
	private TestResponse getRequest(HttpURLConnection connection) {
		try {
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	private static class TestResponse {

		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}
	}

	/***
	 *
	 * @param method HTTP Method
	 * @param path Endpoint path
	 * @return A HttpURLConnection connection configured for test
	 */
	private HttpURLConnection getConnection(String method, String path){
		try {
			URL url = new URL(Config.TEST_HOST + ":" + Config.PORT + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			return connection;
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}
}