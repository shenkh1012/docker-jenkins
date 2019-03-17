FROM openjdk:8u171-jre-alpine
RUN apk --no-cache add curl
CMD java ${JAVA_OPTS} -jar docker-jenkins-0.0.1-SNAPSHOT.jar
COPY target/docker-jenkins-0.0.1-SNAPSHOT.jar .