# use tomcat 11 base image with java 21
FROM tomcat:11-jdk21
# remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# copy the war file to the webapps directory and add a custom name with double context path

COPY target/*.war /usr/local/tomcat/webapps/container123.war

# expose port 8080
EXPOSE 8080

# set the entrypoint to start tomcat
CMD ["catalina.sh", "run"]