package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.LoadDataFile;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaLEQCurricularCourses extends LoadDataFile {

	private static LoadAlmeidaLEQCurricularCourses loader = null;
	protected static String logString = "";
	protected static IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	protected static IDegreeCurricularPlan newDegreeCurricularPlan = null;

	public LoadAlmeidaLEQCurricularCourses() {
		super.setupDAO();
		oldDegreeCurricularPlan = this.processOldDegreeCurricularPlan();
		newDegreeCurricularPlan = super.persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2003");
		super.shutdownDAO();
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaLEQCurricularCourses();
		}
		loader.run();
	}

	public void run() {
		if (loader == null) {
			loader = new LoadAlmeidaLEQCurricularCourses();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String code = stringTokenizer.nextToken();
		String name = stringTokenizer.nextToken();

		Almeida_curricular_course almeida_curricular_course = new Almeida_curricular_course();
		almeida_curricular_course.setId_internal(loader.numberElementsWritten + 1);
		almeida_curricular_course.setCode(code);
		almeida_curricular_course.setName(name);

		processCurricularCourse(almeida_curricular_course);
	}

	private void processCurricularCourse(Almeida_curricular_course almeida_curricular_course) {

//		Delete blank space in the beggining of code
		if (almeida_curricular_course.getCode().charAt(0) == ' ') {
			almeida_curricular_course.setCode(almeida_curricular_course.getCode().substring(1));
		}
		
		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(almeida_curricular_course.getCode(), almeida_curricular_course.getName(), oldDegreeCurricularPlan);
		if (curricularCourse == null) {
			curricularCourse = new CurricularCourse();
			curricularCourse.setDegreeCurricularPlan(oldDegreeCurricularPlan);
			curricularCourse.setName(almeida_curricular_course.getName());
			curricularCourse.setCode(almeida_curricular_course.getCode());
			curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);
			curricularCourse.setMandatory(new Boolean(false));
			curricularCourse.setCurricularCourseExecutionScope(CurricularCourseExecutionScope.SEMESTRIAL_OBJ);
			writeElement(curricularCourse);
			//logString += "A disciplina: " + almeida_curricular_course.getName() + " foi acrescentada no Plano Curricular Antigo\n";
		} else {
			logString += "ERRO: A disciplina: " + almeida_curricular_course.getName() + " já existe no Plano Curricular Antigo\n";
			loader.numberUntreatableElements++;
		}
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ");
		if (degreeCurricularPlan == null) {
			degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setName("LEQ");
			degreeCurricularPlan.setDegree(persistentObjectOJB.readDegreeByCode("LEQ"));
			degreeCurricularPlan.setState(DegreeCurricularPlanState.CONCLUDED_OBJ);
			degreeCurricularPlan.setDegreeDuration(new Integer(5));
			degreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));
			writeElement(degreeCurricularPlan);
		}
		return degreeCurricularPlan;
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/disciplinas_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/AlmeidaLEQCurricularCoursesMigrationLog.txt";
	}

	private String processCurricularCourseCode(String code) {
		// Delete blank space in the beggining of code
		if (code.charAt(0) == ' ') {
			code = code.substring(1);
		}

		if ((code.equals("24")) || (code.equals("P6")) || (code.equals("QJ"))) {
			code = "QN";
		} else if ((code.equals("AG")) || (code.equals("QA")) || (code.equals("QM")) || (code.equals("AC0"))) {
			code = "PY";
		} else if ((code.equals("AH")) || (code.equals("QF")) || (code.equals("PS")) || (code.equals("AC1"))) {
			code = "P5";
		} else if ((code.equals("AJ")) || (code.equals("S6")) || (code.equals("UY")) || (code.equals("AC2"))) {
			code = "UN";
		} else if ((code.equals("AK")) || (code.equals("V5"))) {
			code = "U8";
		} else if (code.equals("8Z")) {
			code = "AV7";
		} else if (code.equals("BG")) {
			code = "APS";
		} else if (code.equals("2R")) {
			code = "AME";
		} else if (code.equals("9R")) {
			code = "AR7";
		} else if (code.equals("ALG")) {
			code = "AP9";
		} else if (code.equals("Z7")) {
			code = "C4";
		} else if (code.equals("ZD")) {
			code = "C5";
		} else if (code.equals("2S")) {
			code = "AMG";
		} else if (code.equals("2U")) {
			code = "AMD";
		} else if (code.equals("9S")) {
			code = "7W";
		} else if ((code.equals("UP")) || (code.equals("UZ"))) {
			code = "SF";
		} else if (code.equals("A5H")) {
			code = "AMH";
		} else if (code.equals("A5Y")) {
			code = "AR8";
		} else if (code.equals("QW")) {
			code = "HU";
		} else if ((code.equals("V4")) || (code.equals("HV"))) {
			code = "AJM";
		}

		return code;
	}
}