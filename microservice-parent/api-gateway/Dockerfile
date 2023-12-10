# --------------
# Dockerfile for order-service
#---------------
# BUILD stage
#--

#base image
FROM gradle:8-jdk17-alpine AS builder

# Copy the application filess from the host machine to the image filesystem
COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory within the image, for executing future commands
WORKDIR /home/gradle/src

# Run gradle build within the container - NOT on the host machine
# for RUN command, the command only apply to images
# RUN gradle build ---no-daemon -x test -- this did NOT WORK
RUN gradle build -x test


#---------------
#package stage
#---------------
#base image
FROM openjdk:17-alpine

# Create directory within the container - NOT the host machine
# for RUN command, the command only apply to images
RUN mkdir /app

# Copy the application filess from the host machine to the image filesystem
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar

# Expose the port where the application will be listening on
# Expose does not make the ports of the container accessible to the host.
EXPOSE 8181

# Execute the following 1 command when the container is starts
ENTRYPOINT ["java","-jar","/app/api-gateway.jar"]