FROM anapsix/alpine-java
#FROM alpine-java:base
#FROM openjdk:8-jdk-alpine
MAINTAINER maj.dagostino@gmail.com

#RUN mkdir /service
#RUN wget -c https://download.hazelcast.com/management-center/hazelcast-management-center-3.12.5.tar.gz -O - | tar -xz -C service
#RUN mv /service/hazelcast-management-center-3.12.5 /service/hazelcast-management-center
#RUN chmod +x /service/hazelcast-management-center/start.sh

#ARG BUILD_VERSION=SNAPSHOT
ARG SERVICE_PORT=8880
ARG METRICS_PORT=8881

ENV JAVA_OPTS "--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED ${JAVA_OPTS}"
ENV SERVICE_PORT=$SERVICE_PORT
ENV METRICS_PORT=$METRICS_PORT


COPY ./libs/amplifix-demo.jar /service/amplifix-demo.jar
COPY docker/amplifix-demo-entrypoint.sh /

#CMD ["bash", "/service/hazelcast-management-center/start.sh", "5700", "/mancenter"]
ENTRYPOINT ["/amplifix-demo-entrypoint.sh"]
#ENTRYPOINT ["java","-Xms1024m -Xmx2048m","-jar","/service/amplifix.jar"]