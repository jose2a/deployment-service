package com.revature.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.ConnectionVariables;
import com.revature.models.Deployment;
import com.revature.models.EnvironmentVariable;
import com.revature.services.DeploymentService;

/**
 * Rest controller for the deployment service
 *
 * @author Java, JUN 19 - USF
 *
 */
@RestController
@RequestMapping("/deployment")
public class DeploymentController {
	
	private DeploymentService deploymentService;
	
	@Autowired
	public void setDeploymentService(DeploymentService deploymentService) {
		this.deploymentService = deploymentService;
	}

	/**
	 * Deploy project to the EC2 instance.
	 * 
	 * @param projectId Project id associated to this deployment
	 * @param gitHubUrl GitHub URL for this project
	 * @param pomLocation POM location inside the project
	 * @param sqlScript Script to create database tables for this project
	 * @param connVariables Name of the environment variables used to connect to the database 
	 * @param envVariables Other environment variables needed for the project to run
	 * @return EC2 instance id
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deployProject(
			@RequestParam("projectId") String projectId,
			@RequestParam("gitHubUrl") String gitHubUrl,
			@RequestParam("pomLocation") String pomLocation,
			@RequestParam("sqlScript") MultipartFile sqlScript,
			@RequestParam("connVariables") String connVariables,
			@RequestParam("envVariables")  String envVariables
			) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ConnectionVariables connectionVariables = null;
		List<EnvironmentVariable> environmentVariables = null;
		
		try {
			// Parsing java objects using ObjectMapper
			// Spring is not able to parse the parameters into Java objects when receiving
			// files. It is easier to get this parameters as String and parse them afterwards
			// https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects#6349488
			connectionVariables = mapper.readValue(connVariables, ConnectionVariables.class);
			environmentVariables = mapper.readValue(envVariables, new TypeReference<List<EnvironmentVariable>>(){});
		} catch (IOException e) {
			// TODO Handle exception
			e.printStackTrace();
		}
		
		// Validating environment variables for null values, this way we avoid an Exception
		if (environmentVariables == null) {
			environmentVariables = new ArrayList<EnvironmentVariable>();
		}
		
		// Creating deployment instance in order to start the process
		Deployment deployment = new Deployment(
				projectId,
				gitHubUrl,
				pomLocation,
				sqlScript,
				connectionVariables,
				environmentVariables
				);
		
		// Deploying the project
		return deploymentService.deployProject(deployment);
	}
	
	/**
	 * Deploy project to the EC2 instance using a preexisting Dockerfile.
	 * 
	 * @param projectId Project id associated to this deployment.
	 * @param dockerfileUrl URL of the docker file to be deployed.
	 * @param sqlScript SQL script file to create tables for this project.
	 * @return EC2 instance id
	 */
	@PostMapping(value = "/dockerfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deployProjectDockerfile(
			@RequestParam("projectId") Integer projectId,
			@RequestParam("DockerFileUrl") String dockerfileUrl,
			@RequestParam("sqlScript") MultipartFile sqlScript
			) {
		// TODO: Implement this
		return "";
	}
	
	/**
	 * Get public DNS of the EC2 instance that was deployed.
	 * @param instanceId EC2's instance id.
	 * @return Public DNS URL.
	 */
	@GetMapping(value = "/dns/{instanceId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getPublicEC2Dns(@PathVariable("instanceId") String instanceId) {
		return deploymentService.getEC2ProjectPublicDns(instanceId);
	}
}
