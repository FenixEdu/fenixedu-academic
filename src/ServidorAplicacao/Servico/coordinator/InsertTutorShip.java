/*
 * Created on 4/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertTutorShip implements IService
{
	public Boolean verifyStudentOfThisDegree(IStudent student, TipoCurso degreeType, String degreeCode)
		throws FenixServiceException
	{
		boolean result = false;

		ISuportePersistente sp;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();

			IStudentCurricularPlan studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
					student.getNumber(),
					degreeType);

			result =
				studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
					degreeCode);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}

		return Boolean.valueOf(result);
	}
}
