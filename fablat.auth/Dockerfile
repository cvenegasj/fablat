FROM openjdk:8-jre-alpine
ADD target/fablat-auth.jar fablat-auth.jar
ENV JAVA_OPTS=""
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "fablat-auth.jar"]