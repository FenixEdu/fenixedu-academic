/*
 * Created on 29/Fev/2004
 */
package ServidorPersistente.OJB.credits;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.credits.ManagementPositionCreditLine;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.credits.IPersistentManagementPositionCreditLine;

/**
 * @author jpvl
 */
public class ManagementPositionCreditLineOJB extends PersistentObjectOJB implements
        IPersistentManagementPositionCreditLine {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentManagementPosistionCreditLine#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addGreaterThan("end", executionPeriod.getBeginDate());
        criteria.addLessThan("start", executionPeriod.getEndDate());

        return queryList(ManagementPositionCreditLine.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentManagementPositionCreditLine#readByTeacher(java.lang.Integer)
     */
    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());

        return queryList(ManagementPositionCreditLine.class, criteria);
    }

}