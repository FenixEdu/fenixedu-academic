/*
 * Created on 22/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.BibliographicReference;
import Dominio.DisciplinaExecucao;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class SiglaDataLoader {

	

	public static void main(String[] args) {
		SiglaDataLoader loader = new SiglaDataLoader();

		System.out.println("A carregar disciplinas execução do fenix");
		List fenixExecutionCourses = loader.loadFenixExecutionCourses();
		System.out.println(
			"carregamento efectuado! nº de disciplinas->"
				+ fenixExecutionCourses.size());
		Iterator fenixIter = fenixExecutionCourses.iterator();
		while (fenixIter.hasNext()) {
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) fenixIter.next();
			System.out.println(
				"a efectuar update à disciplinaExecução->"
					+ executionCourse.getSigla()
					+ "-"
					+ executionCourse.getNome());
			List fenixCurricularCourses =
				executionCourse.getAssociatedCurricularCourses();
			List siglaCurricularCourses =
				loader.fenixToSiglaCurricularCourses(
					fenixCurricularCourses,
					executionCourse);
			List siglaCurricularCoursesEng =
				loader.fenixToSiglaCurricularCoursesEng(
					fenixCurricularCourses,
					executionCourse);

			try {
				loader.updateFenixCourses(
					executionCourse,
					siglaCurricularCourses,
					siglaCurricularCoursesEng);

				System.out.println("update efectuado");
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourses
	 */
	private void updateResponsibleTeachers(
		IDisciplinaExecucao executionCourse,
		List siglaCurricularCourses) {
		List teachers = new ArrayList();
		Iterator iter = siglaCurricularCourses.iterator();
		Query query = null;
		PersistenceBroker broker = null;
		List numsMec = new ArrayList();
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		
		while (iter.hasNext()) {
			
			Curr_licenciatura siglaCurricularCourse =
				(Curr_licenciatura) iter.next();
			Criteria crit = new Criteria();
			crit.addEqualTo(
				"codigo_Disc",
				siglaCurricularCourse.getCodigo_disc());
			crit.addEqualTo(
				"ano_Lectivo",
				siglaCurricularCourse.getAno_lectivo());
			crit.addEqualTo(
				"codigo_Curso",
				siglaCurricularCourse.getCodigo_lic());
			crit.addEqualTo(
				"ano_Curricular",
				siglaCurricularCourse.getAno_curricular());
			crit.addEqualTo("semestre", siglaCurricularCourse.getSemestre());
			crit.addEqualTo(
				"codigo_Ramo",
				siglaCurricularCourse.getCodigo_ramo());
			query = new QueryByCriteria(Responsavel.class, crit);
			Collection siglaTeachers =
				(Collection) broker.getCollectionByQuery(query);
			Iterator iter1 = siglaTeachers.iterator();
			while (iter1.hasNext()) {
				Responsavel responsavel = (Responsavel) iter1.next();
				if (!numsMec.contains(responsavel.no_Mec)) {
					numsMec.add(responsavel.no_Mec);
				}
			}

		}
		Iterator iter2 = numsMec.iterator();
		while (iter2.hasNext()) {
			Integer numMec = (Integer) iter2.next();
			Criteria crit = new Criteria();
			crit.addEqualTo("person.username", numMec);
			query = new QueryByCriteria(Teacher.class, crit);
			ITeacher teacher = (ITeacher) broker.getObjectByQuery(query);
			if (teacher == null) {
				System.out.println("não encontrei o docente com número mecanográfico:"+numMec);
			}else {
				IProfessorship professorship = new Professorship(teacher,executionCourse);
				IResponsibleFor responsibleFor = new ResponsibleFor(teacher,executionCourse);
				
				broker.store(professorship);
				broker.store(responsibleFor);
				
			}
		}
		
		broker.close();
	}

	private String fenixYearToSiglaYear(String fenixYear) {
		String[] res = fenixYear.split("/");
		return res[0];
	}

	private List fenixToSiglaCurricularCoursesEng(
		List fenixCurricularCourses,
		IDisciplinaExecucao executionCourse) {
		List siglaCurricularCourses = new ArrayList();
		PersistenceBroker broker = null;

		Query query = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Iterator iter = fenixCurricularCourses.iterator();
			while (iter.hasNext()) {
				ICurricularCourse fenixCurricularCourse =
					(ICurricularCourse) iter.next();

				List scopes = fenixCurricularCourse.getScopes();
				Iterator iter1 = scopes.iterator();
				while (iter.hasNext()) {
					ICurricularCourseScope scope =
						(ICurricularCourseScope) iter.next();
					Criteria crit = new Criteria();
					crit.addEqualTo(
						"nome_disc",
						fenixCurricularCourse.getName());
					crit.addEqualTo(
						"codigo_disc",
						fenixCurricularCourse.getCode());
					crit.addEqualTo(
						"ano_lectivo",
						fenixYearToSiglaYear(
							executionCourse
								.getExecutionPeriod()
								.getExecutionYear()
								.getYear()));
					crit.addEqualTo(
						"semestre",
						executionCourse.getExecutionPeriod().getSemester());
					crit.addEqualTo(
						"codigo_lic",
						fenixCurricularCourse
							.getDegreeCurricularPlan()
							.getDegree()
							.getIdInternal());
					crit.addEqualTo(
						"ano_curricular",
						scope
							.getCurricularSemester()
							.getCurricularYear()
							.getYear());

					query = new QueryByCriteria(Curr_lic_ingles.class, crit);
					Collection curricularCourses =
						(Collection) broker.getCollectionByQuery(query);
					siglaCurricularCourses.addAll(curricularCourses);
				}

			}

			broker.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return siglaCurricularCourses;
	}

	private void updateFenixCourses(
		IDisciplinaExecucao executionCourse,
		List siglaCurricularCourses,
		List siglaCurricularCoursesEng)
		throws ExcepcaoPersistencia {
		ISite site = null;
		ICurriculum curriculum = null;
		IEvaluation evaluation = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentEvaluation persistentEvaluation =
				sp.getIPersistentEvaluation();
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();
			sp.iniciarTransaccao();
			site = persistentSite.readByExecutionCourse(executionCourse);
			curriculum =
				persistentCurriculum.readCurriculumByExecutionCourse(
					executionCourse);
			evaluation =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			site = updateSite(site, siglaCurricularCourses);
			curriculum = updateCurriculum(curriculum, siglaCurricularCourses);
			curriculum =
				updateCurriculumEng(curriculum, siglaCurricularCoursesEng);
			evaluation = updateEvaluation(evaluation, siglaCurricularCourses);
			evaluation =
				updateEvaluationEng(evaluation, siglaCurricularCoursesEng);
			updateBibliography(executionCourse, siglaCurricularCourses);
			updateResponsibleTeachers(executionCourse, siglaCurricularCourses);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}

	}

	/**
	 * @param evaluation
	 * @param siglaCurricularCoursesEng
	 * @return
	 */
	private IEvaluation updateEvaluationEng(
		IEvaluation evaluation,
		List siglaCurricularCoursesEng) {

		String crit_av = "";

		Iterator iter = siglaCurricularCoursesEng.iterator();
		while (iter.hasNext()) {
			Curr_lic_ingles siglaCurricularCourse =
				(Curr_lic_ingles) iter.next();
			if (siglaCurricularCourse.getCrit_av() != null
				&& siglaCurricularCourse.getCrit_av().length()
					> crit_av.length()) {
				crit_av = siglaCurricularCourse.getCrit_av();
			}
		}
		if (evaluation.getEvaluationElementsEn() == null
			|| evaluation.getEvaluationElementsEn().length()
				<= crit_av.length()) {
			evaluation.setEvaluationElementsEn(crit_av);
		}
		return evaluation;
	}

	/**
	 * @param curriculum
	 * @param siglaCurricularCoursesEng
	 * @return
	 */
	private ICurriculum updateCurriculumEng(
		ICurriculum curriculum,
		List siglaCurricularCoursesEng) {
		String objectivosOP = "";
		String objectivosGerais = "";
		String programa = "";

		Iterator iter = siglaCurricularCoursesEng.iterator();
		while (iter.hasNext()) {
			Curr_lic_ingles siglaCurricularCourse =
				(Curr_lic_ingles) iter.next();
			if (siglaCurricularCourse.getObjectivos() != null
				&& siglaCurricularCourse.getObjectivos().length()
					>= objectivosGerais.length()) {
				objectivosGerais = siglaCurricularCourse.getObjectivos();
			}
			if (siglaCurricularCourse.getObjectivos_op() != null
				&& siglaCurricularCourse.getObjectivos_op().length()
					>= objectivosOP.length()) {
				objectivosOP = siglaCurricularCourse.getObjectivos_op();
			}
			if (siglaCurricularCourse.getProgr_res() != null
				&& siglaCurricularCourse.getProgr_res().length()
					>= programa.length()) {
				programa = siglaCurricularCourse.getProgr_res();
			}

		}
		if (curriculum.getGeneralObjectivesEn().length()
			< objectivosGerais.length()) {
			curriculum.setGeneralObjectivesEn(objectivosGerais);
		}
		if (curriculum.getOperacionalObjectivesEn().length()
			< objectivosOP.length()) {
			curriculum.setOperacionalObjectivesEn(objectivosOP);
		}
		if (curriculum.getProgramEn().length() < programa.length()) {
			curriculum.setProgramEn(programa);
		}

		return curriculum;

	}

	/**
	 * @param evaluation
	 * @param siglaCurricularCourses
	 * @return
	 */
	private IEvaluation updateEvaluation(
		IEvaluation evaluation,
		List siglaCurricularCourses) {
		String crit_av = "";

		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Curr_licenciatura siglaCurricularCourse =
				(Curr_licenciatura) iter.next();
			if (siglaCurricularCourse.getCrit_av() != null
				&& siglaCurricularCourse.getCrit_av().length()
					> crit_av.length()) {
				crit_av = siglaCurricularCourse.getCrit_av();
			}
		}
		if (evaluation.getEvaluationElements() == null
			|| evaluation.getEvaluationElements().length() <= crit_av.length()) {
			evaluation.setEvaluationElements(crit_av);
		}
		return evaluation;
	}

	/**
	 * @param curriculum
	 * @param siglaCurricularCourses
	 * @return
	 */
	private ICurriculum updateCurriculum(
		ICurriculum curriculum,
		List siglaCurricularCourses) {
		String objectivosOP = "";
		String objectivosGerais = "";
		String programa = "";

		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Curr_licenciatura siglaCurricularCourse =
				(Curr_licenciatura) iter.next();
			if (siglaCurricularCourse.getObjectivos() != null
				&& siglaCurricularCourse.getObjectivos().length()
					>= objectivosGerais.length()) {
				objectivosGerais = siglaCurricularCourse.getObjectivos();
			}
			if (siglaCurricularCourse.getObjectivos_op() != null
				&& siglaCurricularCourse.getObjectivos_op().length()
					>= objectivosOP.length()) {
				objectivosOP = siglaCurricularCourse.getObjectivos_op();
			}
			if (siglaCurricularCourse.getProgr_res() != null
				&& siglaCurricularCourse.getProgr_res().length()
					>= programa.length()) {
				programa = siglaCurricularCourse.getProgr_res();
			}

		}
		if (curriculum.getGeneralObjectives().length()
			< objectivosGerais.length()) {
			curriculum.setGeneralObjectives(objectivosGerais);
		}
		if (curriculum.getOperacionalObjectives().length()
			< objectivosOP.length()) {
			curriculum.setOperacionalObjectives(objectivosOP);
		}
		if (curriculum.getProgram().length() < programa.length()) {
			curriculum.setProgram(programa);
		}

		return curriculum;
	}

	/**
	 * @param site
	 * @param siglaCurricularCourses
	 * @return
	 */
	private ISite updateSite(ISite site, List siglaCurricularCourses) {
		String alternativeSite = "";

		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Curr_licenciatura siglaCurricularCourse =
				(Curr_licenciatura) iter.next();
			if (siglaCurricularCourse.getEnder_web() != null
				&& siglaCurricularCourse.getEnder_web().length()
					> alternativeSite.length()) {
				alternativeSite = siglaCurricularCourse.getEnder_web();
			}
		}
		if (site.getAlternativeSite() == null
			|| site.getAlternativeSite().length() <= alternativeSite.length()) {
			site.setAlternativeSite(alternativeSite);
		}

		return site;
	}

	private List fenixToSiglaCurricularCourses(
		List fenixCurricularCourses,
		IDisciplinaExecucao executionCourse) {
		List siglaCurricularCourses = new ArrayList();
		PersistenceBroker broker = null;

		Query query = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Iterator iter = fenixCurricularCourses.iterator();
			while (iter.hasNext()) {
				ICurricularCourse fenixCurricularCourse =
					(ICurricularCourse) iter.next();

				List scopes = fenixCurricularCourse.getScopes();
				Iterator iter1 = scopes.iterator();
				while (iter.hasNext()) {
					ICurricularCourseScope scope =
						(ICurricularCourseScope) iter.next();
					Criteria crit = new Criteria();
					crit.addEqualTo(
						"nome_disc",
						fenixCurricularCourse.getName());
					crit.addEqualTo(
						"codigo_disc",
						fenixCurricularCourse.getCode());
					crit.addEqualTo(
						"ano_lectivo",
						fenixYearToSiglaYear(
							executionCourse
								.getExecutionPeriod()
								.getExecutionYear()
								.getYear()));
					crit.addEqualTo(
						"semestre",
						executionCourse.getExecutionPeriod().getSemester());
					crit.addEqualTo(
						"codigo_lic",
						fenixCurricularCourse
							.getDegreeCurricularPlan()
							.getDegree()
							.getIdInternal());
					crit.addEqualTo(
						"ano_curricular",
						scope
							.getCurricularSemester()
							.getCurricularYear()
							.getYear());
					query = new QueryByCriteria(Curr_lic_ingles.class, crit);
					Collection curricularCourses =
						(Collection) broker.getCollectionByQuery(query);
					siglaCurricularCourses.addAll(curricularCourses);
				}

			}

			broker.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return siglaCurricularCourses;
	}

	private List loadFenixExecutionCourses() {
		List fenixExecutionCourses = null;
		PersistenceBroker broker = null;

		Query query = new QueryByCriteria(DisciplinaExecucao.class, null);
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();

			// ask the broker to retrieve the Extent collection
			fenixExecutionCourses = (List) broker.getCollectionByQuery(query);
			// now iterate over the result to print each product

			broker.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return fenixExecutionCourses;
	}

	private List loadCurrLicPt() {
		List currLicPtList = null;
		PersistenceBroker broker = null;

		Query query = new QueryByCriteria(Curr_licenciatura.class, null);
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();

			// ask the broker to retrieve the Extent collection
			currLicPtList = (List) broker.getCollectionByQuery(query);
			// now iterate over the result to print each product
			broker.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return currLicPtList;
	}

	private List loadCurrLicEng() {
		List currLicEngList = null;
		PersistenceBroker broker = null;

		Query query = new QueryByCriteria(Curr_lic_ingles.class, null);
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();

			// ask the broker to retrieve the Extent collection
			currLicEngList = (List) broker.getCollectionByQuery(query);
			// now iterate over the result to print each product
			broker.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return currLicEngList;
	}
	/**
		 * @param executionCourse
		 * @param siglaCurricularCourses
		 * @return
		 */
	private void updateBibliography(
		IDisciplinaExecucao executionCourse,
		List siglaCurricularCourses)
		throws ExcepcaoPersistencia {
		List bibliography = new ArrayList();

		String bib_princ_1 = "";

		String bib_princ_2 = "";

		String bib_princ_3 = "";

		String bib_sec_1 = "";

		String bib_sec_2 = "";

		String bib_sec_3 = "";

		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Curr_licenciatura siglaCurricularCourse =
				(Curr_licenciatura) iter.next();
			if (siglaCurricularCourse.getBib_princ_1() != null
				&& siglaCurricularCourse.getBib_princ_1().length()
					> bib_princ_1.length()) {
				bib_princ_1 = siglaCurricularCourse.getBib_princ_1();
			}
			if (siglaCurricularCourse.getBib_princ_2() != null
				&& siglaCurricularCourse.getBib_princ_2().length()
					> bib_princ_2.length()) {
				bib_princ_2 = siglaCurricularCourse.getBib_princ_1();
			}
			if (siglaCurricularCourse.getBib_princ_3() != null
				&& siglaCurricularCourse.getBib_princ_3().length()
					> bib_princ_3.length()) {
				bib_princ_3 = siglaCurricularCourse.getBib_princ_3();
			}
			if (siglaCurricularCourse.getBib_sec_1() != null
				&& siglaCurricularCourse.getBib_sec_1().length()
					> bib_sec_1.length()) {
				bib_sec_1 = siglaCurricularCourse.getBib_sec_1();
			}
			if (siglaCurricularCourse.getBib_sec_2() != null
				&& siglaCurricularCourse.getBib_sec_2().length()
					> bib_sec_2.length()) {
				bib_sec_2 = siglaCurricularCourse.getBib_sec_2();
			}
			if (siglaCurricularCourse.getBib_sec_2() != null
				&& siglaCurricularCourse.getBib_sec_2().length()
					> bib_sec_2.length()) {
				bib_sec_2 = siglaCurricularCourse.getBib_sec_2();
			}
		}
		if (bib_princ_1.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_princ_1,
					null,
					null,
					null,
					new Boolean(false)));
		}
		if (bib_princ_2.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_princ_2,
					null,
					null,
					null,
					new Boolean(false)));
		}
		if (bib_princ_3.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_princ_3,
					null,
					null,
					null,
					new Boolean(false)));
		}
		if (bib_sec_1.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_1,
					null,
					null,
					null,
					new Boolean(true)));
		}
		if (bib_sec_2.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_2,
					null,
					null,
					null,
					new Boolean(true)));
		}
		if (bib_sec_3.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_3,
					null,
					null,
					null,
					new Boolean(true)));
		}

		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentBibliographicReference persistentBibliographicReference =
			sp.getIPersistentBibliographicReference();
		Iterator iter2 = bibliography.iterator();
		while (iter2.hasNext()) {
			IBibliographicReference reference =
				(IBibliographicReference) iter.next();
			persistentBibliographicReference.lockWrite(reference);
		}

	}

}
