package com.blizzard.d2ritemauction;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.util.StringUtils;

@Configuration
@EnableDynamoDBRepositories (basePackages = "com.baeldung.spring.data.dynamodb.repositories")
public class DynamoDBConfig {
	@Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
	public AmazonDynamoDB AmazonDynamoDB() {

		AmazonDynamoDB dynamodb = AmazonDynamoDBClientBuilder.standard()
				.withRegion("ap-northeast-2")
				.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
				.build();
		return dynamodb;
	}

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
          amazonAWSAccessKey, amazonAWSSecretKey);
    }
    
    @Bean
    public DynamoDB DynomaDB()
    {
    	AmazonDynamoDB dynamodb = AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
				 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.amazonDynamoDBEndpoint,"ap-northeast-2"))
				.build();
		return new DynamoDB(dynamodb);
    }
    
}
