package com.ozru.kinesisproducer.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsProperties {

    @Value("${aws.kinesis.stream.name}")
    private String awsStreamName;

    @Value("${aws.kinesis.region}")
    private String awsRegion;

    public String getAwsAccessKey() {
        return System.getenv("AWS_ACCESS_KEY");
    }

    public String getAwsSecretKey() {
        return System.getenv("AWS_SECRET_KEY");
    }

    public String getAwsStreamName() {
        return awsStreamName;
    }

    public void setAwsStreamName(String awsStreamName) {
        this.awsStreamName = awsStreamName;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

}
