/*
 * Created on 28/Abr/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.Date;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EnrolmentPeriod;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;

/**
 * @author jpvl
 */
public class PersistentEnrolmentPeriod extends ObjectFenixOJB implements IPersistentEnrolmentPeriod
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readActualEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
    public IEnrolmentPeriod readActualEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit.addEqualTo(
            "degreeCurricularPlan.degree.sigla",
            degreeCurricularPlan.getDegree().getSigla());
        Date date = new Date();
        crit.addLessOrEqualThan("startDate", date);
        crit.addGreaterOrEqualThan("endDate", date);

        return (IEnrolmentPeriod) queryObject(EnrolmentPeriod.class, crit);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readNextEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
    public IEnrolmentPeriod readNextEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan plan)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", plan.getName());
        crit.addEqualTo("degreeCurricularPlan.degree.sigla", plan.getDegree().getSigla());
        Date date = new Date();
        crit.addGreaterThan("startDate", date);
        crit.addOrderBy("startDate", true);
        return (IEnrolmentPeriod) queryObject(EnrolmentPeriod.class, crit);

    }

    public EnrolmentPeriod readEnrolmentPeriodByKeyAndDegreeCurricularPlan(
        Integer key,
        IDegreeCurricularPlan plan)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", key);
        criteria.addEqualTo("keyDegreeCurricularPlan", plan.getIdInternal());
        return (EnrolmentPeriod) queryObject(EnrolmentPeriod.class, criteria);
    }

}
