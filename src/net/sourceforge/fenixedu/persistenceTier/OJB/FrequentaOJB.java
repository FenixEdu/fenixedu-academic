/*
 * FrequentaOJB.java
 * 
 * Created on 20 de Outubro de 2002, 15:36
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

public class FrequentaOJB extends PersistentObjectOJB implements IFrequentaPersistente {

    public List readByUsername(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.person.username", username);
        return queryList(Attends.class, crit);
    }

    public IAttends readByAlunoAndDisciplinaExecucao(IStudent aluno,
            IExecutionCourse disciplinaExecucao) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.idInternal", aluno.getIdInternal());
        crit.addEqualTo("chaveDisciplinaExecucao", disciplinaExecucao.getIdInternal());
        return (IAttends) queryObject(Attends.class, crit);

    }

    //by gedl AT rnl DOT IST DOT UTL DOT PT , september the 16th, 2003
    public IAttends readByAlunoIdAndDisciplinaExecucaoId(Integer alunoId, Integer disciplinaExecucaoId)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("chaveAluno", alunoId);
        crit.addEqualTo("chaveDisciplinaExecucao", disciplinaExecucaoId);
        return (IAttends) queryObject(Attends.class, crit);

    }

    public void delete(IAttends frequenta) throws ExcepcaoPersistencia {
        super.delete(frequenta);
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

    public List readByStudentIdAndExecutionPeriodId(Integer studentId, Integer executionPeriodId) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.idInternal", studentId);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriodId);

        return queryList(Attends.class, crit);
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        return queryList(Attends.class, crit);
    }

    public Integer countStudentsAttendingExecutionCourse(IExecutionCourse executionCourse) {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        Query queryCriteria = new QueryByCriteria(Attends.class, criteria);
        return new Integer(pb.getCount(queryCriteria));
    }

    public IAttends readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (IAttends) queryObject(Attends.class, crit);
    }
}