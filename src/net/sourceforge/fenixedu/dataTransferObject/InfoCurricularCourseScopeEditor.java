package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

public class InfoCurricularCourseScopeEditor extends InfoObject {

    private InfoCurricularCourse infoCurricularCourse;

    private InfoCurricularSemester infoCurricularSemester;

    private InfoBranch infoBranch;

    private Calendar beginDate;

    private Calendar endDate;
    
    private String anotation;

    public InfoCurricularCourseScopeEditor() {
    }

	public String getAnotation() {
		return anotation;
	}

	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}

	public Calendar getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public InfoBranch getInfoBranch() {
		return infoBranch;
	}

	public void setInfoBranch(InfoBranch infoBranch) {
		this.infoBranch = infoBranch;
	}

	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}

	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}

	public InfoCurricularSemester getInfoCurricularSemester() {
		return infoCurricularSemester;
	}

	public void setInfoCurricularSemester(InfoCurricularSemester infoCurricularSemester) {
		this.infoCurricularSemester = infoCurricularSemester;
	}

}
