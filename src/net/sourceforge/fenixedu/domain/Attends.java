/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * 
 * @author tfc130
 */
public class Attends extends Attends_Base {

    public Attends() {}
	
	public Attends (Student student, ExecutionCourse executionCourse) {
		setAluno(student);
		setDisciplinaExecucao(executionCourse);
	}
	
    public String toString() {
        String result = "[ATTEND";
        result += ", codigoInterno=" + getIdInternal();
        result += ", Student=" + getAluno();
        result += ", ExecutionCourse=" + getDisciplinaExecucao();
        result += ", Enrolment=" + getEnrolment();
        result += "]";
        return result;
    }

	public void delete() throws DomainException {
		
		if (!hasAnyStudentGroups() && !hasAnyAssociatedMarks() && !hasEnrolment()) {
			removeAluno();
			removeDisciplinaExecucao();
			super.deleteDomainObject();
		}
		else
			throw new DomainException("error.attends.cant.delete");
	}
	
	public FinalMark getFinalMark() {
		for (Mark mark : getAssociatedMarks()) {
			if(mark instanceof FinalMark) {
				return (FinalMark) mark;
			}
		}
		return null;
	}

	public Mark getMarkByEvaluation(Evaluation evaluation) {
		for (Mark mark : getAssociatedMarks()) {
			if(mark.getEvaluation().equals(evaluation)) {
				return mark;
			}
		}
		return null;
	}

    public List<Mark> getAssociatedMarksOrderedByEvaluationDate() {
        final List<Evaluation> orderedEvaluations = getDisciplinaExecucao().getOrderedAssociatedEvaluations();
        final List<Mark> orderedMarks = new ArrayList<Mark>(orderedEvaluations.size());
        for (int i = 0; i < orderedEvaluations.size(); i++) {
            orderedMarks.add(null);
        }
        for (final Mark mark : getAssociatedMarks()) {
            final Evaluation evaluation = mark.getEvaluation();
            orderedMarks.set(orderedEvaluations.indexOf(evaluation), mark);
        }
        return orderedMarks;
    }
}
