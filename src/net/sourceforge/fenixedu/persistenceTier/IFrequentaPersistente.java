package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface IFrequentaPersistente extends IPersistentObject {

    public List readByDegreeCurricularPlanAndExecutionPeriodOrderedByStudentId(
            Integer degreeCurricularPlanId, Integer executionPeriodId) throws ExcepcaoPersistencia;

    public IAttends readByAlunoAndDisciplinaExecucao(Integer studentID, Integer executionCourseID)
            throws ExcepcaoPersistencia;

    public Integer countStudentsAttendingExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia;

    public List readByStudentIdAndExecutionPeriodId(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia;

    public List readByStudentNumber(Integer id, DegreeType tipoCurso) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

    public IAttends readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia;

    public List readByUsername(String username) throws ExcepcaoPersistencia;
}