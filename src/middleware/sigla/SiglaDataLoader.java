/*
 * Created on 22/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.BibliographicReference;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ExecutionPeriod;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISite;
import Dominio.Site;
import Util.PeriodState;

/**
 * @author João Mota
 *
 */
public class SiglaDataLoader {

	private List siglaCurricularCoursesWithProblems;
	private List fenixExecutionCoursesWithProblems;
	/**
	 * 
	 */
	public SiglaDataLoader() {
		setSiglaCurricularCoursesWithProblems(new ArrayList());
		setFenixExecutionCoursesWithProblems(new ArrayList());
	}

	/**
	 * @return
	 */
	public List getSiglaCurricularCoursesWithProblems() {
		return siglaCurricularCoursesWithProblems;
	}

	/**
	 * @param siglaCurricularCoursesWithProblems
	 */
	public void setSiglaCurricularCoursesWithProblems(List siglaCurricularCoursesWithProblems) {
		this.siglaCurricularCoursesWithProblems =
			siglaCurricularCoursesWithProblems;
	}

	public static void main(String[] args) {
		SiglaDataLoader loader = new SiglaDataLoader();
		PersistenceBroker broker =
			PersistenceBrokerFactory.defaultPersistenceBroker();
		
		updateFenix(loader, broker);
		loader.printSiglaProblems(loader);
		loader.printFenixProblems(loader);
		
		broker.close();
	}

	/**
	 * 
	 */
	private void printFenixProblems(SiglaDataLoader loader) {
		System.out.println(
			"######################################################");
		System.out.println(
			"##############Problemas com o Fenix###################");
		System.out.println(
			"######################################################");
		List problems = loader.getFenixExecutionCoursesWithProblems();
		System.out.println("Número disciplinas do fenix com problemas->"+loader.getFenixExecutionCoursesWithProblems().size());
		
		Iterator iter = problems.iterator();
		while (iter.hasNext()) {
			IExecutionCourse executionCourse =
				(IExecutionCourse) iter.next();
			System.out.println(executionCourse);
		}
		System.out.println(
			"######################################################");

	}

	/**
	 * 
	 */
	private void printSiglaProblems(SiglaDataLoader loader) {
		System.out.println(
			"######################################################");
		System.out.println(
			"##############Problemas com o Sigla###################");
		
		System.out.println(
			"######################################################");
		List problems = loader.getSiglaCurricularCoursesWithProblems();
		System.out.println("Número disciplinas do sigla com problemas->"+loader.getSiglaCurricularCoursesWithProblems().size());
		Iterator iter = problems.iterator();
		while (iter.hasNext()) {
			Plano_curricular_2003final siglaCurricularCourse =
				(Plano_curricular_2003final) iter.next();
			System.out.println(siglaCurricularCourse);
		}
		System.out.println(
			"######################################################");
	}

	/**
	 * @param broker
	 */
	private List loadSiglaCurricularCourses(PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo("semestre","1");
		Query query =
			new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List siglaCurricularCourses = (List) broker.getCollectionByQuery(query);
		System.out.println(
			"finished loading sigla Curricular Courses! size->"
				+ siglaCurricularCourses.size());
		return siglaCurricularCourses;
	}

	private Integer sigla2FenixDegreeNumbers(Integer siglaDegreeNumber) {
		if (siglaDegreeNumber.intValue() == 24) {
			siglaDegreeNumber = new Integer(51);
		}
		return siglaDegreeNumber;
	}
	
	private Integer fenix2SiglaFenixDegreeNumbers(Integer siglaDegreeNumber) {
			if (siglaDegreeNumber.intValue() == 51) {
				siglaDegreeNumber = new Integer(24);
			}
			return siglaDegreeNumber;
		}

	private static void updateFenix(
		SiglaDataLoader loader,
		PersistenceBroker broker) {

		System.out.println("A carregar disciplinas curriculares do sigla");
		List siglaCurricularCourses = loader.loadSiglaCurricularCourses(broker);
		Iterator iterSigla = siglaCurricularCourses.iterator();
		while (iterSigla.hasNext()) {
			Plano_curricular_2003final siglaCurricularCourse =
				(Plano_curricular_2003final) iterSigla.next();
			List fenixCurricularCourses =
				getFenixCurricularCourses(
					loader,
					broker,
					siglaCurricularCourse);
			if (fenixCurricularCourses == null
				|| fenixCurricularCourses.size() == 0) {

				if (!loader
					.getSiglaCurricularCoursesWithProblems()
					.contains(siglaCurricularCourse)) {

					loader.getSiglaCurricularCoursesWithProblems().add(
						siglaCurricularCourse);
				}
			} else {
				updateFenixCurricularCourses(
					fenixCurricularCourses,
					siglaCurricularCourse,
					broker);
				updateFenixExecutionCourses(
					fenixCurricularCourses,
					siglaCurricularCourse,
					broker,
					loader);
			}
		}
	}

	private static List getFenixCurricularCourses(
		SiglaDataLoader loader,
		PersistenceBroker broker,
		Plano_curricular_2003final siglaCurricularCourse) {
		Criteria fenixCurricularCourseCriteria = new Criteria();
		fenixCurricularCourseCriteria.addEqualTo(
			"code",
			siglaCurricularCourse.getCodigo_disc());
		fenixCurricularCourseCriteria.addEqualTo(
			"degreeCurricularPlan.degreeKey",
			loader.sigla2FenixDegreeNumbers(
				siglaCurricularCourse.getCodigo_lic()));
		fenixCurricularCourseCriteria.addLike(
			"degreeCurricularPlan.name",
			"%2003/2004%");
//		fenixCurricularCourseCriteria.addEqualTo(
//			"name",
//			siglaCurricularCourse.getNome_disc());
		Query fenixCurricularCourseQuery =
			new QueryByCriteria(
				CurricularCourse.class,
				fenixCurricularCourseCriteria);
		List fenixCurricularCourses =
			(List) broker.getCollectionByQuery(fenixCurricularCourseQuery);
		return fenixCurricularCourses;
	}

	/**
	 * @param fenixCurricularCourses
	 * @param siglaCurricularCourse
	 */
	private static void updateFenixExecutionCourses(
		List fenixCurricularCourses,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker,
		SiglaDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo("state", PeriodState.CURRENT);
		Query query = new QueryByCriteria(ExecutionPeriod.class, crit);
		IExecutionPeriod currentExecutionPeriod =
			(IExecutionPeriod) broker.getObjectByQuery(query);
		Iterator iter = fenixCurricularCourses.iterator();
		List executionCourses = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			executionCourses =
				(List) CollectionUtils.union(
					executionCourses,
					curricularCourse.getAssociatedExecutionCourses());
		}

		iter = executionCourses.iterator();

		while (iter.hasNext()) {
			IExecutionCourse executionCourse =
				(IExecutionCourse) iter.next();
			//NOTE: only executionCourses of current executionPeriod are updated
			if (executionCourse
				.getExecutionPeriod()
				.equals(currentExecutionPeriod)) {
				updateBibliographicReferences(executionCourse, broker, loader);
				updateSite(executionCourse, siglaCurricularCourse, broker);
			}
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateSite(
		IExecutionCourse executionCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		Query query = new QueryByCriteria(Site.class, crit);
		ISite site = (ISite) broker.getObjectByQuery(query);
		if ((site.getAlternativeSite() == null
			|| site.getAlternativeSite().equals(""))
			&& (siglaCurricularCourse.getEnder_web() != null
				&& !siglaCurricularCourse.getEnder_web().equals(""))) {
			site.setAlternativeSite(siglaCurricularCourse.getEnder_web());
			broker.beginTransaction();
			broker.store(site);
			broker.commitTransaction();
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateBibliographicReferences(
		IExecutionCourse executionCourse,
		PersistenceBroker broker,
		SiglaDataLoader loader) {
		List fenixCurricularCourses =
			executionCourse.getAssociatedCurricularCourses();
		List siglaCurricularCourses =
			getSiglaCurricularCourses(fenixCurricularCourses, broker,loader);
		if (siglaCurricularCourses == null
			|| siglaCurricularCourses.isEmpty()) {
			if (!loader
				.getFenixExecutionCoursesWithProblems()
				.contains(executionCourse)) {
				loader.getFenixExecutionCoursesWithProblems().add(
					executionCourse);
			}
		} else {
			Plano_curricular_2003final siglaCurricularCourse =
				getBestSiglaCurricularCourse(siglaCurricularCourses);
			if (siglaCurricularCourse != null) {
				insertBibliographicReferences(
					executionCourse,
					siglaCurricularCourse,
					broker);
			}
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void insertBibliographicReferences(
		IExecutionCourse executionCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
			
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_princ_1());
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_princ_2());
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_princ_3());
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_sec_1());
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_sec_2());
		storeBibliographicReference(executionCourse, siglaCurricularCourse, broker,siglaCurricularCourse.getBib_sec_3());
	
	}

	private static void storeBibliographicReference(IExecutionCourse executionCourse, Plano_curricular_2003final siglaCurricularCourse, PersistenceBroker broker, String title) {
		if (title!=null && !title.equals("")){
			IBibliographicReference bibliographicReference =
						new BibliographicReference(
							executionCourse,
							title,
							" ",
							" ",
							" ",
							new Boolean(false));
					
					Criteria crit = new Criteria();
					crit.addEqualTo("title",bibliographicReference.getTitle());
					Query query = new QueryByCriteria(BibliographicReference.class,crit);
					IBibliographicReference reference = (IBibliographicReference) broker.getObjectByQuery(query);
					if (reference==null){
						broker.beginTransaction();	
						broker.store(bibliographicReference);
						broker.commitTransaction();
					}	
					
		}
		
	}

	/**
	 * @param siglaCurricularCourses
	 * @return
	 */
	private static Plano_curricular_2003final getBestSiglaCurricularCourse(List siglaCurricularCourses) {
		Plano_curricular_2003final result = null;
		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Plano_curricular_2003final aux =
				(Plano_curricular_2003final) iter.next();
			if (aux.getBib_princ_1() != null
				&& aux.getBib_princ_1().length() != 0
				&& (result == null
					|| result.getBib_princ_1().length()
						< aux.getBib_princ_1().length())) {
				result = aux;
			}
		}
		return result;
	}

	/**
	 * @param siglaCurricularCourses
	 * @param broker
	 * @return
	 */
	private static List getSiglaCurricularCourses(
		List siglaCurricularCourses,
		PersistenceBroker broker,SiglaDataLoader loader) {
		Iterator iter = siglaCurricularCourses.iterator();
		List result = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			result =
				(List) CollectionUtils.union(
					result,
					getSiglaCurricularCourse(curricularCourse, broker,loader));
		}
		return result;
	}

	/**
	 * @param curricularCourse
	 * @param broker
	 * @return
	 */
	private static List getSiglaCurricularCourse(
		ICurricularCourse curricularCourse,
		PersistenceBroker broker, SiglaDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo("codigo_disc", curricularCourse.getCode());
		crit.addEqualTo("ano_lectivo", "2003");
		//crit.addEqualTo("nome_disc", curricularCourse.getName());
		crit.addEqualTo("Codigo_Lic",loader.fenix2SiglaFenixDegreeNumbers(curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal()));
		crit.addEqualTo("semestre", "1");
		Query query =
			new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List result = (List) broker.getCollectionByQuery(query);
		return result;
	}

	/**
	 * @param fenixCurricularCourses
	 * @param siglaCurricularCourse
	 */
	private static void updateFenixCurricularCourses(
		List fenixCurricularCourses,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Iterator iter = fenixCurricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iter.next();
			//NOTE: if the curricular course in fenix is basic it will continue basic
			if (!curricularCourse.getBasic().booleanValue()) {
				curricularCourse.setBasic(
					new Boolean(
						sigla2FenixBasic(
							siglaCurricularCourse.getCiencia_base())));
				updateCurriculum(
					curricularCourse,
					siglaCurricularCourse,
					broker);
				broker.beginTransaction();
				broker.store(curricularCourse);
				broker.commitTransaction();
			}
		}

	}

	/**
	 * @param curricularCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateCurriculum(
		ICurricularCourse curricularCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo(
			"keyCurricularCourse",
			curricularCourse.getIdInternal());
		Query query = new QueryByCriteria(Curriculum.class, crit);
		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
		if (curriculum == null) {
			curriculum = new Curriculum(curricularCourse);
		}
//		if (siglaCurricularCourse.getCrit_av() != null) {
//			curriculum.setEvaluationElements(
//				siglaCurricularCourse.getCrit_av());
//		}
//		if (siglaCurricularCourse.getIngles_crit_av() != null) {
//			curriculum.setEvaluationElementsEn(
//				siglaCurricularCourse.getIngles_crit_av());
//		}

		if (siglaCurricularCourse.getObjectivos() != null) {
			curriculum.setGeneralObjectives(
				siglaCurricularCourse.getObjectivos());
		}
		if (siglaCurricularCourse.getIngles_objectivos() != null) {
			curriculum.setGeneralObjectivesEn(
				siglaCurricularCourse.getIngles_objectivos());
		}
		if (siglaCurricularCourse.getProgr_res() != null) {
			curriculum.setProgram(siglaCurricularCourse.getProgr_res());
		}
		if (siglaCurricularCourse.getIngles_progr_res() != null) {
			curriculum.setProgramEn(
				siglaCurricularCourse.getIngles_progr_res());
		}
		broker.beginTransaction();
		broker.store(curriculum);
		broker.commitTransaction();
	}

	/**
	 * @param string
	 */
	private static boolean sigla2FenixBasic(String string) {
		return string.equals("True");
	}

	/**
	 * @return
	 */
	public List getFenixExecutionCoursesWithProblems() {
		return fenixExecutionCoursesWithProblems;
	}

	/**
	 * @param fenixExecutionCoursesWithProblems
	 */
	public void setFenixExecutionCoursesWithProblems(List fenixExecutionCoursesWithProblems) {
		this.fenixExecutionCoursesWithProblems =
			fenixExecutionCoursesWithProblems;
	}

}
