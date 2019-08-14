package com.revature.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.revature.models.ConnectionVariables;
import com.revature.models.EnvironmentVariable;
import com.revature.utils.FileHelper;

/**
 * Service to handle Dockerfiles.
 * 
 * @author Java, JUN 19 - USF
 *
 */
@Component
public class DockerfileServiceImpl implements DockerfileService {
	
	private final String DOCKERFILE_POSTGRES = "DockerfilePostgres";
	private final String DOCKERFILE_TOMCAT = "DockerfileTomcat";
	private final String DOCKER_FILE_DIR = "dockerfiles/";

	/**
	 * Tomcat Dockerfile obtained from: https://hub.docker.com/_/tomcat
	 */
	@Override
	public File generateTomcatServerDockerfile(
			String projectId,
			String gitHubUrl,
			String pomLocation,
			ConnectionVariables conVar,
			List<EnvironmentVariable> environmentVariables) throws IOException {
		
		// Loading the Dockerfile, it has placeholder values (denoted by %%) that will be 
		// replaced with the specific values for this project
		String dockerfileContent = FileHelper.getTextFileContent(DOCKER_FILE_DIR + DOCKERFILE_TOMCAT);
		
		// Replace placeholder values
		dockerfileContent = dockerfileContent.replaceAll("%gitHubUrl%", gitHubUrl + ".git");
		dockerfileContent = dockerfileContent.replaceAll("%pomLocation%", pomLocation);
		dockerfileContent = dockerfileContent.replaceAll("%db_url%", conVar.getUrlVariableName());
		dockerfileContent = dockerfileContent.replaceAll("%db_username%", conVar.getUsernameVariableName());
		dockerfileContent = dockerfileContent.replaceAll("%db_password%", conVar.getPasswordVariableName());
		
		String envVarStr = "";
		
		for (EnvironmentVariable envVar : environmentVariables) {
			envVarStr += "ENV " + envVar.getVariableName() + " " + envVar.getVariableValue() + "\n";
		}
		
		dockerfileContent = dockerfileContent.replaceAll("%enviromentVariables%", envVarStr);
		
		// Create a new file and write the Dockerfile in this
		File dockerfile = new File(DOCKERFILE_TOMCAT + "-" + projectId);
		dockerfile.deleteOnExit();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(dockerfile));
	    out.write(dockerfileContent);
	    out.close();
		
		return dockerfile;
	}

	@Override
	public File generatePostgreSQLDockerfile(String projectId, String sqlScriptUrl) throws IOException {
		// Loading the Dockerfile, it has placeholder values (denoted by %%) that will be 
		// replaced with the specific values for this project
		String dockerfileContent = FileHelper.getTextFileContent(DOCKER_FILE_DIR + DOCKERFILE_POSTGRES);
		
		dockerfileContent = dockerfileContent.replaceAll("%sqlScriptUrl%", sqlScriptUrl);
		
		// Create a new file and write the Dockerfile in this
		File dockerfile = new File(DOCKERFILE_POSTGRES + "-" + projectId);
		dockerfile.deleteOnExit();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(dockerfile));
	    out.write(dockerfileContent);
	    out.close();
		
		return dockerfile;
	}

}
