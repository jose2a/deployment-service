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
	
	/**
	 * Script to be run in the EC2 when creating it using AWS sdk.
	 * 
	 * 1. Install docker and start docker in the EC2
	 * 2. Download the docker file for the database and application and rename them
	 * 3. Build the DockerfileDb and create the postgresql container
	 * 4. Build the DockerfileWeb and create the tomcat container
	 * 5. Run postgresql container and give it the alias 'database'
	 * 6. Run tomcat container and give it the alias 'web' and link it to the database
	 *    giver the alias 'db'. This alias will allow to establish a connection between our
	 *    Java application running in 'web' container and our Postgresql database running in 'database'
	 *    using a jdbc url like this: jdbc:postgresql://db:5432/docker
	 * 
	 */
	private String bashScript = 
			"#!/bin/bash\n"
			+ "sudo yum update -y\n"
			+ "sudo yum install -y docker\n"
			+ "sudo service docker start\n"
			+ "sudo yum install -y curl\n"
			+ "cd /tmp\n"
			+ "sudo curl %dbDockerfileUrl% --output DockerfileDb\n"
			+ "sudo curl %appDockerfileUrl% --output DockerfileWeb\n"
			+ "sudo docker build -f DockerfileDb -t postgresql .\n"
			+ "sudo docker build -f DockerfileWeb -t tomcat .\n"
			+ "sudo docker run -p 5432:5432 -d --name database postgresql\n"
			+ "sudo docker run -p 80:8080 -d --name web --link=database:db tomcat\n"; 

	@Override
	public String generateBashScript(String dbDockerfileUrl, String appDockerfileUrl) {
		bashScript = bashScript.replaceAll("%dbDockerfileUrl%", dbDockerfileUrl);
		bashScript = bashScript.replaceAll("%appDockerfileUrl%", appDockerfileUrl);
		
		return bashScript;
	}

}
