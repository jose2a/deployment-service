package com.revature.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Service to store files in an S3 bucket.
 * 
 * @author Java, JUN 19 - USF
 *
 */
@Component
public class S3FileStorageServiceImpl implements S3FileStorageService {

	private String bucketName; // S3 bucket name
	
	private String awsRegion; // AWS region
	
	private String s3EndPoint; // S3 end point URL

	private S3Client s3Client; // S3 client

	@Autowired
	public void setS3Client(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	@Value("${aws.config.bucket-name}")
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	@Value("${aws.config.region}")
	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	@Value("${aws.config.s3-endpoint}")
	public void setS3EndPoint(String s3EndPoint) {
		this.s3EndPoint = s3EndPoint;
	}

	/**
	 * Code based from: 
	 * http://jamesabrannan.com/2019/04/19/amazons-aws-s3-java-api-2-0-using-spring-boot-as-client/
	 */
	@Override
	public String storeFile(File file) {
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(file.getName())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();

		s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

		// Generate file URL in the S3 bucket
		return "https://" + bucketName + ".s3." + awsRegion + "."+ s3EndPoint + "/" + file.getName();
	}

}
