/*
 * Created on 29/Fev/2004
 */
package ServidorPersistente.OJB.credits;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.credits.ServiceExemptionCreditLine;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;

/**
 * @author jpvl
 */
public class ServiceExemptionCreditLineOJB extends ObjectFenixOJB implements IPersistentServiceExemptionCreditLine
{

    /* (non-Javadoc)
     * @see ServidorPersistente.credits.IPersistentServiceExemptionCreditLine#readByTeacherAndExecutionPeriod(Dominio.ITeacher, Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addGreaterThan("end", executionPeriod.getBeginDate());
        criteria.addLessThan("start", executionPeriod.getEndDate());
        
        return queryList(ServiceExemptionCreditLine.class, criteria);
    }
    
    /* (non-Javadoc)
     * @see ServidorPersistente.credits.IPersistentManagementPositionCreditLine#readByTeacher(java.lang.Integer)
     */
    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        
        return queryList(ServiceExemptionCreditLine.class, criteria);
    }
    

}
