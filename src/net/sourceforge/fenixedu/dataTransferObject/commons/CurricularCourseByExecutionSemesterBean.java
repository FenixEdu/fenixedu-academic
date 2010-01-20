package net.sourceforge.fenixedu.dataTransferObject.commons;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class CurricularCourseByExecutionSemesterBean implements Serializable, Comparable<CurricularCourseByExecutionSemesterBean> {

    private CurricularCourse curricularCourse;
    private ExecutionSemester executionSemester;

    public CurricularCourseByExecutionSemesterBean() {
    }

    public CurricularCourseByExecutionSemesterBean(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester) {
	setCurricularCourse(curricularCourse);
	setExecutionSemester(executionSemester);
    }

    public CurricularCourse getCurricularCourse() {
	return this.curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = curricularCourse;
    }

    public ExecutionSemester getExecutionSemester() {
	return this.executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public String getCurricularCourseName() {
	return getCurricularCourse().getName(getExecutionSemester());
    }

    public Double getCurricularCourseEcts() {
	return getCurricularCourse().getEctsCredits(getExecutionSemester());
    }

    public String getDegreeName() {
	return getCurricularCourse().getDegree().getNameFor(getExecutionSemester()).getContent();
    }

    public String getKey() {
	return getCurricularCourse().getOID() + ":" + getExecutionSemester().getOID();
    }

    @Override
    public boolean equals(Object obj) {
	return (obj instanceof CurricularCourseByExecutionSemesterBean) ? equals((CurricularCourseByExecutionSemesterBean) obj)
		: false;
    }

    public boolean equals(CurricularCourseByExecutionSemesterBean obj) {
	return getCurricularCourse() == obj.getCurricularCourse();
    }

    @Override
    public int hashCode() {
	return getCurricularCourse().hashCode();
    }

    @Override
    public int compareTo(CurricularCourseByExecutionSemesterBean other) {
	return other == null ? 1 : CurricularCourse.COMPARATOR_BY_NAME
		.compare(getCurricularCourse(), other.getCurricularCourse());
    }

    static public CurricularCourseByExecutionSemesterBean buildFrom(final String key) {
	if (key == null || key.isEmpty()) {
	    return null;
	}
	final String[] values = key.split(":");
	final CurricularCourse course = (CurricularCourse) DomainObject.fromOID(Long.valueOf(values[0]).longValue());
	final ExecutionSemester semester = (ExecutionSemester) DomainObject.fromOID(Long.valueOf(values[1]).longValue());
	return new CurricularCourseByExecutionSemesterBean(course, semester);
    }
}
