#PLAN-GENERATOR

##Stack
* Java 8
* Spring boot
* Open-api/Swagger
* Maven

##How to run
To run the application you can simply use the maven spring-boot plugin: `mvn spring-boot:run`

If you want to deploy in your onw environment, you can generate a runnable java with the command: `mvn clean package`.

The generated jar is under `target` folder, and it's named `plan-generator-0.0.1-SNAPSHOT.jar`, to run the executable jar use the command `java -jar plan-generator-0.0.1-SNAPSHOT.jar`

##Running the tests
To run the tests the command is `mvn test`, note that this command will not run the integration-test.

To run the integration test the command is `mvn failsafe:integration-test`, the reason they run separately is that usually integration-tests takes more time to run, and you would not want them to run every build.

##Endpoints
The endpoint exposed by this service is `http://localhost:8080/generate-plan`

Also, a swagger interface can be found in `http://localhost:8080/swagger-ui.html` you can use to test the endpoint via browser;

The endpoint only accepts POST requests, and the payload expected is following:
```
{
  "loanAmount": "5000",
  "nominalRate": "5",
  "duration": 24,
  "startDate": "2020-01-01T00:00:01Z"
}
```

It returns a list with the payment plan for the duration sent in the payload, and the status code `201 Created`:
```
[
    {
        "date":"2020-01-01T00:00:01Z",
        "borrowerPaymentAmount":"219.36",
        "principal":"198.53",
        "interest":"20.83",
        "initialOutstandingPrincipal":"5000",
        "remainingOutstandingPrincipal":"4801.47"
    },
    {
        "date":"2020-02-01T00:00:01Z",
        "borrowerPaymentAmount":"219.36",
        "principal":"199.35",
        "interest":"20.01",
        "initialOutstandingPrincipal":"4801.47",
        "remainingOutstandingPrincipal":"4602.12"
    },
    ...
    {
        "date":"2021-12-01T00:00:01Z",
        "borrowerPaymentAmount":"219.28",
        "principal":"218.37",
        "interest":"0.91",
        "initialOutstandingPrincipal":"218.37",
        "remainingOutstandingPrincipal":"0"
    }
]
```

In case you send a payload that is valid, but it's not possible to do the calculations with, the endpoint will answer with a status code `204 No Content`

You can find the API documentation accessing: `http://localhost:8080/v3/api-docs/`. The documentation follows the Open API standards.