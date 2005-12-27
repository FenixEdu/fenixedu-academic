package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;

import org.apache.commons.beanutils.BeanComparator;


public class InfoSiteEvaluationMarks extends DataTranferObject implements ISiteComponent {

    private static final Comparator comparator = new BeanComparator("attend.aluno.number");

    private Integer evaluationID;
    private IEvaluation evaluation;
    private IExecutionCourse executionCourse;

    public IEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(IEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Integer getEvaluationID() {
        return evaluationID;
    }

    public void setEvaluationID(Integer evaluationID) {
        this.evaluationID = evaluationID;
    }

    public Collection<IMark> getSortedMarks() {
        final Collection<IMark> sortedMarks = new TreeSet<IMark>(comparator);
        sortedMarks.addAll(getEvaluation().getMarks());
        return sortedMarks;
    }

    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

}