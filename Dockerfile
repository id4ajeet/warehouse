FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /inventory

ENTRYPOINT ["java", "-jar", "app.jar"]