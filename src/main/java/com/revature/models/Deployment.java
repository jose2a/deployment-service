package com.revature.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * Stores all the information needed for deploy a Java application in an EC2 instance.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public class Deployment {

	private String projectId; // id of project to be deployed

	private String gitHubUrl; // GitHub url without the .git
	// Location of the POM (this can be changed later to include more types of
	// projects)
	private String pomLocation;

	private MultipartFile sqlScript; // Sql script

	private ConnectionVariables connVariables; // Environment variables for the connection
	private List<EnvironmentVariable> environmentVariables; // Other environment variables

	public Deployment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Deployment(String projectId, String gitHubUrl, String pomLocation, MultipartFile sqlScript,
			ConnectionVariables connVariables, List<EnvironmentVariable> environmentVariables) {
		super();
		this.projectId = projectId;
		this.gitHubUrl = gitHubUrl;
		this.pomLocation = pomLocation;
		this.sqlScript = sqlScript;
		this.connVariables = connVariables;
		this.environmentVariables = environmentVariables;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getGitHubUrl() {
		return gitHubUrl;
	}

	public void setGitHubUrl(String gitHubUrl) {
		this.gitHubUrl = gitHubUrl;
	}

	public String getPomLocation() {
		return pomLocation;
	}

	public void setPomLocation(String pomLocation) {
		this.pomLocation = pomLocation;
	}

	public MultipartFile getSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(MultipartFile sqlScript) {
		this.sqlScript = sqlScript;
	}

	public ConnectionVariables getConnVariables() {
		return connVariables;
	}

	public void setConnVariables(ConnectionVariables connVariables) {
		this.connVariables = connVariables;
	}

	public List<EnvironmentVariable> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(List<EnvironmentVariable> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connVariables == null) ? 0 : connVariables.hashCode());
		result = prime * result + ((environmentVariables == null) ? 0 : environmentVariables.hashCode());
		result = prime * result + ((gitHubUrl == null) ? 0 : gitHubUrl.hashCode());
		result = prime * result + ((pomLocation == null) ? 0 : pomLocation.hashCode());
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deployment other = (Deployment) obj;
		if (connVariables == null) {
			if (other.connVariables != null)
				return false;
		} else if (!connVariables.equals(other.connVariables))
			return false;
		if (environmentVariables == null) {
			if (other.environmentVariables != null)
				return false;
		} else if (!environmentVariables.equals(other.environmentVariables))
			return false;
		if (gitHubUrl == null) {
			if (other.gitHubUrl != null)
				return false;
		} else if (!gitHubUrl.equals(other.gitHubUrl))
			return false;
		if (pomLocation == null) {
			if (other.pomLocation != null)
				return false;
		} else if (!pomLocation.equals(other.pomLocation))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Deployment [projectId=" + projectId + ", gitHubUrl=" + gitHubUrl + ", pomLocation=" + pomLocation
				+ ", sqlScript=" + sqlScript + ", connVariables=" + connVariables + ", environmentVariables="
				+ environmentVariables + "]";
	}

}
