package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class ExecutionCourseWrittenEvaluationAgregationBean {

    public static final Comparator<ExecutionCourseWrittenEvaluationAgregationBean> COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR = new Comparator<ExecutionCourseWrittenEvaluationAgregationBean>() {

	@Override
	public int compare(ExecutionCourseWrittenEvaluationAgregationBean o1, ExecutionCourseWrittenEvaluationAgregationBean o2) {
	    final int c = o1.getExecutionCourse().getSigla().compareTo(o2.getExecutionCourse().getSigla());
	    return c == 0 ? o1.getCurricularYear().compareTo(o2.getCurricularYear()) : c;
	}

    };

    private ExecutionCourse executionCourse;
    private Integer curricularYear;
    private Collection<WrittenEvaluation> writtenEvaluations;

    public ExecutionCourseWrittenEvaluationAgregationBean(Integer curricularYear, ExecutionCourse executionCourse,
	    Set<WrittenEvaluation> writtenEvaluations) {
	this.curricularYear = curricularYear;
	this.executionCourse = executionCourse;
	this.writtenEvaluations = writtenEvaluations;
    }

    public Integer getCurricularYear() {
	return curricularYear;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public Collection<WrittenEvaluation> getWrittenEvaluations() {
	return writtenEvaluations;
    }

}
