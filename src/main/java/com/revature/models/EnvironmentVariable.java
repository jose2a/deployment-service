package com.revature.models;

/**
 * Stores other environment variables needed to run the application.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public class EnvironmentVariable {

	private String variableName;
	private String variableValue;

	public EnvironmentVariable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EnvironmentVariable(String variableName, String variableValue) {
		super();
		this.variableName = variableName;
		this.variableValue = variableValue;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
		result = prime * result + ((variableValue == null) ? 0 : variableValue.hashCode());
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
		EnvironmentVariable other = (EnvironmentVariable) obj;
		if (variableName == null) {
			if (other.variableName != null)
				return false;
		} else if (!variableName.equals(other.variableName))
			return false;
		if (variableValue == null) {
			if (other.variableValue != null)
				return false;
		} else if (!variableValue.equals(other.variableValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EnvironmentVariable [variableName=" + variableName + ", variableValue=" + variableValue + "]";
	}

}
