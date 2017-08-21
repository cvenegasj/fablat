FROM openjdk:8-jre-alpine
ADD target/fablat-ui.jar fablat-ui.jar
ENV JAVA_OPTS=""
EXPOSE 80
EXPOSE 443
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fablat-ui.jar"]
