package com.revature.services;

import org.springframework.stereotype.Component;

/**
 * Service to generate Bash script that is going to be run in the new ec2 instance.
 * 
 * @author Java, JUN 19 - USF
 *
 */
@Component
public class BashScriptServiceImpl implements BashScriptService {
	
	// Script to be run in the EC2 when creating it
	private String bashScript = 
			"#!/bin/bash\n"
			+ "sudo yum update -y\n"
			+ "sudo yum install -y docker\n"
			+ "sudo service docker start\n"
			+ "sudo yum install -y curl\n"
			+ "cd /tmp\n"
			+ "sudo curl %dbDockerfileUrl% --output DockerfileDb\n"
			+ "sudo curl %appDockerfileUrl% --output DockerfileApp\n"
			+ "sudo docker build -f DockerfileDb -t postgresql .\n"
			+ "sudo docker build -f DockerfileApp -t app .\n"
			+ "sudo docker run -p 5432:5432 -d postgresql\n"
			+ "sudo docker run -p 80:8080 -d app\n";

	@Override
	public String generateBashScript(String dbDockerfileUrl, String appDockerfileUrl) {
		bashScript = bashScript.replaceAll("%dbDockerfileUrl%", dbDockerfileUrl);
		bashScript = bashScript.replaceAll("%appDockerfileUrl%", appDockerfileUrl);
		
		return bashScript;
	}

}
