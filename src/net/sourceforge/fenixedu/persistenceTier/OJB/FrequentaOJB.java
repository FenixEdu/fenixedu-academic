package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

public class FrequentaOJB extends PersistentObjectOJB implements IFrequentaPersistente {

    public List readByDegreeCurricularPlanAndExecutionPeriodOrderedByStudentId(
            Integer degreeCurricularPlanId, Integer executionPeriodId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.studentCurricularPlan.degreeCurricularPlan.idInternal",
                degreeCurricularPlanId);
        // criteria.addEqualTo("enrolment.executionPeriod.idInternal",
        // executionPeriodId);
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriodId);
        criteria.addOrderBy("aluno.idInternal");
        return queryList(Attends.class, criteria);
    }

    public List readByUsername(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.person.username", username);
        return queryList(Attends.class, crit);
    }

    public Attends readByAlunoAndDisciplinaExecucao(Integer studentID, Integer executionCourseID)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.idInternal", studentID);
        crit.addEqualTo("chaveDisciplinaExecucao", executionCourseID);
        return (Attends) queryObject(Attends.class, crit);

    }

    public List readByStudentNumber(Integer id, DegreeType tipoCurso) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("aluno.number", id);
        criteria.addEqualTo("aluno.degreeType", tipoCurso);
        return queryList(Attends.class, criteria);
    }

    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.number", number);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.state", PeriodState.CURRENT);

        return queryList(Attends.class, crit);
    }

    public List readByStudentIdAndExecutionPeriodId(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.idInternal", studentId);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriodId);

        return queryList(Attends.class, crit);
    }

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourseID);
        return queryList(Attends.class, crit);
    }

    public Integer countStudentsAttendingExecutionCourse(ExecutionCourse executionCourse) {
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        Query queryCriteria = new QueryByCriteria(Attends.class, criteria);
        return new Integer(pb.getCount(queryCriteria));
    }

    public Attends readByEnrolment(Enrolment enrolment) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (Attends) queryObject(Attends.class, crit);
    }
}
