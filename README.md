## Sample REST Api using Java, without any frameworks

### Prerequisites

* [Java](https://www.java.com/) 11
* [Maven](https://maven.apache.org/)

### Build

In order to build the project using Maven, on root folder:
```
mvn package
```

### Running

To run, just type on root folder:
```
mvn exec:java
```

The application will run indefinitely until a manual stop. 

### Endpoints
http://localhost:8000/post - This endpoint can be called several times per user and not return anything. 
The points will be added to the user’s score.

http://localhost:8000/{userId}/position - Retrieves the current position of a specific user, considering the score for all users. 
If a user hasn't submitted a score, the user will be added to the last position with 0 score.

http://localhost:8000/highscorelist - Retrieves the high scores list, in order, limited to the 20 higher scores. 
A request for a high score list without any scores submitted will return an empty list.

## Built With

* [Java](https://www.java.com/) - Programming language
* [Jackson](https://github.com/FasterXML/jackson) - JSON library for Java.
* [Lombok](https://projectlombok.org/) - Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.