FROM java:8
VOLUME /tmp
ADD edge-svc-*.jar edge-svc.jar
RUN bash -c 'touch /edge-svc.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/edge-svc.jar"]
EXPOSE 8080