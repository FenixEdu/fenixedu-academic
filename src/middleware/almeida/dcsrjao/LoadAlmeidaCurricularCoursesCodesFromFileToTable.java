package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.LoadDataFile;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaCurricularCoursesCodesFromFileToTable extends LoadDataFile {

	private static final String ONE_SPACE = " ";

	private static LoadAlmeidaCurricularCoursesCodesFromFileToTable loader = null;
	protected static String logString = "";

	public LoadAlmeidaCurricularCoursesCodesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurricularCoursesCodesFromFileToTable();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	public void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String code = stringTokenizer.nextToken();
		String name = stringTokenizer.nextToken();

		if (code.startsWith(LoadAlmeidaCurricularCoursesCodesFromFileToTable.ONE_SPACE)) {
			code = code.substring(1);
		}

		Almeida_curricular_course almeida_curricular_course = new Almeida_curricular_course();
		almeida_curricular_course.setId_internal(loader.numberElementsWritten + 1);
		almeida_curricular_course.setCode(code);
		almeida_curricular_course.setName(name);
		writeElement(almeida_curricular_course);
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

}