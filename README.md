# spark-example
This is an example of integrating a simple Spark application with google protobuff inside a Docker container.

### Quick Start:
 - Clone this git repo.

    `git clone https://github.com/daesu/spark-example`

 - Run shell script cmd.

    `./spark_run.sh`

    Shall script launches docker-compose and pulls image from docker hub.

 - Hit the /users endpoint with a POST. 

    `curl -X POST http://localhost:4567/users -d '{"id": 1, "name": "adam"}'`

 - Hit the /users endpoint with a GET. 

    `curl -X GET http://localhost:4567/users`

### Build:
Building locally requires gradle. Gradle 4.4 is used in the Dockerfile. 

 - Build 
 
    `gradle build`

    Tests will also be run.

 - Run. 
 
    `gradle run`

 - Run tests.
 
    `gradle test`

### Makefile:
A Makefile is included in this repo for convenience. 

 - Pull all dependencies including gradle, google protbuf binary and run tests. 
 
    `make docker`

 - Run docker image. 
 
    `make docker-run`

 - Run tests 
 
    `make test`

 - Launch docker-compose and pull image from docker hub repo.
 
    `make run`

### google protobuff:
Docker will automatically download and run the protobuf compiler against the `app.user.user.proto` file when building the docker image. 

The google protobuff compiler is required if making changes to the user protobuff object and developing locally. 

On *nix systems it is likely to be available in your package management software. 

Else, it can be obtained from `https://github.com/protocolbuffers/protobuf` 

