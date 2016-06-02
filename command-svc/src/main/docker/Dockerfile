FROM java:8
VOLUME /tmp
ADD command-svc-*.jar command-svc.jar
RUN bash -c 'touch /command-svc.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/command-svc.jar"]
EXPOSE 8080