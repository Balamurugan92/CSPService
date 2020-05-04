** Customer Statement file Processing **

** Summary

  * Rabo bank receives customer statement records. This information is delivered in two formats, CSV and XML. 
  * These records need to be validated. based on below conditions
 
     * All transaction references should be unique
     * End balance needs to be validated 

** App Version 1.0

** 1. Clone the Repository
```bash
https://github.com/Balamurugan92/CSPService.git
```
** 2. Run the app using maven
```bash
cd CSPService
mvn spring-boot:run
```
** 3. The application can be accessed at `http://localhost:8080` in local browser.

** The application can also be accessed at `https://localhost:8080/swagger-ui.html`

** You can build the application to jar and deploy in server or cloud

```bash
mvn clean package
java -jar target/CSPService-.0.0.1-RELEASE.jar
```

** You can reach me @ `ruhanbala@gmail.com`