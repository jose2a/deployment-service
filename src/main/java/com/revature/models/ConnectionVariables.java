package com.revature.models;

/**
 * Variables used to establish the connection with the database in the docker container.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public class ConnectionVariables {

	private String urlVariableName; // Environment variable for the database URL
	private String usernameVariableName; // Environment variable for the username
	private String passwordVariableName; // Environment variable for the password

	public ConnectionVariables() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConnectionVariables(String urlVariableName, String usernameVariableName, String passwordVaraibleName) {
		super();
		this.urlVariableName = urlVariableName;
		this.usernameVariableName = usernameVariableName;
		this.passwordVariableName = passwordVaraibleName;
	}

	public String getUrlVariableName() {
		return urlVariableName;
	}

	public void setUrlVariableName(String urlVariableName) {
		this.urlVariableName = urlVariableName;
	}

	public String getUsernameVariableName() {
		return usernameVariableName;
	}

	public void setUsernameVariableName(String usernameVariableName) {
		this.usernameVariableName = usernameVariableName;
	}

	public String getPasswordVariableName() {
		return passwordVariableName;
	}

	public void setPasswordVariableName(String passwordVariableName) {
		this.passwordVariableName = passwordVariableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passwordVariableName == null) ? 0 : passwordVariableName.hashCode());
		result = prime * result + ((urlVariableName == null) ? 0 : urlVariableName.hashCode());
		result = prime * result + ((usernameVariableName == null) ? 0 : usernameVariableName.hashCode());
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
		ConnectionVariables other = (ConnectionVariables) obj;
		if (passwordVariableName == null) {
			if (other.passwordVariableName != null)
				return false;
		} else if (!passwordVariableName.equals(other.passwordVariableName))
			return false;
		if (urlVariableName == null) {
			if (other.urlVariableName != null)
				return false;
		} else if (!urlVariableName.equals(other.urlVariableName))
			return false;
		if (usernameVariableName == null) {
			if (other.usernameVariableName != null)
				return false;
		} else if (!usernameVariableName.equals(other.usernameVariableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConnectionVariables [urlVariableName=" + urlVariableName + ", usernameVariableName="
				+ usernameVariableName + ", passwordVariableName=" + passwordVariableName + "]";
	}

}
