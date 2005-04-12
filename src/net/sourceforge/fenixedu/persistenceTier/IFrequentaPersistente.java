/*
 * IFrequentaPersistente.java
 *
 * Created on 20 de Outubro de 2002, 15:28
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.TipoCurso;

public interface IFrequentaPersistente extends IPersistentObject {
    public IAttends readByAlunoIdAndDisciplinaExecucaoId(Integer alunoId, Integer disciplinaExecucaoId)
            throws ExcepcaoPersistencia;

    public IAttends readByAlunoAndDisciplinaExecucao(IStudent aluno,
            IExecutionCourse disciplinaExecucao) throws ExcepcaoPersistencia;

    public void delete(IAttends frequenta) throws ExcepcaoPersistencia;

    public Integer countStudentsAttendingExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia;

    public List readByStudentNumber(Integer id, TipoCurso tipoCurso) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public IAttends readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia;

    public List readByUsername(String username) throws ExcepcaoPersistencia;
}