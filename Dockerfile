FROM ubuntu as build

RUN chmod +x /usr

RUN mkdir /usr/lamarrulla

WORKDIR /usr/lamarrulla

RUN apt-get update

RUN apt-get install -y git

RUN git clone https://github.com/angelnmara/appEngineLaMarrullaWS.git

RUN apt-get install -y maven

WORKDIR /usr/lamarrulla/appEngineLaMarrullaWS/

RUN mvn clean

RUN mvn install

WORKDIR /usr/lamarrulla/appEngineLaMarrullaWS/target

FROM tomcat:8.0-alpine

COPY --from=build "/usr/lamarrulla/appEngineLaMarrullaWS/target/com.lamarrulla.ws-0.1.0-SNAPSHOT.war" "/usr/local/tomcat/webapps/com.lamarrulla.ws-0.1.0-SNAPSHOT.war"

RUN ls /usr/local/tomcat/bin/

RUN chmod +x /usr/local/tomcat/webapps/com.lamarrulla.ws-0.1.0-SNAPSHOT.war

EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

#RUN /usr/local/tomcat/bin/catalina-sh start