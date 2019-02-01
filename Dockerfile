FROM openjdk:8-jdk-alpine

ENV GRADLE_HOME /opt/gradle
ENV GRADLE_VERSION 4.4

RUN set -o errexit -o nounset \
    && echo "Downloading Gradle" \
    && wget -qO gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && echo "Installing Gradle" \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mkdir /opt \
    && mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/" \
    && ln -s "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle \
    && echo "Symlinking root Gradle cache to gradle Gradle cache" \
    && ln -s /home/gradle/.gradle /root/.gradle

# Create Gradle volume
VOLUME "/home/gradle/.gradle"
WORKDIR /home/gradle

RUN set -o errexit -o nounset \
    && echo "Testing Gradle installation" \
&& gradle --version

WORKDIR /app
COPY build.gradle /app/

# Install protobuf binary
RUN echo "Installing protobuf" 
RUN ["apk", "add", "protobuf=3.5.2-r0"]

# Adding source
ADD src /app/src

# Generate java file from protos definition
RUN echo "Generate java file from protos definition" 
RUN ["protoc", "-I=src/", "--java_out=src/main/java/", "src/main/java/app/user/user.proto"]

# run tests
RUN ["gradle", "test"]

# compile and package into a fat jar
RUN ["gradle", "fatJar"]

EXPOSE 4567 4567
CMD ["java", "-jar", "build/libs/app-all.jar"]