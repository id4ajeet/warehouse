FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/warehouse-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN mkdir -p /inventory

ENTRYPOINT ["java", "-jar", "app.jar"]