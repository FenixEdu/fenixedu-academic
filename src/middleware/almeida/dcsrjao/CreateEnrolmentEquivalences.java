package middleware.almeida.dcsrjao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEquivalenceRestriction;
import Dominio.EnrolmentEvaluation;
import Dominio.EnrolmentInOptionalCurricularCourse;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestriction;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;
import Util.StudentCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class CreateEnrolmentEquivalences extends LoadDataToFenix {

	private static CreateEnrolmentEquivalences loader = null;
	private static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";

	private int numOfStudentsTotal = 0;
	private int numOfStudentsBefore1997 = 0;
	private int numOfStudentsAfter1997 = 0;
	private int numOfAllAprovedEnrolmentsAfter1997WithEquivalence = 0;
	private int numOfAllAprovedEnrolmentsAfter1997WithNoEquivalence = 0;
	private int numOfAllAprovedEnrolmentsAfter1997 = 0;
	private int numOfAllAprovedEnrolmentsAfter1997ForActualStudent = 0;
	private int numOfAllAprovedEnrolmentsAfter1997WithEquivalenceForStudentBeingIterated = 0;
	private int numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated = 0;
	private Integer numberOfStudentBeingIterated = null;
	private List studentsNumbersAfter1997WithNotAllEquivalences = new ArrayList();
	private List studentsNumbersAfter1997WithAllEquivalences = new ArrayList();

	private IDegreeCurricularPlan newDegreeCurricularPlan = null;
	private IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	private boolean before1977 = true;
	private String outputFilename = "After1997.txt";
	private IBranch newBranch = null;
	List curricularCourseEquivalencesList = null;

	public CreateEnrolmentEquivalences(int time) {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new CreateEnrolmentEquivalences(1);
		}

		//		logString = "";
		//		error = new HashMap();;
		//		errorMessage = "";
		//		errorDBID = "";
		//		
		//		loader.before1977 = flag;
		//		if (loader.before1977 == true) {
		//			loader.outputFilename = "Before1997.txt";
		//		} else {
		//			loader.outputFilename = "After1997.txt";
		//		}

		loader.migrationStart(loader.getClassName());

		loader.setupDAO();

		loader.oldDegreeCurricularPlan = loader.processOldDegreeCurricularPlan();
		if (loader.oldDegreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		loader.newDegreeCurricularPlan = loader.processNewDegreeCurricularPlan();
		if (loader.newDegreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		loader.curricularCourseEquivalencesList = loader.persistentObjectOJB.readAllCurricularCourseEquivalences();
		loader.shutdownDAO();

		loader.newBranch = loader.processNewBranch();
		if (loader.newBranch == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}
		loader.setupDAO();
		List oldStudentCurricularPlanList = null;
		oldStudentCurricularPlanList = loader.persistentObjectOJB.readAllStudentCurricularPlansFromDegreeCurricularPlan(loader.oldDegreeCurricularPlan);
		loader.numOfStudentsTotal = oldStudentCurricularPlanList.size();
		loader.shutdownDAO();

		IStudentCurricularPlan oldStudentCurricularPlan = null;
		Iterator iterator = oldStudentCurricularPlanList.iterator();
		//		for (int i = 0; i < 50; i++) {
		while (iterator.hasNext()) {
			oldStudentCurricularPlan = (IStudentCurricularPlan) iterator.next();
//			oldStudentCurricularPlan = (IStudentCurricularPlan) oldStudentCurricularPlanList.get(i);
			loader.printIteration(loader.getClassName(), oldStudentCurricularPlan.getIdInternal().longValue());
			loader.setupDAO();
			loader.numberOfStudentBeingIterated = oldStudentCurricularPlan.getStudent().getNumber();
			loader.processOldStudentCurricularPlanIteration(oldStudentCurricularPlan);
			loader.shutdownDAO();
		}

		//				List curricularCourseEquivalencesList = null;
		//				curricularCourseEquivalencesList = loader.persistentObjectOJB.readAllCurricularCourseEquivalences();
		//				loader.shutdownDAO();
		//
		//		Iterator iterator = curricularCourseEquivalencesList.iterator();
		//		ICurricularCourseEquivalence curricularCourseEquivalence = null;
		//		while (iterator.hasNext()) {
		//			curricularCourseEquivalence = (ICurricularCourseEquivalence) iterator.next();
		//			loader.printIteration(loader.getClassName(), curricularCourseEquivalence.getIdInternal().longValue());
		//			loader.setupDAO();
		//			loader.processCourseEquivalence(curricularCourseEquivalence);
		//			loader.shutdownDAO();
		//		}

		logString += loader.processStatistics();
		logString += error.toString();
		loader.migrationEnd(loader.getClassName(), logString);
	}

	private String processStatistics() {
		String statistics = "";

		statistics += "\n----------------------------------------------------------------------------------------";
		statistics += "\n      Numero Total de estudantes: " + this.numOfStudentsTotal;
		statistics += "\n      Numero de estudantes antes de 1997: " + this.numOfStudentsBefore1997;
		statistics += "\n      Numero de estudantes depois de 1997: " + this.numOfStudentsAfter1997;
		statistics += "\n      Numero de inscrições aprovadas depois 1997: " + this.numOfAllAprovedEnrolmentsAfter1997;
		statistics += "\n      Numero de inscrições aprovadas depois 1997 com equivalencia dada: " + this.numOfAllAprovedEnrolmentsAfter1997WithEquivalence;
		statistics += "\n      Numero de inscrições aprovadas depois 1997 sem equivalencia dada: " + this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalence;
		statistics += "\n      Numero de estudantes depois 1997 com todas as equivalencia dadas: " + this.studentsNumbersAfter1997WithAllEquivalences.size();
		statistics += "\n      \t numeros: " + this.studentsNumbersAfter1997WithAllEquivalences.toString();
		statistics += "\n																						 ";
		statistics += "\n      Numero de estudantes depois 1997 sem todas as equivalencia dadas: " + this.studentsNumbersAfter1997WithNotAllEquivalences.size();
		statistics += "\n      \t numeros: " + this.studentsNumbersAfter1997WithNotAllEquivalences.toString();
		statistics += "\n----------------------------------------------------------------------------------------";

		return statistics;
	}

	private void processOldStudentCurricularPlanIteration(IStudentCurricularPlan oldStudentCurricularPlan) {
		Integer firstEnrolmentYear = persistentObjectOJB.readFirstEnrolmentYearOfStudentCurricularPlan(oldStudentCurricularPlan);
		
		if(oldStudentCurricularPlan.getIdInternal().intValue() == 1628){
			System.out.print("w");
		}
		
		IStudentCurricularPlan newStudentCurricularPlan = null;
		if(firstEnrolmentYear.intValue() < 1997){
			this.numOfStudentsBefore1997++;
		}else {
			this.numOfAllAprovedEnrolmentsAfter1997ForActualStudent = 0;
			this.numOfAllAprovedEnrolmentsAfter1997WithEquivalenceForStudentBeingIterated = 0;
			this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated = 0;
			this.numOfStudentsAfter1997++;
			this.numOfAllAprovedEnrolmentsAfter1997ForActualStudent = processNumOfAprovedEnrolmentsForActualStudent(oldStudentCurricularPlan);
			this.numOfAllAprovedEnrolmentsAfter1997 += this.numOfAllAprovedEnrolmentsAfter1997ForActualStudent;

			newStudentCurricularPlan = processNewStudentCurricularPlan(oldStudentCurricularPlan.getStudent());
			Iterator curricularCourseEquivalencesIterator = this.curricularCourseEquivalencesList.iterator();
			ICurricularCourseEquivalence curricularCourseEquivalence = null;
			while (curricularCourseEquivalencesIterator.hasNext()) {
				curricularCourseEquivalence = (ICurricularCourseEquivalence) curricularCourseEquivalencesIterator.next();

				List courseEquivalenceRestrictions = curricularCourseEquivalence.getEquivalenceRestrictions();
				int numRestrictionsNecessaryToEquivalence = courseEquivalenceRestrictions.size();
				List enrolmentsInRestrictions = new ArrayList();

				Iterator courseEquivalenceRestrictionsIterator = courseEquivalenceRestrictions.iterator();
				while (courseEquivalenceRestrictionsIterator.hasNext()) {
					ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestriction =
						(ICurricularCourseEquivalenceRestriction) courseEquivalenceRestrictionsIterator.next();

					ICurricularCourse equivalentCurricularCourse = curricularCourseEquivalenceRestriction.getEquivalentCurricularCourse();
					
					List enrolmentsInEquivalentCourse = null;
					
					if(curricularCourseEquivalenceRestriction.getYearOfEquivalence().equals("")){
						enrolmentsInEquivalentCourse = persistentObjectOJB.readEnrolmentsByCurricularCourseAndStudentCurricularPlan(equivalentCurricularCourse, oldStudentCurricularPlan);
					}else{
						enrolmentsInEquivalentCourse = persistentObjectOJB.readEnrolmentsByCurricularCourseAndStudentCurricularPlanAndAcademicYear(equivalentCurricularCourse, oldStudentCurricularPlan, curricularCourseEquivalenceRestriction.getYearOfEquivalence());
					}
											
					if ((enrolmentsInEquivalentCourse != null) && (!enrolmentsInEquivalentCourse.isEmpty())) {
						enrolmentsInRestrictions.addAll(enrolmentsInEquivalentCourse);
					}
				}

				final List aprovedEnrolmentsInRestrictions = (List) CollectionUtils.select(enrolmentsInRestrictions, new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrolment enrolment = (IEnrolment) obj;
						return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
					}
				});

				if (aprovedEnrolmentsInRestrictions.size() == numRestrictionsNecessaryToEquivalence) {
					IEnrolment newEnrolment = processNewEnrolment(curricularCourseEquivalence.getCurricularCourse(), newStudentCurricularPlan);
					if (newEnrolment == null) {
						return;
					}

					IEnrolmentEvaluation enrolmentEvaluation = processEnrolmentEvaluation(newEnrolment, aprovedEnrolmentsInRestrictions);
					if (enrolmentEvaluation == null) {
						return;
					}

					IEnrolmentEquivalence enrolmentEquivalence = processEnrolmentEquivalence(newEnrolment, aprovedEnrolmentsInRestrictions);
					if (enrolmentEquivalence == null) {
						return;
					}
				}
			}

			processStudentEquivalenceState();
		}
	}
	private void processStudentEquivalenceState() {
		this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated =
			this.numOfAllAprovedEnrolmentsAfter1997ForActualStudent
				- this.numOfAllAprovedEnrolmentsAfter1997WithEquivalenceForStudentBeingIterated
				+ this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated;

		this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalence += this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated;
		this.numOfAllAprovedEnrolmentsAfter1997WithEquivalence += this.numOfAllAprovedEnrolmentsAfter1997WithEquivalenceForStudentBeingIterated;

		if (this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated > 0) {
			this.studentsNumbersAfter1997WithNotAllEquivalences.add(this.numberOfStudentBeingIterated);
		} else {
			this.studentsNumbersAfter1997WithAllEquivalences.add(this.numberOfStudentBeingIterated);
		}
	}

	private int processNumOfAprovedEnrolmentsForActualStudent(IStudentCurricularPlan oldStudentCurricularPlan) {

		final List aprovedEnrolments = (List) CollectionUtils.select(oldStudentCurricularPlan.getEnrolments(), new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
			}
		});
		return aprovedEnrolments.size();
	}

	private IEnrolmentEvaluation processEnrolmentEvaluation(IEnrolment newEnrolment, List aprovedEnrolmentsInRestrictions) {
		int actualGrade = 0;
		int maxGrade = 0;
		List grades = new ArrayList();

		Iterator enrolmentsIterator = aprovedEnrolmentsInRestrictions.iterator();
		while (enrolmentsIterator.hasNext()) {
			IEnrolment oldEnrolment = (IEnrolment) enrolmentsIterator.next();
			List oldEvaluations = oldEnrolment.getEvaluations();

			Iterator evaluationsIterator = oldEvaluations.iterator();
			while (evaluationsIterator.hasNext()) {
				IEnrolmentEvaluation oldEnrolmentEvaluation = (IEnrolmentEvaluation) evaluationsIterator.next();
				try {
					if (!oldEnrolmentEvaluation.getGrade().equals("RE")) {
						actualGrade = new Integer(oldEnrolmentEvaluation.getGrade()).intValue();
					}

				} catch (NumberFormatException e) {
					errorMessage = "\n Nota " + oldEnrolmentEvaluation.getGrade() + " inválida! Registos: ";
					errorDBID = "";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
					numberUntreatableElements++;
					return null;
				}

				if (actualGrade > maxGrade) {
					maxGrade = actualGrade;
				}
			}
			grades.add(new Integer(maxGrade));
		}

		IEnrolmentEvaluation equivalentEnrolmentEvaluation = new EnrolmentEvaluation();

		Iterator gradeIterator = grades.iterator();
		int somaNotas = 0;
		int numeroNotas = 0;
		int notaFinal = 0;
		while (gradeIterator.hasNext()) {
			somaNotas = somaNotas + ((Integer) gradeIterator.next()).intValue();
			numeroNotas++;
		}

		notaFinal = somaNotas / numeroNotas;
		// NOTE DAVID-RICARDO: Qual a nota quando à mais que um enrolment para equivalencia?
		equivalentEnrolmentEvaluation.setGrade("" + notaFinal);
		// NOTE DAVID-RICARDO: Quais são os restantes campos quando à mais que um enrolment para equivalencia?
		equivalentEnrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
		equivalentEnrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		equivalentEnrolmentEvaluation.setEnrolment(newEnrolment);
		// TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
		equivalentEnrolmentEvaluation.setCheckSum(null);

		return equivalentEnrolmentEvaluation;
	}

	private IEnrolment processNewEnrolment(ICurricularCourse curricularCourseToEnrol, IStudentCurricularPlan newStudentCurricularPlan) {
		int year = 2003;
		// o semestre vai a 1 porque não interessa qual o semestre curricular do novo enrolment (por equivalencia)
		IExecutionPeriod executionPeriod = processExecutionPeriod(year, 1);
		IBranch branch = newStudentCurricularPlan.getBranch();
		ICurricularCourseScope curricularCourseScope =
			persistentObjectOJB.readCurricularCourseScopeByCurricularCourseAndBranch(curricularCourseToEnrol, branch);
		if (curricularCourseScope == null) {
			errorMessage =
				"\n Não existe o curricularCourseScope no course = "
					+ curricularCourseToEnrol.getCode()
					+ "["
					+ curricularCourseToEnrol.getIdInternal()
					+ "]"
					+ " Ano = 2003 Ramo = "
					+ branch.getCode()
					+ "["
					+ branch.getInternalID()
					+ "]"
					+ "!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated++;
			numberUntreatableElements++;
			return null;
		}

		IEnrolment newEnrolment = persistentObjectOJB.readEnrolmentByUnique(newStudentCurricularPlan, curricularCourseScope, executionPeriod);
		if (newEnrolment == null) {
			
			if(curricularCourseToEnrol.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)){
				newEnrolment = new EnrolmentInOptionalCurricularCourse();
			}else{
				newEnrolment = new Enrolment();	
			}
			
			newEnrolment.setCurricularCourseScope(curricularCourseScope);
			newEnrolment.setExecutionPeriod(executionPeriod);
			newEnrolment.setStudentCurricularPlan(newStudentCurricularPlan);
			newEnrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			newEnrolment.setEnrolmentState(EnrolmentState.APROVED);
			writeElement(newEnrolment);
		} else {
			errorMessage =
				"\n O aluno "
					+ newStudentCurricularPlan.getStudent().getNumber().intValue()
					+ " já tem uma inscrição por equivalência para a cadeira = "
					+ curricularCourseScope.getCurricularCourse().getName()
					+ "!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			this.numOfAllAprovedEnrolmentsAfter1997WithNoEquivalenceForStudentBeingIterated++;
			numberUntreatableElements++;
			return null;
		}

		this.numOfAllAprovedEnrolmentsAfter1997WithEquivalenceForStudentBeingIterated++;
		return newEnrolment;
	}

	private IEnrolmentEquivalence processEnrolmentEquivalence(IEnrolment newEnrolment, List aprovedEnrolmentsInRestrictions) {
		IEnrolmentEquivalence enrolmentEquivalence = persistentObjectOJB.readEnrolmentEquivalenceByUnique(newEnrolment);

		if (enrolmentEquivalence == null) {
			enrolmentEquivalence = new EnrolmentEquivalence();
			enrolmentEquivalence.setEnrolment(newEnrolment);
			writeElement(enrolmentEquivalence);

			Iterator iterator = aprovedEnrolmentsInRestrictions.iterator();
			while (iterator.hasNext()) {
				IEnrolment oldEnrolment = (IEnrolment) iterator.next();
				IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestriction =
					persistentObjectOJB.readEnrolmentEquivalenceRestrictionByUnique(oldEnrolment, enrolmentEquivalence);
				if (enrolmentEquivalenceRestriction == null) {
					enrolmentEquivalenceRestriction = new EnrolmentEquivalenceRestriction();
					enrolmentEquivalenceRestriction.setEnrolmentEquivalence(enrolmentEquivalence);
					enrolmentEquivalenceRestriction.setEquivalentEnrolment(oldEnrolment);
					writeElement(enrolmentEquivalenceRestriction);
					numberElementsWritten--;
				} else {
					errorMessage =
						"\n já existe uma restricão entre o enrolment novo = "
							+ newEnrolment.getIdInternal()
							+ " e o enrolment antigo = "
							+ oldEnrolment.getIdInternal()
							+ "!";
					errorDBID = "";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
				}
			}
		} else {
			errorMessage = "\n já existe uma equivalência para o enrolment" + newEnrolment.getIdInternal() + "!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		return enrolmentEquivalence;
	}

	//	private IEnrolmentEvaluation processEnrolmentEvaluation(
	//		ICurricularCourseEquivalence curricularCourseEquivalence,
	//		IEnrolment newEnrolment,
	//		List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
	//		int actualGrade = 0;
	//		int maxGrade = 0;
	//		List grades = new ArrayList();
	//
	//		Iterator enrolmentsIterator = enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.iterator();
	//		while (enrolmentsIterator.hasNext()) {
	//			IEnrolment oldEnrolment = (IEnrolment) enrolmentsIterator.next();
	//			List oldEvaluations = oldEnrolment.getEvaluations();
	//
	//			Iterator evaluationsIterator = oldEvaluations.iterator();
	//			while (evaluationsIterator.hasNext()) {
	//				IEnrolmentEvaluation oldEnrolmentEvaluation = (IEnrolmentEvaluation) evaluationsIterator.next();
	//				try {
	//					if (!oldEnrolmentEvaluation.getGrade().equals("RE")) {
	//						actualGrade = new Integer(oldEnrolmentEvaluation.getGrade()).intValue();
	//					}
	//
	//				} catch (NumberFormatException e) {
	//					errorMessage = "\n Nota " + oldEnrolmentEvaluation.getGrade() + " inválida! Registos: ";
	//					errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
	//					error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//					numberUntreatableElements++;
	//					return null;
	//				}
	//
	//				if (actualGrade > maxGrade) {
	//					maxGrade = actualGrade;
	//				}
	//			}
	//			grades.add(new Integer(maxGrade));
	//		}
	//
	//		IEnrolmentEvaluation equivalentEnrolmentEvaluation = new EnrolmentEvaluation();
	//
	//		Iterator gradeIterator = grades.iterator();
	//		int somaNotas = 0;
	//		int numeroNotas = 0;
	//		int notaFinal = 0;
	//		while (gradeIterator.hasNext()) {
	//			somaNotas = somaNotas + ((Integer) gradeIterator.next()).intValue();
	//			numeroNotas++;
	//		}
	//
	//		notaFinal = somaNotas / numeroNotas;
	//		// NOTE DAVID-RICARDO: Qual a nota quando à mais que um enrolment para equivalencia?
	//		equivalentEnrolmentEvaluation.setGrade("" + notaFinal);
	//		// NOTE DAVID-RICARDO: Quais são os restantes campos quando à mais que um enrolment para equivalencia?
	//		equivalentEnrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
	//		equivalentEnrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
	//		equivalentEnrolmentEvaluation.setEnrolment(newEnrolment);
	//		// TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
	//		equivalentEnrolmentEvaluation.setCheckSum(null);
	//
	//		return equivalentEnrolmentEvaluation;
	//	}

	private IExecutionPeriod processExecutionPeriod(long yearLong, long semesterLong) {

		Integer semester = new Integer("" + semesterLong);
		Integer year = new Integer("" + yearLong);
		String yearStr = new String((year.intValue() - 1) + "/" + year.intValue());

		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(yearStr);
		if (executionYear == null) {
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CLOSED);
			executionYear.setYear(yearStr);
			writeElement(executionYear);
			loader.numberElementsWritten--;
		}

		IExecutionPeriod executionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if (executionPeriod == null) {
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(semester);
			executionPeriod.setState(PeriodState.CLOSED);
			String name = "";
			if (semester.intValue() == 1) {
				name = "1º Semestre";
			} else if (semester.intValue() == 2) {
				name = "2º Semestre";
			}
			executionPeriod.setName(name);
			writeElement(executionPeriod);
			loader.numberElementsWritten--;
		}

		return executionPeriod;
	}

	private IStudentCurricularPlan processNewStudentCurricularPlan(IStudent student) {
		IStudentCurricularPlan studentCurricularPlan = null;

		StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.ACTIVE_OBJ;
		int newYear = new Integer(2003).intValue();
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
		Date newDate = newCalendar.getTime();
		studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.newDegreeCurricularPlan, this.newBranch, studentCurricularPlanState);
		if (studentCurricularPlan == null) {
			studentCurricularPlan = new StudentCurricularPlan();
			studentCurricularPlan.setBranch(this.newBranch);
			studentCurricularPlan.setCurrentState(studentCurricularPlanState);
			studentCurricularPlan.setDegreeCurricularPlan(this.newDegreeCurricularPlan);
			studentCurricularPlan.setStudent(student);
			studentCurricularPlan.setStartDate(newDate);
			writeElement(studentCurricularPlan);
			loader.numberElementsWritten--;
		}

		return studentCurricularPlan;
	}

	private IDegreeCurricularPlan processNewDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular" + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_OLD_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular" + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private IBranch processNewBranch() { // no novo curriculo da LEQ não há branches
		String code = "";
		IBranch branch = persistentObjectOJB.readBranchByUnique(code, this.newDegreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n Não existe o ramo com o code [" + code + "] e plano curricular = " + this.newDegreeCurricularPlan.getName() + "!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return branch;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/CreateEnrolmentEquivalences" + this.outputFilename;
	}

	protected String getClassName() {
		return "CreateEnrolmentEquivalences";
	}

	//	private void processCourseEquivalence(ICurricularCourseEquivalence curricularCourseEquivalence) {
	//		if (curricularCourseEquivalence.getCurricularCourse().getDegreeCurricularPlan().equals(this.newDegreeCurricularPlan)) {
	//			List courseEquivalenceRestrictions = curricularCourseEquivalence.getEquivalenceRestrictions();
	//			int numRestrictionsNecessaryToEquivalence = courseEquivalenceRestrictions.size();
	//			List enrolmentsInRestrictions = new ArrayList();
	//
	//			Iterator iterator = courseEquivalenceRestrictions.iterator();
	//			while (iterator.hasNext()) {
	//				ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestriction = (ICurricularCourseEquivalenceRestriction) iterator.next();
	//				ICurricularCourse equivalentCurricularCourse = curricularCourseEquivalenceRestriction.getEquivalentCurricularCourse();
	//				List enrolmentsInEquivalentCourse = persistentObjectOJB.readEnrolmentsByCurricularCourse(equivalentCurricularCourse);
	//				if (enrolmentsInEquivalentCourse == null) {
	//					errorMessage =
	//						"\n O curricularCourse "
	//							+ curricularCourseEquivalence.getCurricularCourse().getName()
	//							+ " tem equivalencias mas não tem restrictions! Registos: ";
	//					errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
	//					error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//					numberUntreatableElements++;
	//				} else {
	//					enrolmentsInRestrictions.addAll(enrolmentsInEquivalentCourse);
	//				}
	//			}
	//
	//			final List aprovedEnrolmentsInRestrictions = (List) CollectionUtils.select(enrolmentsInRestrictions, new Predicate() {
	//				public boolean evaluate(Object obj) {
	//					IEnrolment enrolment = (IEnrolment) obj;
	//					return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
	//				}
	//			});
	//
	//			List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment = new ArrayList();
	//			while (!aprovedEnrolmentsInRestrictions.isEmpty()) {
	//				iterator = aprovedEnrolmentsInRestrictions.iterator();
	//				IEnrolment firstEnrolment = null;
	//				if (iterator.hasNext()) {
	//					firstEnrolment = (IEnrolment) iterator.next();
	//					enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.add(firstEnrolment);
	//				}
	//
	//				while (iterator.hasNext()) {
	//					IEnrolment nextEnrolment = (IEnrolment) iterator.next();
	//					if (nextEnrolment.getStudentCurricularPlan().equals(firstEnrolment.getStudentCurricularPlan())) {
	//						enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.add(nextEnrolment);
	//					}
	//				}
	//
	//				aprovedEnrolmentsInRestrictions.removeAll(enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
	//
	//				enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment =
	//					processEnrolmentEquivalencesByYear(enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
	//
	//				if (enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment == null) {
	//					return;
	//				}
	//
	//				if (enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.size() == numRestrictionsNecessaryToEquivalence) {
	//					processEquivalentEnrolment(firstEnrolment, curricularCourseEquivalence, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
	//				}
	//
	//				enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.clear();
	//			}
	//		}
	//	}
	//	private List processEnrolmentEquivalencesByYear(List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
	//		List result = new ArrayList();
	//		Iterator iterator = enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.iterator();
	//		while (iterator.hasNext()) {
	//			IEnrolment enrolment = (IEnrolment) iterator.next();
	//			String enrolmentYearStr = enrolment.getExecutionPeriod().getExecutionYear().getYear().substring(0, 3);
	//			Integer enrolmentYear = null;
	//			try {
	//				enrolmentYear = new Integer(enrolmentYearStr);
	//			} catch (NumberFormatException e) {
	//				errorMessage = "\n O ano " + enrolmentYearStr + " inválido para a criação de Intergers!";
	//				errorDBID = "";
	//				error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//				numberUntreatableElements++;
	//				return null;
	//			}
	//			int enrolmentSemester = enrolment.getExecutionPeriod().getSemester().intValue();
	//
	//			if ((this.before1977 == true) && (enrolmentSemester == 2) && (enrolmentYear.intValue() < 1997)) {
	//				result.add(enrolment);
	//			}
	//
	//			if ((this.before1977 == false) && (enrolmentSemester == 2) && (enrolmentYear.intValue() >= 1997)) {
	//				result.add(enrolment);
	//			}
	//		}
	//		return result;
	//	}

	//	private void processEquivalentEnrolment(
	//		IEnrolment firstEnrolment,
	//		ICurricularCourseEquivalence curricularCourseEquivalence,
	//		List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
	//		IStudent student = firstEnrolment.getStudentCurricularPlan().getStudent();
	//		IStudentCurricularPlan activeStudentCurricularPlan =
	//			persistentObjectOJB.readActiveStudentCurricularPlanByStudentAndDegreeCurricularPlan(student, this.newDegreeCurricularPlan);
	//		if (student == null) {
	//			errorMessage =
	//				"\n O StudentCurricularPlan do estudante "
	//					+ student.getNumber()
	//					+ " no plano curricular "
	//					+ this.newDegreeCurricularPlan.getName()
	//					+ "não existe! Registos: ";
	//			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
	//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//			numberUntreatableElements++;
	//			return;
	//		}
	//
	//		int year = 2003;
	//		// o semestre vai a 1 porque não interessa qual o semestre curricular do novo enrolment (por equivalencia)
	//		IExecutionPeriod executionPeriod = processExecutionPeriod(year, 1);
	//		ICurricularCourse curricularCourseToEnrol = curricularCourseEquivalence.getCurricularCourse();
	//		IBranch branch = activeStudentCurricularPlan.getBranch();
	//		ICurricularCourseScope curricularCourseScope =
	//			persistentObjectOJB.readCurricularCourseScopeByCurricularCourseAndBranch(curricularCourseToEnrol, branch);
	//		if (curricularCourseScope == null) {
	//			errorMessage =
	//				"\n Não existe o curricularCourseScope no course = "
	//					+ curricularCourseToEnrol.getCode()
	//					+ "["
	//					+ curricularCourseToEnrol.getIdInternal()
	//					+ "]"
	//					+ " Ano = 2003 Ramo = "
	//					+ branch.getCode()
	//					+ "["
	//					+ branch.getInternalID()
	//					+ "]"
	//					+ ". Registos: ";
	//			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
	//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//			numberUntreatableElements++;
	//			return;
	//		}
	//
	//		IEnrolment newEnrolment = persistentObjectOJB.readEnrolmentByUnique(activeStudentCurricularPlan, curricularCourseScope, executionPeriod);
	//		if (newEnrolment == null) {
	//			newEnrolment = new Enrolment();
	//			newEnrolment.setCurricularCourseScope(curricularCourseScope);
	//			newEnrolment.setExecutionPeriod(executionPeriod);
	//			newEnrolment.setStudentCurricularPlan(activeStudentCurricularPlan);
	//			newEnrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
	//			newEnrolment.setEnrolmentState(EnrolmentState.APROVED);
	//			writeElement(newEnrolment);
	//
	//			IEnrolmentEquivalence enrolmentEquivalence =
	//				processEnrolmentEquivalence(curricularCourseEquivalence, newEnrolment, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
	//			if (enrolmentEquivalence == null) {
	//				return;
	//			}
	//
	//			IEnrolmentEvaluation enrolmentEvaluation =
	//				processEnrolmentEvaluation(curricularCourseEquivalence, newEnrolment, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
	//			if (enrolmentEvaluation == null) {
	//				return;
	//			}
	//		}
	//		// O else não é erro porque existem várias equivalencias para a mesma disciplina, por isso, vão se o aluno tiver feito
	//		// uma equivalencia é criado um enrolment, na iteracao seguintem, já existe um enrolment para outra equivalencia
	//		//		 else {
	//		//			errorMessage = "\n já existe um enrolment para o aluno " + student.getNumber() + " na cadeira " + curricularCourseToEnrol.getName() + " no ano " + executionPeriod.getName() + " Registos: ";
	//		//			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
	//		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
	//		//			numberUntreatableElements++;
	//		//			return;
	//		//		}
	//
	//	}

}