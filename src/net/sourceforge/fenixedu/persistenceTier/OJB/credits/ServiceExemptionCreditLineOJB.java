/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.credits;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class ServiceExemptionCreditLineOJB extends PersistentObjectOJB implements
        IPersistentServiceExemptionCreditLine {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentServiceExemptionCreditLine#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addGreaterThan("end", executionPeriod.getBeginDate());
        criteria.addLessThan("start", executionPeriod.getEndDate());

        return queryList(ServiceExemptionCreditLine.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentManagementPositionCreditLine#readByTeacher(java.lang.Integer)
     */
    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());

        return queryList(ServiceExemptionCreditLine.class, criteria);
    }

}