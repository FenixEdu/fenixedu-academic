/*
 * Created on 29/Fev/2004
 */
package ServidorPersistente.OJB.credits;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.credits.OtherTypeCreditLine;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.credits.IPersistentOtherTypeCreditLine;

/**
 * @author jpvl
 */
public class OtherTypeCreditLineOJB extends PersistentObjectOJB implements
        IPersistentOtherTypeCreditLine {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentOtherTypeCreditLine#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(OtherTypeCreditLine.class, criteria);
    }

}