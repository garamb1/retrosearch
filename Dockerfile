FROM eclipse-temurin:17
MAINTAINER garambo.it
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} retrosearch.jar
ENTRYPOINT ["java","-jar","/retrosearch.jar"]
HEALTHCHECK --interval=30s --timeout=1s \
  CMD curl -f http://localhost:3000/actuator/health || exit 1
