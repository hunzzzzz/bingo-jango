FROM ubuntu:22.04

LABEL maintainer="bingo-jango"
LABEL version="1.0.0"
LABEL description="docker for bingo-jango back-end server"

ARG JAR_FILE=build/libs/bingo-jango.jar

COPY ${JAR_FILE} ./bingo-jango.jar

ENV TZ=Asia/Seoul

RUN apt-get update && apt-get install -y --no-install-recommends \
    vim \
    apt-utils \
    && rm -rf /var/lib/apt/lists/*
RUN apt-get update && apt-get install -y bash
RUN apt-get update && apt-get install -y curl
RUN apt-get update && apt-get install -y sudo

RUN apt-get update && apt-get install -y redis
RUN apt-get update && apt-get install -y openjdk-17-jdk

ENTRYPOINT ["sudo", "java", "-jar", "bingo-jango.jar"]