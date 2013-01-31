package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class FindPersonBean implements Serializable {
	RoleType roleType;
	DegreeType degreeType;
	Degree degree;
	Department department;
	String name;
	Boolean viewPhoto;

	public FindPersonBean() {

	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public DegreeType getDegreeType() {
		if (!RoleType.STUDENT.equals(roleType)) {
			return null;
		}
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getViewPhoto() {
		return viewPhoto;
	}

	public void setViewPhoto(Boolean viewPhoto) {
		this.viewPhoto = viewPhoto;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Integer getDepartmentExternalId() {
		if (department != null && RoleType.TEACHER.equals(roleType)) {
			return department.getIdInternal();
		}
		return null;
	}

	public Integer getDegreeExternalId() {
		if (degree != null && RoleType.STUDENT.equals(roleType)) {
			return degree.getIdInternal();
		}
		return null;
	}

}
