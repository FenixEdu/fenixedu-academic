package middleware.degreeCurricularPlan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.Site;
import Util.DegreeCurricularPlanState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DuplicateDegreeCurricularPlan
{

	private static PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

	public DuplicateDegreeCurricularPlan()
	{
	}

	public static void main(String args[]) throws Exception
	{

		//		String originalDCPNames[] = new String[15];
		//		String newDCPNames[] = new String[15];

		//		originalDCPNames[0] = "MC02/03";
		//		newDCPNames[0] = "MC03/05";
		//		
		//		originalDCPNames[1] = "MEGMTRM02/03";
		//		newDCPNames[1] = "MEGMTRM03/05";
		//		
		//		originalDCPNames[2] = "MEE02/03";
		//		newDCPNames[2] = "MEE03/05";
		//		
		//		originalDCPNames[3] = "MEGT02/03";
		//		newDCPNames[3] = "MEGT03/05";
		//		
		//		originalDCPNames[4] = "MEEC02/03";
		//		newDCPNames[4] = "MEEC03/05";
		//		
		//		originalDCPNames[5] = "MEIC02/03";
		//		newDCPNames[5] = "MEIC03/05";
		//		
		//		originalDCPNames[6] = "MEM02/03";
		//		newDCPNames[6] = "MEM03/05";
		//		
		//		originalDCPNames[7] = "MF02/03";
		//		newDCPNames[7] = "MF03/05";
		//		
		//		originalDCPNames[8] = "MG02/03";
		//		newDCPNames[8] = "MG03/05";
		//		
		//		originalDCPNames[9] = "MHRH02/03";
		//		newDCPNames[9] = "MHRH03/05";
		//		
		//		originalDCPNames[10] = "MMA02/03";
		//		newDCPNames[10] = "MMA03/05";
		//		
		//		originalDCPNames[11] = "MT02/03";
		//		newDCPNames[11] = "MT03/05";
		//		
		//		originalDCPNames[12] = "MUGT02/03";
		//		newDCPNames[12] = "MUGT03/05";
		//
		//		originalDCPNames[13] = "ML02/03";
		//		newDCPNames[13] = "ML03/05";
		//
		//		originalDCPNames[14] = "MB02/03";
		//		newDCPNames[14] = "MB03/05";
		//
		//
		//		for(int i = 0; i < originalDCPNames.length ; i++){
		//
		//			IDegreeCurricularPlan degreeCurricularPlan =
		// DuplicateDegreeCurricularPlan.createDegreeCurricularPlan(originalDCPNames[i], newDCPNames[i],
		// DuplicateDegreeCurricularPlan.broker);
		//
		//			// Specific Code
		//			updateExecutionDegrees(originalDCPNames[i], "2003/2004", degreeCurricularPlan,
		// DuplicateDegreeCurricularPlan.broker);
		//
		//			DuplicateDegreeCurricularPlan.duplicateBranchs(originalDCPNames[i], degreeCurricularPlan,
		// DuplicateDegreeCurricularPlan.broker);
		//
		//			DuplicateDegreeCurricularPlan.duplicateCurricularCoursesAndScopes(originalDCPNames[i],
		// degreeCurricularPlan, DuplicateDegreeCurricularPlan.broker);
		//			
		//			IExecutionYear executionYear = new ExecutionYear();
		//			executionYear.setYear("2003/2004");
		//			
		//			IExecutionPeriod executionPeriod = new ExecutionPeriod();
		//			executionPeriod.setName("1 Semestre");
		//			executionPeriod.setExecutionYear(executionYear);
		//			
		//			DuplicateDegreeCurricularPlan.createExecutionCourses(degreeCurricularPlan, executionPeriod,
		// new Integer(1), DuplicateDegreeCurricularPlan.broker);
		//			
		//		}
		//		

		String dcps[] = new String[1];

		dcps[0] = "MMA03/05";

		for (int i = 0; i < dcps.length; i++)
		{

			Criteria criteria = new Criteria();
			Query query = null;
			List result = null;

			criteria.addEqualTo("name", dcps[i]);
			query = new QueryByCriteria(DegreeCurricularPlan.class, criteria);
			result = (List) broker.getCollectionByQuery(query);

			if (result.size() != 1)
			{
				throw new Exception("Error Reading Degree Curricular Plan [" + dcps[i] + "]");
			}

			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);

			IExecutionYear executionYear = new ExecutionYear();
			executionYear.setYear("2003/2004");

			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			executionPeriod.setName("1 Semestre");
			executionPeriod.setExecutionYear(executionYear);

			DuplicateDegreeCurricularPlan.createExecutionCourses(
				degreeCurricularPlan,
				executionPeriod,
				new Integer(1),
				DuplicateDegreeCurricularPlan.broker);

		}

	}

	/**
	 * @param degreeCurricularPlan
	 * @param executionPeriod
	 * @param Semester
	 * @param broker
	 * 
	 * This method creates Execution Courses for all CurricularCourses for this Degree Curricular Plan
	 * It also creates the Association Between them and the Sites
	 *  
	 */
	public static void createExecutionCourses(
		IDegreeCurricularPlan degreeCurricularPlan,
		IExecutionPeriod executionPeriod,
		Integer semester,
		PersistenceBroker broker)
		throws Exception
	{
		System.out.println(
			"------------------------------------------------------------------------------------------------");
		System.out.print(
			"Creating Execution Courses for Degree Curricular Plan ["
				+ degreeCurricularPlan.getName()
				+ "] ...  ");

		Criteria criteria = new Criteria();
		Query query = null;
		List result = null;

		broker.beginTransaction();

		// Read The Curricular Courses

		criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
		query = new QueryByCriteria(CurricularCourse.class, criteria);
		result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0)
		{
			throw new Exception(
				"Error Reading Curricular Courses for Degree Curricular Plan ["
					+ degreeCurricularPlan.getName()
					+ "]");
		}

		criteria = new Criteria();
		criteria.addEqualTo("executionYear.year", executionPeriod.getExecutionYear().getYear());
		criteria.addEqualTo("name", executionPeriod.getName());
		query = new QueryByCriteria(ExecutionPeriod.class, criteria);
		List executionPeriodResult = (List) broker.getCollectionByQuery(query);

		if (executionPeriodResult.size() != 1)
		{
			throw new Exception(
				"Error Reading Execution Period for Degree Curricular Plan ["
					+ degreeCurricularPlan.getName()
					+ "]");
		}

		executionPeriod = (IExecutionPeriod) executionPeriodResult.get(0);

		broker.commitTransaction();
		broker.beginTransaction();

		Iterator iterator = result.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setAssociatedCurricularCourses(new ArrayList());
			executionCourse.getAssociatedCurricularCourses().add(curricularCourse);
			executionCourse.setAssociatedEvaluations(null);
			executionCourse.setAssociatedExams(null);
			executionCourse.setAttendingStudents(null);
			executionCourse.setComment("None");
			executionCourse.setExecutionPeriod(executionPeriod);
			executionCourse.setNome(curricularCourse.getName());

			//executionCourse.setLabHours(curricularCourse.getLabHours());
			//executionCourse.setPraticalHours(curricularCourse.getPraticalHours());
			//executionCourse.setTheoPratHours(curricularCourse.getTheoPratHours());
			//executionCourse.setTheoreticalHours(curricularCourse.getTheoreticalHours());

			executionCourse.setSigla(curricularCourse.getCode());
			broker.store(executionCourse);

			Site site = new Site();
			site.setExecutionCourse(executionCourse);
			broker.store(site);

		}
		broker.commitTransaction();
		System.out.println("Done !");
		System.out.println(" - " + result.size() + " Execution Course(s) written ");

	}

	/**
	 * @param originalDCPName
	 * @param degreeCurricularPlan
	 * @param broker
	 * 
	 * This Method Duplicates All the Curricular Courses and Curricular Course Scopes
	 *  
	 */
	public static void duplicateCurricularCoursesAndScopes(
		String originalDCPName,
		IDegreeCurricularPlan degreeCurricularPlan,
		PersistenceBroker broker)
		throws Exception
	{
		System.out.println(
			"------------------------------------------------------------------------------------------------");
		System.out.print(
			"Duplicating Curricular Courses and Scopes for Degree Curricular Plan ["
				+ originalDCPName
				+ "] ...  ");

		Criteria criteria = new Criteria();
		Query query = null;
		List result = null;
		int curricularCoursesWritten = 0;
		int curricularCourseScopesWritten = 0;

		broker.beginTransaction();

		// Read The Curricular Courses

		criteria.addEqualTo("degreeCurricularPlan.name", originalDCPName);
		query = new QueryByCriteria(CurricularCourse.class, criteria);
		result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0)
		{
			throw new Exception(
				"Error Reading Curricular Courses for Degree Curricular Plan [" + originalDCPName + "]");
		}

		broker.commitTransaction();

		Iterator iterator = result.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourse curricularCourseFromBD = (ICurricularCourse) iterator.next();
			ICurricularCourse newCurrilarCourse = new CurricularCourse();

			broker.beginTransaction();

			newCurrilarCourse.setAssociatedExecutionCourses(null);
			newCurrilarCourse.setCode(curricularCourseFromBD.getCode());
			newCurrilarCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			newCurrilarCourse.setDepartmentCourse(null);
			newCurrilarCourse.setName(curricularCourseFromBD.getName());
			newCurrilarCourse.setType(curricularCourseFromBD.getType());
			newCurrilarCourse.setMandatory(curricularCourseFromBD.getMandatory());
			newCurrilarCourse.setBasic(curricularCourseFromBD.getBasic());

			newCurrilarCourse.setLabHours(curricularCourseFromBD.getLabHours());
			newCurrilarCourse.setPraticalHours(curricularCourseFromBD.getPraticalHours());
			newCurrilarCourse.setTheoPratHours(curricularCourseFromBD.getTheoPratHours());
			newCurrilarCourse.setTheoreticalHours(curricularCourseFromBD.getTheoreticalHours());
			newCurrilarCourse.setMaximumValueForAcumulatedEnrollments(curricularCourseFromBD.getMaximumValueForAcumulatedEnrollments());
			newCurrilarCourse.setMinimumValueForAcumulatedEnrollments(curricularCourseFromBD.getMinimumValueForAcumulatedEnrollments());
			newCurrilarCourse.setCredits(curricularCourseFromBD.getCredits());
			newCurrilarCourse.setEctsCredits(curricularCourseFromBD.getEctsCredits());
			newCurrilarCourse.setEnrollmentWeigth(curricularCourseFromBD.getEnrollmentWeigth());
			
			
			newCurrilarCourse.setUniversity(curricularCourseFromBD.getUniversity());
			newCurrilarCourse.setCurricularCourseExecutionScope(
				curricularCourseFromBD.getCurricularCourseExecutionScope());

			broker.store(newCurrilarCourse);
			curricularCoursesWritten++;

			// Create The Scopes

			if (curricularCourseFromBD.getScopes().size() == 0)
			{
				System.out.println(
					" ERROR - Curricular Course ["
						+ curricularCourseFromBD.getIdInternal()
						+ "] has ZERO Scopes !!");
				broker.commitTransaction();
				continue;
			}

			Iterator scopeIterator = curricularCourseFromBD.getScopes().iterator();
			while (scopeIterator.hasNext())
			{
				ICurricularCourseScope curricularCourseScopeFromDB =
					(ICurricularCourseScope) scopeIterator.next();
				ICurricularCourseScope newCurricularCourseScope = new CurricularCourseScope();

				// Read The new Branch
				criteria = new Criteria();
				criteria.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
				criteria.addEqualTo("code", curricularCourseScopeFromDB.getBranch().getCode());
				query = new QueryByCriteria(Branch.class, criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1)
				{
					throw new Exception(
						" Error Reading new Branch for Curricular Course Scope ["
							+ curricularCourseScopeFromDB.getIdInternal()
							+ "] ");
				}

				IBranch branch = (IBranch) result.get(0);

				newCurricularCourseScope.setBranch(branch);
				//			TODO: add sets for begin and end dates and ectsCredits
				broker.store(newCurricularCourseScope);
				curricularCourseScopesWritten++;
			}

			broker.commitTransaction();
		}

		System.out.println("Done !");
		System.out.println(" - " + curricularCoursesWritten + " Curricular Course(s) written ");
		System.out.println(
			" - " + curricularCourseScopesWritten + " Curricular Course Scope(s) written ");

	}

	/**
	 * @param originalDCPName
	 * @param degreeCurricularPlan
	 *            This method copies the branchs from the original Degree Curricular Plan to the new
	 */
	public static void duplicateBranchs(
		String originalDCPName,
		IDegreeCurricularPlan degreeCurricularPlan,
		PersistenceBroker broker)
		throws Exception
	{

		System.out.println(
			"------------------------------------------------------------------------------------------------");
		System.out.print(
			"Duplicating Branchs for Degree Curricular Plan [" + originalDCPName + "] ...  ");

		Criteria criteria = new Criteria();
		Query query = null;
		List result = null;

		broker.beginTransaction();

		// Read The Branchs

		criteria.addEqualTo("degreeCurricularPlan.name", originalDCPName);
		query = new QueryByCriteria(Branch.class, criteria);
		result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0)
		{
			throw new Exception(
				"Error Reading Branchs for Degree Curricular Plan [" + originalDCPName + "]");
		}

		broker.commitTransaction();

		Iterator iterator = result.iterator();
		while (iterator.hasNext())
		{
			IBranch branch = (IBranch) iterator.next();
			IBranch newBranch = new Branch();

			broker.beginTransaction();
			newBranch.setCode(branch.getCode());
			newBranch.setDegreeCurricularPlan(degreeCurricularPlan);
			newBranch.setName(branch.getName());
			newBranch.setScopes(null);

			broker.store(newBranch);
			broker.commitTransaction();
		}

		System.out.println("Done !");
		System.out.println(" - " + result.size() + " Branch(s) written ");

	}

	/**
	 * @param originalDCPName
	 * @param newDCPName
	 * @return A new Instance of a Degree Curricular Plan It's Assumed that the Degree Curricular Plan
	 *         is Unique. At This Time this is True
	 *  
	 */
	public static IDegreeCurricularPlan createDegreeCurricularPlan(
		String originalDCPName,
		String newDCPName,
		PersistenceBroker broker)
		throws Exception
	{
		System.out.println(
			"------------------------------------------------------------------------------------------------");
		System.out.print(
			"Duplicating Degree Curricular Plan ["
				+ originalDCPName
				+ "] to ["
				+ newDCPName
				+ "] ...  ");

		IDegreeCurricularPlan newDegreeCurricularPlan = new DegreeCurricularPlan();

		Criteria criteria = new Criteria();
		Query query = null;
		List result = null;

		broker.beginTransaction();

		// Read The Original Degree Curricular Plan

		criteria.addEqualTo("name", originalDCPName);
		query = new QueryByCriteria(DegreeCurricularPlan.class, criteria);
		result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0)
		{
			throw new Exception("Error Reading Degree Curricular Plan [" + originalDCPName + "]");
		}

		IDegreeCurricularPlan degreeCurricularPlanFromBD = (IDegreeCurricularPlan) result.get(0);
		newDegreeCurricularPlan.setCurricularCourses(null);
		newDegreeCurricularPlan.setDegree(degreeCurricularPlanFromBD.getDegree());

		// FIXME : This is a Duration Of a Master Degree.
		// 		   To Aply to a degree one must change this next line
		newDegreeCurricularPlan.setDegreeDuration(new Integer(2));

		newDegreeCurricularPlan.setEndDate(null);
		newDegreeCurricularPlan.setInitialDate(Calendar.getInstance().getTime());
		newDegreeCurricularPlan.setMarkType(degreeCurricularPlanFromBD.getMarkType());
		newDegreeCurricularPlan.setMinimalYearForOptionalCourses(
			degreeCurricularPlanFromBD.getMinimalYearForOptionalCourses());
		newDegreeCurricularPlan.setName(newDCPName);
		newDegreeCurricularPlan.setNeededCredits(degreeCurricularPlanFromBD.getNeededCredits());
		newDegreeCurricularPlan.setNumerusClausus(degreeCurricularPlanFromBD.getNumerusClausus());
		newDegreeCurricularPlan.setState(DegreeCurricularPlanState.ACTIVE_OBJ);

		broker.store(newDegreeCurricularPlan);
		broker.commitTransaction();

		System.out.println("Done !");
		System.out.println(" - 1 Degree Curricular Plan Written");

		return newDegreeCurricularPlan;
	}

}
