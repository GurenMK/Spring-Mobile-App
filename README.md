Spring MVC mobile app using Restful Web Services

Spring boot - 2.1.5.RELEASE
MySQL - Ver 14.14 Distrib 5.7.26
Java - openjdk 11.0.3 2019-04-16
Uses Maven

UserDto and UserEntity fields must match
JSON payload fields must match fields in UserDetailsRequestModel
Spring security generates a password, User: user
user authentication url: /users/login
UserRest return user details object after it has been recorded in the database

Need to login to get a list of users, page 1 is the first page and page 0 = page 1

===================== Run with Maven =============================
mvn install (compile and run unit tests)
mvn spring-boot:run

===================== Run as Java Application ====================
nvm install
java -jar <file in target folder>.jar