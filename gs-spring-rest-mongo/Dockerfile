FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/arkham-api-0.1.0.jar app.jar
# the jar versions should be externalized

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

#Resource : https://spring.io/guides/gs/spring-boot-docker/
#We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other Spring Boot applications if they need to actually write in the filesystem.
#To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy.


# https://springframework.guru/running-spring-boot-in-a-docker-container/
# You can also specify an external properties file. I usually just override env specfic properties via environment variables – which will override values of application.properties.
