package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CreditsInScientificArea;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;

public class CreditsInSpecificScientificAreaOJB extends ObjectFenixOJB implements IPersistentCreditsInSpecificScientificArea
{
	public CreditsInSpecificScientificAreaOJB()
	{
	}

	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		return queryList(CreditsInScientificArea.class, criteria);
	}
}