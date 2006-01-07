package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface IFrequentaPersistente extends IPersistentObject {

    public List readByDegreeCurricularPlanAndExecutionPeriodOrderedByStudentId(
            Integer degreeCurricularPlanId, Integer executionPeriodId) throws ExcepcaoPersistencia;

    public Attends readByAlunoAndDisciplinaExecucao(Integer studentID, Integer executionCourseID)
            throws ExcepcaoPersistencia;

    public Integer countStudentsAttendingExecutionCourse(ExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia;

    public List readByStudentIdAndExecutionPeriodId(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia;

    public List readByStudentNumber(Integer id, DegreeType tipoCurso) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

    public Attends readByEnrolment(Enrolment enrolment) throws ExcepcaoPersistencia;

    public List readByUsername(String username) throws ExcepcaoPersistencia;
}