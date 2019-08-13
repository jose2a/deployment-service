package com.revature.services;

import java.io.File;
import java.util.List;

import com.revature.models.ConnectionVariables;
import com.revature.models.EnvironmentVariable;

/**
 * Service to handle Dockerfiles.
 * 
 * @author Java bash, Jun 19 - USF
 *
 */
public interface DockerfileService {

	/**
	 * Generate a Dockerfile for a server container in memory in order to save it in an S3 bucket.
	 * @param projectId Project id for this deployment
	 * @param gitHubUrl GitHub URL
	 * @param pomLocation Location of the POM (Java) without including pom.xml
	 * @param connectionVariables Connection variables for this server container
	 * @param environmentVariables Other environment variables needed for this app to run
	 * @return In memory Dockerfile for application server
	 */
	public File generateTomcatServerDockerfile(String projectId, String gitHubUrl, String pomLocation,
			ConnectionVariables connectionVariables, List<EnvironmentVariable> environmentVariables);
	
	/**
	 * Generate a Dockerfile for a postgresql server container in memory in order to save it in an S3 bucket.
	 * @param projectId Project id for this deployment
	 * @param sqlScriptUrl Url of the sql file in the S3 bucket
	 * @return In memory Dockerfile for database server
	 */
	public File generatePostgreSQLDockerfile(String projectId, String sqlScriptUrl);

}
