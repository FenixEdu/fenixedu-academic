package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CreditsInScientificArea;
import Dominio.ICreditsInScientificArea;
import Dominio.IEnrollment;
import Dominio.IScientificArea;
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

	public ICreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
		IStudentCurricularPlan studentCurricularPlan,
		IEnrollment enrolment,
		IScientificArea scientificArea)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		criteria.addEqualTo("scientificArea.idInternal", scientificArea.getIdInternal());
		return (ICreditsInScientificArea) queryObject(CreditsInScientificArea.class, criteria);
	}
}