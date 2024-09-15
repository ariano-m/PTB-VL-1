FROM --platform=linux/amd64 openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} token-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/token-0.0.1-SNAPSHOT.jar"]