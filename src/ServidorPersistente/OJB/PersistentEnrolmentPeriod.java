/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import java.util.Date;
import java.util.List;

import org.odmg.QueryException;

import Dominio.EnrolmentPeriod;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class PersistentEnrolmentPeriod extends ObjectFenixOJB implements IPersistentEnrolmentPeriod {

	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readActualEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
	public IEnrolmentPeriod readActualEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia{
		String queryString = "select actual from "+EnrolmentPeriod.class.getName()+
					   " where degreeCurricularPlan.name = $1 "+
					   " and degreeCurricularPlan.degree.sigla = $2 "+
					   " and startDate <= $3 and endDate >= $4 ";
		IEnrolmentPeriod enrolmentPeriod = null;
		try {
			query.create(queryString);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			Date date = new Date();
			
			query.bind(date);
			query.bind(date);
			
			List list = (List) query.execute();
			
			if (!list.isEmpty())
				enrolmentPeriod = (IEnrolmentPeriod) list.get(0);
			
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
	    }
		return enrolmentPeriod;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readNextEnrolmentPeriodForDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
	public IEnrolmentPeriod readNextEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan plan) throws ExcepcaoPersistencia {
		String queryString = "select actual from "+EnrolmentPeriod.class.getName()+
					   " where degreeCurricularPlan.name = $1 "+
					   " and degreeCurricularPlan.degree.sigla = $2 "+
					   " and startDate > $3 " +
					   " order by startDate ";
		IEnrolmentPeriod enrolmentPeriod = null;
		try {
			Date date = new Date();
			
			query.create(queryString);
			query.bind(plan.getName());
			query.bind(plan.getDegree().getSigla());

			query.bind(date);
			
			List list = (List) query.execute();
			
			if (!list.isEmpty())
				enrolmentPeriod = (IEnrolmentPeriod) list.get(0);
			
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
		return enrolmentPeriod;
	}
}
