/*
 * FrequentaOJB.java
 * 
 * Created on 20 de Outubro de 2002, 15:36
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.Frequenta;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;

public class FrequentaOJB extends ObjectFenixOJB implements IFrequentaPersistente
{

    public List readByUsername(String username) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.person.username", username);
        return queryList(Frequenta.class, crit);
    }

    public IFrequenta readByAlunoAndDisciplinaExecucao(
        IStudent aluno,
        IExecutionCourse disciplinaExecucao)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.idInternal", aluno.getIdInternal());
        crit.addEqualTo("chaveDisciplinaExecucao", disciplinaExecucao.getIdInternal());
        return (IFrequenta) queryObject(Frequenta.class, crit);

    }

    //by gedl AT rnl DOT IST DOT UTL DOT PT , september the 16th, 2003
    public IFrequenta readByAlunoIdAndDisciplinaExecucaoId(
        Integer alunoId,
        Integer disciplinaExecucaoId)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("chaveAluno", alunoId);
        crit.addEqualTo("chaveDisciplinaExecucao", disciplinaExecucaoId);
        return (IFrequenta) queryObject(Frequenta.class, crit);

    }

    public void lockWrite(IFrequenta attendanceToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IFrequenta attendanceFromDB = null;

        // If there is nothing to write, simply return.
        if (attendanceToWrite == null)
            return;

        // Read attendance from database.
        attendanceFromDB =
            this.readByAlunoAndDisciplinaExecucao(
                attendanceToWrite.getAluno(),
                attendanceToWrite.getDisciplinaExecucao());

        // If attendance is not in database, then write it.
        if (attendanceFromDB == null)
            super.lockWrite(attendanceToWrite);
        // else If the attendance is mapped to the database, then write any
        // existing changes.
        else if (
            (attendanceToWrite instanceof Frequenta)
                && ((Frequenta) attendanceFromDB).getIdInternal().equals(
                    ((Frequenta) attendanceToWrite).getIdInternal()))
        {
            super.lockWrite(attendanceToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(IFrequenta frequenta) throws ExcepcaoPersistencia
    {
        super.delete(frequenta);
    }

    

    public List readByStudentNumber(Integer id) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("aluno.number", id);
        return queryList(Frequenta.class, criteria);
    }
    public List readByStudentNumberInCurrentExecutionPeriod(Integer number) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.number", number);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.state", PeriodState.CURRENT);

        return queryList(Frequenta.class, crit);
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        return queryList(Frequenta.class, crit);
    }

    public Integer countStudentsAttendingExecutionCourse(IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        Query queryCriteria = new QueryByCriteria(Frequenta.class, criteria);
        return new Integer(pb.getCount(queryCriteria));
    }

    public IFrequenta readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (IFrequenta) queryObject(Frequenta.class, crit);
    }
}