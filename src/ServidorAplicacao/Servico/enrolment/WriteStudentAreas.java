package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
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
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.BranchType;
import Util.EnrolmentState;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas implements IService
{
	public WriteStudentAreas()
	{
	}

	// some of these arguments may be null. they are only needed for filter
	public void run(
		Integer executionDegreeId, 
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
		final IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();

		IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
		
		List enrollmentsWithAprovedState =
			enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.APROVED);
		
		List enrollmentsWithEnrolledState =
		enrolmentDAO.readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
				studentCurricularPlan,
				EnrolmentState.ENROLED,
				executionPeriod);

		List studentApprovedEnrollments = (List) CollectionUtils.select(enrollmentsWithAprovedState, new Predicate()
		{
			public boolean evaluate(Object obj)
			{
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse().getDegreeCurricularPlan().equals(
					studentCurricularPlan.getDegreeCurricularPlan());
			}
		});
		
		List studentCurrentSemesterEnrollments = (List) CollectionUtils.select(enrollmentsWithEnrolledState, new Predicate()
		{
			public boolean evaluate(Object obj)
			{
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse().getDegreeCurricularPlan().equals(
					studentCurricularPlan.getDegreeCurricularPlan());
			}
		});
		
		final List baseAreasCurricularCourses =
			getBaseAreasCurricularCourses(
				studentApprovedEnrollments,
				studentCurrentSemesterEnrollments,
				executionPeriod,
				studentCurricularPlan);
		
		List studentEnrollmentsTemp = new ArrayList();
		studentEnrollmentsTemp.addAll(studentApprovedEnrollments);
		studentEnrollmentsTemp.addAll(studentCurrentSemesterEnrollments);
		
		List studentEnrollments = (List) CollectionUtils.select(studentEnrollmentsTemp, new Predicate()
		{
			public boolean evaluate(Object obj)
			{
				IEnrolment enrolment = (IEnrolment) obj;
				return !baseAreasCurricularCourses.contains(enrolment.getCurricularCourse());
			}
		});
		
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
		
		Iterator iterator = studentEnrollments.iterator();
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

	/**
	 * @param enrollmentsWithAprovedState
	 * @param enrollmentsWithEnrolledState
	 * @return BaseAreasCurricularCourses
	 * @throws ExcepcaoPersistencia
	 */
	private List getBaseAreasCurricularCourses(
			List enrollmentsWithAprovedState,
			List enrollmentsWithEnrolledState,
			IExecutionPeriod executionPeriod,
			IStudentCurricularPlan studentCurricularPlan)
	throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
		
		List baseAreasCurricularCourses = new ArrayList();

		List baseBranches =
		branchDAO.readAllByDegreeCurricularPlanAndBranchType(
				studentCurricularPlan.getDegreeCurricularPlan(),
				BranchType.COMMON_BRANCH);

		Iterator iterator = baseBranches.iterator();
		while (iterator.hasNext())
		{
			IBranch baseArea = (IBranch) iterator.next();
			List groups = curricularCourseGroupDAO.readByBranchAndAreaType(baseArea, AreaType.BASE_OBJ);
			Iterator iterator2 = groups.iterator();
			while (iterator2.hasNext())
			{
				ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator2.next();
				baseAreasCurricularCourses.addAll(curricularCourseGroup.getCurricularCourses());
			}
		}

//		selectDesiredCurricularCourses(enrollmentsWithAprovedState, baseAreasCurricularCourses);
//		selectDesiredCurricularCourses(enrollmentsWithEnrolledState, baseAreasCurricularCourses);
//		selectDesiredCurricularCourses(baseAreasCurricularCourses, executionPeriod.getSemester());

		return baseAreasCurricularCourses;
	}

//	/**
//	 * @param enrollmentsInCurricularCourseToRemove
//	 * @param curricularCoursesToRemoveFrom
//	 */
//	private void selectDesiredCurricularCourses(List enrollmentsInCurricularCourseToRemove, List curricularCoursesToRemoveFrom)
//	{
//		List curricularCoursesToRemove = new ArrayList();
//		Iterator iterator = enrollmentsInCurricularCourseToRemove.iterator();
//		while(iterator.hasNext())
//		{
//			IEnrolment enrolment = (IEnrolment) iterator.next();
//			if (curricularCoursesToRemoveFrom.contains(enrolment.getCurricularCourse()))
//			{
//				curricularCoursesToRemove.add(enrolment.getCurricularCourse());
//			}
//		}
//		curricularCoursesToRemoveFrom.removeAll(curricularCoursesToRemove);
//	}
//
//	/**
//	 * @param curricularCoursesToRemoveFrom
//	 * @param semester
//	 */
//	private void selectDesiredCurricularCourses(List curricularCoursesToRemoveFrom, Integer semester)
//	{
//		List curricularCoursesToRemove = new ArrayList();
//		Iterator iterator = curricularCoursesToRemoveFrom.iterator();
//		while(iterator.hasNext())
//		{
//			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
//			List scopes = curricularCourse.getScopes();
//			boolean courseIsToMantain = false;
//			Iterator iteratorScopes = scopes.iterator();
//			while(iteratorScopes.hasNext())
//			{
//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopes.next();
//				if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester))
//				{
//					courseIsToMantain = true;
//				}
//			}
//			if (!courseIsToMantain)
//			{
//				curricularCoursesToRemove.add(curricularCourse);
//			}
//		}
//		curricularCoursesToRemoveFrom.removeAll(curricularCoursesToRemove);
//	}
	
}