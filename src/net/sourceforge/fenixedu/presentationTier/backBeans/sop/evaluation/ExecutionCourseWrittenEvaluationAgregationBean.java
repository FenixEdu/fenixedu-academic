package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;


public class ExecutionCourseWrittenEvaluationAgregationBean {

    public static final Comparator<ExecutionCourseWrittenEvaluationAgregationBean> COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR).addComparator(new BeanComparator("executionCourse.sigla"));
	((ComparatorChain) COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR).addComparator(new BeanComparator("curricularYear"));
    }

    private ExecutionCourse executionCourse;
    private Integer curricularYear;
    private Collection<WrittenEvaluation> writtenEvaluations;

    public ExecutionCourseWrittenEvaluationAgregationBean(Integer curricularYear, ExecutionCourse executionCourse, Set<WrittenEvaluation> writtenEvaluations) {
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
