package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.EnrolmentState;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas implements IService
{
	public WriteStudentAreas()
	{
	}

	public void run(
		Integer studentCurricularPlanID,
		Integer specializationAreaID,
		Integer secundaryAreaID)
		throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO =
				persistentSuport.getIStudentCurricularPlanPersistente();

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(
					StudentCurricularPlan.class,
					studentCurricularPlanID);

			IBranch specializationArea =
				(IBranch) branchDAO.readByOID(Branch.class, specializationAreaID);

			IBranch secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);

			if (studentCurricularPlan == null)
			{
				throw new ExistingServiceException();
			}

			if (specializationArea != null
				&& secundaryArea != null
				&& specializationArea.equals(secundaryArea))
			{
				throw new BothAreasAreTheSameServiceException();
			}

			if ((specializationArea == null && secundaryArea == null)
				|| (specializationArea != null && secundaryArea != null))
			{
				if (areNewAreasCompatible(specializationArea, secundaryArea, studentCurricularPlan))
				{
					studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);
					studentCurricularPlan.setBranch(specializationArea);
					studentCurricularPlan.setSecundaryBranch(secundaryArea);
				} else
				{
					throw new ChosenAreasAreIncompatibleServiceException();
				}
			} else {
				throw new InvalidArgumentsServiceException();
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param specializationArea
	 * @param secundaryArea
	 * @param studentCurricularPlan
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean areNewAreasCompatible(
		IBranch specializationArea,
		IBranch secundaryArea,
		IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		List enrollmentsWithAprovedState =
			enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.APROVED);

		List curricularCoursesFromNewSpecializationArea = new ArrayList();
		List curricularCoursesFromNewSecundaryArea = new ArrayList();
		
		if(specializationArea == null && secundaryArea == null) {
			return true;
		}
		
		if (studentCurricularPlan.getBranch() != specializationArea)
		{
			List groups = curricularCourseGroupDAO.readByBranchAndAreaType(specializationArea, AreaType.BASE_OBJ);
			Iterator iterator = groups.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
				curricularCoursesFromNewSpecializationArea.addAll(curricularCourseGroup.getCurricularCourses());
			}
		}
		
		if (studentCurricularPlan.getSecundaryBranch() != secundaryArea)
		{
			List groups = curricularCourseGroupDAO.readByBranchAndAreaType(secundaryArea, AreaType.BASE_OBJ);
			Iterator iterator = groups.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
				curricularCoursesFromNewSecundaryArea.addAll(curricularCourseGroup.getCurricularCourses());
			}
		}
		
		List newCurricularCourses = new ArrayList();
		newCurricularCourses.addAll(curricularCoursesFromNewSpecializationArea);
		newCurricularCourses.addAll(curricularCoursesFromNewSecundaryArea);
		
		
		Iterator iterator = enrollmentsWithAprovedState.iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (!newCurricularCourses.contains(enrolment.getCurricularCourse()))
			{
				return false;
			}
		}
		
		return true;
	}

}