# --stage: build
FROM gradle:8-jdk17-alpine AS build

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

# --stage: package
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/mycheque-1.0.0-beta.jar mycheque.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "mycheque.jar"]
