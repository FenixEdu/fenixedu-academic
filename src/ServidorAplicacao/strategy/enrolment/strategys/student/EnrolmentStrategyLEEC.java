package ServidorAplicacao.strategy.enrolment.strategys.student;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInSpecificScientificArea;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.IEnrolment;
import Dominio.IScientificArea;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.BranchType;

/**
 * @author David Santos
 * Jan 16, 2004
 */

public class EnrolmentStrategyLEEC extends EnrolmentStrategy implements IEnrolmentStrategy
{
	public EnrolmentStrategyLEEC()
	{
	}

	public EnrolmentContext getAvailableCurricularCourses()
	{
		return null;
	}

	public EnrolmentContext validateEnrolment()
	{
		return null;
	}

	public EnrolmentContext getOptionalCurricularCourses()
	{
		return null;
	}

	public EnrolmentContext getDegreesForOptionalCurricularCourses()
	{
		return null;
	}

	private void specificAlgorithm(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		HashMap creditsInSpecificScientificArea = null; // VACx
		HashMap creditsInSpecializationAreaGroups = null; // VGAEy
		HashMap creditsInSecundaryAreaGroups = null; // VGASz
		HashMap givenCreditsInSpecificScientificArea = null; // CACx
		int givenCreditsInAnySecundaryArea = 0; // CAS
		int creditsInSecundaryArea = 0; // TAS
		int creditsInSpecializationArea = 0; // TAE
		
		List enrolmentsList = studentCurricularPlan.getEnrolments();

		givenCreditsInSpecificScientificArea = this.calculateGivenCreditsInSpecificScientificArea(studentCurricularPlan);
		givenCreditsInAnySecundaryArea = this.calculateGivenCreditsInAnySecundaryArea(studentCurricularPlan);
		
		Iterator iterator = enrolmentsList.iterator();
		while(iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if(enrolment.getCurricularCourseScope().getBranch().getBranchType().equals(BranchType.SECUNDARY_BRANCH))
			{
				// Calculate credits in secundary area groups
				this.calculateCreditsInSomeAreaGroups(enrolment, studentCurricularPlan, creditsInSecundaryAreaGroups, creditsInSpecificScientificArea, givenCreditsInSpecificScientificArea);
			} else if(enrolment.getCurricularCourseScope().getBranch().getBranchType().equals(BranchType.SPECIALIZATION_BRANCH))
			{
				// Calculate credits in specialization area groups
				this.calculateCreditsInSomeAreaGroups(enrolment, studentCurricularPlan, creditsInSpecializationAreaGroups, creditsInSpecificScientificArea, givenCreditsInSpecificScientificArea);
			}
		}
		
		int maxCreditsInSecundaryArea = studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue();
		int maxCreditsInSpecializationArea = studentCurricularPlan.getSecundaryBranch().getSpecializationCredits().intValue();
		creditsInSecundaryArea = this.getCreditsInSecundaryAreaGroupsSum(creditsInSecundaryAreaGroups, givenCreditsInAnySecundaryArea, maxCreditsInSecundaryArea);
		creditsInSpecializationArea = this.getCreditsInSpecializationAreaGroupsSum(creditsInSpecializationAreaGroups, maxCreditsInSpecializationArea);
	}

	/**
	 * @param studentCurricularPlan
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private int calculateGivenCreditsInAnySecundaryArea(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO = fenixPersistentSuport.getIPersistentCreditsInAnySecundaryArea();
		int givenCreditsInAnySecundaryArea = 0;

		List givenCreditsInAnySecundaryAreaList = creditsInAnySecundaryAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);
		
		if(givenCreditsInAnySecundaryAreaList != null && !givenCreditsInAnySecundaryAreaList.isEmpty())
		{
			Iterator iterator = givenCreditsInAnySecundaryAreaList.iterator();
			while(iterator.hasNext())
			{
				CreditsInAnySecundaryArea creditsInAnySecundaryArea = (CreditsInAnySecundaryArea) iterator.next();
				givenCreditsInAnySecundaryArea += creditsInAnySecundaryArea.getGivenCredits().intValue();
			}
		}
		
		return givenCreditsInAnySecundaryArea;
	}

	/**
	 * @param studentCurricularPlan
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private HashMap calculateGivenCreditsInSpecificScientificArea(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCreditsInSpecificScientificArea creditsInSpecificScientificAreaDAO = fenixPersistentSuport.getIPersistentCreditsInSpecificScientificArea();
		HashMap givenCreditsInSpecificScientificArea = new HashMap();
		
		List givenCreditsInAnySecundaryAreaList = creditsInSpecificScientificAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);
		
		if(givenCreditsInAnySecundaryAreaList != null && !givenCreditsInAnySecundaryAreaList.isEmpty())
		{
			Iterator iterator = givenCreditsInAnySecundaryAreaList.iterator();
			while(iterator.hasNext())
			{
				CreditsInSpecificScientificArea creditsInSpecificScientificArea = (CreditsInSpecificScientificArea) iterator.next();
				if(!givenCreditsInSpecificScientificArea.containsKey(creditsInSpecificScientificArea.getScientificArea().getIdInternal()))
				{
					givenCreditsInSpecificScientificArea.put(creditsInSpecificScientificArea.getScientificArea().getIdInternal(), creditsInSpecificScientificArea.getGivenCredits());
				} else
				{
					Integer value = (Integer) givenCreditsInSpecificScientificArea.get(creditsInSpecificScientificArea.getScientificArea().getIdInternal());
					Integer givenCredits = creditsInSpecificScientificArea.getGivenCredits();
					int result = value.intValue() + givenCredits.intValue();
					givenCreditsInSpecificScientificArea.put(creditsInSpecificScientificArea.getScientificArea().getIdInternal(), new Integer(result));
				}
			}
		}
		
		return givenCreditsInSpecificScientificArea;
	}

	/**
	 * @param enrolment
	 * @param studentCurricularPlan
	 * @param creditsInSecundaryAreaGroups
	 */
	private void calculateCreditsInSomeAreaGroups(IEnrolment enrolment, IStudentCurricularPlan studentCurricularPlan, HashMap creditsInSomeAreaGroups, HashMap creditsInSpecificScientificArea, HashMap givenCreditsInSpecificScientificArea) throws ExcepcaoPersistencia
	{
		ICurricularCourse curricularCourse = enrolment.getCurricularCourseScope().getCurricularCourse();
		IScientificArea scientificArea = curricularCourse.getScientificArea();
		Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

		if(this.curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(curricularCourse, studentCurricularPlan))
		{
			if(creditsInSpecificScientificArea == null)
			{
				creditsInSpecificScientificArea = new HashMap();
			}

			if(!creditsInSpecificScientificArea.containsKey(scientificArea.getIdInternal()))
			{
				creditsInSpecificScientificArea.put(scientificArea.getIdInternal(), curricularCourseCredits);
			} else
			{
				Integer value = (Integer) creditsInSpecificScientificArea.get(scientificArea.getIdInternal());
				int result = value.intValue() + curricularCourseCredits.intValue();
				creditsInSpecificScientificArea.put(scientificArea.getIdInternal(), new Integer(result));
			}
		} else
		{
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseGroup curricularCourseGroupDAO = fenixPersistentSuport.getIPersistentCurricularCourseGroup();
			IBranch branch = enrolment.getCurricularCourseScope().getBranch();
			
			ICurricularCourseGroup curricularCourseGroup = curricularCourseGroupDAO.readByBranchAndCurricularCourse(branch, curricularCourse);

			if(creditsInSomeAreaGroups == null)
			{
				creditsInSomeAreaGroups = new HashMap();
			}

			if(!creditsInSomeAreaGroups.containsKey(curricularCourseGroup.getIdInternal()))
			{
				creditsInSomeAreaGroups.put(curricularCourseGroup.getIdInternal(), curricularCourseCredits);
			} else
			{
				Integer value = (Integer) creditsInSomeAreaGroups.get(curricularCourseGroup.getIdInternal());
				int result = value.intValue() + curricularCourseCredits.intValue();
				value = (Integer) givenCreditsInSpecificScientificArea.get(scientificArea.getIdInternal());
				result += value.intValue();
				givenCreditsInSpecificScientificArea.remove(scientificArea.getIdInternal());
				int maxCreditsForThisGroup = curricularCourseGroup.getMaximumCredits().intValue();
				if(result > maxCreditsForThisGroup)
				{
					result = maxCreditsForThisGroup;
				}
				creditsInSomeAreaGroups.put(curricularCourseGroup.getIdInternal(), new Integer(result));
			}
		}
	}

	/**
	 * @param enrolment
	 * @param studentCurricularPlan
	 */
	private boolean curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(ICurricularCourse curricularCourse, IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia
	{
		ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentScientificArea scientificAreaDAO = fenixPersistentSuport.getIPersistentScientificArea();
		boolean isInSpecializationArea = false;
		boolean isInSecundaryArea = false;
		IScientificArea scientificArea = curricularCourse.getScientificArea();
		
		List scientificAreasFromSpecializationArea = scientificAreaDAO.readAllByBranch(studentCurricularPlan.getBranch());
		List scientificAreasFromSecundaryArea = scientificAreaDAO.readAllByBranch(studentCurricularPlan.getSecundaryBranch());
		
		if(scientificAreasFromSpecializationArea.contains(scientificArea))
		{
			isInSpecializationArea = true;
		}

		if(scientificAreasFromSecundaryArea.contains(scientificArea))
		{
			isInSecundaryArea = true;
		}

		if(isInSpecializationArea && isInSecundaryArea)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * @param creditsInSpecializationAreaGroups
	 * @param maxCreditsInSpecializationArea
	 * @return
	 */
	private int getCreditsInSpecializationAreaGroupsSum(HashMap creditsInSpecializationAreaGroups, int maxCreditsInSpecializationArea)
	{
		int sum = 0;
		if(!creditsInSpecializationAreaGroups.entrySet().isEmpty())
		{
			Iterator iterator = creditsInSpecializationAreaGroups.entrySet().iterator();
			while(iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer value = (Integer) mapEntry.getValue();
				sum += value.intValue();
			}
		}
		
		if(sum > maxCreditsInSpecializationArea)
		{
			sum = maxCreditsInSpecializationArea;
		}
		return sum;
	}

	/**
	 * @param creditsInSecundaryAreaGroups
	 * @param givenCreditsInAnySecundaryArea
	 * @param maxCreditsInSecundaryArea
	 * @return
	 */
	private int getCreditsInSecundaryAreaGroupsSum(HashMap creditsInSecundaryAreaGroups, int givenCreditsInAnySecundaryArea, int maxCreditsInSecundaryArea)
	{
		int sum = 0;
		if(!creditsInSecundaryAreaGroups.entrySet().isEmpty())
		{
			Iterator iterator = creditsInSecundaryAreaGroups.entrySet().iterator();
			while(iterator.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				Integer value = (Integer) mapEntry.getValue();
				sum += value.intValue();
			}
		}
		sum += givenCreditsInAnySecundaryArea;
		
		if(sum > maxCreditsInSecundaryArea)
		{
			sum = maxCreditsInSecundaryArea;
		}
		return sum;
	}

}