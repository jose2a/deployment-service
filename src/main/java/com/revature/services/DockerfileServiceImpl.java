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

@Component
public class DockerfileServiceImpl implements DockerfileService {
	
	private final String DOCKER_FILE_DIR = "dockerfiles/";

	@Override
	public File generateTomcatServerDockerfile(
			String projectId,
			String gitHubUrl,
			String pomLocation,
			ConnectionVariables conVar,
			List<EnvironmentVariable> environmentVariables) throws IOException {
		
		String dockerfileContent = FileHelper.getTextFileContent(DOCKER_FILE_DIR + "DockerfileJava");
		
		dockerfileContent = dockerfileContent.replaceAll("%gitHubUrl%", gitHubUrl + ".git");
		dockerfileContent = dockerfileContent.replaceAll("%pomLocation%", pomLocation);
		dockerfileContent = dockerfileContent.replaceAll("%db_url%", conVar.getUrlVariableName());
		dockerfileContent = dockerfileContent.replaceAll("%db_username%", conVar.getUsernameVariableName());
		dockerfileContent = dockerfileContent.replaceAll("%db_password%", conVar.getPasswordVaraibleName());
		
		String envVarStr = "";
		
		for (EnvironmentVariable envVar : environmentVariables) {
			envVarStr += "ENV " + envVar.getVariableName() + " " + envVar.getVariableValue() + "\n";
		}
		
		dockerfileContent = dockerfileContent.replaceAll("%enviromentVariables%", envVarStr);
		
		File dockerfile = new File("DockerfileJava-" + projectId);
		dockerfile.deleteOnExit();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(dockerfile));
	    out.write(dockerfileContent);
	    out.close();
		
		return dockerfile;
	}

	@Override
	public File generatePostgreSQLDockerfile(String projectId, String sqlScriptUrl) throws IOException {
		String dockerfileContent = FileHelper.getTextFileContent(DOCKER_FILE_DIR + "DockerfilePG");
		
		dockerfileContent = dockerfileContent.replaceAll("%sqlScriptUrl%", sqlScriptUrl);
		
		File dockerfile = new File("DockerfilePG-" + projectId);
		dockerfile.deleteOnExit();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(dockerfile));
	    out.write(dockerfileContent);
	    out.close();
		
		return dockerfile;
	}

}
