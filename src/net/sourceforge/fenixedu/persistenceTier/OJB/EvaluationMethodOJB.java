/*
 * Created on 23/Abr/2003 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 */
public class EvaluationMethodOJB extends PersistentObjectOJB implements IPersistentEvaluationMethod {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEvaluationMethod#readByExecutionCourse(Dominio.IDisciplinaExecucao)
     */
    public IEvaluationMethod readByExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());

        return (IEvaluationMethod) queryObject(EvaluationMethod.class, crit);
    }

    public IEvaluationMethod readByIdExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", new Integer(String.valueOf(executionCourse
                .getIdInternal())));
        return (IEvaluationMethod) queryObject(EvaluationMethod.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEvaluationMethod#readAll()
     */
    public List readAll() throws ExcepcaoPersistencia {
        return queryList(EvaluationMethod.class, new Criteria());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentEvaluationMethod#delete(Dominio.IEvaluationMethod)
     */
    public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia {
        super.delete(evaluation);
        //TODO: add to the execution Course delete methods to delete all
        // evaluations, all curriculums and all sites
    }

}