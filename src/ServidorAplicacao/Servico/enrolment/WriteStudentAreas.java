package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
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
			IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

			IBranch specializationArea = (IBranch) branchDAO.readByOID(Branch.class, specializationAreaID);

			IBranch secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);

			if (studentCurricularPlan == null)
			{
				throw new ExistingServiceException();
			}

			if (specializationArea != null && secundaryArea != null && specializationArea.equals(secundaryArea))
			{
				throw new BothAreasAreTheSameServiceException();
			}

			if ((specializationArea == null && secundaryArea == null) || (specializationArea != null && secundaryArea != null))
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
			} else
			{
				throw new InvalidArgumentsServiceException();
			}
		} catch (ExcepcaoPersistencia e)
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
		IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		if (specializationArea == null && secundaryArea == null)
		{
			return true;
		}

		List curricularCoursesFromGivenAreas =
			getCurricularCoursesFromGivenAreas(studentCurricularPlan, specializationArea, secundaryArea);

		List curricularCoursesBelongingToAnySpecializationAndSecundaryArea =
			getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea(studentCurricularPlan);

		List studentsAprovedEnrollments = getStudentsAprovedEnrollments(studentCurricularPlan);

		Iterator iterator = studentsAprovedEnrollments.iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (curricularCoursesBelongingToAnySpecializationAndSecundaryArea
				.contains(enrolment.getCurricularCourse())
				&& !curricularCoursesFromGivenAreas.contains(enrolment.getCurricularCourse()))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * @param studentCurricularPlan
	 * @return CurricularCoursesBelongingToAnySpecializationAndSecundaryArea
	 * @throws ExcepcaoPersistencia
	 */
	private List getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();

		List curricularCourses = new ArrayList();

		List specializationAreas =
			branchDAO.readAllByDegreeCurricularPlanAndBranchType(
				studentCurricularPlan.getDegreeCurricularPlan(),
				BranchType.SPECIALIZATION_BRANCH);

		List secundaryAreas =
			branchDAO.readAllByDegreeCurricularPlanAndBranchType(
				studentCurricularPlan.getDegreeCurricularPlan(),
				BranchType.SECUNDARY_BRANCH);

		addAreasCurricularCoursesWithoutRepetitions(curricularCourses, specializationAreas, AreaType.SPECIALIZATION_OBJ);
		addAreasCurricularCoursesWithoutRepetitions(curricularCourses, secundaryAreas, AreaType.SECONDARY_OBJ);

		return curricularCourses;
	}

	/**
	 * @param curricularCourses
	 * @param specializationAreas
	 * @throws ExcepcaoPersistencia
	 */
	private void addAreasCurricularCoursesWithoutRepetitions(List curricularCourses, List areas, AreaType areaType)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		Iterator iterator = areas.iterator();
		while (iterator.hasNext())
		{
			IBranch area = (IBranch) iterator.next();
			List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);
			Iterator iterator2 = groups.iterator();
			while (iterator2.hasNext())
			{
				ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator2.next();
				Iterator iterator3 = curricularCourseGroup.getCurricularCourses().iterator();
				while (iterator3.hasNext())
				{
					ICurricularCourse curricularCourse = (ICurricularCourse) iterator3.next();
					if (!curricularCourses.contains(curricularCourse))
					{
						curricularCourses.add(curricularCourse);
					}
				}
			}
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @return StudentsAprovedEnrollments
	 * @throws ExcepcaoPersistencia
	 */
	private List getStudentsAprovedEnrollments(final IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();

		List enrollmentsWithAprovedState =
			enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.APROVED);

		List studentApprovedEnrollments = (List) CollectionUtils.select(enrollmentsWithAprovedState, new Predicate()
		{
			public boolean evaluate(Object obj)
			{
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse().getDegreeCurricularPlan().equals(
					studentCurricularPlan.getDegreeCurricularPlan());
			}
		});

		return studentApprovedEnrollments;
	}

	/**
	 * @param studentCurricularPlan
	 * @param specializationArea
	 * @param secundaryArea
	 * @return CurricularCoursesFromGivenAreas
	 * @throws ExcepcaoPersistencia
	 */
	private List getCurricularCoursesFromGivenAreas(
		IStudentCurricularPlan studentCurricularPlan,
		IBranch specializationArea,
		IBranch secundaryArea)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		List curricularCoursesFromNewSpecializationArea = new ArrayList();
		List curricularCoursesFromNewSecundaryArea = new ArrayList();

		List groups = curricularCourseGroupDAO.readByBranchAndAreaType(specializationArea, AreaType.SPECIALIZATION_OBJ);
		Iterator iterator = groups.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
			curricularCoursesFromNewSpecializationArea.addAll(curricularCourseGroup.getCurricularCourses());
		}

		groups = curricularCourseGroupDAO.readByBranchAndAreaType(secundaryArea, AreaType.SECONDARY_OBJ);
		iterator = groups.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
			curricularCoursesFromNewSecundaryArea.addAll(curricularCourseGroup.getCurricularCourses());
		}

		List newCurricularCourses = new ArrayList();
		newCurricularCourses.addAll(curricularCoursesFromNewSpecializationArea);
		newCurricularCourses.addAll(curricularCoursesFromNewSecundaryArea);

		return newCurricularCourses;
	}

}