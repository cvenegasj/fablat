FROM openjdk:8-jdk-alpine
ADD target/fablat-ui.jar fablat-ui.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fablat-ui.jar"]
