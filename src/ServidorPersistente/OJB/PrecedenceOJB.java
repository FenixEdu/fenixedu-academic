/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.Precedence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public class PrecedenceOJB extends ObjectFenixOJB implements IPersistentPrecedence {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentPrecedence#readByDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan plan, PrecedenceScopeToApply scope) throws ExcepcaoPersistencia {
		String oqlQuery = "select all from "+ Precedence.class.getName()
						+ " where curricularCourse.degreeCurricularPlan.name = $1 "
						+ " and curricularCourse.degreeCurricularPlan.degree.sigla = $2 "
						+ " and precedenceScopeToApply = $3 ";
		List precedenceList = new ArrayList();
		try {
			query.create(oqlQuery);
			query.bind(plan.getName());
			query.bind(plan.getDegree().getSigla());
			query.bind(scope);
			precedenceList = (List) query.execute();
			
			lockRead(precedenceList);
			return precedenceList;
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentPrecedence#readByCurricularCourse(Dominio.ICurricularCourse)
	 */
	public List readByCurricularCourse(ICurricularCourse curricularCourse, PrecedenceScopeToApply scope) throws ExcepcaoPersistencia {
		String oqlQuery = "select all from "+ Precedence.class.getName()
						+ " where curricularCourse.code = $1 "
						+ " and curricularCourse.name = $2 "
						+ " and curricularCourse.degreeCurricularPlan.name = $3 "
						+ " and curricularCourse.degreeCurricularPlan.degree.sigla = $4 "
						+ " and precedenceScopeToApply = $5 ";

		List precedenceList = new ArrayList();
		try {
			query.create(oqlQuery);
			
			query.bind (curricularCourse.getCode());
			query.bind(curricularCourse.getName());
			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
			query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
			query.bind(scope);
			precedenceList = (List) query.execute();
			
			lockRead(precedenceList);
			return precedenceList;
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
	}

}
