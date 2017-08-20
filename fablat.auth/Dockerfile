FROM openjdk:8-jre-alpine
ADD target/fablat-auth.jar fablat-auth.jar
ENV JAVA_OPTS=""
EXPOSE 9999
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fablat-auth.jar"]
