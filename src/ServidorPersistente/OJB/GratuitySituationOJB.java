/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GratuitySituation;
import Dominio.IDegreeCurricularPlan;
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
	
	public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		
		return (List) queryList(GratuitySituation.class, criteria);
	}
}
