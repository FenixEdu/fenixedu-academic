package net.sourceforge.fenixedu.dataTransferObject.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.ProjectParticipationType;

public class ProjectParticipantUnitCreationBean implements Serializable {

	private Unit unit;
	private String role;
	private String unitName;

	public ProjectParticipantUnitCreationBean() {
		role = ProjectParticipationType.getDefaultUnitRoleType().toString();
	}

	public ProjectParticipationType getRole() {
		return ProjectParticipationType.valueOf(role);
	}

	public void setRole(ProjectParticipationType projectParticipationRole) {
		this.role = projectParticipationRole.toString();
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String name) {
		this.unitName = name;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
