package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.LoadDataFile;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaLEQEnrolmentsFromFileToTable extends LoadDataFile {

	private static final String ONE_SPACE = " ";
	private static final String TWO_SPACES = "  ";
	private static final String FOUR_SPACES = "    ";
	private static final String TEN_SPACES = "          ";
	private static final String FIFTEEN_SPACES = "               ";

	private static LoadAlmeidaLEQEnrolmentsFromFileToTable loader = null;
	private static String logString = "";

	public LoadAlmeidaLEQEnrolmentsFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaLEQEnrolmentsFromFileToTable();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	public void processLine(String line) {

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

		if (epoca.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.ONE_SPACE)) {
			epoca = "5"; // corresponde ao valor de uma inscrição noutra universidade	
		}
		if ((branchCode.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.ONE_SPACE)) || (branchCode.equals("4"))) {
			branchCode = "0";
		}
		if (curricularCourseCode.startsWith(LoadAlmeidaLEQEnrolmentsFromFileToTable.ONE_SPACE)) {
			curricularCourseCode = curricularCourseCode.substring(1);
		}
		if (grade.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.TWO_SPACES)) {
			grade = null;
		}
		if (teacherNumber.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.FOUR_SPACES)) {
			teacherNumber = null;
		}
		if (evaluationDate.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.TEN_SPACES)) {
			evaluationDate = null;
		}
		if (observation.equals(LoadAlmeidaLEQEnrolmentsFromFileToTable.FIFTEEN_SPACES)) {
			observation = null;
		}

		try {
			almeida_enrolment.setAnoins((new Integer(enrolmentYear)).longValue());
			almeida_enrolment.setAnodis((new Integer(curricularYear)).longValue());
			almeida_enrolment.setSemdis((new Integer(curricularSemester)).longValue());
			almeida_enrolment.setEpoca((new Integer(epoca)).longValue());
			almeida_enrolment.setRamo((new Integer(branchCode)).longValue());
		} catch (NumberFormatException e) {
			logString += "ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
		}

		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		// FIXME DAVID-RICARDO: Este teste é para sair!
		if (almeida_enrolment.getCurso().equals("05")) {
			writeElement(almeida_enrolment);
		} else {
			logString += "INFO: Na linha [" + (loader.numberLinesProcessed + 1) + "] existe uma inscrição no curso [" + almeida_enrolment.getCurso() + "]!\n";
			loader.numberUntreatableElements++;
		}
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/curriculo_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadAlmeidaLEQEnrolmentsFromFileToTable.txt";
	}
}