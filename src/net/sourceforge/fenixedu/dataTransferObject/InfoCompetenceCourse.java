package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;

public class InfoCompetenceCourse extends InfoObject {
    private String name;
    private String code;
    private List<InfoDepartment> departments;
    private List<InfoCurricularCourse> associatedCurricularCourses;

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public List<InfoDepartment> getDepartments() {
	return departments;
    }

    public void setDepartment(List<InfoDepartment> departments) {
	this.departments = departments;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<InfoCurricularCourse> getAssociatedCurricularCourses() {
	return associatedCurricularCourses;
    }

    public void setAssociatedCurricularCourses(List<InfoCurricularCourse> associatedCurricularCourses) {
	this.associatedCurricularCourses = associatedCurricularCourses;
    }

    public void copyFromDomain(CompetenceCourse competenceCourse) {
	super.copyFromDomain(competenceCourse);
	if (competenceCourse != null) {
	    setCode(competenceCourse.getCode());
	    setName(competenceCourse.getName());
	    List<InfoDepartment> infoDepartments = new ArrayList<InfoDepartment>();
	    for (Department department : competenceCourse.getDepartments()) {
		infoDepartments.add(InfoDepartment.newInfoFromDomain(department));
	    }
	    setDepartment(infoDepartments);
	}
    }

    public static InfoCompetenceCourse newInfoFromDomain(CompetenceCourse competenceCourse) {
	InfoCompetenceCourse infoCompetenceCourse = null;
	if (competenceCourse != null) {
	    infoCompetenceCourse = new InfoCompetenceCourse();
	    infoCompetenceCourse.copyFromDomain(competenceCourse);
	}
	return infoCompetenceCourse;
    }

}
