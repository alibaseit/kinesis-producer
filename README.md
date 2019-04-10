# kinesis-producer
AWS Kinesis Producer App

The application is a Spring boot MVC application. 
It has only one end point which post json data to Kinesis
```
URI: /uploadData
Request Method: POST
Content Type: Application/json
```

Sample Data
```json
{
"userId":  "user-1",
"action":  "START",
"email": "user-1@test.test"
}
```

