/*
 * Created on 22/Mai/2003
 * 
 *  
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourseScope;
import Dominio.Curriculum;
import Dominio.ExecutionYear;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.Pessoa;

/**
 * @author João Mota Changed by Fernanda Quitério 28/Nov/2003
 *  
 */
public class SiglaDataFixer
{
	private List notFoundInSigla;
	/**
	 *  
	 */
	public SiglaDataFixer()
	{
		setNotFoundInSigla(new ArrayList());
	}

	public static void main(String[] args)
	{
		SiglaDataFixer loader = new SiglaDataFixer();
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		loader.updateFenix(broker, loader);
		loader.printProblems();
		broker.close();
	}

	/**
	 *  
	 */
	private void printProblems()
	{
		System.out.println("NOT FOUND IN SIGLA->" + getNotFoundInSigla().size());
		Iterator iter = getNotFoundInSigla().iterator();
		while (iter.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			System.out.println("******************************************");
			System.out.println("idInternal->" + curricularCourse.getIdInternal());
			System.out.println("name->" + curricularCourse.getName());
			System.out.println("code->" + curricularCourse.getCode());
			System.out.println(
				"degreeCurricularPlan->" + curricularCourse.getDegreeCurricularPlan().getName());
			System.out.println(
				"degree->" + curricularCourse.getDegreeCurricularPlan().getDegree().getNome());

		}

	}

	/**
	 * @param broker
	 * @param loader
	 */
	private void updateFenix(PersistenceBroker broker, SiglaDataFixer loader)
	{
//		update curriculum set last_modification_date='2003-01-01 00:00:00';
		
		
		List curricularCourses = loader.loadFenixCurricularCourses(broker, loader);
		System.out.println(
			"loaded fenixCurricularCourses from this semester->" + curricularCourses.size());
		Iterator iter = curricularCourses.iterator();
		while (iter.hasNext())
		{
			ICurricularCourse element = (ICurricularCourse) iter.next();

			//			if (loader.isCurriculumEmpty(element, broker, loader))
			//			{
			//				loader.updateCurriculum(element, broker, loader);
			//			}

			loader.insertCurriculum(element, broker, loader);
		}
	}

	/**
	 * @param element
	 * @param broker
	 * @param loader
	 */
	private void insertCurriculum(
		ICurricularCourse curricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader)
	{

		Criteria crit = new Criteria();
		crit.addEqualTo(
			"codigo_lic",
			loader.fenix2SiglaFenixDegreeNumbers(
				curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal()));
		crit.addEqualTo("codigo_disc", curricularCourse.getCode());
		Query query = new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List siglaCurriculums = (List) broker.getCollectionByQuery(query);
		Iterator iter = siglaCurriculums.iterator();
		while (iter.hasNext())
		{
			Plano_curricular_2003final element = (Plano_curricular_2003final) iter.next();
			if ((element.getProgr_res() != null && !element.getProgr_res().equals(""))
				|| (element.getObjectivos() != null && !element.getObjectivos().equals("")))
			{
				loader.writeCurriculum(curricularCourse, element, broker, loader);
				break;
			}
		}
	}

	/**
	 * @param element
	 * @param broker
	 * @param loader
	 */
	//	private void updateCurriculum(
	//		ICurricularCourse curricularCourse,
	//		PersistenceBroker broker,
	//		SiglaDataFixer loader)
	//	{
	//		Criteria crit = new Criteria();
	//		crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
	//		Query query = new QueryByCriteria(Curriculum.class, crit);
	//		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
	//		if (curriculum == null)
	//		{
	//			curriculum = new Curriculum(curricularCourse);
	//		}
	//		curriculum = loader.fillCurriculumFields(curriculum, curricularCourse, broker, loader);
	//	}

	/**
	 * @param curriculum
	 * @param curricularCourse
	 * @param broker
	 * @param loader
	 */
	//	private ICurriculum fillCurriculumFields(
	//		ICurriculum curriculum,
	//		ICurricularCourse curricularCourse,
	//		PersistenceBroker broker,
	//		SiglaDataFixer loader)
	//	{
	//		Criteria crit = new Criteria();
	//		crit.addEqualTo(
	//			"codigo_lic",
	//			loader.fenix2SiglaFenixDegreeNumbers(
	//				curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal()));
	//		crit.addEqualTo("codigo_disc", curricularCourse.getCode());
	//		Query query = new QueryByCriteria(Plano_curricular_2003final.class, crit);
	//		List siglaCurriculums = (List) broker.getCollectionByQuery(query);
	//		if (siglaCurriculums == null || siglaCurriculums.isEmpty())
	//		{
	//			loader.addToNotFound(curricularCourse, loader);
	//		}
	//		Iterator iter = siglaCurriculums.iterator();
	//		while (iter.hasNext())
	//		{
	//			Plano_curricular_2003final element = (Plano_curricular_2003final) iter.next();
	//			if (element.getObjectivos() != null && !element.getObjectivos().equals(""))
	//			{ // acrescentar prog
	//				loader.writeCurriculum(curriculum, curricularCourse, element, broker, loader);
	//				break;
	//			}
	//		}
	//
	//		return null;
	//	}

	/**
	 * @param curricularCourse
	 * @param loader
	 */
	private void addToNotFound(ICurricularCourse curricularCourse, SiglaDataFixer loader)
	{
		getNotFoundInSigla().add(curricularCourse);

	}

	/**
	 * @param curriculum
	 * @param curricularCourse
	 * @param element
	 * @param broker
	 * @param loader
	 */
	private void writeCurriculum(
		ICurricularCourse curricularCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader)
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("username", new String("L45446"));

		Query query = new QueryByCriteria(Pessoa.class, crit);
		IPessoa person = (IPessoa) broker.getObjectByQuery(query);

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
		Query queryCurriculum = new QueryByCriteria(Curriculum.class, criteria);

		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(queryCurriculum);

		Calendar today = Calendar.getInstance();
		if (curriculum == null)
		{
			curriculum = new Curriculum();
			curriculum.setLastModificationDate(today.getTime());
		}

		Criteria criteriaExecutionYear = new Criteria();
		criteriaExecutionYear.addEqualTo("state", new String("C"));
		Query queryEY = new QueryByCriteria(ExecutionYear.class, criteriaExecutionYear);
		IExecutionYear executionYear = (IExecutionYear) broker.getObjectByQuery(queryEY);

		if (curriculum.getLastModificationDate().before(executionYear.getBeginDate())
			|| curriculum.getLastModificationDate().after(executionYear.getEndDate()))
		{
			curriculum = new Curriculum();
		}

		curricularCourse.setBasic(
			new Boolean(sigla2FenixBasic(siglaCurricularCourse.getCiencia_base())));
		curriculum.setCurricularCourse(curricularCourse);
		curriculum.setProgram(siglaCurricularCourse.getProgr_res());
		curriculum.setGeneralObjectives(siglaCurricularCourse.getObjectivos());
		curriculum.setProgramEn(siglaCurricularCourse.getIngles_progr_res());
		curriculum.setGeneralObjectivesEn(siglaCurricularCourse.getIngles_objectivos());

		curriculum.setLastModificationDate(today.getTime());

		curriculum.setPersonWhoAltered(person);

		broker.beginTransaction();
		broker.store(curricularCourse);
		broker.store(curriculum);
		broker.commitTransaction();
	}
	/**
	 * @param string
	 */
	private boolean sigla2FenixBasic(String string)
	{
		return string.equals("True");
	}
	/**
	 * @param element
	 * @param broker
	 * @param loader
	 * @return
	 */
	private boolean isCurriculumEmpty(
		ICurricularCourse curricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader)
	{

		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		Query query = new QueryByCriteria(Curriculum.class, crit);
		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
		if (curriculum == null
			|| ((curriculum.getProgram() == null || curriculum.getProgram().equals(""))
				&& (curriculum.getGeneralObjectives() == null
					|| curriculum.getGeneralObjectives().equals(""))))
		{
			return true;
		}
		return false;
	}

	/**
	 * @param broker
	 * @param loader
	 * @return
	 */
	private List loadFenixCurricularCourses(PersistenceBroker broker, SiglaDataFixer loader)
	{
		// get curricular course scopes from execution year 2003/2004 and second semester
		Criteria crit = new Criteria();
		crit.addLike("curricularCourse.degreeCurricularPlan.name", "%2003/2004");
		crit.addEqualTo("curricularSemester.semester", new Integer(2));
		Query query = new QueryByCriteria(CurricularCourseScope.class, crit);

		List curricularCourseScopes = (List) broker.getCollectionByQuery(query);

		// filter curricular courses
		List curricularCourses = new ArrayList();
		Iterator iterCCS = curricularCourseScopes.iterator();
		while (iterCCS.hasNext())
		{
			CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iterCCS.next();

			Criteria criteriaPlanoCurricular;
			Query queryCP;
			List curricularPlans = getCurriculums(broker, loader, curricularCourseScope);
			if (curricularPlans == null || curricularPlans.isEmpty())
			{
				loader.addToNotFound(curricularCourseScope.getCurricularCourse(), loader);
			}

			if (curricularPlans != null && !curricularPlans.isEmpty())
			{
				printFound(curricularCourseScope, curricularPlans);

				if (((Plano_curricular_2003final) curricularPlans.get(0))
					.getCodigo_disc()
					.equals(curricularCourseScope.getCurricularCourse().getCode())
					&& ((Plano_curricular_2003final) curricularPlans.get(0)).getCodigo_lic().equals(
						curricularCourseScope
							.getCurricularCourse()
							.getDegreeCurricularPlan()
							.getDegree()
							.getIdInternal())
					&& !curricularCourses.contains(curricularCourseScope.getCurricularCourse()))
				{
					curricularCourses.add(curricularCourseScope.getCurricularCourse());
				}
			}
		}
		System.out.println(
			"número de curricular courses obtidas do fenix de 2003/2004 no segundo semestre: "
				+ curricularCourses.size());

		return curricularCourses;
	}

	//	private Integer sigla2FenixDegreeNumbers(Integer siglaDegreeNumber) {
	//		if (siglaDegreeNumber.intValue() == 24) {
	//			siglaDegreeNumber = new Integer(51);
	//		}
	//		return siglaDegreeNumber;
	//	}

	private void printFound(CurricularCourseScope curricularCourseScope, List curricularPlans)
	{
		System.out.println("***************** FOUND IN SIGLA *************************");
		System.out.println("idInternal->" + curricularCourseScope.getCurricularCourse().getIdInternal());
		System.out.println("name->" + curricularCourseScope.getCurricularCourse().getName());
		System.out.println("code->" + curricularCourseScope.getCurricularCourse().getCode());
		System.out.println(
			"degreeCurricularPlan->" + curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getName());
		System.out.println(
			"degree->" + curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
		System.out.println(
			"planos curriculares para esta curricularCourse: "
				+ curricularPlans.size());
	}

	private List getCurriculums(PersistenceBroker broker, SiglaDataFixer loader, CurricularCourseScope curricularCourseScope)
	{
		// add to list only curricular courses that exist in table
		Criteria criteriaPlanoCurricular = new Criteria();
		criteriaPlanoCurricular.addEqualTo(
			"codigo_lic",
			loader.fenix2SiglaFenixDegreeNumbers(
				curricularCourseScope
					.getCurricularCourse()
					.getDegreeCurricularPlan()
					.getDegree()
					.getIdInternal()));
		criteriaPlanoCurricular.addEqualTo(
			"codigo_disc",
			curricularCourseScope.getCurricularCourse().getCode());
		Query queryCP =
			new QueryByCriteria(Plano_curricular_2003final.class, criteriaPlanoCurricular);
		List curricularPlans = (List) broker.getCollectionByQuery(queryCP);
		return curricularPlans;
	}

	private Integer fenix2SiglaFenixDegreeNumbers(Integer siglaDegreeNumber)
	{
		if (siglaDegreeNumber.intValue() == 51)
		{
			siglaDegreeNumber = new Integer(24);
		}
		return siglaDegreeNumber;
	}

	/**
	 * @return
	 */
	public List getNotFoundInSigla()
	{
		return notFoundInSigla;
	}

	/**
	 * @param siglaObjectsWithoutInfo
	 */
	public void setNotFoundInSigla(List siglaObjectsWithoutInfo)
	{
		this.notFoundInSigla = siglaObjectsWithoutInfo;
	}

}
