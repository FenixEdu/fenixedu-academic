/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public List getAttendsSets() {
        List attendsSets = new ArrayList();
        Iterator iterAttendInAttendsSet = getAttendInAttendsSet().iterator();
        IAttendInAttendsSet attendInAttendsSet = null;
        while (iterAttendInAttendsSet.hasNext()) {
            attendInAttendsSet = (IAttendInAttendsSet) iterAttendInAttendsSet.next();
            attendsSets.add(attendInAttendsSet.getAttendsSet());
        }
        return attendsSets;
    }

    public boolean existsAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
        return getAttendInAttendsSet().contains(attendInAttendsSet);
    }
	
	public void delete() throws DomainException {
		
		if (!hasAnyAttendInAttendsSet() && !hasAnyStudentGroupAttends() && !hasMark()) {
			removeAluno();
			removeDisciplinaExecucao();
			removeEnrolment();
			super.deleteDomainObject();
		}
		else
			throw new DomainException(this.getClass().getName(), "ola mundo");
	}

}
