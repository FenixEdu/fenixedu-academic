package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_curricular_course;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaCurricularCoursesCodesFromFileToTable extends LoadAlmeidaDataToTable {

	private static final String ONE_SPACE = " ";
	private static final String THREE_SPACE = "   ";

	private static LoadAlmeidaCurricularCoursesCodesFromFileToTable loader = null;
	protected static String logString = "";

	public LoadAlmeidaCurricularCoursesCodesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurricularCoursesCodesFromFileToTable();
		}
		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String code = stringTokenizer.nextToken();
		String name = stringTokenizer.nextToken();
		String universityCode = stringTokenizer.nextToken();

		if (code.startsWith(LoadAlmeidaCurricularCoursesCodesFromFileToTable.ONE_SPACE)) {
			code = code.substring(1);
		}

		if (universityCode.startsWith(LoadAlmeidaCurricularCoursesCodesFromFileToTable.THREE_SPACE)) {
			universityCode = null;
		}

		loader.setupDAO();
		if (persistentObjectOJB.readAlmeidaCurricularCourseByCode(code) == null) {
			Almeida_curricular_course almeida_curricular_course = new Almeida_curricular_course();
			almeida_curricular_course.setId_internal(loader.numberElementsWritten + 1);
			almeida_curricular_course.setCode(code);
			almeida_curricular_course.setName(name);
			almeida_curricular_course.setUniversityCode(universityCode);
			writeElement(almeida_curricular_course);
		} else {
			loader.numberUntreatableElements++;
			logString += "ERRO(linha[" + (numberLinesProcessed + 1) + "]): A disciplina com o codigo = " + code + " ja existe!\n";
		}
		loader.shutdownDAO();

	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/NOMEDIS.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadAlmeidaCurricularCoursesCodesFromFileToTable.txt";
	}
	protected String getClassName() {
		return "LoadAlmeidaCurricularCoursesCodesFromFileToTable";
	}
}