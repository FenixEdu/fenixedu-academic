/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.posgrad;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.Employee;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateInscricoes2FenixEnrolments {

	PersistenceBroker broker = null;

	public MigrateInscricoes2FenixEnrolments() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}

	public static void main(String args[]) throws Exception {
		MigrateInscricoes2FenixEnrolments migrateAreasCientificas2FenixBrach = new MigrateInscricoes2FenixEnrolments();

		//		migrateAreasCientificas2FenixBrach.broker.beginTransaction();
		//		migrateAreasCientificas2FenixBrach.broker.clearCache();
		migrateAreasCientificas2FenixBrach.migratePosgradInscricoes2FenixEnrolment();

		//		migrateAreasCientificas2FenixBrach.broker.commitTransaction();
	}

	private void migratePosgradInscricoes2FenixEnrolment() throws Exception {
		Posgrad_disc_area_aluno inscricao = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
//		IBranch branch = null;
		int inscricoesNotWritten = 0;
		int enrolmentsWritten = 0;
		int evaluationsWritten = 0;

		try {
			System.out.print("Reading PosGrad Inscricoes ...");
			List inscricoes = getInscricoes(broker);
			System.out.println("  Done !");

			System.out.println("Migrating " + inscricoes.size() + " Inscricoes to Fenix Enrolment ...");
			Iterator iterator = inscricoes.iterator();
			while (iterator.hasNext()) {
				broker.beginTransaction();

				broker.clearCache();
				inscricao = (Posgrad_disc_area_aluno) iterator.next();

				// Invalid Enrolments
				if ((inscricao.getCodigoareadisciplina() == 29)
					|| (inscricao.getCodigoareadisciplina() == 58)
					|| (inscricao.getCodigoareadisciplina() == 80)
					|| (inscricao.getCodigoareadisciplina() == 85)
					|| (inscricao.getCodigoareadisciplina() == 118)
					|| (inscricao.getCodigoareadisciplina() == 122)
					|| (inscricao.getCodigoareadisciplina() == 127)
					|| (inscricao.getCodigoareadisciplina() == 893)) {
					inscricoesNotWritten++;
					broker.commitTransaction();
					continue;
				}

				// Read Disciplina Area

				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(inscricao.getCodigoareadisciplina())));
				query = new QueryByCriteria(Posgrad_disc_area.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Disciplina Area " + result.size());
				}

				Posgrad_disc_area discArea = (Posgrad_disc_area) result.get(0);

				// Read Disciplina

				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(discArea.getCodigodisciplina())));
				query = new QueryByCriteria(Posgrad_disciplina.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Disciplina ");
				}

				Posgrad_disciplina posgrad_disciplina = (Posgrad_disciplina) result.get(0);

				// Read Curricular Course

				criteria = new Criteria();
				criteria.addEqualTo("id_internal", posgrad_disciplina.getCodigoCurricularCourse());
				query = new QueryByCriteria(CurricularCourse.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Curricular Course (" + posgrad_disciplina.getNome() + ") [" + result.size() + "]");
				}

				ICurricularCourse curricularCourse = (ICurricularCourse) result.get(0);

				// Read Area Cientifica

				criteria = new Criteria();
				criteria.addEqualTo("codigoInterno", new Integer(String.valueOf(discArea.getCodigoareacientifica())));
				query = new QueryByCriteria(Posgrad_area_cientifica.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Area Cientifica  [" + result.size() + "]");
				}

//				Posgrad_area_cientifica posgrad_area_cientifica = (Posgrad_area_cientifica) result.get(0);

//				if ((posgrad_area_cientifica.getCodigointerno() == 37)
//					|| (posgrad_area_cientifica.getCodigointerno() == 17)
//					|| (posgrad_area_cientifica.getCodigointerno() == 86)) {
//					branch = getEmptyBranch(posgrad_area_cientifica, broker);
//				} else {
//					// Read Branch
//
//					criteria = new Criteria();
//					criteria.addEqualTo("id_internal", new Integer(String.valueOf(posgrad_area_cientifica.getCodigoInternoRamo())));
//					query = new QueryByCriteria(Branch.class, criteria);
//
//					result = (List) broker.getCollectionByQuery(query);
//
//					if (result.size() != 1) {
//						throw new Exception(
//							"Error Reading Branch " + posgrad_area_cientifica.getCodigoInternoRamo() + " [" + result.size() + "]");
//					}
//
//					branch = (IBranch) result.get(0);
//				}
//
//				// Read Curricular Year
//
//				criteria = new Criteria();
//				criteria.addEqualTo("year", new Integer(1));
//				query = new QueryByCriteria(CurricularYear.class, criteria);
//
//				result = (List) broker.getCollectionByQuery(query);
//
//				if (result.size() != 1) {
//					throw new Exception("Error Reading Curricular Year [" + result.size() + "]");
//				}
//
//				CurricularYear curricularYear = (CurricularYear) result.get(0);
//
//				// Read Curricular Semester
//
//				criteria = new Criteria();
//				criteria.addEqualTo("semester", new Integer(1));
//				criteria.addEqualTo("curricularYearKey", curricularYear.getIdInternal());
//				query = new QueryByCriteria(CurricularSemester.class, criteria);
//
//				result = (List) broker.getCollectionByQuery(query);
//
//				if (result.size() != 1) {
//					throw new Exception("Error Reading Curricular Course Scope [" + result.size() + "]");
//				}
//
//				CurricularSemester curricularSemester = (CurricularSemester) result.get(0);
//
//				// Read Curricular Course Scope
//
//				criteria = new Criteria();
//				criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
//				criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
//				criteria.addEqualTo("branchKey", branch.getIdInternal());
//				query = new QueryByCriteria(CurricularCourseScope.class, criteria);
//
//				result = (List) broker.getCollectionByQuery(query);
//
//				if (result.size() != 1) {
//					throw new Exception("Error Reading Curricular Course Scope [" + result.size() + "]");
//				}
//
//				CurricularCourseScope curricularCourseScope = (CurricularCourseScope) result.get(0);

				// Read Execution Year
				criteria = new Criteria();
				criteria.addEqualTo("year", "2002/2003");
				query = new QueryByCriteria(ExecutionYear.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Execution Year [" + result.size() + "]");
				}

				ExecutionYear executionYear = (ExecutionYear) result.get(0);

				// Read Execution Period

				criteria = new Criteria();
				criteria.addEqualTo("semester", new Integer(1));
				criteria.addEqualTo("keyExecutionYear", executionYear.getIdInternal());
				query = new QueryByCriteria(ExecutionPeriod.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Execution Period [" + result.size() + "]");
				}

				ExecutionPeriod executionPeriod = (ExecutionPeriod) result.get(0);

				// Read Aluno Mestrado 
				criteria = new Criteria();
				criteria.addEqualTo("codigoInterno", new Integer(String.valueOf(inscricao.getCodigoaluno())));
				query = new QueryByCriteria(Posgrad_aluno_mestrado.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Aluno Mestrado [" + result.size() + "]");
				}

				Posgrad_aluno_mestrado posgrad_aluno_mestrado = (Posgrad_aluno_mestrado) result.get(0);

				// Read Student 
				criteria = new Criteria();

				if (posgrad_aluno_mestrado.getNumero() == 253) {
					posgrad_aluno_mestrado.setNumero(245);
				}
				criteria.addEqualTo("number", new Integer(String.valueOf(posgrad_aluno_mestrado.getNumero())));
				criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.MESTRADO));
				query = new QueryByCriteria(Student.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Student [" + result.size() + "]");
				}

				Student student = (Student) result.get(0);

				// Read Student Curricular Plan
				criteria = new Criteria();
				criteria.addEqualTo("studentKey", student.getIdInternal());
				criteria.addEqualTo("degreeCurricularPlanKey", curricularCourse.getDegreeCurricularPlan().getIdInternal());
				query = new QueryByCriteria(StudentCurricularPlan.class, criteria);

				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					System.out.println(student.getIdInternal());
					System.out.println(curricularCourse.getDegreeCurricularPlan().getIdInternal());
					throw new Exception("Error Reading Student Curricular Plan [" + result.size() + "]");
				}

				StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) result.get(0);

				// Create Enrolment
				IEnrolment enrolment = new Enrolment();
//				enrolment.setCurricularCourseScope(curricularCourseScope);
				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				enrolment.setExecutionPeriod(executionPeriod);
				boolean evaluationNeeded = false;

				if (inscricao.getNota() != null) {
					if (inscricao.getEquivalencia().equalsIgnoreCase("equiv")) {
						enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
					} else {
						enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					}

					if ((inscricao.getNota().equalsIgnoreCase("re")) || (inscricao.getNota().equalsIgnoreCase("rep"))) {
						enrolment.setEnrolmentState(EnrolmentState.NOT_APROVED);

					} else {
						enrolment.setEnrolmentState(EnrolmentState.APROVED);
					}
					evaluationNeeded = true;
				} else {
					enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				}

				// Check of enrolment already Exists
				IEnrolment enrolment2Write = getEnrolment2Write(enrolment, broker);

				broker.store(enrolment2Write);
				enrolmentsWritten++;

				broker.commitTransaction();

				broker.beginTransaction();

				broker.clearCache();

				IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
				enrolmentEvaluation.setEnrolment(enrolment2Write);
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());

				enrolmentEvaluation.setWhen(new Timestamp(Calendar.getInstance().getTimeInMillis()));

				if (evaluationNeeded) {
					enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
					enrolmentEvaluation.setExamDate(inscricao.getDataexame());
					enrolmentEvaluation.setGradeAvailableDate(inscricao.getDatalancamento());
					if ((inscricao.getNota().equalsIgnoreCase("re")) || (inscricao.getNota().equalsIgnoreCase("rep"))) {
						enrolmentEvaluation.setGrade("REP");
					} else {
						enrolmentEvaluation.setGrade(inscricao.getNota());
					}

					//Read The Person Responsible for The Grade
					criteria = new Criteria();
					criteria.addEqualTo("employeeNumber", inscricao.getCreditos());
					query = new QueryByCriteria(Employee.class, criteria);

					result = (List) broker.getCollectionByQuery(query);

					if (result.size() != 1) {

						//	System.out.println("TEMPORARIO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!111 [" + inscricao.getCreditos() + "]");
						//	broker.commitTransaction();
						//	continue;

						throw new Exception("Error Reading Funcionario [" + inscricao.getCreditos() + "]");

					}

					Employee employee = (Employee) result.get(0);

					criteria = new Criteria();
					criteria.addEqualTo("id_internal", new Integer(String.valueOf(employee.getKeyPerson())));
					query = new QueryByCriteria(Pessoa.class, criteria);

					result = (List) broker.getCollectionByQuery(query);

					if (result.size() != 1) {
						throw new Exception("Error Reading Pessoa [" + result.size() + "]");
					}

					IPessoa person = (IPessoa) result.get(0);

					enrolmentEvaluation.setPersonResponsibleForGrade(person);

				} else {
					enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
					enrolmentEvaluation.setCheckSum(null);
					enrolmentEvaluation.setEmployee(null);
					enrolmentEvaluation.setExamDate(null);
					enrolmentEvaluation.setGrade(null);
					enrolmentEvaluation.setGradeAvailableDate(null);
					enrolmentEvaluation.setObservation(null);
					enrolmentEvaluation.setPersonResponsibleForGrade(null);

				}

				// Check if the grade Already Exists

				IEnrolmentEvaluation enrolmentEvaluation2Write = getEnrolmentEvaluation2Write(enrolmentEvaluation, broker);

				broker.store(enrolmentEvaluation2Write);
				evaluationsWritten++;

				broker.commitTransaction();

			}

			System.out.println("  Enrolments Written " + enrolmentsWritten);
			System.out.println("  Evaluations Written " + evaluationsWritten);
			System.out.println("  Inscricoes NOT Written " + inscricoesNotWritten);
			System.out.println("  Done !");

		} catch (Exception e) {
			System.out.println();
			throw new Exception("Error Migrating Inscricao " + inscricao.getCodigointerno(), e);
		}
	}

	private IEnrolment getEnrolment2Write(IEnrolment enrolment, PersistenceBroker broker) throws Exception {

		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"studentCurricularPlanKey",
			new Integer(String.valueOf(((StudentCurricularPlan) enrolment.getStudentCurricularPlan()).getIdInternal())));
		criteria.addEqualTo(
			"keyExecutionPeriod",
			new Integer(String.valueOf(((ExecutionPeriod) enrolment.getExecutionPeriod()).getIdInternal())));
		criteria.addEqualTo(
			"curricularCourseKey",
			new Integer(String.valueOf(((CurricularCourseScope) enrolment.getCurricularCourse()).getIdInternal())));
		Query query = new QueryByCriteria(Enrolment.class, criteria);

		List result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0) {
			//			System.out.println("Nao encontrei o Enrolment");
			return enrolment;
		} else if (result.size() == 1) {
			//			System.out.println("Encontrei o enrolment");
			Enrolment enrolmentFromDatabase = (Enrolment) result.get(0);
			Integer idInternal = enrolmentFromDatabase.getIdInternal();
			BeanUtils.copyProperties(enrolmentFromDatabase, enrolment);
			enrolmentFromDatabase.setIdInternal(idInternal);
			return enrolmentFromDatabase;
		}

		throw new Exception("More than one Enrolment Matching Criteria." + enrolment);
	}

	private IEnrolmentEvaluation getEnrolmentEvaluation2Write(IEnrolmentEvaluation enrolmentEvaluation, PersistenceBroker broker)
		throws Exception {

		Criteria criteria = new Criteria();

		//		System.out.println(((Enrolment) enrolmentEvaluation.getEnrolment()).getInternalID());

		criteria.addEqualTo("enrolmentEvaluationType", enrolmentEvaluation.getEnrolmentEvaluationType().getType());
		criteria.addEqualTo(
			"enrolmentKey",
			new Integer(String.valueOf(((Enrolment) enrolmentEvaluation.getEnrolment()).getIdInternal())));
		Query query = new QueryByCriteria(EnrolmentEvaluation.class, criteria);

		List result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0) {
			return enrolmentEvaluation;
		} else if (result.size() == 1) {
			EnrolmentEvaluation enrolmentEvaluationFromDatabase = (EnrolmentEvaluation) result.get(0);
			Integer idInternal = enrolmentEvaluationFromDatabase.getIdInternal();
			BeanUtils.copyProperties(enrolmentEvaluationFromDatabase, enrolmentEvaluation);
			enrolmentEvaluationFromDatabase.setIdInternal(idInternal);
			return enrolmentEvaluationFromDatabase;
		}

		throw new Exception("More than one Enrolment Evaluation Matching Criteria." + enrolmentEvaluation);
	}

	private List getInscricoes(PersistenceBroker broker) throws Exception {
		broker.beginTransaction();
		broker.clearCache();
		Criteria criteria = new Criteria();
		QueryByCriteria query = new QueryByCriteria(Posgrad_disc_area_aluno.class, criteria);

		List result = (List) broker.getCollectionByQuery(query);
		broker.commitTransaction();
		return result;
	}

//	private Branch getEmptyBranch(Posgrad_area_cientifica areaCientifica, PersistenceBroker broker) throws Exception {
//		// Read the Curso Mestrado 
//
//		Criteria criteria = new Criteria();
//		criteria.addEqualTo("codigoInterno", new Integer(String.valueOf(areaCientifica.getCodigocursomestrado())));
//		Query query = new QueryByCriteria(Posgrad_curso_mestrado.class, criteria);
//		List result = (List) broker.getCollectionByQuery(query);
//
//		if (result.size() == 0) {
//			throw new Exception("Error Reading Curso Mestrado (" + areaCientifica.getCodigointerno() + ")");
//		}
//
//		Posgrad_curso_mestrado posgrad_curso_mestrado = (Posgrad_curso_mestrado) result.get(0);
//
//		// Get the Degree											
//
//		criteria = new Criteria();
//		criteria.addEqualTo("nome", posgrad_curso_mestrado.getNomemestrado());
//		criteria.addEqualTo("tipoCurso", new Integer(TipoCurso.MESTRADO));
//		query = new QueryByCriteria(Curso.class, criteria);
//		result = (List) broker.getCollectionByQuery(query);
//
//		if (result.size() == 0) {
//			throw new Exception("Error Reading Degree (" + areaCientifica.getCodigointerno() + ")");
//		}
//
//		Curso degree = (Curso) result.get(0);
//
//		// Get the Degree Curricular Plan
//
//		criteria = new Criteria();
//		criteria.addEqualTo("degreeKey", degree.getIdInternal());
//		query = new QueryByCriteria(DegreeCurricularPlan.class, criteria);
//		result = (List) broker.getCollectionByQuery(query);
//
//		if (result.size() == 0) {
//			throw new Exception("Error Reading Degree Curricular Plan (" + areaCientifica.getCodigointerno() + ")");
//		}
//
//		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0);
//
//		// Get the Branch
//
//		criteria = new Criteria();
//		criteria.addEqualTo("code", "");
//		criteria.addEqualTo("name", "");
//		criteria.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
//		query = new QueryByCriteria(Branch.class, criteria);
//		result = (List) broker.getCollectionByQuery(query);
//
//		if (result.size() == 0) {
//			throw new Exception("Error Reading Branch (Curricular Plan Key: " + degreeCurricularPlan.getIdInternal() + ")");
//		}
//
//		return (Branch) result.get(0);
//
//	}

}
