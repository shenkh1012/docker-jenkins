FROM openjdk:8u171-jre-alpine
VOLUME /tmp
ADD target/docker-jenkins-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
