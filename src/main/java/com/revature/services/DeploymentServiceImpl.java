package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.models.Deployment;
import com.revature.utils.FileHelper;

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

	@Override
	public String deployProject(Deployment deployment) {
		
		File dbDockefile = null;
		File appDockerfile = null;

		try {
			String sqlSctiptUrl = s3FileStorageService.storeFile(FileHelper.convert(deployment.getSqlScript()));

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

	@Override
	public String getEC2ProjectPublicDns(String ec2InstanceId) {
		
		return ec2InstanceService.getEC2InstancePublicDNS(ec2InstanceId);
	}

}
