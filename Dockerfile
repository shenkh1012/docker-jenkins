FROM openjdk:8u171-jre-alpine
VOLUME /tmp
ADD target/docker-jenkins-0.0.1-SNAPSHOT.jar app.war
RUN sh -c 'touch /app.war'
ENTRYPOINT ["java","-jar","/app.war"]
