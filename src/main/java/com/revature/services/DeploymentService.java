package com.revature.services;

import java.io.IOException;

import com.revature.models.Deployment;

public interface DeploymentService {

	/**
	 * Deploy project to EC2 instance.
	 * @param deployment The information needed to deploy the project.
	 * @return Instance id of the newly created EC2.
	 * @throws IOException 
	 */
	public String deployProject(Deployment deployment);
	
	/**
	 * Get public EC2's public DNS for the project that was just deployed.
	 * @param ec2InstanceId Instance Id.
	 * @return Public DNS
	 */
	public String getEC2ProjectPublicDns(String ec2InstanceId);
	
}
