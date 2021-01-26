FROM ubuntu

RUN chmod +x /usr

RUN mkdir /usr/lamarrulla

WORKDIR /usr/lamarrulla

RUN pwd

RUN apt-get update

RUN apt-get install -y git

RUN git clone https://github.com/angelnmara/appEngineLaMarrullaWS.git

RUN apt-get install -y maven

WORKDIR /usr/lamarrulla/appEngineLaMarrullaWS

RUN mvn clean

RUN mvn install

