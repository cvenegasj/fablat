FROM openjdk:8-jre-alpine
ADD target/fablat-resource.jar fablat-resource.jar
ENV JAVA_OPTS=""
EXPOSE 9000
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fablat-resource.jar"]
