Learning-navigator is a Spring Boot app for managing the user's learning. User can register in a subject before registering for exam in that subject.

Requirements:

For building and running the application you need:

JDK 21

Gradle

MySql

Running the application locally:

You can run the spring-boot application locally by using the gradle command:

./gradlew bootrun

Endpoints for accessing the application:

POST /subjects - For adding subjects

POST /students - For adding students

PUT /students/{studentId}/subjects/{subjectId} - For registering a student in a subject

PUT /students/{studentId}/subjects/{subjectId}/exams/{examId} - For registerin a registered student into the exam for the corresponding subject
