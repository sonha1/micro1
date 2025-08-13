FROM registry.gtelsoft.vn/library/java/eclipse-temurin:21-jre-alpine
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
COPY target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]


