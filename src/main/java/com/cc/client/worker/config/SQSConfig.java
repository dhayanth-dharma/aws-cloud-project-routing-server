package com.cc.client.worker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class SQSConfig {
	    @Value("${cloud.aws.region.static}")
	    private String region;
	    @Value("${aws.accessKey}")
	    private String awsAccessKey;
	    @Value("${aws.secretKey}")
	    private String awsSecretKey;
	    @Value("${aws.sessionToken}")
	    private String sessionToken ;
	    @Bean
	    public QueueMessagingTemplate queueMessagingTemplate() {
	        return new QueueMessagingTemplate(amazonSQSAsync());
	    }
	    @Primary
	    @Bean
	    public AmazonSQSAsync amazonSQSAsync() {
	     AWSSessionCredentials credentials = new BasicSessionCredentials(awsAccessKey, awsSecretKey, sessionToken);
	      return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1)
	              .withCredentials(new AWSStaticCredentialsProvider(credentials))
	              .build();
	    }
	   
}
