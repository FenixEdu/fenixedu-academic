package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public class ExecutionDegreeCoordinatorsBean implements Serializable {

	private ExecutionYear executionYear;
	private ExecutionDegree executionDegree;
	private Person newCoordinator;
	private String backPath;

	public ExecutionYear getExecutionYear() {
		return executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public ExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	public Person getNewCoordinator() {
		return newCoordinator;
	}

	public void setNewCoordinator(Person newCoordinator) {
		this.newCoordinator = newCoordinator;
	}

	public Set<Coordinator> getCoordinators() {
		return executionDegree.getCoordinatorsListSet();
	}

	public ExecutionDegreeCoordinatorsBean(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	public String getBackPath() {
		return backPath;
	}

	public void setBackPath(String backPath) {
		this.backPath = backPath;
	}

	public String getEscapedBackPath() {
		return new String(this.backPath.replace('&', '§'));
	}

	public void setEscapedBackPath(String escapedBackPath) {
		this.backPath = escapedBackPath.replace('§', '&');
	}

	public ExecutionDegreeCoordinatorsBean() {

	}

}
