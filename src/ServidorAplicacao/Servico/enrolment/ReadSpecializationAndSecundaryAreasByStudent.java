package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import DataBeans.util.Cloner;
import Dominio.IBranch;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.BranchType;
import Util.TipoCurso;

/**
 * @author Fernanda Quitério 31/Jan/2004
 *  
 */
public class ReadSpecializationAndSecundaryAreasByStudent implements IService
{
	public ReadSpecializationAndSecundaryAreasByStudent()
	{
	}
	
	// some of these arguments may be null. they are only needed for filter
	public List run(Integer executionDegreeId, Integer studentCurricularPlanId, Integer studentNumber) throws FenixServiceException
	{
		List finalAreas = null;
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO =
				persistentSuport.getIStudentCurricularPlanPersistente();

			IStudent student =
				studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);

			if (student == null)
			{
				throw new ExistingServiceException("student");
			}
			IStudentCurricularPlan studentCurricularPlan =
				studentCurricularPlanDAO.readActiveStudentCurricularPlan(
					student.getNumber(),
					student.getDegreeType());

			if (studentCurricularPlan == null)
			{
				throw new ExistingServiceException("studentCurricularPlan");
			}

			IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
			List areas =
				branchDAO.readByDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());
			if (areas != null)
			{
				finalAreas = new ArrayList();
				Iterator iterator = areas.iterator();
				while (iterator.hasNext())
				{
					IBranch area = (IBranch) iterator.next();
					if (!area.getBranchType().equals(BranchType.COMMON_BRANCH))
					{
						InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(area);
						finalAreas.add(infoBranch);
					}
				}
			}

		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return finalAreas;
	}
}