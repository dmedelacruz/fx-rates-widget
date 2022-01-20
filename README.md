# FX Rates Widget

##author
```
Dan Michael Dela Cruz (Java Developer)
```

##Tech Used
```
1. Java 11
2. Spring Boot Framework
3. Spring Web Client for REST API calls
4. https://exchangeratesapi.io/ for rates api
5. junit5 for unit test
6. swagger for documentation
7. jacoco for code coverage
```

##Packaging, Building and Running
```
1. clone the repository
2. go to the directory and build using 
     for windows: gradlew.bat clean build
     for linux: gradlew clean build
3. run the application using
     java -jar build/libs/fx-rates-widget-0.0.1-SNAPSHOT.jar
   note that he application uses port 8080
```

##Usage
```
Visit the swagger docs at http://localhost:8080/api-docs.html

For exchangeRate:
Scenario: I want to know how much is 1 USD in PHP
base = USD
target = PHP

result will be amount in PHP (which is the target param)

===

For Selling:
Scenario: I'm Im selling 100 USD for PHP
amount = 100
selling_currency = USD
target = PHP

result will be amount in PHP (which is the target param)

===

For Buying:
Scenario: Im buying USD using 100 PHP
amount = 100
buying_currency = USD
base = PHP

result will be amount in USD (which is the buying_currency param)

```

##Unit Testing
```
1. run unit tests using 
     for windows: gradlew.bat clean test
     for linux: gradlew clean test
2. you can see the code coverage report on 
     build/reports/jacoco/test/html/index.html
```