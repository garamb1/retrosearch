FROM eclipse-temurin:17
MAINTAINER garambo.it
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} retrosearch.jar
ENTRYPOINT ["java","-jar","/retrosearch.jar"]