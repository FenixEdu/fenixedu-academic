package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.ICreditsInAnySecundaryArea;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;

public class CreditsInAnySecundaryAreaOJB extends ObjectFenixOJB implements IPersistentCreditsInAnySecundaryArea
{
	public CreditsInAnySecundaryAreaOJB()
	{
	}

	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		return queryList(CreditsInAnySecundaryArea.class, criteria);
	}

	public ICreditsInAnySecundaryArea readByStudentCurricularPlanAndEnrollment(
		IStudentCurricularPlan studentCurricularPlan,
		IEnrollment enrolment)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		return (ICreditsInAnySecundaryArea) queryObject(CreditsInAnySecundaryArea.class, criteria);
	}

}