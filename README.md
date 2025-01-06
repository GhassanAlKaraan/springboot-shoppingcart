# Spring Boot 3 Shopping Cart

Spring Boot, JPA, Lombok, Spring Security, JWT – Shopping Cart Backend Java Project, connected to MySql database, on Docker. Built a React app for frontend.

React App repository here: <https://github.com/ghassanalkaraan/react-shoppingcart>

## Working with Docker

Assuming you have Docker installed on your server:

0. First take a look at the Dockerfile, we're exposing port **9191**

1. Create a local network: `docker network create my-network`

2. Clean and build the project: `./gradlew clean build`, then create a Jar build for the app: `./gradlew clean bootJar`

3. Create and run a MySql container on the network on port 3306: `docker run --name mysql-container --network my-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=shoppingcart -p 3306:3306 -d mysql:8.0`

4. Use this mysql connection configs in application.properties \
**spring.datasource.url=jdbc:mysql://mysql-container:3306/shoppingcart** \
**spring.datasource.username=root** \
**spring.datasource.password=root**

5. Create the container for the app, in the root of the project: `docker build -t dreamshops .`

6. Run the container on port 9191: `docker run -d --name dreamshops-container --network my-network -p 9191:9191 dreamshops`

7. Verify both of the containers are up and connected: `Docker ps`

8. You're all set! Test 1 of the endpoints like this: `curl http://localhost:9191/api/v1/products/all`

## Important Note

1. Verify the db connection configs in application.properties
2. On first run set this to ```spring.jpa.hibernate.ddl-auto=create``` inside application.properties
3. Then set to ```spring.jpa.hibernate.ddl-auto=update``` when you already have ran the project and prepared the database, to avoid dropping the db every time you restart the project.
4. Have fun!
