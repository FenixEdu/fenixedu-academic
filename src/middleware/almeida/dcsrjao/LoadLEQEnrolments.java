package middleware.almeida.dcsrjao;

import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.Almeida_inscricoes;
import middleware.almeida.LoadDataFile;
import Dominio.Enrolment;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import Util.EnrolmentEvaluationType;
import Util.PeriodState;
import Util.UniversityCode;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadLEQEnrolments extends LoadDataFile {

	private static LoadLEQEnrolments loader = null;
	protected static String logString = "";
	protected List almeidaCurricularCourseList = null;

	private LoadLEQEnrolments() {
		this.almeidaCurricularCourseList = persistentObjectOJB.readAllAlmeidaCurricularCourses();
	}

	public static void main(String[] args) {
		loader = new LoadLEQEnrolments();
		loader.load();
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String studentNumber = stringTokenizer.nextToken();
		String enrolmentYear = stringTokenizer.nextToken();
		String curricularYear = stringTokenizer.nextToken();
		String curricularSemester = stringTokenizer.nextToken();
		String epoca = stringTokenizer.nextToken();
		String curricularCourseCode = stringTokenizer.nextToken();
		String degreeCode = stringTokenizer.nextToken();
		String branchCode = stringTokenizer.nextToken();
		String grade = stringTokenizer.nextToken();
		String teacherNumber = stringTokenizer.nextToken();
		String evaluationDate = stringTokenizer.nextToken();
		String universityCode = stringTokenizer.nextToken();
		String observation = stringTokenizer.nextToken();

		Almeida_enrolment almeida_enrolment = new Almeida_enrolment();
		almeida_enrolment.setId_internal(loader.numberElementsWritten + 1);
		almeida_enrolment.setNumalu(studentNumber);
	
		if(epoca.equals(" ")){
			epoca = "5"; // corresponde ao valor de uma inscrição noutra universidade	
		}
		
		if(branchCode.equals(" ")){
			branchCode = "-1"; // por enquanto nao interessa, por isso, passe-se um valor só para nao dar erro no set	
		}
		
		try {
			almeida_enrolment.setAnoins((new Integer(enrolmentYear)).longValue());
			almeida_enrolment.setAnodis((new Integer(curricularYear)).longValue());
			almeida_enrolment.setSemdis((new Integer(curricularSemester)).longValue());
			almeida_enrolment.setEpoca((new Integer(epoca)).longValue());
			almeida_enrolment.setRamo((new Integer(branchCode)).longValue());
		} catch (NumberFormatException e) {
			logString += " Os valores lidos do ficheiro são invalidos para a criação de Integers!\n" ;
		}
		
		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		processEnrolment(almeida_enrolment);
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {

		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(almeida_enrolment);
		ICurricularCourse curricularCourse = readCurricularCourse(almeida_enrolment);
		IExecutionPeriod executionPeriod = processExecutionPeriod(almeida_enrolment.getAnoins(),almeida_enrolment.getSemdis());

		// existe referencia a esta CC na BD, ou seja, a CC pertence ao degreeCurricularPlan deste semestre (e não o novo)
		if (curricularCourse != null) {

			IEnrolment enrolment = persistentObjectOJB.readEnrolment(studentCurricularPlan, curricularCourse, executionPeriod);
			if (enrolment == null) {
				enrolment = new Enrolment();
				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				enrolment.setEnrolmentEvaluationType(processEvaluationType(almeida_enrolment.getEpoca()));
				enrolment.setUniversityCode(processUniversityCode(almeida_enrolment.getCoduniv()));
				writeElement(enrolment);
			}
//			} else {
//				enrolment.setCurricularCourse(curricularCourse);
//				enrolment.setExecutionPeriod(executionPeriod);
//				enrolment.setEnrolmentState(EnrolmentState.ENROLED_OBJ);
//				enrolment.setStudentCurricularPlan(studentCurricularPlan);
//				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
//				enrolment.setUniversityCode(UniversityCode.IST);
//				writeElement(enrolment);
//			}
		}
	}


	private String processUniversityCode(String universityCodeRead) {
		String universityCode = null;
		if(universityCode.equals("IST")){
			universityCode = UniversityCode.IST;
		}else if (universityCode.equals("UBI")){
			universityCode = UniversityCode.UBI;
		}else {
			logString += "Novo University Code: " + universityCodeRead + "\n";
		}
		return universityCode;
	}

	private EnrolmentEvaluationType processEvaluationType(long epocaLong) {
		EnrolmentEvaluationType enrolmentEvaluationType = null;
		
		int epoca = (new Integer("" + epocaLong)).intValue();
		switch (epoca) {
			case 0 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
			case 1 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
			case 2 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
			case 3 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_OBJ;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
			default:
				logString += "A epoca [" + epocaLong + "] é invalida!\n";
				break;
		}
		
		return enrolmentEvaluationType;
	}

	private IExecutionPeriod processExecutionPeriod(long yearLong, long semesterLong) {
		
		Integer semester = new Integer("" + semesterLong);
		Integer year = new Integer("" + yearLong);
		
		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(year);
		if(executionYear == null)
		{
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CLOSED);
			executionYear.setYear(new String("" + year));
			writeElement(executionYear); 
		}
		
		IExecutionPeriod executionPeriod  = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if(executionPeriod == null)
		{
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(semester);
			executionPeriod.setState(PeriodState.CLOSED);
			executionPeriod.setName(year.intValue() + "/" + (year.intValue() + 1));
			writeElement(executionPeriod); 
		}
		
		return executionPeriod;	
	}

	private IDisciplinaExecucao readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readExecutionCourse(curricularCourse, executionPeriod);
	}

	private IExecutionPeriod readActiveExecutionPeriod() {
		return persistentObjectOJB.readActiveExecutionPeriod();
	}

	private ICurricularCourse readCurricularCourse(Almeida_enrolment almeida_enrolment) {
		ICurricularCourse curricularCourse = null;
		
		Almeida_curricular_course almeida_curricular_course = new Almeida_curricular_course();
		almeida_curricular_course.setCode(almeida_enrolment.getCoddis());
		if(this.almeidaCurricularCourseList.contains(almeida_curricular_course)) {
			String code = almeida_enrolment.getCoddis();
			int index = this.almeidaCurricularCourseList.indexOf(almeida_curricular_course);
			String name = ((Almeida_curricular_course) this.almeidaCurricularCourseList.get(index)).getName();
			//curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndName(code, name);
		}

//		curricularCourse = persistentObjectOJB.readCurricularCourse(almeida_disc.getNomedis(), new Integer("" + almeida_disc.getCodcur()), almeida_disc.getCoddis());
//		if (curricularCourse == null) {
//			// Log the ones we can't match
//			writeElement(almeida_inscricoes);
//			numberUntreatableElements++;
//		}

		return curricularCourse;
	}

	private IStudentCurricularPlan readStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IStudentCurricularPlan studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlan(new Integer(almeida_enrolment.getNumalu()));
		if (studentCurricularPlan == null) {
			logString += "NÃO HÁ PLANO CURRICULAR DE ALUNO PARA O ALUNO [" + almeida_enrolment.getNumalu() + "]\n";
		}
		return studentCurricularPlan;
	}

	private IBranch getBranch(Almeida_inscricoes almeida_inscricoes) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getFilename() {
		return "etc/migration/curriculo_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "enrolmentMigrationLog.txt";
	}
}