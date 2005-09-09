/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * 
 * @author tfc130
 */
public class Attends extends Attends_Base {

	public Attends() {}
	
	public Attends (IStudent student, IExecutionCourse executionCourse) {
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
		
		if (!hasAnyStudentGroups() && !hasMark() && !hasEnrolment()) {
			removeAluno();
			removeDisciplinaExecucao();
			super.deleteDomainObject();
		}
		else
			throw new DomainException("error.attends.cant.delete");
	}

}
