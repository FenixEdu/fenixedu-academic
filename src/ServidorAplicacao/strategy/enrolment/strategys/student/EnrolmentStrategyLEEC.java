package ServidorAplicacao.strategy.enrolment.strategys.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInScientificArea;
import Dominio.CurricularCourseGroup;
import Dominio.Enrolment;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IScientificArea;
import Dominio.IStudentCurricularPlan;
import Dominio.ScientificArea;
import ServidorAplicacao.Servico.enrolment.WriteEnrolment;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentApplyPrecedencesRule;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.BranchType;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author David Santos Jan 16, 2004
 */

public class EnrolmentStrategyLEEC extends EnrolmentStrategy implements IEnrolmentStrategy
{
	private IStudentCurricularPlan studentCurricularPlan = null;
	private boolean studentHasSpecializationArea = false;
	private boolean studentHasSecundaryArea = false;

	public EnrolmentStrategyLEEC(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
		this.studentEnrolmentContext = new StudentEnrolmentContext();
	}
	
	public StudentEnrolmentContext getAvailableCurricularCourses() throws ExcepcaoPersistencia
	{
		prepareCurricularCoursesForEnrollment();
		computeAvailableCurricularCourses();
		automaticalyEnrollInMandatoryCurricularCourses();
		return this.studentEnrolmentContext;
	}

	/**
	 * @throws ExcepcaoPersistencia
	 */
	private void computeAvailableCurricularCourses() throws ExcepcaoPersistencia
	{
		if (this.studentHasSpecializationArea && this.studentHasSecundaryArea)
		{
			List specializationAndSecundaryAreaCurricularCourses = leecAlgorithm(
					studentCurricularPlan,
					this.getStudentEnrolmentContext().getStudentApprovedEnrollments(),
					this.getStudentEnrolmentContext().getStudentCurrentSemesterEnrollments(),
					this.getStudentEnrolmentContext().getExecutionPeriod());
			
			List finalCurricularCoursesWhereStudentCanBeEnrolled =
			studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled();

			finalCurricularCoursesWhereStudentCanBeEnrolled.addAll(specializationAndSecundaryAreaCurricularCourses);

			finalCurricularCoursesWhereStudentCanBeEnrolled.addAll(getOptionalAndTFCCurricularCourses(studentEnrolmentContext));

			studentEnrolmentContext.setFinalCurricularCoursesWhereStudentCanBeEnrolled(
					finalCurricularCoursesWhereStudentCanBeEnrolled);
		}

		IEnrolmentRule enrolmentRule = new EnrolmentApplyPrecedencesRule();
		this.setStudentEnrolmentContext(enrolmentRule.apply(getStudentEnrolmentContext()));
		enrolmentRule = new EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule();
		this.setStudentEnrolmentContext(enrolmentRule.apply(getStudentEnrolmentContext()));
	}

	/**
	 * @param studentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	private void prepareCurricularCoursesForEnrollment()
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
		IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();

		IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

		List enrollmentsWithAprovedState =
			enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan, EnrolmentState.APROVED);

		List enrollmentsWithEnrolledState =
			enrolmentDAO.readAllByStudentCurricularPlanAndEnrolmentStateAndExecutionPeriod(
				studentCurricularPlan,
				EnrolmentState.ENROLED,
				executionPeriod);

		List baseAreasCurricularCourses =
			getBaseAreasCurricularCourses(enrollmentsWithAprovedState, enrollmentsWithEnrolledState, executionPeriod);

		if (studentCurricularPlan.getBranch() != null)
		{
			this.studentHasSpecializationArea = true;
		}

		if (studentCurricularPlan.getSecundaryBranch() != null)
		{
			this.studentHasSecundaryArea = true;
		}

		this.studentEnrolmentContext.setStudentCurricularPlan(this.studentCurricularPlan);
		this.studentEnrolmentContext.setExecutionPeriod(executionPeriod);
		this.studentEnrolmentContext.setStudentApprovedEnrollments(enrollmentsWithAprovedState);
		this.studentEnrolmentContext.setStudentCurrentSemesterEnrollments(enrollmentsWithEnrolledState);
		this.studentEnrolmentContext.setFinalCurricularCoursesWhereStudentCanBeEnrolled(baseAreasCurricularCourses);

		List acumulatedCurricularCourses = getStudentEverEnrolledCurricularCourses();
		this.studentEnrolmentContext.setAcumulatedEnrolments(acumulatedCurricularCourses);
	}

	/**
	 * @return studentEverEnrolledCurricularCourses
	 */
	private List getStudentEverEnrolledCurricularCourses() throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();

		List enrollments =
			enrolmentDAO.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				studentEnrolmentContext.getStudentCurricularPlan(),
				EnrolmentState.ENROLED);
		
		List studentEverEnrolledCurricularCourses = new ArrayList();
		
		if (enrollments != null && !enrollments.isEmpty())
		{
			Iterator iterator = enrollments.iterator();
			while (iterator.hasNext())
			{
				IEnrolment enrolment = (IEnrolment) iterator.next();
				studentEverEnrolledCurricularCourses.add(enrolment.getCurricularCourse());
			}
		}

		return studentEverEnrolledCurricularCourses;
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
		IExecutionPeriod executionPeriod)
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

		selectDesiredCurricularCourses(enrollmentsWithAprovedState, baseAreasCurricularCourses);
		selectDesiredCurricularCourses(enrollmentsWithEnrolledState, baseAreasCurricularCourses);
		selectDesiredCurricularCourses(baseAreasCurricularCourses, executionPeriod.getSemester());

		return baseAreasCurricularCourses;
	}

	/**
	 * @param studentCurricularPlan
	 * @param studentApprovedEnrollments
	 * @param studentCurrentSemesterEnrollments
	 * @param executionPeriod
	 * @return finalListOfCurricularCourses
	 * @throws ExcepcaoPersistencia
	 */
	private List leecAlgorithm(
		IStudentCurricularPlan studentCurricularPlan,
		List studentApprovedEnrollments,
		List studentCurrentSemesterEnrollments,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia
	{
		HashMap creditsInScientificAreas = new HashMap(); // CACx
		HashMap creditsInSpecializationAreaGroups = new HashMap(); // CGAEy
		HashMap creditsInSecundaryAreaGroups = new HashMap(); // CGASz
		int creditsInAnySecundaryArea = 0; // CAS

		getGivenCreditsInScientificArea(studentCurricularPlan, creditsInScientificAreas);

		creditsInAnySecundaryArea = getGivenCreditsInAnySecundaryArea(studentCurricularPlan);

		List specializationAreaCurricularCourses =
			selectAllCurricularCoursesFromGivenArea(studentCurricularPlan.getBranch(), AreaType.SPECIALIZATION_OBJ);
		List secundaryAreaCurricularCourses =
			selectAllCurricularCoursesFromGivenArea(studentCurricularPlan.getSecundaryBranch(), AreaType.SECONDARY_OBJ);
		List allCurricularCourses = new ArrayList();
		allCurricularCourses.addAll(specializationAreaCurricularCourses);
		allCurricularCourses.addAll(secundaryAreaCurricularCourses);

		List studentApprovedEnrollmentsFromSpecializationAndSecundaryAreas =
			selectEnrollmentsInSpecializationAndSecundaryAreasCurricularCourses(studentApprovedEnrollments, allCurricularCourses);

		List studentCurrentSemesterEnrollmentsFromSpecializationAndSecundaryAreas =
			selectEnrollmentsInSpecializationAndSecundaryAreasCurricularCourses(
				studentCurrentSemesterEnrollments,
				allCurricularCourses);
		
		calculateGroupsCreditsFromEnrollments(
			studentCurricularPlan,
			studentApprovedEnrollmentsFromSpecializationAndSecundaryAreas,
			studentCurrentSemesterEnrollmentsFromSpecializationAndSecundaryAreas,
			creditsInScientificAreas,
			creditsInSpecializationAreaGroups,
			creditsInSecundaryAreaGroups);

		calculateGroupsCreditsFromScientificAreas(
			studentCurricularPlan,
			creditsInScientificAreas,
			creditsInSpecializationAreaGroups,
			creditsInSecundaryAreaGroups);

		Integer creditsInSecundaryArea = calculateCreditsInSecundaryArea(studentCurricularPlan, creditsInSecundaryAreaGroups);
		Integer creditsInSpecializationArea =
			calculateCreditsInSpecializationArea(studentCurricularPlan, creditsInSpecializationAreaGroups);
		
		this.studentEnrolmentContext.setCreditsInSecundaryArea(creditsInSecundaryArea);
		this.studentEnrolmentContext.setCreditsInSpecializationArea(creditsInSpecializationArea);
		
		return selectCurricularCourses(
			studentCurricularPlan,
			creditsInSpecializationAreaGroups,
			creditsInSecundaryAreaGroups,
			creditsInAnySecundaryArea,
			studentApprovedEnrollmentsFromSpecializationAndSecundaryAreas,
			studentCurrentSemesterEnrollmentsFromSpecializationAndSecundaryAreas,
			executionPeriod.getSemester());
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInAnySecundaryArea
	 * @param enrollmentsWithAprovedState
	 * @param enrollmentsWithEnrolledState
	 * @param semester
	 * @return finalListOfCurricularCourses
	 * @throws ExcepcaoPersistencia
	 */
	private List selectCurricularCourses(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups,
		int creditsInAnySecundaryArea,
		List enrollmentsWithAprovedState,
		List enrollmentsWithEnrolledState,
		Integer semester)
		throws ExcepcaoPersistencia
	{
		boolean isSpecializationAreaDone = isSpecializationAreaDone(studentCurricularPlan, creditsInSpecializationAreaGroups);
		boolean isSecundaryAreaDone =
			isSecundaryAreaDone(studentCurricularPlan, creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);
		
		List finalListOfCurricularCourses = null;

		if (isSpecializationAreaDone && isSecundaryAreaDone)
		{
			finalListOfCurricularCourses = new ArrayList();
		} else if (!isSpecializationAreaDone && isSecundaryAreaDone)
		{
			finalListOfCurricularCourses =
				selectSpecializationAreaCurricularCoursesFromGroupsNotFull(studentCurricularPlan, creditsInSpecializationAreaGroups);
		} else if (isSpecializationAreaDone && !isSecundaryAreaDone)
		{
			finalListOfCurricularCourses =
				selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
					studentCurricularPlan,
					creditsInSecundaryAreaGroups,
					creditsInAnySecundaryArea);
		} else
		{
			List specializationAreaCurricularCourses =
				selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
					studentCurricularPlan,
					creditsInSpecializationAreaGroups);

			List secundaryAreaCurricularCourses =
				selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
					studentCurricularPlan,
					creditsInSecundaryAreaGroups,
					creditsInAnySecundaryArea);
			
			List disjunction =
				(List) CollectionUtils.disjunction(specializationAreaCurricularCourses, secundaryAreaCurricularCourses);
			List intersection =
				(List) CollectionUtils.intersection(specializationAreaCurricularCourses, secundaryAreaCurricularCourses);
			
			finalListOfCurricularCourses = new ArrayList();
			finalListOfCurricularCourses.addAll(disjunction);
			finalListOfCurricularCourses.addAll(intersection);
		}
		
		selectDesiredCurricularCourses(enrollmentsWithAprovedState, finalListOfCurricularCourses);
		selectDesiredCurricularCourses(enrollmentsWithEnrolledState, finalListOfCurricularCourses);
		selectDesiredCurricularCourses(finalListOfCurricularCourses, semester);
		return finalListOfCurricularCourses;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInAnySecundaryArea
	 * @return secundaryAreaCurricularCoursesFromGroupsNotFull
	 * @throws ExcepcaoPersistencia
	 */
	private List selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSecundaryAreaGroups,
		int creditsInAnySecundaryArea)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		
		List secundaryAreaCurricularCourses = new ArrayList();

		List groups =
			curricularCourseGroupDAO.readByBranchAndAreaType(studentCurricularPlan.getSecundaryBranch(), AreaType.SECONDARY_OBJ);

		Iterator iterator = groups.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourseGroup group = (ICurricularCourseGroup) iterator.next();

			if (!isSecundaryGroupDone(studentCurricularPlan, group, creditsInSecundaryAreaGroups, creditsInAnySecundaryArea))
			{
				secundaryAreaCurricularCourses.addAll(group.getCurricularCourses());
			}
		}
		return secundaryAreaCurricularCourses;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSpecializationAreaGroups
	 * @return specializationAreaCurricularCoursesFromGroupsNotFull
	 * @throws ExcepcaoPersistencia
	 */
	private List selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSpecializationAreaGroups)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		
		List specializationAreaCurricularCourses = new ArrayList();

		List groups =
			curricularCourseGroupDAO.readByBranchAndAreaType(studentCurricularPlan.getBranch(), AreaType.SPECIALIZATION_OBJ);

		Iterator iterator = groups.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourseGroup group = (ICurricularCourseGroup) iterator.next();
			
			if (!isSpecializationGroupDone(studentCurricularPlan, group, creditsInSpecializationAreaGroups))
			{
				specializationAreaCurricularCourses.addAll(group.getCurricularCourses());
			}
		}

		return specializationAreaCurricularCourses;
	}

	/**
	 * @param enrollmentsInCurricularCourseToRemove
	 * @param curricularCoursesToRemoveFrom
	 */
	private void selectDesiredCurricularCourses(List enrollmentsInCurricularCourseToRemove, List curricularCoursesToRemoveFrom)
	{
		List curricularCoursesToRemove = new ArrayList();
		Iterator iterator = enrollmentsInCurricularCourseToRemove.iterator();
		while(iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (curricularCoursesToRemoveFrom.contains(enrolment.getCurricularCourse()))
			{
				curricularCoursesToRemove.add(enrolment.getCurricularCourse());
			}
		}
		curricularCoursesToRemoveFrom.removeAll(curricularCoursesToRemove);
	}

	/**
	 * @param curricularCoursesToRemoveFrom
	 * @param semester
	 */
	private void selectDesiredCurricularCourses(List curricularCoursesToRemoveFrom, Integer semester)
	{
		List curricularCoursesToRemove = new ArrayList();
		Iterator iterator = curricularCoursesToRemoveFrom.iterator();
		while(iterator.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
			List scopes = curricularCourse.getScopes();
			boolean courseIsToMantain = false;
			Iterator iteratorScopes = scopes.iterator();
			while(iteratorScopes.hasNext())
			{
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopes.next();
				if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
					&& curricularCourseScope.getEndDate() == null)
				{
					courseIsToMantain = true;
				}
			}
			if (!courseIsToMantain)
			{
				curricularCoursesToRemove.add(curricularCourse);
			}
		}
		curricularCoursesToRemoveFrom.removeAll(curricularCoursesToRemove);
	}
	
	/**
	 * @param studentCurricularPlan
	 * @param studentApprovedEnrollments
	 * @param studentCurrentSemesterEnrollments
	 * @param creditsInScientificAreas
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @throws ExcepcaoPersistencia
	 */
	private void calculateGroupsCreditsFromEnrollments(
		IStudentCurricularPlan studentCurricularPlan,
		List studentApprovedEnrollments,
		List studentCurrentSemesterEnrollments,
		HashMap creditsInScientificAreas,
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups)
		throws ExcepcaoPersistencia
	{
		List enrollments = new ArrayList();
		enrollments.addAll(studentApprovedEnrollments);
		enrollments.addAll(studentCurrentSemesterEnrollments);

		Iterator iterator = enrollments.iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			ICurricularCourse curricularCourse = enrolment.getCurricularCourse();

			if (curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(curricularCourse, studentCurricularPlan))
			{
				sumCreditsInScientificArea(curricularCourse, creditsInScientificAreas);
			} else
			{
				sumCreditsInAreasGroups(
					curricularCourse,
					studentCurricularPlan,
					creditsInSecundaryAreaGroups,
					creditsInSpecializationAreaGroups);
			}
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInScientificAreas
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @throws ExcepcaoPersistencia
	 */
	private void calculateGroupsCreditsFromScientificAreas(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInScientificAreas,
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups)
		throws ExcepcaoPersistencia
	{
		HashMap clashingGroups = new HashMap();

		calculateNonClashingScientificAreas(
			studentCurricularPlan,
			creditsInScientificAreas,
			creditsInSpecializationAreaGroups,
			creditsInSecundaryAreaGroups,
			clashingGroups);

		calculateClashingScientificAreas(
			creditsInScientificAreas,
			creditsInSpecializationAreaGroups,
			creditsInSecundaryAreaGroups,
			clashingGroups);
	}

	/**
	 * @param creditsInScientificAreas
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @param clashingGroups
	 */
	private void calculateClashingScientificAreas(
		HashMap creditsInScientificAreas,
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups,
		HashMap clashingGroups)
	{
		if (!clashingGroups.entrySet().isEmpty())
		{
			Iterator iterator = clashingGroups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				List objects = (List) mapEntry.getValue();
				Integer scientificAreaID = (Integer) mapEntry.getKey();

				ICurricularCourseGroup specializationGroup = (ICurricularCourseGroup) objects.get(0);
				ICurricularCourseGroup secundaryGroup = (ICurricularCourseGroup) objects.get(1);

				Integer creditsInSpecializationGroup =
					(Integer) creditsInSpecializationAreaGroups.get(specializationGroup.getIdInternal());
				Integer creditsInSecundaryGroup = (Integer) creditsInSecundaryAreaGroups.get(secundaryGroup.getIdInternal());
				Integer creditsInScientificArea = (Integer) creditsInScientificAreas.get(scientificAreaID);
				
				if (creditsInSpecializationGroup == null)
				{
					creditsInSpecializationGroup = new Integer(0);
				}

				if (creditsInSecundaryGroup == null)
				{
					creditsInSecundaryGroup = new Integer(0);
				}

				if (creditsInScientificArea == null)
				{
					creditsInScientificArea = new Integer(0);
				}

				int sumOfGroupsCredits =
					creditsInSpecializationGroup.intValue()
						+ creditsInSecundaryGroup.intValue()
						+ creditsInScientificArea.intValue();
//				int sumOfMaximumGroupsCredits =
//					specializationGroup.getMaximumCredits().intValue() + secundaryGroup.getMaximumCredits().intValue();
				int sumOfMinimumGroupsCredits =
					specializationGroup.getMinimumCredits().intValue() + secundaryGroup.getMinimumCredits().intValue();

				if (sumOfGroupsCredits < sumOfMinimumGroupsCredits)
				{
					distributeCreditsInScientificAreaByTheTowGroups(
						creditsInSpecializationAreaGroups,
						creditsInSecundaryAreaGroups,
						creditsInScientificAreas,
						specializationGroup,
						secundaryGroup,
						scientificAreaID,
						specializationGroup.getMinimumCredits(),
						secundaryGroup.getMinimumCredits());
				} else // if (sumOfGroupsCredits < sumOfMaximumGroupsCredits) AND if (sumOfGroupsCredits >=
					   // sumOfMaximumGroupsCredits)
				{
					distributeCreditsInScientificAreaByTheTowGroups(
						creditsInSpecializationAreaGroups,
						creditsInSecundaryAreaGroups,
						creditsInScientificAreas,
						specializationGroup,
						secundaryGroup,
						scientificAreaID,
						specializationGroup.getMaximumCredits(),
						secundaryGroup.getMaximumCredits());
				}
				
//				if (isSpecializationGroupDone(studentCurricularPlan, specializationGroup, creditsInSpecializationAreaGroups))
//				{
//					sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup.getIdInternal(), creditsInScientificArea);
//					creditsInScientificAreas.remove(scientificAreaID);
//				} else if (
//					isSecundaryGroupDone(
//						studentCurricularPlan,
//						secundaryGroup,
//						creditsInSecundaryAreaGroups,
//						creditsInAnySecundaryArea))
//				{
//					sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup.getIdInternal(), creditsInScientificArea);
//					creditsInScientificAreas.remove(scientificAreaID);
//				} else if (
//					(creditsInSpecializationGroup.intValue()
//						+ creditsInSecundaryGroup.intValue()
//						+ creditsInScientificArea.intValue())
//						>= (specializationGroup.getMaximumCredits().intValue() + secundaryGroup.getMaximumCredits().intValue()))
//				{
//					int tmp = secundaryGroup.getMaximumCredits().intValue() - creditsInSecundaryGroup.intValue();
//					creditsInSecundaryAreaGroups.put(secundaryGroup.getIdInternal(), secundaryGroup.getMaximumCredits());
//					int result = creditsInScientificArea.intValue() - tmp;
//					sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup.getIdInternal(), new Integer(result));
//					creditsInScientificAreas.remove(scientificAreaID);
//				}
			}
		}
	}

	/**
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInScientificAreas
	 * @param specializationGroup
	 * @param secundaryGroup
	 * @param scientificAreaID
	 * @param maximumLevelToConsider
	 */
	private void distributeCreditsInScientificAreaByTheTowGroups(
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups,
		HashMap creditsInScientificAreas,
		ICurricularCourseGroup specializationGroup,
		ICurricularCourseGroup secundaryGroup,
		Integer scientificAreaID,
		Integer maximumLevelToConsiderForSpecializationGroup,
		Integer maximumLevelToConsiderForSecundaryGroup)
	{
		Integer aux = (Integer) creditsInSpecializationAreaGroups.get(specializationGroup.getIdInternal());
		Integer bux = (Integer) creditsInSecundaryAreaGroups.get(secundaryGroup.getIdInternal());
		Integer cux = (Integer) creditsInScientificAreas.get(scientificAreaID);

		if (aux == null)
		{
			aux = new Integer(0);
		}

		if (bux == null)
		{
			bux = new Integer(0);
		}
		
		if (cux == null)
		{
			cux = new Integer(0);
		}
		
		long creditsInSpecializationGroup = aux.longValue();
		long creditsInSecundaryGroup = bux.longValue();
		long creditsInScientificArea = cux.longValue();

		long distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider =
			maximumLevelToConsiderForSpecializationGroup.longValue() - creditsInSpecializationGroup;
		if (distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider < 0)
		{
			distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider = 0;
		}

		long distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider =
			maximumLevelToConsiderForSecundaryGroup.longValue() - creditsInSecundaryGroup;
		if (distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider < 0)
		{
			distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider = 0;
		}

		long creditsForSecundaryGroup = Math.round(Math.floor(creditsInScientificArea * 0.5));
		long creditsForSpecializationGroup = creditsInScientificArea - creditsForSecundaryGroup;

		long aux1 = Math.max((creditsForSpecializationGroup - distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider), 0);
		long aux2 = Math.max((creditsForSecundaryGroup - distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider), 0);
		long creditsForDistribution = aux1 + aux2;
		
		sumInHashMap(
			creditsInSpecializationAreaGroups,
			specializationGroup.getIdInternal(),
			new Integer(String.valueOf(creditsForSpecializationGroup - aux1)));
		
		sumInHashMap(
			creditsInSecundaryAreaGroups,
			secundaryGroup.getIdInternal(),
			new Integer(String.valueOf(creditsForSecundaryGroup - aux2)));
		
		if ((creditsInSpecializationGroup + creditsForDistribution) < maximumLevelToConsiderForSpecializationGroup.longValue())
		{
			sumInHashMap(
				creditsInSpecializationAreaGroups,
				specializationGroup.getIdInternal(),
				new Integer(String.valueOf(creditsForDistribution)));
		} else if ((creditsInSecundaryGroup + creditsForDistribution) < maximumLevelToConsiderForSecundaryGroup.longValue())
		{
			sumInHashMap(
				creditsInSecundaryAreaGroups,
				secundaryGroup.getIdInternal(),
				new Integer(String.valueOf(creditsForDistribution)));
		}

//		int diferenceBetweenDistances =
//			Math.max(
//				distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider,
//				distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider)
//				- Math.min(
//					distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider,
//					distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider);
//
//		if (distanceFromCreditsInSpecializationGroupToMaximumLevelToConsider
//			>= distanceFromCreditsInSecundaryGroupToMaximumLevelToConsider)
//		{
//			sumInHashMap(
//				creditsInSpecializationAreaGroups,
//				specializationGroup.getIdInternal(),
//				new Integer(diferenceBetweenDistances));
//		} else
//		{
//			sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup.getIdInternal(), new Integer(diferenceBetweenDistances));
//		}
//
//		double valueToAddToBothGroups =
//			(creditsInScientificArea - Math.min(creditsInScientificArea, diferenceBetweenDistances)) * 0.5;
//
//		long valueToAddToSpecializationGroup = Math.round(Math.ceil(valueToAddToBothGroups));
//		long valueToAddToSecundaryGroup = Math.round(Math.floor(valueToAddToBothGroups));
//
//		sumInHashMap(
//			creditsInSpecializationAreaGroups,
//			specializationGroup.getIdInternal(),
//			new Integer(String.valueOf(valueToAddToSpecializationGroup)));
//
//		sumInHashMap(
//			creditsInSecundaryAreaGroups,
//			secundaryGroup.getIdInternal(),
//			new Integer(String.valueOf(valueToAddToSecundaryGroup)));
	}

	/**
	 * @param studentCurricularPlan
	 * @param group
	 * @param creditsInSpecializationAreaGroups
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean isSpecializationGroupDone(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourseGroup group,
		HashMap creditsInSpecializationAreaGroups)
		throws ExcepcaoPersistencia
	{
		Integer credits = (Integer) creditsInSpecializationAreaGroups.get(group.getIdInternal());

		if (credits != null)
		{
			if (credits.intValue() >= group.getMaximumCredits().intValue())
			{
				return true;
			}

			if (credits.intValue() < group.getMinimumCredits().intValue())
			{
				return false;
			}

			return isSpecializationAreaDone(studentCurricularPlan, creditsInSpecializationAreaGroups);
		} else
		{
			return false;
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @param group
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInAnySecundaryArea
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean isSecundaryGroupDone(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourseGroup group,
		HashMap creditsInSecundaryAreaGroups,
		int creditsInAnySecundaryArea)
		throws ExcepcaoPersistencia
	{
		Integer credits = (Integer) creditsInSecundaryAreaGroups.get(group.getIdInternal());

		if (credits != null)
		{
			if (credits.intValue() >= group.getMaximumCredits().intValue())
			{
				return true;
			}

			if (credits.intValue() < group.getMinimumCredits().intValue())
			{
				return false;
			}

			return isSecundaryAreaDone(studentCurricularPlan, creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);
		} else
		{
			return false;
		}
}

	/**
	 * @param studentCurricularPlan
	 * @param group
	 * @param creditsInSpecializationAreaGroups
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean isSpecializationAreaDone(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSpecializationAreaGroups)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		if (!creditsInSpecializationAreaGroups.entrySet().isEmpty())
		{
			int areaCredits = 0;

			Iterator iterator = creditsInSpecializationAreaGroups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer otherCredits = (Integer) mapEntry.getValue();
				Integer groupID = (Integer) mapEntry.getKey();

				ICurricularCourseGroup otherGroup =
					(ICurricularCourseGroup) curricularCourseGroupDAO.readByOID(CurricularCourseGroup.class, groupID);

				if (otherCredits.intValue() < otherGroup.getMinimumCredits().intValue())
				{
					return false;
				} else
				{
					areaCredits += otherCredits.intValue();
				}
			}

			if (areaCredits >= studentCurricularPlan.getBranch().getSpecializationCredits().intValue())
			{
				return true;
			} else
			{
				return false;
			}
		}

		return false;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInAnySecundaryArea
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean isSecundaryAreaDone(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSecundaryAreaGroups,
		int creditsInAnySecundaryArea)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		if (!creditsInSecundaryAreaGroups.entrySet().isEmpty())
		{
			int areaCredits = 0;

			Iterator iterator = creditsInSecundaryAreaGroups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer otherCredits = (Integer) mapEntry.getValue();
				Integer groupID = (Integer) mapEntry.getKey();

				ICurricularCourseGroup otherGroup =
					(ICurricularCourseGroup) curricularCourseGroupDAO.readByOID(CurricularCourseGroup.class, groupID);

				if (otherCredits.intValue() >= otherGroup.getMaximumCredits().intValue())
				{
					return true;
				} else
				{
					areaCredits += otherCredits.intValue();
				}
			}

			areaCredits += creditsInAnySecundaryArea;

			if (areaCredits >= studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue())
			{
				return true;
			} else
			{
				return false;
			}
		}

		return false;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInScientificAreas
	 * @param creditsInSpecializationAreaGroups
	 * @param creditsInSecundaryAreaGroups
	 * @param clashingGroups
	 * @throws ExcepcaoPersistencia
	 */
	private void calculateNonClashingScientificAreas(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInScientificAreas,
		HashMap creditsInSpecializationAreaGroups,
		HashMap creditsInSecundaryAreaGroups,
		HashMap clashingGroups)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentScientificArea scientificAreaDAO = persistentSuport.getIPersistentScientificArea();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		
		if (!creditsInScientificAreas.entrySet().isEmpty())
		{
			List entriesInHashMapToRemove = new ArrayList();

			Iterator iterator = creditsInScientificAreas.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer credits = (Integer) mapEntry.getValue();
				Integer scientificAreaID = (Integer) mapEntry.getKey();

				IScientificArea scientificArea =
					(IScientificArea) scientificAreaDAO.readByOID(ScientificArea.class, scientificAreaID);

				ICurricularCourseGroup specializationGroup =
					curricularCourseGroupDAO.readByBranchAndScientificAreaAndAreaType(
						studentCurricularPlan.getBranch(),
						scientificArea,
						AreaType.SPECIALIZATION_OBJ);

				ICurricularCourseGroup secundaryGroup =
					curricularCourseGroupDAO.readByBranchAndScientificAreaAndAreaType(
						studentCurricularPlan.getSecundaryBranch(),
						scientificArea,
						AreaType.SECONDARY_OBJ);

				if ((specializationGroup != null) && (secundaryGroup != null))
				{
					List groups = new ArrayList();
					groups.add(0, specializationGroup);
					groups.add(1, secundaryGroup);
					groups.add(2, scientificArea);
					clashingGroups.put(scientificArea.getIdInternal(), groups);
				} else if (specializationGroup != null)
				{
					sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup.getIdInternal(), credits);
					entriesInHashMapToRemove.add(scientificAreaID);
				} else if (secundaryGroup != null)
				{
					sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup.getIdInternal(), credits);
					entriesInHashMapToRemove.add(scientificAreaID);
				}
			}
			
			cleanHashMap(creditsInScientificAreas, entriesInHashMapToRemove);
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @return creditsInAnySecundaryAreas
	 * @throws ExcepcaoPersistencia
	 */
	private int getGivenCreditsInAnySecundaryArea(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO =
			persistentSuport.getIPersistentCreditsInAnySecundaryArea();
		int creditsInAnySecundaryAreas = 0;

		List givenCreditsInAnySecundaryAreas = creditsInAnySecundaryAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);

		if (givenCreditsInAnySecundaryAreas != null && !givenCreditsInAnySecundaryAreas.isEmpty())
		{
			Iterator iterator = givenCreditsInAnySecundaryAreas.iterator();
			while (iterator.hasNext())
			{
				CreditsInAnySecundaryArea creditsInAnySecundaryArea = (CreditsInAnySecundaryArea) iterator.next();
				creditsInAnySecundaryAreas += creditsInAnySecundaryArea.getGivenCredits().intValue();
			}
		}

		return creditsInAnySecundaryAreas;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInScientificAreas
	 * @throws ExcepcaoPersistencia
	 */
	private void getGivenCreditsInScientificArea(IStudentCurricularPlan studentCurricularPlan, HashMap creditsInScientificAreas)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCreditsInSpecificScientificArea creditsInScientificAreaDAO =
			persistentSuport.getIPersistentCreditsInSpecificScientificArea();

		List givenCreditsInScientificAreas = creditsInScientificAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);

		if (givenCreditsInScientificAreas != null && !givenCreditsInScientificAreas.isEmpty())
		{
			Iterator iterator = givenCreditsInScientificAreas.iterator();
			while (iterator.hasNext())
			{
				CreditsInScientificArea creditsInScientificArea = (CreditsInScientificArea) iterator.next();
				sumInHashMap(
					creditsInScientificAreas,
					creditsInScientificArea.getScientificArea().getIdInternal(),
					creditsInScientificArea.getGivenCredits());
			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @param creditsInSecundaryAreaGroups
	 * @param creditsInSpecializationAreaGroups
	 * @throws ExcepcaoPersistencia
	 */
	private void sumCreditsInAreasGroups(
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSecundaryAreaGroups,
		HashMap creditsInSpecializationAreaGroups)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

		HashMap creditsInAreaGroups = null;
		IBranch branch = null;
		AreaType areaType = null;

		if (curricularCourseBelongsToSpecializationArea(curricularCourse, studentCurricularPlan))
		{
			// Sum credits in specialization area groups
			creditsInAreaGroups = creditsInSpecializationAreaGroups;
			branch = studentCurricularPlan.getBranch();
			areaType = AreaType.SPECIALIZATION_OBJ;
		} else if (curricularCourseBelongsToSecundaryArea(curricularCourse, studentCurricularPlan))
		{
			// Sum credits in secundary area groups
			creditsInAreaGroups = creditsInSecundaryAreaGroups;
			branch = studentCurricularPlan.getSecundaryBranch();
			areaType = AreaType.SECONDARY_OBJ;
		}

		if (creditsInAreaGroups != null && branch != null && areaType != null)
		{
			ICurricularCourseGroup curricularCourseGroup =
			curricularCourseGroupDAO.readByBranchAndCurricularCourseAndAreaType(branch, curricularCourse, areaType);

			sumInHashMap(creditsInAreaGroups, curricularCourseGroup.getIdInternal(), curricularCourseCredits);
		}
	}

	/**
	 * @param curricularCourse
	 * @param creditsInSpecificScientificArea
	 * @throws ExcepcaoPersistencia
	 */
	private void sumCreditsInScientificArea(
		ICurricularCourse curricularCourse,
		HashMap creditsInScientificAreas)
	{
		IScientificArea scientificArea = curricularCourse.getScientificArea();
		Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

		sumInHashMap(creditsInScientificAreas, scientificArea.getIdInternal(), curricularCourseCredits);
	}

	/**
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		return (
			curricularCourseBelongsToSpecializationArea(curricularCourse, studentCurricularPlan)
				&& curricularCourseBelongsToSecundaryArea(curricularCourse, studentCurricularPlan));
	}

	/**
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean curricularCourseBelongsToSpecializationArea(
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		ICurricularCourseGroup curricularCourseGroup =
			curricularCourseGroupDAO.readByBranchAndCurricularCourseAndAreaType(
				studentCurricularPlan.getBranch(),
				curricularCourse,
				AreaType.SPECIALIZATION_OBJ);
		
		return curricularCourseGroup != null;
	}

	/**
	 * @param curricularCourse
	 * @param studentCurricularPlan
	 * @return true/false
	 * @throws ExcepcaoPersistencia
	 */
	private boolean curricularCourseBelongsToSecundaryArea(
		ICurricularCourse curricularCourse,
		IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		ICurricularCourseGroup curricularCourseGroup =
		curricularCourseGroupDAO.readByBranchAndCurricularCourseAndAreaType(
				studentCurricularPlan.getSecundaryBranch(),
				curricularCourse,
				AreaType.SECONDARY_OBJ);
		
		return curricularCourseGroup != null;
	}

	/**
	 * @param map
	 * @param key
	 * @param value
	 */
	private void sumInHashMap(HashMap map, Integer key, Integer value)
	{
		if (!map.containsKey(key))
		{
			map.put(key, value);
		} else
		{
			Integer oldValue = (Integer) map.get(key);
			int result = oldValue.intValue() + value.intValue();
			map.put(key, new Integer(result));
		}
	}

	/**
	 * @param map
	 * @param keysOfItemsToRemove
	 */
	private void cleanHashMap(HashMap map, List keysOfItemsToRemove)
	{
		Iterator iterator = keysOfItemsToRemove.iterator();
		while(iterator.hasNext())
		{
			Integer key = (Integer) iterator.next();
			map.remove(key);
		}
	}

	/**
	 * @param enrollments
	 * @param curricularCourses
	 * @return enrollments
	 */
	private List selectEnrollmentsInSpecializationAndSecundaryAreasCurricularCourses(
		List enrollments,
		List curricularCourses)
	{
		List enrollmentsToKeep = new ArrayList();
		Iterator iterator = enrollments.iterator();
		while(iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (curricularCourses.contains(enrolment.getCurricularCourse()))
			{
				if (!enrollmentsToKeep.contains(enrolment))
				{
					enrollmentsToKeep.add(enrolment);
				}
			}
		}
		
		return enrollmentsToKeep;
	}

	/**
	 * @param area
	 * @param areaType
	 * @return curricularCourses
	 * @throws ExcepcaoPersistencia
	 */
	private List selectAllCurricularCoursesFromGivenArea(
		IBranch area,
		AreaType areaType)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();
		List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);
		List curricularCourses = new ArrayList();
		Iterator iterator = groups.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
			curricularCourses.addAll(curricularCourseGroup.getCurricularCourses());
		}
		return curricularCourses;
	}

	/**
	 * @param studentEnrolmentContext
	 * @return optionalAndTFCCurricularCourses
	 * @throws ExcepcaoPersistencia
	 */
	private List getOptionalAndTFCCurricularCourses(StudentEnrolmentContext studentEnrolmentContext) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourse curricularCourseDAO = persistentSuport.getIPersistentCurricularCourse();

		List optionalAndTFCCurricularCourses = new ArrayList();
		
		List result =
			curricularCourseDAO.readAllByDegreeCurricularPlanAndType(
				studentEnrolmentContext.getStudentCurricularPlan().getDegreeCurricularPlan(),
				CurricularCourseType.OPTIONAL_COURSE_OBJ);

		optionalAndTFCCurricularCourses.addAll(result);

		result =
			curricularCourseDAO.readAllByDegreeCurricularPlanAndType(
				studentEnrolmentContext.getStudentCurricularPlan().getDegreeCurricularPlan(),
				CurricularCourseType.TFC_COURSE_OBJ);

		optionalAndTFCCurricularCourses.addAll(result);
		
		selectDesiredCurricularCourses(studentEnrolmentContext.getStudentApprovedEnrollments(), optionalAndTFCCurricularCourses);
		selectDesiredCurricularCourses(studentEnrolmentContext.getStudentCurrentSemesterEnrollments(), optionalAndTFCCurricularCourses);
		selectDesiredCurricularCourses(optionalAndTFCCurricularCourses, studentEnrolmentContext.getExecutionPeriod().getSemester());

		return optionalAndTFCCurricularCourses;
	}

	/**
	 * @throws ExcepcaoPersistencia
	 */
	private void automaticalyEnrollInMandatoryCurricularCourses() throws ExcepcaoPersistencia
	{
		HashMap enrollmentsCreated = new HashMap();

		// FIXME [DAVID]: There is a bug here!
		// Try this: Enroll in all mandatory curricular courses and some others more untill you get a total of maxCourses
		// enrolled. Now try to unenroll a mandatory curricular course.
		// It was allowed because totalSize >= maxCourses so it will not automaticaly enroll in mandatory curricular courses
		// that were unenrolled before.
		List curricularCourses =
			getMandatoryCurricularCourses(this.studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled());

		int maxCourses =
			this.studentEnrolmentContext.getStudentCurricularPlan().getStudent().getStudentKind().getMaxCoursesToEnrol().intValue();

		int currentSemesterEnrollmentsSize = this.studentEnrolmentContext.getStudentCurrentSemesterEnrollments().size();
		
		int totalSize = curricularCourses.size() + currentSemesterEnrollmentsSize;
		
		if (curricularCourses != null && !curricularCourses.isEmpty() && totalSize <= maxCourses)
		{
			IStudentCurricularPlan studentCurricularPlan = this.studentEnrolmentContext.getStudentCurricularPlan();
			IExecutionPeriod executionPeriod = this.studentEnrolmentContext.getExecutionPeriod();

			List enrollmentsToAdd = new ArrayList();
			Iterator iterator = curricularCourses.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				IEnrolment enrolment = writeEnrollment(studentCurricularPlan, executionPeriod, curricularCourse, enrollmentsCreated);
				if (enrolment != null)
				{
					enrollmentsToAdd.add(enrolment);
					enrollmentsCreated.put(curricularCourse.getIdInternal(), enrolment);
				}
			}
			
			List studentEverEnrolledCurricularCourses = getStudentEverEnrolledCurricularCourses();

			iterator = enrollmentsCreated.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				IEnrolment enrolment = (IEnrolment) mapEntry.getValue();
				studentEverEnrolledCurricularCourses.add(enrolment.getCurricularCourse());
			}
			
			this.studentEnrolmentContext.getStudentCurrentSemesterEnrollments().addAll(enrollmentsToAdd);
			this.studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().removeAll(curricularCourses);
			this.studentEnrolmentContext.setAcumulatedEnrolments(studentEverEnrolledCurricularCourses);
			computeAvailableCurricularCourses();
		}
	}

	/**
	 * @param curricularCourses
	 * @return mandatoryCurricularCourses
	 */
	private List getMandatoryCurricularCourses(List curricularCourses)
	{
		List mandatoryCurricularCourses = new ArrayList();
		if (curricularCourses != null && !curricularCourses.isEmpty())
		{
			Iterator iterator = curricularCourses.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				
				if (curricularCourse.getMandatory() != null && curricularCourse.getMandatory().booleanValue())
				{
					mandatoryCurricularCourses.add(curricularCourse);
				}
			}
		}
		return mandatoryCurricularCourses;
	}

	/**
	 * @param studentCurricularPlan
	 * @param executionPeriod
	 * @param curricularCourse
	 * @return IEnrolment
	 * @throws ExcepcaoPersistencia
	 */
	private IEnrolment writeEnrollment(
		IStudentCurricularPlan studentCurricularPlan,
		IExecutionPeriod executionPeriod,
		ICurricularCourse curricularCourse,
		HashMap enrollmentsCreated) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
		
		IEnrolment enrolment =
			enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan,
				curricularCourse,
				executionPeriod);
		
		if (enrolment == null)
		{
			enrolment = (IEnrolment) enrollmentsCreated.get(curricularCourse.getIdInternal());
			if (enrolment == null)
			{
				enrolment = new Enrolment();
				enrolmentDAO.simpleLockWrite(enrolment);
				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				WriteEnrolment.createAttend(enrolment);
				return enrolment;
			}
		}
		return null;
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSpecializationAreaGroups
	 * @return Integer
	 */
	private Integer calculateCreditsInSpecializationArea(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSpecializationAreaGroups)
	{
		int areaCredits = 0;

		if (!creditsInSpecializationAreaGroups.entrySet().isEmpty())
		{
			Iterator iterator = creditsInSpecializationAreaGroups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer credits = (Integer) mapEntry.getValue();

				areaCredits += credits.intValue();
			}

			if (areaCredits >= studentCurricularPlan.getBranch().getSpecializationCredits().intValue())
			{
				areaCredits = studentCurricularPlan.getBranch().getSpecializationCredits().intValue();
			}
		}

		return new Integer(areaCredits);
	}

	/**
	 * @param studentCurricularPlan
	 * @param creditsInSecundaryAreaGroups
	 * @return Integer
	 */
	private Integer calculateCreditsInSecundaryArea(
		IStudentCurricularPlan studentCurricularPlan,
		HashMap creditsInSecundaryAreaGroups)
	{
		int areaCredits = 0;

		if (!creditsInSecundaryAreaGroups.entrySet().isEmpty())
		{
			Iterator iterator = creditsInSecundaryAreaGroups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer credits = (Integer) mapEntry.getValue();

				areaCredits += credits.intValue();
			}

			if (areaCredits >= studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue())
			{
				areaCredits = studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue();
			}
		}

		return new Integer(areaCredits);
	}

}