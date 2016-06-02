FROM java:8
VOLUME /tmp
ADD state-svc-*.jar state-svc.jar
RUN bash -c 'touch /state-svc.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/state-svc.jar"]
EXPOSE 8080