/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author tfc130
 */
public class Attends extends Attends_Base {

    public Attends() {
    }

    public Attends(IStudent aluno, IExecutionCourse disciplinaExecucao) {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
    }

    public Attends(IStudent aluno, IExecutionCourse disciplinaExecucao, IEnrolment enrolment) {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
        setEnrolment(enrolment);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IAttends) {
            IAttends frequenta = (IAttends) obj;
            resultado = getIdInternal().equals(frequenta.getIdInternal());
        }
        return resultado;
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

    public void addAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
        if (getAttendInAttendsSet() == null) {
            setAttendInAttendsSet(new ArrayList());
            getAttendInAttendsSet().add(attendInAttendsSet);
        } else {
            getAttendInAttendsSet().add(attendInAttendsSet);
        }
    }

    public void removeAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
        getAttendInAttendsSet().remove(attendInAttendsSet);
    }

    public boolean existsAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
        return getAttendInAttendsSet().contains(attendInAttendsSet);
    }

}
