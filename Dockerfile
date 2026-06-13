FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache \
    nasm \
    binutils \
    libc6-compat \
    maven

WORKDIR /workspace

CMD ["mvn", "test"]