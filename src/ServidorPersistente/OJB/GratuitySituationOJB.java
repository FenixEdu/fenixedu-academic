/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;

/**
 * @author Tânia Pousão
 *
 */
public class GratuitySituationOJB extends ObjectFenixOJB implements IPersistentGratuitySituation
{

	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
				
		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}
}
