/*
 * Created on 23/Abr/2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EvaluationMethod;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author João Mota
 * 
 *  
 */
public class EvaluationMethodOJB extends ObjectFenixOJB implements IPersistentEvaluationMethod
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentEvaluationMethod#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
    public IEvaluationMethod readByExecutionCourse(IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "executionCourse.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "executionCourse.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());

        return (IEvaluationMethod) queryObject(EvaluationMethod.class, crit);
    }

    public IEvaluationMethod readByIdExecutionCourse(IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(
            "keyExecutionCourse",
            new Integer(String.valueOf(executionCourse.getIdInternal())));
        return (IEvaluationMethod) queryObject(EvaluationMethod.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentEvaluationMethod#readAll()
	 */
    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(EvaluationMethod.class, new Criteria());
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentEvaluationMethod#delete(Dominio.IEvaluationMethod)
	 */
    public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia
    {
        super.delete(evaluation);
        //TODO: add to the execution Course delete methods to delete all
        // evaluations, all curriculums and all sites
    }

   

    public void lockWrite(IEvaluationMethod evaluation) throws ExcepcaoPersistencia
    {
        IEvaluationMethod evaluationFromDB = null;

        // If there is nothing to write, simply return.
        if (evaluation == null)
        {
            return;
        }

        // Read evaluation method from database.
        evaluationFromDB = this.readByIdExecutionCourse(evaluation.getExecutionCourse());

        // If evaluation methodp is not in database, then write it.
        if (evaluationFromDB == null)
        {
            super.lockWrite(evaluation);
        }
        else
        {

            if (evaluation.getIdInternal() == null)
            {
                evaluation.setIdInternal(evaluationFromDB.getIdInternal());
            }

            if (evaluationFromDB.getIdInternal().equals(evaluation.getIdInternal()))
            {
                // else If the evaluation method is mapped to the database,
                // then write any existing changes.
                super.lockWrite(evaluation);
                // else Throw an already existing exception
            }
            else
            {
                throw new ExistingPersistentException();
            }
        }
    }
}
