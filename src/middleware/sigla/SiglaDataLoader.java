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
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.BibliographicReference;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.DisciplinaExecucao;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author João Mota
 *
 */
public class SiglaDataLoader {

	public static void main(String[] args) {
		SiglaDataLoader loader = new SiglaDataLoader();
		PersistenceBroker broker =
			PersistenceBrokerFactory.defaultPersistenceBroker();

		updateCurriculum(loader, broker); 
		updateBibliography(loader, broker);

		broker.close();

	}

	private static void updateBibliography(SiglaDataLoader loader, PersistenceBroker broker) {
		System.out.println("a carregar disciplinas execução do fénix");
		List fenixExecutionCourses = loader.loadFenixExecutionCourses(broker);
		Iterator iter = fenixExecutionCourses.iterator();
		System.out.println("iniciando actualização das bibliografias");	
		while (iter.hasNext()) {
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) iter.next();
			
			List siglaCurricularCourses =
				loader.fenixToSiglaCurricularCourses(
					executionCourse.getAssociatedCurricularCourses(),
					executionCourse,
					broker);
			try {
				loader.updateBibliography(executionCourse,siglaCurricularCourses,broker);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}
			System.out.println("update da bibliografia concluído");
		}
		System.out.println("update das bibliografias concluído");
	}

	private static void updateCurriculum(
		SiglaDataLoader loader,
		PersistenceBroker broker) {
		System.out.println("A carregar disciplinas curriculares do fenix");
		//todo: usar uma query melhor para apanhar as disciplinas
		List fenixCurricularCourses = loader.loadFenixCurricularCourses(broker);
		System.out.println(
			"carregamento efectuado! nº de disciplinas->"
				+ fenixCurricularCourses.size());
		Criteria notInCrit = new Criteria();
		notInCrit.addEqualTo("ano_lectivo", "2002");
		notInCrit.addEqualTo("semestre", "1");
		Iterator fenixIter = fenixCurricularCourses.iterator();
		while (fenixIter.hasNext()) {
			ICurricularCourse fenixCurricularCourse =
				(ICurricularCourse) fenixIter.next();
			notInCrit.addNotEqualTo("codigo_disc",codeFixerFenixToSigla(fenixCurricularCourse.getCode()));
			
			Criteria crit = new Criteria();
			crit.addEqualTo(
				"codigo_disc",
				codeFixerFenixToSigla(fenixCurricularCourse.getCode()));
			//			//note: change the year for future migrations
						crit.addEqualTo("ano_lectivo", "2002");
			//			crit.addEqualTo("semestre", "1");
			//			crit.addEqualTo(
			//				"codigo_lic",
			//				fenixCurricularCourse
			//					.getDegreeCurricularPlan()
			//					.getDegree()
			//					.getIdInternal());

			Query query = new QueryByCriteria(Curr_licenciatura.class, crit);
			Curr_licenciatura curr_licenciatura =
				(Curr_licenciatura) broker.getObjectByQuery(query);
			Criteria critEn = new Criteria();
			critEn.addEqualTo(
				"codigo_disc",
				codeFixerFenixToSigla(fenixCurricularCourse.getCode()));
			//note: change the year for future migrations
						critEn.addEqualTo("ano_lectivo", "2002");
			//			critEn.addEqualTo("semestre", "1");
			//			critEn.addEqualTo(
			//				"codigo_lic",
			//				fenixCurricularCourse
			//					.getDegreeCurricularPlan()
			//					.getDegree()
			//					.getIdInternal());

			Query queryEn = new QueryByCriteria(Curr_lic_ingles.class, critEn);
			Curr_lic_ingles curr_lic_ingles =
				(Curr_lic_ingles) broker.getObjectByQuery(queryEn);
			if (curr_licenciatura == null) {
				System.out.println(
					"Não encontrei nada no sigla para a disciplina curricular ->"
						+ fenixCurricularCourse.getCode()
						+ "-"
						+ fenixCurricularCourse.getName());
			} else {
				Criteria crit1 = new Criteria();
				crit1.addEqualTo(
					"keyCurricularCourse",
					fenixCurricularCourse.getIdInternal());
				Query query1 = new QueryByCriteria(Curriculum.class, crit1);
				ICurriculum curriculum =
					(ICurriculum) broker.getObjectByQuery(query1);
				if (curriculum == null) {
					insertCurriculum(
						broker,
						fenixCurricularCourse,
						curr_licenciatura,
						curr_lic_ingles);
				} else {
					updateCurriculum(
						broker,
						fenixCurricularCourse,
						curr_licenciatura,
						curr_lic_ingles,
						curriculum);
				}

			}
			

		}
		
		
		System.out.println(
			"update de todas as  disciplinas curriculares efectuado");
			
		Query notInQuery = new QueryByCriteria(Curr_licenciatura.class,notInCrit);
		Collection siglaCurricularCoursesNotInFenix = broker.getCollectionByQuery(notInQuery);	
		Iterator notInIter = siglaCurricularCoursesNotInFenix.iterator();
		System.out.println("Lista de disciplinas curriculares que estão no Sigla e não no Fénix:");
		while (notInIter.hasNext()) {
			Curr_licenciatura curr_licenciatura = (Curr_licenciatura) notInIter.next();
			System.out.println(curr_licenciatura.getCodigo_disc()+"-"+curr_licenciatura.getNome_disc());
			System.out.println("Licenciatura:" +curr_licenciatura.getCodigo_lic());			
			System.out.println("ano curricular:" +curr_licenciatura.getAno_curricular());
			System.out.println("ano lectivo:" +curr_licenciatura.getAno_lectivo()+" - semestre:"+curr_licenciatura.getSemestre());
			
		} 
			
	}

	/**
	 * @param broker
	 * @param fenixCurricularCourse
	 * @param curr_licenciatura
	 * @param curriculum
	 */
	private static void updateCurriculum(
		PersistenceBroker broker,
		ICurricularCourse fenixCurricularCourse,
		Curr_licenciatura curr_licenciatura,
		Curr_lic_ingles curr_lic_ingles,
		ICurriculum curriculum) {
		curriculum.setEvaluationElements(curr_licenciatura.getCrit_av());
		curriculum.setGeneralObjectives(curr_licenciatura.getObjectivos());
		curriculum.setOperacionalObjectives(
			curr_licenciatura.getObjectivos_op());
		curriculum.setProgram(curr_licenciatura.getProgr_res());
		if (curr_lic_ingles != null) {
			curriculum.setEvaluationElementsEn(curr_lic_ingles.getCrit_av());
			curriculum.setGeneralObjectivesEn(curr_lic_ingles.getObjectivos());
			curriculum.setOperacionalObjectivesEn(
				curr_lic_ingles.getObjectivos_op());
			curriculum.setProgramEn(curr_lic_ingles.getProgr_res());

		}
		try {
			broker.store(curriculum);
		} catch (PersistenceBrokerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param broker
	 * @param fenixCurricularCourse
	 * @param curr_licenciatura
	 */
	private static void insertCurriculum(
		PersistenceBroker broker,
		ICurricularCourse fenixCurricularCourse,
		Curr_licenciatura curr_licenciatura,
		Curr_lic_ingles curr_lic_ingles) {
		ICurriculum curriculum = new Curriculum();
		curriculum.setCurricularCourse(fenixCurricularCourse);
		curriculum.setEvaluationElements(curr_licenciatura.getCrit_av());
		curriculum.setGeneralObjectives(curr_licenciatura.getObjectivos());
		curriculum.setOperacionalObjectives(
			curr_licenciatura.getObjectivos_op());
		curriculum.setProgram(curr_licenciatura.getProgr_res());
		if (curr_lic_ingles != null) {
			curriculum.setEvaluationElementsEn(curr_lic_ingles.getCrit_av());
			curriculum.setGeneralObjectivesEn(curr_lic_ingles.getObjectivos());
			curriculum.setOperacionalObjectivesEn(
				curr_lic_ingles.getObjectivos_op());
			curriculum.setProgramEn(curr_lic_ingles.getProgr_res());

		}
		try {
			broker.store(curriculum);
		} catch (PersistenceBrokerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourses
	 */
	private void updateResponsibleTeachers(
		IDisciplinaExecucao executionCourse,
		List siglaCurricularCourses,
		PersistenceBroker broker) {
		List teachers = new ArrayList();
		Iterator iter = siglaCurricularCourses.iterator();
		Query query = null;

		List numsMec = new ArrayList();

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
		System.out.println(
			"número de docentes responsáveis->" + numsMec.size());
		Iterator iter2 = numsMec.iterator();
		while (iter2.hasNext()) {
			Integer numMec = (Integer) iter2.next();
			Criteria crit = new Criteria();

			crit.addEqualTo("person.username", "D" + numMec);
			query = new QueryByCriteria(Teacher.class, crit);
			ITeacher teacher = (ITeacher) broker.getObjectByQuery(query);
			if (teacher == null) {
				System.out.println(
					"não encontrei o docente com número mecanográfico:"
						+ numMec);
			} else {
				IProfessorship professorship =
					new Professorship(teacher, executionCourse);
				IResponsibleFor responsibleFor =
					new ResponsibleFor(teacher, executionCourse);
				System.out.println(
					"a escrever professorship"
						+ professorship.getExecutionCourse().getNome()
						+ "-"
						+ professorship.getTeacher().getPerson().getUsername());
				try {
					broker.store(professorship);

					broker.store(responsibleFor);
				} catch (PersistenceBrokerException e1) {
					System.out.println(
						"a  professorship já existe: "
							+ professorship.getExecutionCourse().getNome()
							+ "-"
							+ professorship
								.getTeacher()
								.getPerson()
								.getUsername());
				}

			}
		}

	}

	private String fenixYearToSiglaYear(String fenixYear) {
		String[] res = fenixYear.split("/");
		return res[0];
	}

	private List fenixToSiglaCurricularCoursesEng(
		List fenixCurricularCourses,
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker) {
		List siglaCurricularCourses = new ArrayList();

		Query query = null;
		try {

			Iterator iter = fenixCurricularCourses.iterator();
			while (iter.hasNext()) {
				ICurricularCourse fenixCurricularCourse =
					(ICurricularCourse) iter.next();

				List scopes = fenixCurricularCourse.getScopes();
				Iterator iter1 = scopes.iterator();
				while (iter1.hasNext()) {
					ICurricularCourseScope scope =
						(ICurricularCourseScope) iter1.next();
					Criteria crit = new Criteria();

					crit.addEqualTo(
						"codigo_disc",
						codeFixerFenixToSigla(fenixCurricularCourse.getCode()));
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

					query = new QueryByCriteria(Curr_lic_ingles.class, crit);
					Collection curricularCourses =
						(Collection) broker.getCollectionByQuery(query);
					siglaCurricularCourses.addAll(curricularCourses);
				}

			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println(
			"número de curriculares do fenix->"
				+ fenixCurricularCourses.size());
		System.out.println(
			"número de curriculares (em inglês) do sigla->"
				+ siglaCurricularCourses.size());
		return siglaCurricularCourses;
	}

	//	private void updateFenixCourses(
	//		IDisciplinaExecucao executionCourse,
	//		List siglaCurricularCourses,
	//		List siglaCurricularCoursesEng,
	//		PersistenceBroker broker)
	//		throws ExcepcaoPersistencia {
	//		ISite site = null;
	//		ICurriculum curriculum = null;
	//		IEvaluationMethod evaluation = null;
	//
	//		try {
	//
	//			Criteria crit = new Criteria();
	//			crit.addEqualTo(
	//				"executionCourse.idInternal",
	//				executionCourse.getIdInternal());
	//			Query query = new QueryByCriteria(Site.class, crit);
	//			site = (ISite) broker.getObjectByQuery(query);
	//			if (site == null) {
	//				System.out.println("não encontrei o site");
	//			}
	//			query = new QueryByCriteria(Curriculum.class, crit);
	//			curriculum = (ICurriculum) broker.getObjectByQuery(query);
	//			if (curriculum == null) {
	//				curriculum = new Curriculum();
	//				//FIXME: curriculum now has curricularCourse
	//				//curriculum.setExecutionCourse(executionCourse);
	//			}
	//			query = new QueryByCriteria(Evaluation.class, crit);
	//
	//			evaluation = (IEvaluationMethod) broker.getObjectByQuery(query);
	//			site = updateSite(site, siglaCurricularCourses);
	//
	//			curriculum =
	//				updateCurriculum(curriculum, siglaCurricularCourses, broker);
	//			curriculum =
	//				updateCurriculumEng(
	//					curriculum,
	//					siglaCurricularCoursesEng,
	//					broker);
	//			if (evaluation == null) {
	//				//evaluation = new EvaluationMethod(executionCourse);
	//			}
	//			evaluation =
	//				updateEvaluation(evaluation, siglaCurricularCourses, broker);
	//
	//			evaluation =
	//				updateEvaluationEng(
	//					evaluation,
	//					siglaCurricularCoursesEng,
	//					broker);
	//			updateBibliography(executionCourse, siglaCurricularCourses, broker);
	//			updateResponsibleTeachers(
	//				executionCourse,
	//				siglaCurricularCourses,
	//				broker);
	//			System.out.println("a guardar evaluation");
	//			broker.store(evaluation);
	//			System.out.println("a guardar curriculum");
	//			broker.store(curriculum);
	//			System.out.println("a guardar site");
	//			broker.store(site);
	//
	//		} catch (ExcepcaoPersistencia e) {
	//			throw e;
	//		}
	//
	//	}

	//	/**
	//	 * @param evaluation
	//	 * @param siglaCurricularCoursesEng
	//	 * @deprecated
	//	 * @return
	//	 */
	//	private IEvaluationMethod updateEvaluationEng(
	//		IEvaluationMethod evaluation,
	//		List siglaCurricularCoursesEng,
	//		PersistenceBroker broker) {
	//
	//		String crit_av = "";
	//
	//		Iterator iter = siglaCurricularCoursesEng.iterator();
	//		while (iter.hasNext()) {
	//			Curr_lic_ingles siglaCurricularCourse =
	//				(Curr_lic_ingles) iter.next();
	//			if (siglaCurricularCourse.getCrit_av() != null
	//				&& siglaCurricularCourse.getCrit_av().length()
	//					> crit_av.length()) {
	//				crit_av = siglaCurricularCourse.getCrit_av();
	//			}
	//		}
	//		if (evaluation.getEvaluationElementsEn() == null
	//			|| evaluation.getEvaluationElementsEn().length()
	//				<= crit_av.length()) {
	//			evaluation.setEvaluationElementsEn(crit_av);
	//		}
	//		return evaluation;
	//	}

	/**
	 * @param curriculum
	 * @param siglaCurricularCoursesEng
	 * @return
	 */
	private ICurriculum updateCurriculumEng(
		ICurriculum curriculum,
		List siglaCurricularCoursesEng,
		PersistenceBroker broker) {
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
		if (curriculum.getGeneralObjectivesEn() == null
			|| curriculum.getGeneralObjectivesEn().length()
				< objectivosGerais.length()) {
			curriculum.setGeneralObjectivesEn(objectivosGerais);
		}
		if (curriculum.getOperacionalObjectivesEn() == null
			|| curriculum.getOperacionalObjectivesEn().length()
				< objectivosOP.length()) {
			curriculum.setOperacionalObjectivesEn(objectivosOP);
		}
		if (curriculum.getProgramEn() == null
			|| curriculum.getProgramEn().length() < programa.length()) {
			curriculum.setProgramEn(programa);
		}

		return curriculum;

	}

	//	/**
	//	 * 
	//	 * @param curriculum
	//	 * @param siglaCurricularCourses
	//	 * @param broker
	//	 * @deprecated
	//	 * @return
	//	 */
	//	private ICurriculum updateCurriculum(
	//		ICurriculum curriculum,
	//		List siglaCurricularCourses,
	//		PersistenceBroker broker) {
	//		String objectivosOP = "";
	//		String objectivosGerais = "";
	//		String programa = "";
	//
	//		Iterator iter = siglaCurricularCourses.iterator();
	//		while (iter.hasNext()) {
	//			Curr_licenciatura siglaCurricularCourse =
	//				(Curr_licenciatura) iter.next();
	//			if (siglaCurricularCourse.getObjectivos() != null
	//				&& siglaCurricularCourse.getObjectivos().length()
	//					>= objectivosGerais.length()) {
	//				objectivosGerais = siglaCurricularCourse.getObjectivos();
	//			}
	//			if (siglaCurricularCourse.getObjectivos_op() != null
	//				&& siglaCurricularCourse.getObjectivos_op().length()
	//					>= objectivosOP.length()) {
	//				objectivosOP = siglaCurricularCourse.getObjectivos_op();
	//			}
	//			if (siglaCurricularCourse.getProgr_res() != null
	//				&& siglaCurricularCourse.getProgr_res().length()
	//					>= programa.length()) {
	//				programa = siglaCurricularCourse.getProgr_res();
	//			}
	//
	//		}
	//		if (curriculum.getGeneralObjectives() == null
	//			|| curriculum.getGeneralObjectives().length()
	//				< objectivosGerais.length()) {
	//			curriculum.setGeneralObjectives(objectivosGerais);
	//		}
	//		if (curriculum.getOperacionalObjectives() == null
	//			|| curriculum.getOperacionalObjectives().length()
	//				< objectivosOP.length()) {
	//			curriculum.setOperacionalObjectives(objectivosOP);
	//		}
	//		if (curriculum.getProgram() == null
	//			|| curriculum.getProgram().length() < programa.length()) {
	//			curriculum.setProgram(programa);
	//		}
	//
	//		return curriculum;
	//	}

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
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker) {
		List siglaCurricularCourses = new ArrayList();

		Query query = null;

		Iterator iter = fenixCurricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse fenixCurricularCourse =
				(ICurricularCourse) iter.next();

			List scopes = fenixCurricularCourse.getScopes();
			if (scopes.size() == 0) {
				System.out.println(
					"scopes vazios! o problema é dos dados do fenix");
			}
			Iterator iter1 = scopes.iterator();
			while (iter1.hasNext()) {
				ICurricularCourseScope scope =
					(ICurricularCourseScope) iter1.next();
				Criteria crit = new Criteria();

				crit.addEqualTo(
					"codigo_disc",
					codeFixerFenixToSigla(fenixCurricularCourse.getCode()));
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

				query = new QueryByCriteria(Curr_licenciatura.class, crit);
				Collection curricularCourses =
					(Collection) broker.getCollectionByQuery(query);
				if (curricularCourses.size() == 0) {
					System.out.println(
						"para este scope não encontrei curriculares! a query foi:");
					System.out.println(
						"nome_disc=" + fenixCurricularCourse.getName());
					System.out.println(
						"codigo_disc=" + fenixCurricularCourse.getCode());
					System.out.println(
						"ano_lectivo="
							+ fenixYearToSiglaYear(
								executionCourse
									.getExecutionPeriod()
									.getExecutionYear()
									.getYear()));
					System.out.println(
						"semestre="
							+ executionCourse.getExecutionPeriod().getSemester());
					System.out.println(
						"codigo_lic="
							+ fenixCurricularCourse
								.getDegreeCurricularPlan()
								.getDegree()
								.getIdInternal());
					
				}
				siglaCurricularCourses.addAll(curricularCourses);
			}

		}

		if (siglaCurricularCourses.size() == 0) {
			System.out.println("#####");
			System.out.println(
				"Não encontrei curriculares para: " + executionCourse);

		}
		System.out.println(
			"número de curriculares do fenix->"
				+ fenixCurricularCourses.size());
		System.out.println(
			"número de curriculares do sigla->"
				+ siglaCurricularCourses.size());
		return siglaCurricularCourses;
	}

	/**
	 * @param string
	 * @return
	 */
	private static Object codeFixerFenixToSigla(String string) {
		String result = null;
		if (string.equals("2")) {
			result = "02";
		} else if (string.equals("1")) {
			result = "01";
		} else {
			result = string;
		}
		return result;
	}

	private List loadFenixExecutionCourses(PersistenceBroker broker) {
		List fenixExecutionCourses = null;
		
		Criteria crit = new Criteria();
		crit.addEqualTo("executionPeriod.name","1 Semestre");
		crit.addEqualTo("executionPeriod.executionYear.year","2003/2004");
		Query query = new QueryByCriteria(DisciplinaExecucao.class, crit);
		try {
			

			// ask the broker to retrieve the Extent collection
			fenixExecutionCourses = (List) broker.getCollectionByQuery(query);

			
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return fenixExecutionCourses;
	}

	private List loadFenixCurricularCourses(PersistenceBroker broker) {
		List fenixCurricularCourses = null;
		
		
		Query query = new QueryByCriteria(CurricularCourse.class, null);
		try {
		

			// ask the broker to retrieve the Extent collection
			fenixCurricularCourses = (List) broker.getCollectionByQuery(query);

			
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return fenixCurricularCourses;
	}

	private List loadCurrLicPt() {
		List currLicPtList = null;
		PersistenceBroker broker = null;

		Query query = new QueryByCriteria(Curr_licenciatura.class, null);
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();

			// ask the broker to retrieve the Extent collection
			currLicPtList = (List) broker.getCollectionByQuery(query);

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
		List siglaCurricularCourses,
		PersistenceBroker broker)
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
					"",
					"",
					"",
					new Boolean(false)));
		}
		if (bib_princ_2.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_princ_2,
					"",
					"",
					"",
					new Boolean(false)));
		}
		if (bib_princ_3.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_princ_3,
					"",
					"",
					"",
					new Boolean(false)));
		}
		if (bib_sec_1.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_1,
					"",
					"",
					"",
					new Boolean(true)));
		}
		if (bib_sec_2.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_2,
					"",
					"",
					"",
					new Boolean(true)));
		}
		if (bib_sec_3.length() > 0) {
			bibliography.add(
				new BibliographicReference(
					executionCourse,
					bib_sec_3,
					"",
					"",
					"",
					new Boolean(true)));
		}

		Iterator iter2 = bibliography.iterator();
		while (iter2.hasNext()) {
			IBibliographicReference reference =
				(IBibliographicReference) iter2.next();
			try {
				broker.store(reference);
			} catch (PersistenceBrokerException e) {
				System.out.println("a referência bibliográfica já existe");
			}
		}

	}

}
