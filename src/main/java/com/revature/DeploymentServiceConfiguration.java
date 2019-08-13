package com.revature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class DeploymentServiceConfiguration {
	
	@Bean
	public S3Client s3Client() {
		return S3Client.builder().build();
	}
	
	@Bean
	public Ec2Client ec2Client() {
		return Ec2Client.create();
	}

}
