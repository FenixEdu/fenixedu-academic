package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_enrolment;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaEnrolmentsFromFileToTable extends LoadAlmeidaDataToTable {

	private static final String ONE_SPACE = " ";
	private static final String TWO_SPACES = "  ";
	private static final String FOUR_SPACES = "    ";
	private static final String TEN_SPACES = "          ";
	private static final String FIFTEEN_SPACES = "               ";

	private static LoadAlmeidaEnrolmentsFromFileToTable loader = null;
	private static String logString = "";

	public LoadAlmeidaEnrolmentsFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaEnrolmentsFromFileToTable();
		}
		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());

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

		if (epoca.equals(LoadAlmeidaEnrolmentsFromFileToTable.ONE_SPACE)) {
			epoca = "5"; // corresponde ao valor de uma inscrição noutra universidade	
		}
		if (branchCode.equals(LoadAlmeidaEnrolmentsFromFileToTable.ONE_SPACE)) {
			branchCode = "0";
		}
		if (degreeCode.startsWith("0")) {
			degreeCode = degreeCode.substring(1);
		}
		if (curricularCourseCode.startsWith(LoadAlmeidaEnrolmentsFromFileToTable.ONE_SPACE)) {
			curricularCourseCode = curricularCourseCode.substring(1);
		}
		if (grade.equals(LoadAlmeidaEnrolmentsFromFileToTable.TWO_SPACES)) {
			grade = null;
		}
		if (teacherNumber.equals(LoadAlmeidaEnrolmentsFromFileToTable.FOUR_SPACES)) {
			teacherNumber = null;
		}
		if (evaluationDate.equals(LoadAlmeidaEnrolmentsFromFileToTable.TEN_SPACES)) {
			evaluationDate = null;
		}
		if (observation.equals(LoadAlmeidaEnrolmentsFromFileToTable.FIFTEEN_SPACES)) {
			observation = null;
		}

		long longCurricularYear = 0;
		long longCurricularSemester = 0;
		long longEpoca = 0;

		try {
			longCurricularYear = (new Integer(curricularYear)).longValue();
			longCurricularSemester = (new Integer(curricularSemester)).longValue();
			longEpoca = (new Integer(epoca)).longValue();
		} catch (NumberFormatException e2) {
			loader.numberUntreatableElements++;
			logString += "ERRO: Na linha [" + (numberLinesProcessed + 1) + "] os valores lidos do ficheiro são invalidos para a criação de Integers e/ou Doubles!\n";
		}

		if ((longCurricularYear < 1) || (longCurricularYear > 5)) {
			logString += "INFO: curricularYear " + curricularYear + " na linha " + (numberLinesProcessed + 1) + "!\n";
			curricularYear = "1";
		}

		if ((longCurricularSemester < 1) || (longCurricularSemester > 2)) {
			logString += "INFO: curricularSemester " + curricularSemester + " na linha " + (numberLinesProcessed + 1) + "!\n";
			curricularSemester = "0";
		}
		if ((longEpoca < 0) || (longEpoca > 5)) {
			logString += "INFO: epoca " + epoca + " na linha " + (numberLinesProcessed + 1) + "!\n";
			curricularSemester = "0";
		}
		
		Almeida_enrolment almeida_enrolment = new Almeida_enrolment();
		almeida_enrolment.setId_internal(loader.numberElementsWritten + 1);
		almeida_enrolment.setNumalu(studentNumber);

		try {
			almeida_enrolment.setAnoins((new Integer(enrolmentYear)).longValue());
			almeida_enrolment.setAnodis(longCurricularYear);
			almeida_enrolment.setSemdis(longCurricularSemester);
			almeida_enrolment.setEpoca(longEpoca);
			almeida_enrolment.setRamo((new Integer(branchCode)).longValue());
		} catch (NumberFormatException e) {
			numberUntreatableElements++;
			logString += "ERRO: Na linha [" + (numberLinesProcessed + 1) + "] os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
		}

		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		loader.setupDAO();
		writeElement(almeida_enrolment);
		loader.shutdownDAO();
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/curriculo_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}
	
	protected String getClassName() {
		return "LoadAlmeidaEnrolmentsFromFileToTable";
	}
}