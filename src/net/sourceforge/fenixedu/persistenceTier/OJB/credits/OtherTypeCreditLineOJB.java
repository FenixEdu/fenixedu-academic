/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.credits;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;

import org.apache.ojb.broker.query.Criteria;

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