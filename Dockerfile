FROM openjdk:8
LABEL D@v3_R
COPY ./out/production/HelloWorld/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","HelloWorld"]