FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre

ARG APP_VERSION=dev
ENV APP_VERSION=${APP_VERSION}

WORKDIR /deployments

COPY --from=build /app/target/quarkus-app/lib/     lib/
COPY --from=build /app/target/quarkus-app/*.jar    ./
COPY --from=build /app/target/quarkus-app/app/     app/
COPY --from=build /app/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=${PORT:-8080} -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar quarkus-run.jar"]
