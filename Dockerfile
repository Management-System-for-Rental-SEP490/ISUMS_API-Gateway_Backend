FROM amazoncorretto:25-al2023-headless

WORKDIR /app

ARG XMX=512m
ENV JAVA_OPTS="-Xms256m -Xmx${XMX} -XX:+UseContainerSupport -Dsun.net.inetaddr.ttl=10 -Dsun.net.inetaddr.negative.ttl=0"

COPY build/libs/*.jar app.jar

RUN mkdir -p /app/logs

EXPOSE 8888

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
