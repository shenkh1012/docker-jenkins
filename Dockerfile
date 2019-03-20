FROM openjdk:8u171-jre-alpine
VOLUME /tmp
ADD target/ks-jenkins-docker-0.1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.war"]
