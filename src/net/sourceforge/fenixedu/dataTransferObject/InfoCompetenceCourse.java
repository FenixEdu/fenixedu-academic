package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICompetenceCourse;

public class InfoCompetenceCourse extends InfoObject {
    private String name;
    private String code;
    private InfoDepartment department;
    private List<InfoCurricularCourse> associatedCurricularCourses;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public InfoDepartment getDepartment() {
		return department;
	}
	public void setDepartment(InfoDepartment department) {
		this.department = department;
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
	public void setAssociatedCurricularCourses(
			List<InfoCurricularCourse> associatedCurricularCourses) {
		this.associatedCurricularCourses = associatedCurricularCourses;
	}
	public void copyFromDomain(ICompetenceCourse competenceCourse) {
		super.copyFromDomain(competenceCourse);
		if(competenceCourse != null) {
			setCode(competenceCourse.getCode());
			setName(competenceCourse.getName());
			setDepartment(InfoDepartment.newInfoFromDomain(competenceCourse.getDepartment()));
		}
	}
	
	public static InfoCompetenceCourse newInfoFromDomain(ICompetenceCourse competenceCourse) {
		InfoCompetenceCourse infoCompetenceCourse = null;
		if(competenceCourse != null) {
			infoCompetenceCourse = new InfoCompetenceCourse();
			infoCompetenceCourse.copyFromDomain(competenceCourse);
		}
		return infoCompetenceCourse;
	}

}
