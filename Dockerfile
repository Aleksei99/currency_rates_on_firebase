# Юзать это для билда, потом пушить на докер хаб и оттуда юзать
#FROM eclipse-temurin:17
#
#WORKDIR /app
#
#LABEL maintainer="Smuraha Aleksei"
#LABEL version="1.0"
#LABEL description="Telegram bot"
#
## Copy the packaged jar file into the container
#COPY target/tg_bot.jar /app
## Set timizone
#ENV TZ=Europe/Minsk
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#
## Run the command to start the application
#CMD ["java", "-jar", "tg_bot.jar"]
FROM alexiandr99/tg_bot_image
ENV TZ=Europe/Minsk
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone