package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;

import org.apache.commons.beanutils.BeanComparator;

public class InfoSiteEvaluationMarks extends DataTranferObject implements ISiteComponent {

    private static final Comparator comparator = new BeanComparator("attend.aluno.number");

    private Integer evaluationID;

    private DomainReference<Evaluation> evaluation;

    private DomainReference<ExecutionCourse> executionCourse;

    public Evaluation getEvaluation() {
        return evaluation == null ? null : evaluation.getObject();
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = new DomainReference<Evaluation>(evaluation);
    }

    public Integer getEvaluationID() {
        return evaluationID;
    }

    public void setEvaluationID(Integer evaluationID) {
        this.evaluationID = evaluationID;
    }

    public Collection<Mark> getSortedMarks() {
        final Collection<Mark> sortedMarks = new TreeSet<Mark>(comparator);
        sortedMarks.addAll(getEvaluation().getMarks());
        return sortedMarks;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse == null ? null : executionCourse.getObject();
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = new DomainReference<ExecutionCourse>(executionCourse);
    }

}