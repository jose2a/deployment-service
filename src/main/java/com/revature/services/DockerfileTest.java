package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.ConnectionVariables;
import com.revature.models.EnvironmentVariable;

import software.amazon.awssdk.services.s3.S3Client;

public class DockerfileTest {

	public static void main(String[] args) throws IOException {
		DockerfileService dockerServ = new DockerfileServiceImpl();
		S3FileStorageServiceImpl s3FileStorageServ = new S3FileStorageServiceImpl();
		s3FileStorageServ.setS3Client(S3Client.builder().build());
		s3FileStorageServ.setS3EndPoint("amazonaws.com");
		s3FileStorageServ.setBucketName("revature-jose-test-bucket");
		s3FileStorageServ.setAwsRegion("us-east-2");
		
		ConnectionVariables conVariables = new ConnectionVariables(
				"TRMS_URL",
				"TRMS_USER",
				"TRMS_PASS"
				);
		
		List<EnvironmentVariable> envVars = new ArrayList<>();
		envVars.add(new EnvironmentVariable("AWS_REGION", "east-us-2"));
		envVars.add(new EnvironmentVariable("JWT_TOKEN", "local-deploy"));
		
		
		File generateTomcatServerDockerfile = dockerServ.generateTomcatServerDockerfile("sdsdds", "https://github.com/jose2a/trms",
				"/trms/trms", conVariables, envVars);
		
		String javaServUrl = s3FileStorageServ.storeFile(generateTomcatServerDockerfile);
		
		System.out.println(javaServUrl);
		
		File generatePostgreSQLDockerfile = dockerServ.generatePostgreSQLDockerfile("dddee", "s3/script.sql");
		
		String dbServUrl = s3FileStorageServ.storeFile(generatePostgreSQLDockerfile);
		
		System.out.println(dbServUrl);

	}

}
