FROM eclipse-temurin:17
MAINTAINER garambo.it
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} retrosearch.jar
ENTRYPOINT ["java","-jar","/retrosearch.jar"]
HEALTHCHECK --interval=30s --timeout=500ms --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1
