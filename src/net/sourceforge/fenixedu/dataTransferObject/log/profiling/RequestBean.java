package net.sourceforge.fenixedu.dataTransferObject.log.profiling;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class RequestBean implements Serializable {

	String serverName;
	String moduleName;
	LocalDate date;
	String name;
	int invocationCount;
	int minTimeSpent;
	int maxTimeSpent;
	int totalTimeSpent;
	int averageSpent;
	boolean alert;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInvocationCount() {
		return invocationCount;
	}

	public void setInvocationCount(int invocationCount) {
		this.invocationCount = invocationCount;
	}

	public int getMinTimeSpent() {
		return minTimeSpent;
	}

	public void setMinTimeSpent(int minTimeSpent) {
		this.minTimeSpent = minTimeSpent;
	}

	public int getMaxTimeSpent() {
		return maxTimeSpent;
	}

	public void setMaxTimeSpent(int maxTimeSpent) {
		this.maxTimeSpent = maxTimeSpent;
	}

	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

	public int getAverageSpent() {
		return averageSpent;
	}

	public void setAverageSpent(int averageSpent) {
		this.averageSpent = averageSpent;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
