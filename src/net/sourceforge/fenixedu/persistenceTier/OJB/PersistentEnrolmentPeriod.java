/*
 * Created on 28/Abr/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class PersistentEnrolmentPeriod extends PersistentObjectOJB implements IPersistentEnrolmentPeriod {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readActualEnrolmentPeriodForDegreeCurricularPlan(Dominio.DegreeCurricularPlan)
     */
    public EnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
		
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit
                .addEqualTo("degreeCurricularPlan.degree.sigla", degreeCurricularPlan.getDegree()
                        .getSigla());
        Date date = new Date();
        crit.addLessOrEqualThan("startDate", date);
        crit.addGreaterOrEqualThan("endDate", date);

        List result = queryList(EnrolmentPeriodInCurricularCourses.class, crit);
        if (result != null && !result.isEmpty()) {
            return (EnrolmentPeriodInCurricularCourses) result.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readNextEnrolmentPeriodForDegreeCurricularPlan(Dominio.DegreeCurricularPlan)
     */
    public EnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
		
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit.addEqualTo("degreeCurricularPlan.degree.sigla", degreeCurricularPlan.getDegree().getSigla());
        Date date = new Date();
        crit.addGreaterThan("startDate", date);

        List result = queryList(EnrolmentPeriodInCurricularCourses.class, crit, "startDate", true);

        if (result != null && !result.isEmpty()) {
            return (EnrolmentPeriodInCurricularCourses) result.get(0);
        }
        return null;
    }



    public EnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
		
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("executionPeriod.state", PeriodState.CURRENT);
        criteria.addEqualTo("ojbConcreteClass","net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses");
        
        // There seems to be a bug with the queryObject when using extents
        // So we'll use queryList for now.
        List result = queryList(EnrolmentPeriodInClasses.class, criteria);
        if (result != null && !result.isEmpty()) {
            return (EnrolmentPeriodInClasses) result.get(0);
        }
        return null;

    }

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(EnrolmentPeriodInCurricularCourses.class, criteria);
    }

}