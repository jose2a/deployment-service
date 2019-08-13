package com.revature.services;

public class BashScriptServiceImpl implements BashScriptService {
	
	private String bashScript = 
			"#!/bin/bash\n"
			+ "sudo yum update -y\n"
			+ "sudo yum install -y docker\n"
			+ "sudo service docker start\n"
			+ "sudo yum install -y git\n"
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
		bashScript = bashScript.replaceAll("dbDockerfileUrl", dbDockerfileUrl);
		bashScript = bashScript.replaceAll("appDockerfileUrl", appDockerfileUrl);
		
		return bashScript;
	}

}