/*
 * Created on 28/Abr/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EnrolmentPeriodInClasses;
import Dominio.EnrolmentPeriodInCurricularCourses;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriodInClasses;
import Dominio.IEnrolmentPeriodInCurricularCourses;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import Util.PeriodState;

/**
 * @author jpvl
 */
public class PersistentEnrolmentPeriod extends PersistentObjectOJB implements IPersistentEnrolmentPeriod {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readActualEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
     */
    public IEnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
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
            return (IEnrolmentPeriodInCurricularCourses) result.get(0);
        }
        return null;

        //return (IEnrolmentPeriodInCurricularCourses)
        // queryObject(EnrolmentPeriodInCurricularCourses.class, crit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readNextEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
     */
    public IEnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan plan) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", plan.getName());
        crit.addEqualTo("degreeCurricularPlan.degree.sigla", plan.getDegree().getSigla());
        Date date = new Date();
        crit.addGreaterThan("startDate", date);

        List result = queryList(EnrolmentPeriodInCurricularCourses.class, crit, "startDate", true);

        if (result != null && !result.isEmpty()) {
            return (IEnrolmentPeriodInCurricularCourses) result.get(0);
        }
        return null;
    }

    public EnrolmentPeriodInCurricularCourses readEnrolmentPeriodByKeyAndDegreeCurricularPlan(
            Integer key, IDegreeCurricularPlan plan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", key);
        criteria.addEqualTo("keyDegreeCurricularPlan", plan.getIdInternal());
        return (EnrolmentPeriodInCurricularCourses) queryList(EnrolmentPeriodInCurricularCourses.class,
                criteria).get(0);
    }

    public IEnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("executionPeriod.state", PeriodState.CURRENT);
        // There seems to be a bug with the queryObject when using extents
        // So we'll use queryList for now.
        List result = queryList(EnrolmentPeriodInClasses.class, criteria);
        if (result != null && !result.isEmpty()) {
            return (IEnrolmentPeriodInClasses) result.get(0);
        }
        return null;

        //return (IEnrolmentPeriodInClasses)
        // queryObject(EnrolmentPeriodInClasses.class, criteria);
    }

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(EnrolmentPeriodInCurricularCourses.class, criteria);
    }

    public List readAllEnrollmentPeriodsInClasses() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(EnrolmentPeriodInClasses.class, criteria);
    }
}