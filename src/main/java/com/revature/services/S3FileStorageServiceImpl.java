package com.revature.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3FileStorageServiceImpl implements S3FileStorageService {

	private String bucketName;
	
	private String awsRegion;
	
	private String s3EndPoint;

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

	@Override
	public String storeFile(File file) {
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(file.getName())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();

		s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

		return "https://" + bucketName + ".s3." + awsRegion + "."+ s3EndPoint + "/" + file.getName();
	}

}
