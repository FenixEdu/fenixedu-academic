package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ImportContentBean implements Serializable {

    private DomainReference<ExecutionSemester> executionPeriodReference;

    private DomainReference<ExecutionDegree> executionDegreeReference;

    private DomainReference<CurricularYear> curricularYearReference;

    private DomainReference<ExecutionCourse> executionCourseReference;

    public ImportContentBean() {
	setExecutionPeriod(null);
	setExecutionDegree(null);
	setCurricularYear(null);
	setExecutionCourse(null);
    }

    public ExecutionCourse getExecutionCourse() {
	return this.executionCourseReference.getObject();
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourseReference = new DomainReference<ExecutionCourse>(executionCourse);
    }

    public ExecutionSemester getExecutionPeriod() {
	return this.executionPeriodReference.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionPeriodReference = new DomainReference<ExecutionSemester>(executionSemester);
    }

    public ExecutionDegree getExecutionDegree() {
	return this.executionDegreeReference.getObject();
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegreeReference = new DomainReference<ExecutionDegree>(executionDegree);
    }

    public CurricularYear getCurricularYear() {
	return this.curricularYearReference.getObject();
    }

    public void setCurricularYear(CurricularYear curricularYear) {
	this.curricularYearReference = new DomainReference<CurricularYear>(curricularYear);
    }

}
