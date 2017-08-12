FROM openjdk:8-jre-alpine
ADD target/fablat-auth.jar fablat-auth.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "fablat-auth.jar"]
