
# --- building the jar using with a staged build ---
#FROM gradle:4.0.1-jdk8-alpine AS gradleContainer
#USER root
#COPY $HOME/.gradle /home/gradle/.gradle
##ENV GRADLE_USER_HOME /home/gradle/.gradle
#ADD . /app
#WORKDIR /app
#RUN gradle clean build --stacktrace

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/arkham-api-0.1.0.jar app.jar
# the jar versions should be externalized

ENV JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"
ENTRYPOINT [ "sh", "-c", "java $JAVA_DEBUG_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

#We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other Spring Boot applications if they need to actually write in the filesystem.
#To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy.
#(from https://spring.io/guides/gs/spring-boot-docker)
