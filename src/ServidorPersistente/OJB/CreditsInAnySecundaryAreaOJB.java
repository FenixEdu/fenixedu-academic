package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CreditsInAnySecundaryArea;
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

//	public IBranch readByDegreeCurricularPlanAndBranchName(IDegreeCurricularPlan degreeCurricularPlan, String branchName) throws ExcepcaoPersistencia {
//		Criteria crit = new Criteria();
//		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
//		crit.addEqualTo("name", branchName);
//		return (IBranch) queryObject(Branch.class, crit);
//	}
}