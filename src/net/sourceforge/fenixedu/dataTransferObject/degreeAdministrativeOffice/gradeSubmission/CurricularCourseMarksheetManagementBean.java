package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class CurricularCourseMarksheetManagementBean implements Serializable {

    static final public Comparator<CurricularCourseMarksheetManagementBean> COMPARATOR_BY_NAME = new Comparator<CurricularCourseMarksheetManagementBean>() {
	@Override
	public int compare(CurricularCourseMarksheetManagementBean o1, CurricularCourseMarksheetManagementBean o2) {
	    return o1.getName().compareTo(o2.getName());
	}
    };

    private DomainReference<CurricularCourse> curricularCourse;
    private DomainReference<ExecutionSemester> executionSemester;

    public CurricularCourseMarksheetManagementBean(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester) {
	setCurricularCourse(curricularCourse);
	setgetExecutionSemester(executionSemester);
    }

    public CurricularCourse getCurricularCourse() {
	return (this.curricularCourse != null) ? this.curricularCourse.getObject() : null;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = (curricularCourse != null) ? new DomainReference<CurricularCourse>(curricularCourse) : null;
    }

    public ExecutionSemester getExecutionSemester() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setgetExecutionSemester(ExecutionSemester attribute) {
	this.executionSemester = (attribute != null) ? new DomainReference<ExecutionSemester>(attribute) : null;
    }

    public String getKey() {
	return String.valueOf(getCurricularCourse().getOID() + ":" + getExecutionSemester().getOID());
    }

    public String getName() {
	return getCurricularCourse().getName(getExecutionSemester());
    }

    public String getCode() {
	return getCurricularCourse().getCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof CurricularCourseMarksheetManagementBean) {
	    return getCurricularCourse().equals(((CurricularCourseMarksheetManagementBean) obj).getCurricularCourse());
	}
	return false;
    }
}
