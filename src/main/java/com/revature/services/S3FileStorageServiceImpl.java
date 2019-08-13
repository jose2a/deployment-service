package com.revature.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3FileStorageServiceImpl implements S3FileStorageService {

	@Value("${aws.config.bucket-name}")
	private String bucketName;
	
	@Value("${aws.config.s3-endpoint}")
	private String s3EndPoint;

	private S3Client s3Client; // S3 client
	
	private final String s3Url = "https://" + bucketName + "." + s3EndPoint + "/";

	@Autowired
	public void setS3Client(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public String storeFile(File file) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(file.getName())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();

		s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

		return s3Url + file.getName();
	}

}
