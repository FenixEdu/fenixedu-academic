package middleware.almeida.dcsrjao;

import java.util.HashMap;
import java.util.StringTokenizer;

import middleware.almeida.Almeida_curricular_course;
import middleware.almeida.Almeida_disc;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadAlmeidaCurricularCoursesFromFileToTable extends LoadAlmeidaDataToTable {

	private static final String ONE_SPACE = " ";

	private static LoadAlmeidaCurricularCoursesFromFileToTable loader = null;
	protected static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";

	public LoadAlmeidaCurricularCoursesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurricularCoursesFromFileToTable();
		}
		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString += error.toString();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());

		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String anoLectivo = stringTokenizer.nextToken();
		String codigoDisciplina = stringTokenizer.nextToken();
		String codigoCurso = stringTokenizer.nextToken();
		String codigoRamo = stringTokenizer.nextToken();
		String anoCurricular = stringTokenizer.nextToken();
		String semestreCurricular = stringTokenizer.nextToken();
		String tipoDisciplina = stringTokenizer.nextToken(); // (opção ou não)
		String teorica = stringTokenizer.nextToken().replace(',', '.');
		String pratica = stringTokenizer.nextToken().replace(',', '.');
		String laboratorio = stringTokenizer.nextToken().replace(',', '.');
		String teoricopratica = stringTokenizer.nextToken().replace(',', '.');
		String creditos = stringTokenizer.nextToken().replace(',', '.');
		String orientacao = stringTokenizer.nextToken();

		if (codigoCurso.startsWith("0")) {
			codigoCurso = codigoCurso.substring(1);
		}
		
		if (creditos.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			creditos = creditos.substring(1);
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

		if (codigoDisciplina.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			codigoDisciplina = codigoDisciplina.substring(1);
		}


		//		if (codigoCurso.equals("1") && codigoRamo.equals("1") && orientacao.equals("0")) {
		//			codigoRamo = "0";
		//		}
		//
		//		if ((codigoCurso.equals("6") || codigoCurso.equals("7") || codigoCurso.equals("9") || codigoCurso.equals("12") || codigoCurso.equals("15") || codigoCurso.equals("17") || codigoCurso.equals("18") || codigoCurso.equals("19") || codigoCurso.equals("20") || codigoCurso.equals("22") || codigoCurso.equals("23"))) {
		//			codigoRamo = "0";
		//		}

		//		if (codigoCurso.equals("4")) {
		//			codigoCurso = "14";
		//		} else if (codigoCurso.equals("51")) {
		//			codigoCurso = "1";
		//		} else if (codigoCurso.equals("53")) {
		//			codigoCurso = "3";
		//		} else if (codigoCurso.equals("54")) {
		//			codigoCurso = "14";
		//		} else if (codigoCurso.equals("64")) {
		//			codigoCurso = "14";
		//		}

		if (orientacao.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			orientacao = "0";
		}

		long longCodigoCurso = 0;
		long longCodigoRamo = 0;
		long longSemestre = 0;
		long executionYear = 0;
		long longAnoCurricular = 0;
		long longOrientacao = 0;
		long longTipo = 0;
		double doubleCreditos = 0;
		double doubleTeorica = 0;
		double doubleTeoricopratica = 0;
		double doublePratica = 0;
		double doubleLaboratorio = 0;

		try {
			Integer semester = new Integer(semestreCurricular);
			if ((semester.intValue() < 1) || (semester.intValue() > 2)) {
				logString += "INFO: Semester " + semester + " na linha " + (numberLinesProcessed + 1) + "!\n";
				semestreCurricular = "1";
			}

			Integer ramo = new Integer(codigoRamo);
			if ((ramo.intValue() < 0) || (ramo.intValue() > 5)) {
				logString += "INFO: Ramo " + ramo + " na linha " + (numberLinesProcessed + 1) + "!\n";
				codigoRamo = "0";
			}

			Integer orientation = new Integer(orientacao);
			if ((orientation.intValue() < 0) || (orientation.intValue() > 2)) {
				logString += "INFO: orientacao " + orientation + " na linha " + (numberLinesProcessed + 1) + "!\n";
				orientacao = "0";
			}

			Integer year = new Integer(anoCurricular);
			if ((year.intValue() < 1) || (year.intValue() > 6)) {
				logString += "INFO: Ano " + year + " na linha " + (numberLinesProcessed + 1) + "!\n";
				anoCurricular = "1";
			}
			
			longCodigoCurso = (new Integer(codigoCurso)).longValue();
			longCodigoRamo = (new Integer(codigoRamo)).longValue();
			longSemestre = (new Integer(semestreCurricular)).longValue();
			executionYear = (new Integer(anoLectivo)).longValue();
			longAnoCurricular = (new Integer(anoCurricular)).longValue();
			longOrientacao = (new Integer(orientacao)).longValue();
			doubleCreditos = (new Double(creditos)).doubleValue();
			doubleTeorica = (new Double(teorica)).doubleValue();
			doubleTeoricopratica = (new Double(teoricopratica)).doubleValue();
			doublePratica = (new Double(pratica)).doubleValue();
			doubleLaboratorio = (new Double(laboratorio)).doubleValue();
			longTipo = (new Integer(tipoDisciplina)).longValue();

		} catch (NumberFormatException e1) {
			loader.numberUntreatableElements++;
			logString += "ERRO: Na linha ["
				+ (numberLinesProcessed + 1)
				+ "] os valores lidos do ficheiro são invalidos para a criação de Integers e/ou Doubles!\n";
		}

		loader.setupDAO();
		//		Almeida_disc x = persistentObjectOJB.readAlmeidaDiscByUnique(codigoDisciplina, longCodigoCurso, longCodigoRamo, longSemestre, executionYear, longAnoCurricular, longOrientacao, doubleCreditos, tipo, doubleTeorica, doubleTeoricopratica, doublePratica, doubleLaboratorio);
		Almeida_disc x =
			persistentObjectOJB.readAlmeidaDiscByUnique(
				codigoDisciplina,
				longCodigoCurso,
				longCodigoRamo,
				longSemestre,
				executionYear,
				longAnoCurricular,
				longOrientacao);
		if (x == null) {
			Almeida_curricular_course almeida_curricular_course = persistentObjectOJB.readAlmeidaCurricularCourseByCode(codigoDisciplina);

			if (almeida_curricular_course == null) {
				logString += "ERRO(linha["
					+ (numberLinesProcessed + 1)
					+ "]): disciplina com "
					+ " codigo = "
					+ codigoDisciplina
					+ " não existe na tabela ALMEIDA_CURRCICULAR_COURSE!\n";
				loader.numberUntreatableElements++;
				loader.shutdownDAO();
				return;
			}

			//			if (creditos.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			//				creditos = creditos.substring(1);
			//			}
			//
			//			if (teorica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			//				teorica = teorica.substring(1);
			//			}
			//
			//			if (pratica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			//				pratica = pratica.substring(1);
			//			}
			//
			//			if (laboratorio.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			//				laboratorio = laboratorio.substring(1);
			//			}
			//
			//			if (teoricopratica.startsWith(LoadAlmeidaCurricularCoursesFromFileToTable.ONE_SPACE)) {
			//				teoricopratica = teoricopratica.substring(1);
			//			}

			//			try {
			//				doubleCreditos = (new Double(creditos)).doubleValue();
			//				doubleTeorica = (new Double(teorica)).doubleValue();
			//				doubleTeoricopratica = (new Double(teoricopratica)).doubleValue();
			//				doublePratica = (new Double(pratica)).doubleValue();
			//				doubleLaboratorio = (new Double(laboratorio)).doubleValue();
			//				longTipo = (new Integer(tipoDisciplina)).longValue();
			//			} catch (NumberFormatException e3) {
			//				loader.numberUntreatableElements++;
			//				logString += "ERRO: Na linha [" + (numberLinesProcessed + 1) + "] os valores lidos do ficheiro são invalidos para a criação de Integers e/ou Doubles!\n";
			//			}

			Almeida_disc almeida_disc = new Almeida_disc();

			almeida_disc.setCodint(loader.numberElementsWritten + 1);
			almeida_disc.setCoddis(codigoDisciplina);
			almeida_disc.setNomedis(almeida_curricular_course.getName());
			almeida_disc.setUniversidade(almeida_curricular_course.getUniversityCode());

			almeida_disc.setCodcur(longCodigoCurso);
			almeida_disc.setCodram(longCodigoRamo);
			almeida_disc.setAnodis(longAnoCurricular);
			almeida_disc.setSemdis(longSemestre);
			almeida_disc.setTeo(doubleTeorica);
			almeida_disc.setTeopra(doubleTeoricopratica);
			almeida_disc.setPra(doublePratica);
			almeida_disc.setLab(doubleLaboratorio);
			almeida_disc.setTipo(longTipo);
			almeida_disc.setCredits(doubleCreditos);
			almeida_disc.setOrientation(longOrientacao);
			almeida_disc.setAnoLectivo(executionYear);

			writeElement(almeida_disc);
		} else {
			String temp = "";
			if (longTipo != x.getTipo()) {
				temp += " tipos diferentes, ";
			}

			if (doubleCreditos != x.getCredits()) {
				temp += " creditos diferentes, ";
			}

			if (doubleTeorica != x.getTeo()) {
				temp += " teorica diferentes, ";
			}

			if (doubleTeoricopratica != x.getTeopra()) {
				temp += " teoricopratica diferentes, ";
			}

			if (doublePratica != x.getPra()) {
				temp += " pratica diferentes, ";
			}

			if (doubleLaboratorio != x.getLab()) {
				temp += " laboratorio diferentes, ";
			}

			errorMessage =
				"\n Registo duplicado para a disciplina "
					+ codigoDisciplina
					+ " nome "
					+ x.getNomedis()
					+ " no ano lectivo "
					+ executionYear
					+ "!\n"
					+ "\t 1º registo: "
					+ " Tipo = "
					+ x.getTipo()
					+ " creditos = "
					+ x.getCredits()
					+ " teo = "
					+ x.getTeo()
					+ " teoPrat = "
					+ x.getTeopra()
					+ " Prat = "
					+ x.getPra()
					+ " Lab = "
					+ x.getLab()
					+ "\n\t 2º registo: "
					+ " Tipo = "
					+ longTipo
					+ " creditos = "
					+ doubleCreditos
					+ " teo = "
					+ doubleTeorica
					+ " teoPrat = "
					+ doubleTeoricopratica
					+ " Prat = "
					+ doublePratica
					+ " Lab = "
					+ doubleLaboratorio
					+ "\nlinhas: ";
			errorDBID = (numberLinesProcessed + 1) + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);

			//			logString += "INFO(CC[" + (numberLinesProcessed + 1) + "]): já existe!" + temp + "!\n";
			loader.numberUntreatableElements++;
		}
		loader.shutdownDAO();

	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/DISCIPLINAS.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}

	protected String getClassName() {
		return "LoadAlmeidaCurricularCoursesFromFileToTable";
	}
}