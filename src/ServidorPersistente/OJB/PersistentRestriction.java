/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.ICurricularCourse;
import Dominio.Restriction;
import ServidorPersistente.*;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class PersistentRestriction
	extends ObjectFenixOJB
	implements IPersistentRestriction {

	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.IPersistentRestriction#readByCurricularCourse(Dominio.ICurricularCourse)
	 */
	public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		String oqlQuery = " select all from "+ Restriction.class.getName()
						+	" where precedence.curricularCourse.name = $1"
						+   " and precedence.curricularCourse.code = $2"
						+   " and precedence.curricularCourse.degreeCurricularPlan.name = $3 "
						+   " and precedence.curricularCourse.degreeCurricularPlan.degree.sigla = $4";
		List restrictionList;				
		try {
			query.create(oqlQuery);
			
			query.bind(curricularCourse.getName());
			query.bind(curricularCourse.getCode());
			query.bind(curricularCourse.getDegreeCurricularPlan().getName());
			query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
			restrictionList = (List) query.execute();
			lockRead(restrictionList);
		} catch (QueryException e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}
		
		return restrictionList;
	}

}
