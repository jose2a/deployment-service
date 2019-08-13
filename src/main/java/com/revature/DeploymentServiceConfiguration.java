package com.revature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Creates the necessary beans for this application.
 * 
 * @author Java, JUN 19 - USF
 *
 */
@Configuration
public class DeploymentServiceConfiguration {
	
	/**
	 * Instantiating S3 client.
	 * @return s3 client.
	 */
	@Bean
	public S3Client s3Client() {
		return S3Client.builder().build();
	}
	
	/**
	 * Instantiating EC2 client.
	 * @return ec2 client.
	 */
	@Bean
	public Ec2Client ec2Client() {
		return Ec2Client.create();
	}

}
