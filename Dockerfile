FROM tomcat:8.0-alpine

LABEL maintainer="angelnmara@hotmail.com"

EXPOSE 8080

CMD ls /

CMD [“catalina.sh”, “run”]