/*
 * IFrequentaPersistente.java
 *
 * Created on 20 de Outubro de 2002, 15:28
 */
package ServidorPersistente;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import Util.TipoCurso;

import Dominio.IEnrollment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;

public interface IFrequentaPersistente extends IPersistentObject {
    public IFrequenta readByAlunoIdAndDisciplinaExecucaoId(Integer alunoId, Integer disciplinaExecucaoId)
            throws ExcepcaoPersistencia;

    public IFrequenta readByAlunoAndDisciplinaExecucao(IStudent aluno,
            IExecutionCourse disciplinaExecucao) throws ExcepcaoPersistencia;

    public void delete(IFrequenta frequenta) throws ExcepcaoPersistencia;

    public Integer countStudentsAttendingExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia;

    public List readByStudentNumber(Integer id, TipoCurso tipoCurso) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public IFrequenta readByEnrolment(IEnrollment enrolment) throws ExcepcaoPersistencia;

    public List readByUsername(String username) throws ExcepcaoPersistencia;
}