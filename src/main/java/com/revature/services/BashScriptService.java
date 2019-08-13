package com.revature.services;

/**
 * Service to generate Bash script that is going to be run in the new ec2 instance.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public interface BashScriptService {
	
	/**
	 * Generate Bash script in order to install Docker and build and run the containers
	 * inside of the EC2 instance.
	 * @param dbDockerfileUrl Dockerfile URL for data base in S3 bucket
	 * @param appDockerfileUrl Dockerfile URL for the app server in S3 bucket 
	 * @return Bash script
	 */
	public String generateBashScript(String dbDockerfileUrl, String appDockerfileUrl);

}
