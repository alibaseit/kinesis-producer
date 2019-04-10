package com.ozru.kinesisproducer.kinesis;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.kinesis.producer.UserRecordFailedException;
import com.amazonaws.services.kinesis.producer.UserRecordResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ozru.kinesisproducer.config.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


@Component
public class DataProducer {
    @Autowired
    private AwsProperties awsProperties;

    private KinesisProducer kinesisProducer = null;

    private KinesisProducer getKinesisProducer() {
        if (kinesisProducer != null) {
            return kinesisProducer;
        }

        KinesisProducerConfiguration config = new KinesisProducerConfiguration();
        config.setRegion(awsProperties.getAwsRegion());
        BasicAWSCredentials credentials = 
            new BasicAWSCredentials(awsProperties.getAwsAccessKey(), awsProperties.getAwsSecretKey());

        config.setCredentialsProvider(new AWSStaticCredentialsProvider(credentials));
        config.setMaxConnections(1);
        config.setRequestTimeout(6000);
        config.setRecordMaxBufferedTime(5000);
        kinesisProducer = new KinesisProducer(config);
        return kinesisProducer;
    }


    public void putIntoKinesis(String partitionKey, String payload) 
        throws UnsupportedEncodingException, InterruptedException {
        
        kinesisProducer = getKinesisProducer();

        ByteBuffer data = ByteBuffer.wrap(payload.getBytes("UTF-8"));

        while (kinesisProducer.getOutstandingRecordsCount() > 1e4) {
            Thread.sleep(1);
        }

        ListenableFuture<UserRecordResult> f = 
            kinesisProducer.addUserRecord(awsProperties.getAwsStreamName(), partitionKey, data);

        Futures.addCallback(f, new FutureCallback<UserRecordResult>() {
            @Override
            public void onSuccess(UserRecordResult userRecordResult) {
                System.out.println("successful: " + userRecordResult);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.printf("failed");
                if (throwable instanceof UserRecordFailedException) {
                    UserRecordResult result = ((UserRecordFailedException) throwable).getResult();
                    System.out.println("Result: " + result.isSuccessful());
                }
            }
        });
    }


    public void stop() {
        if (kinesisProducer != null) {
            kinesisProducer.flushSync();
            kinesisProducer.destroy();
        }
    }

}
