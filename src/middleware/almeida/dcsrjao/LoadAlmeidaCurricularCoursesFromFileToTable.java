package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_disc;
import middleware.almeida.LoadDataFile;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaCurricularCoursesFromFileToTable extends LoadDataFile {

	private static final String ONE_SPACE = " ";

	private static LoadAlmeidaCurricularCoursesFromFileToTable loader = null;
	protected static String logString = "";

	public LoadAlmeidaCurricularCoursesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurricularCoursesFromFileToTable();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	public void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		stringTokenizer.nextToken(); // ano lectivo
		String codigoDisciplina = stringTokenizer.nextToken();
		String codigoCurso = stringTokenizer.nextToken();
		String codigoRamo = stringTokenizer.nextToken();
		String ano = stringTokenizer.nextToken();	
		String semestre = stringTokenizer.nextToken();
		String tipoDisciplina = stringTokenizer.nextToken(); // (opção ou não)
		String teorica = stringTokenizer.nextToken().replace(',', '.');
		String pratica = stringTokenizer.nextToken().replace(',', '.');
		String laboratorio = stringTokenizer.nextToken().replace(',', '.');
		String teoricopratica = stringTokenizer.nextToken().replace(',', '.');

		try {
			Integer year = new Integer(ano);
			if( (year.intValue() < 1) || (year.intValue() > 6) ){
				logString += "INFO: Ano " + year + " na linha " + (numberLinesProcessed + 1) + "!\n";
				ano = "1";
			}
			
			Integer semester = new Integer(semestre);
			if( (semester.intValue() < 1) || (semester.intValue() > 2) ){
				logString += "INFO: Semester " + semester + " na linha " + (numberLinesProcessed + 1) + "!\n";
				semestre = "1";
			}
			
			Integer ramo = new Integer(codigoRamo);
			if( (ramo.intValue() < 0) || (ramo.intValue() > 5) ){
				logString += "INFO: Ramo " + ramo + " na linha " + (numberLinesProcessed + 1) + "!\n";
				codigoRamo = "1";
			}
		} catch (NumberFormatException e1) {
			logString += "ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers e/ou Doubles!\n";
		}
		
		if (codigoDisciplina.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			codigoDisciplina = codigoDisciplina.substring(1);
		}
		
		if (teorica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			teorica = teorica.substring(1);
		}
		
		if (pratica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			pratica = pratica.substring(1);
		}

		if (laboratorio.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			laboratorio = laboratorio.substring(1);
		}

		if (teoricopratica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			teoricopratica = teoricopratica.substring(1);
		}
		
		Almeida_disc almeida_disc = new Almeida_disc();
		
		almeida_disc.setCodint(loader.numberElementsWritten + 1);
		almeida_disc.setCoddis(codigoDisciplina);
		Almeida_curricular_course almeida_curricular_course = persistentObjectOJB.readAlmeidaCurricularCourse(codigoDisciplina);
		almeida_disc.setNomedis(almeida_curricular_course.getName());
		
		try {
			almeida_disc.setCodcur((new Integer(codigoCurso)).longValue());
			almeida_disc.setCodram((new Integer(codigoRamo)).longValue());
			almeida_disc.setAnodis((new Integer(ano)).longValue());
			almeida_disc.setSemdis((new Integer(semestre)).longValue());
			almeida_disc.setTeo((new Double(teorica)).doubleValue());
			almeida_disc.setTeopra((new Double(teoricopratica)).doubleValue());
			almeida_disc.setPra((new Double(pratica)).doubleValue());
			almeida_disc.setLab((new Double(laboratorio)).doubleValue());
			almeida_disc.setTipo((new Integer(tipoDisciplina)).longValue());
		} catch (NumberFormatException e) {
			logString += "ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers e/ou Doubles!\n";
		}		
		
		writeElement(almeida_disc);
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/DISCIPLINAS.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadAlmeidaCurricularCoursesFromFileToTable.txt";
	}

}