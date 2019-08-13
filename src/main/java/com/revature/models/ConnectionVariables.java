package com.revature.models;

public class ConnectionVariables {

	private String urlVariableName;
	private String usernameVariableName;
	private String passwordVaraibleName;

	public ConnectionVariables() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConnectionVariables(String urlVariableName, String usernameVariableName, String passwordVaraibleName) {
		super();
		this.urlVariableName = urlVariableName;
		this.usernameVariableName = usernameVariableName;
		this.passwordVaraibleName = passwordVaraibleName;
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

	public String getPasswordVaraibleName() {
		return passwordVaraibleName;
	}

	public void setPasswordVaraibleName(String passwordVaraibleName) {
		this.passwordVaraibleName = passwordVaraibleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passwordVaraibleName == null) ? 0 : passwordVaraibleName.hashCode());
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
		if (passwordVaraibleName == null) {
			if (other.passwordVaraibleName != null)
				return false;
		} else if (!passwordVaraibleName.equals(other.passwordVaraibleName))
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
				+ usernameVariableName + ", passwordVaraibleName=" + passwordVaraibleName + "]";
	}

}
