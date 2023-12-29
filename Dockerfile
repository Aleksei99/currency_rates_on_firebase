# Use a Java runtime as a parent image
FROM eclipse-temurin:17

# Set the working directory to /app
WORKDIR /app

LABEL maintainer="Smuraha Aleksei"
LABEL version="1.0"
LABEL description="Telegram bot"

# Copy the packaged jar file into the container
COPY target/tg_bot.jar /app
# Set timizone
ENV TZ=Europe/Minsk
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Run the command to start the application
CMD ["java", "-jar", "tg_bot.jar"]