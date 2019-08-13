package com.revature.controllers;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class DeploymentController {
	
	private DeploymentService deploymentService;
	
	@Autowired
	public void setDeploymentService(DeploymentService deploymentService) {
		this.deploymentService = deploymentService;
	}
	
	/**
	 * This method parse the values passed to the controller along with the SQL script file. Because
	 * spring does not parse the values received through in the form of a class we need to register
	 * a custom editor.
	 * 
	 * Based on: https://stackoverflow.com/questions/20622359/automatic-conversion-of-json-form-parameter-in-spring-mvc-4-0
	 * @param dataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		ObjectMapper mapper = new ObjectMapper();
		
		// Editor for ConnectionVariables
	    dataBinder.registerCustomEditor(ConnectionVariables.class, new PropertyEditorSupport() {
	    	
	        Object value;
	        
	        @Override
	        public Object getValue() {
	            return value;
	        }

	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	            try {
					value = mapper.readValue(text, ConnectionVariables.class);
				} catch (IOException e) {
					// TODO: Handle error
					e.printStackTrace();
				}
	        }
	    });
	    
	    
	    // Editor for EnvironmentVariable
	    dataBinder.registerCustomEditor(EnvironmentVariable.class, new PropertyEditorSupport() {
	    	
	        Object value;
	        
	        @Override
	        public Object getValue() {
	            return value;
	        }

	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	            try {
					value = mapper.readValue(text, EnvironmentVariable.class);
				} catch (IOException e) {
					// TODO Handle exception
					e.printStackTrace();
				}
	        }
	    });
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
	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deployProject(
			@RequestParam("projectId") String projectId,
			@RequestParam("gitHubUrl") String gitHubUrl,
			@RequestParam("pomLocation") String pomLocation,
			@RequestParam("sqlScript") MultipartFile sqlScript,
			@RequestParam("connVariables") ConnectionVariables connVariables,
			@RequestParam("envVariables")  List<EnvironmentVariable> envVariables
			) {
		
		// Validating environment variables for null values
		if (envVariables == null || envVariables.size() == 0 || envVariables.contains(null)) {
			envVariables = new ArrayList<EnvironmentVariable>();
		}
		
		// Creating deployment instance in order to start the process
		Deployment deployment = new Deployment(
				projectId,
				gitHubUrl,
				pomLocation,
				sqlScript,
				connVariables,
				envVariables
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
