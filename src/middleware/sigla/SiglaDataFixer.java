/*
 * Created on 22/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;

/**
 * @author João Mota
 *
 */
public class SiglaDataFixer {
	private List notFoundInSigla;
	/**
	 * 
	 */
	public SiglaDataFixer() {
		setNotFoundInSigla(new ArrayList());
	}

	public static void main(String[] args) {
		SiglaDataFixer loader = new SiglaDataFixer();
		PersistenceBroker broker =
			PersistenceBrokerFactory.defaultPersistenceBroker();
		loader.updateFenix(broker, loader);
		loader.printProblems();
		broker.close();
	}

	/**
	 * 
	 */
	private void printProblems() {
		System.out.println(
			"NOT FOUND IN SIGLA->" + getNotFoundInSigla().size());
		Iterator iter = getNotFoundInSigla().iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			System.out.println("******************************************");
			System.out.println(
				"idInternal->" + curricularCourse.getIdInternal());
			System.out.println("name->" + curricularCourse.getName());
			System.out.println("code->" + curricularCourse.getCode());
			System.out.println(
				"degreeCurricularPlan->"
					+ curricularCourse.getDegreeCurricularPlan().getName());
			System.out.println(
				"degree->"
					+ curricularCourse
						.getDegreeCurricularPlan()
						.getDegree()
						.getNome());

		}

	}

	/**
	 * @param broker
	 * @param loader
	 */
	private void updateFenix(PersistenceBroker broker, SiglaDataFixer loader) {
		List curricularCourses =
			loader.loadFenixCurricularCourses(broker, loader);
		System.out.println(
			"loaded fenixCurricularCourses from this semester->"
				+ curricularCourses.size());
		Iterator iter = curricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse element = (ICurricularCourse) iter.next();

			if (loader.isCurriculumEmpty(element, broker, loader)) {
				loader.updateCurriculum(element, broker, loader);
			}
		}
	}

	/**
	 * @param element
	 * @param broker
	 * @param loader
	 */
	private void updateCurriculum(
		ICurricularCourse curricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"curricularCourse.idInternal",
			curricularCourse.getIdInternal());
		Query query = new QueryByCriteria(Curriculum.class, crit);
		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
		if (curriculum == null) {
			curriculum = new Curriculum(curricularCourse);
		}
		curriculum =
			loader.fillCurriculumFields(
				curriculum,
				curricularCourse,
				broker,
				loader);
	}

	/**
	 * @param curriculum
	 * @param curricularCourse
	 * @param broker
	 * @param loader
	 */
	private ICurriculum fillCurriculumFields(
		ICurriculum curriculum,
		ICurricularCourse curricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"codigo_lic",
			loader.fenix2SiglaFenixDegreeNumbers(
				curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getIdInternal()));
		crit.addEqualTo("codigo_disc", curricularCourse.getCode());
		Query query =
			new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List siglaCurriculums = (List) broker.getCollectionByQuery(query);
		if (siglaCurriculums == null || siglaCurriculums.isEmpty()) {
			loader.addToNotFound(curricularCourse, loader);
		}
		Iterator iter = siglaCurriculums.iterator();
		while (iter.hasNext()) {
			Plano_curricular_2003final element =
				(Plano_curricular_2003final) iter.next();
			if (element.getObjectivos() != null
				&& !element.getObjectivos().equals("")) {
				loader.writeCurriculum(
					curriculum,
					curricularCourse,
					element,
					broker,
					loader);
				break;
			}
		}

		return null;
	}

	/**
	 * @param curricularCourse
	 * @param loader
	 */
	private void addToNotFound(
		ICurricularCourse curricularCourse,
		SiglaDataFixer loader) {
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
		ICurriculum curriculum,
		ICurricularCourse curricularCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker,
		SiglaDataFixer loader) {
		curricularCourse.setBasic(
			new Boolean(
				sigla2FenixBasic(siglaCurricularCourse.getCiencia_base())));
		curriculum.setProgram(siglaCurricularCourse.getProgr_res());
		curriculum.setGeneralObjectives(siglaCurricularCourse.getObjectivos());
		curriculum.setProgramEn(siglaCurricularCourse.getIngles_progr_res());
		curriculum.setGeneralObjectivesEn(
			siglaCurricularCourse.getIngles_objectivos());
		broker.beginTransaction();
		broker.store(curricularCourse);
		broker.store(curriculum);
		broker.commitTransaction();

	}
	/**
	* @param string
	*/
	private boolean sigla2FenixBasic(String string) {
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
		SiglaDataFixer loader) {

		Criteria crit = new Criteria();
		crit.addEqualTo(
			"curricularCourse.idInternal",
			curricularCourse.getIdInternal());
		Query query = new QueryByCriteria(Curriculum.class, crit);
		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
		if (curriculum == null
			|| ((curriculum.getProgram() == null
				|| curriculum.getProgram().equals(""))
				&& (curriculum.getGeneralObjectives() == null
					|| curriculum.getGeneralObjectives().equals("")))) {
			return true;
		}
		return false;
	}

	/**
	 * @param broker
	 * @param loader
	 * @return
	 */
	private List loadFenixCurricularCourses(
		PersistenceBroker broker,
		SiglaDataFixer loader) {
		Criteria crit = new Criteria();
		crit.addLike("degreeCurricularPlan.name", "%2003/2004");
		Query query = new QueryByCriteria(CurricularCourse.class, crit);

		return (List) broker.getCollectionByQuery(query);
	}

//	private Integer sigla2FenixDegreeNumbers(Integer siglaDegreeNumber) {
//		if (siglaDegreeNumber.intValue() == 24) {
//			siglaDegreeNumber = new Integer(51);
//		}
//		return siglaDegreeNumber;
//	}

	private Integer fenix2SiglaFenixDegreeNumbers(Integer siglaDegreeNumber) {
		if (siglaDegreeNumber.intValue() == 51) {
			siglaDegreeNumber = new Integer(24);
		}
		return siglaDegreeNumber;
	}

	/**
	 * @return
	 */
	public List getNotFoundInSigla() {
		return notFoundInSigla;
	}

	/**
	 * @param siglaObjectsWithoutInfo
	 */
	public void setNotFoundInSigla(List siglaObjectsWithoutInfo) {
		this.notFoundInSigla = siglaObjectsWithoutInfo;
	}

}
