package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.models.Deployment;
import com.revature.utils.FileHelper;

/**
 * Takes care of all the necessary process to deploy a project in an EC2 instance.
 * @author Java, JUN 19 - USF
 *
 */
@Component
public class DeploymentServiceImpl implements DeploymentService {

	private EC2InstanceService ec2InstanceService;
	private DockerfileService dockerFileService;
	private S3FileStorageService s3FileStorageService;
	private BashScriptService bashScriptService;

	@Autowired
	public void setEc2InstanceService(EC2InstanceService ec2InstanceService) {
		this.ec2InstanceService = ec2InstanceService;
	}

	@Autowired
	public void setDockerFileService(DockerfileService dockerFileService) {
		this.dockerFileService = dockerFileService;
	}

	@Autowired
	public void setS3FileStorageService(S3FileStorageService s3FileStorageService) {
		this.s3FileStorageService = s3FileStorageService;
	}

	@Autowired
	public void setBashScriptService(BashScriptService bashScriptService) {
		this.bashScriptService = bashScriptService;
	}

	/**
	 * Deploy project to the EC2 instance using Dockerfile.
	 * 
	 * Steps:
	 * 
	 * 1. Save SQL script to an S3 bucket.
	 * 2. Create Dockerfile for a database instance.
	 * 3. Create Dockerfile for an application server instance.
	 * 4. Store database Dockerfile in the S3 bucket to make it accessible to the EC2.
	 * 5. Store application Dockerfile in the S3 bucket to make it accessible to the EC2.
	 * 6. Generate bash script that will be run when we spin up the EC2 instance.
	 * 7. Spin up a new EC2.
	 * 
	 * @param deployment Contains all the information necessary to deploy the project.
	 * @return EC2 instance id
	 */
	@Override
	public String deployProject(Deployment deployment) {
		
		File dbDockefile = null;
		File appDockerfile = null;

		try {
			File sqlScriptFile = FileHelper.convert(deployment.getSqlScript());
			String sqlSctiptUrl = s3FileStorageService.storeFile(sqlScriptFile);
			
			sqlScriptFile.delete(); // Delete sql file after store it in the S3 bucket

			dbDockefile = dockerFileService.generatePostgreSQLDockerfile(deployment.getProjectId(), sqlSctiptUrl);
			
			appDockerfile = dockerFileService.generateTomcatServerDockerfile(deployment.getProjectId(),
					deployment.getGitHubUrl(),
					deployment.getPomLocation(),
					deployment.getConnVariables(),
					deployment.getEnvironmentVariables());
		} catch (IOException e) {
			// TODO Handle the exception
			System.out.println(e.getMessage());
		}
		
		String dbDockerfileUrl = s3FileStorageService.storeFile(dbDockefile);
		String appDockerfileUrl = s3FileStorageService.storeFile(appDockerfile);
		
		String bashScript = bashScriptService.generateBashScript(dbDockerfileUrl, appDockerfileUrl);
		
		String ec2InstanceId = null;
		
		try {
			ec2InstanceId = ec2InstanceService.spinUpEC2Instance(bashScript);
		} catch (UnsupportedEncodingException e) {
			// TODO Handle the exception
			e.printStackTrace();
		}
		
		return ec2InstanceId;
	}

	/**
	 * Get public Dns for the EC2 instance given the id.
	 * 
	 * @param ec2InstanceId EC2's instance id
	 */
	@Override
	public String getEC2ProjectPublicDns(String ec2InstanceId) {
		
		return ec2InstanceService.getEC2InstancePublicDNS(ec2InstanceId);
	}

}
