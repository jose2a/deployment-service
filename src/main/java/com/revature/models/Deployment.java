package com.revature.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Deployment {
	
	private int projectId; // id of project to be deployed
	
	private String gitHubUrl; // GitHub url without the .git
	// Location of the POM (this can be changed later to include more types of projects)
	private String pomLocation;
	
	private MultipartFile sqlScript; // Sql script
	
	private ConnectionVariables connVariables;
	private List<EnvironmentVariable> environmentVariables;

}
