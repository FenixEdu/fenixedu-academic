/*
 * IAttends.java
 *
 * Created on 20 de Outubro de 2002, 15:17
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 *
 * @author  tfc130
 */
public interface IAttends extends IDomainObject {
    public IStudent getAluno();

    public IExecutionCourse getDisciplinaExecucao();

    public IEnrollment getEnrolment();

    public void setAluno(IStudent aluno);

    public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao);

    public void setEnrolment(IEnrollment enrolment);

    public Integer getChaveAluno();

    public Integer getChaveDisciplinaExecucao();

    public Integer getKeyEnrolment();

    public void setChaveAluno(Integer keyAluno);

    public void setChaveDisciplinaExecucao(Integer keyDisciplinaExecucao);

    public void setKeyEnrolment(Integer keyEnrolment);

    public List getAttendInAttendsSet();

    public void setAttendInAttendsSet(List attendInAttendsSet);
	
    public void addAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet);

    public void removeAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet);

    public boolean existsAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet);
}
