package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.revature.models.ConnectionVariables;
import com.revature.models.Deployment;
import com.revature.models.EnvironmentVariable;
import com.revature.services.DeploymentService;

@RestController
public class DeploymentController {
	
	private DeploymentService deploymentService;
	
	@Autowired
	public DeploymentService getDeploymentService() {
		return deploymentService;
	}

	/**
	 * Deploy project to the EC2 instance.
	 * @param deployment Information of the project to be deployed.
	 * @return Instance id.
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
		
		Deployment deployment = new Deployment(
				projectId,
				gitHubUrl,
				pomLocation,
				sqlScript,
				connVariables,
				envVariables
				);
		
		return deploymentService.deployProject(deployment);
	}
	
	/**
	 * Deploy project to the EC2 instance using a preexisting Dockerfile.
	 * @param deployment Information of the project to be deployed.
	 * @return Instance id.
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
