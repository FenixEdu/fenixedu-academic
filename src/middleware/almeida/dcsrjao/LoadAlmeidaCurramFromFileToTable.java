package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_curram;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadAlmeidaCurramFromFileToTable extends LoadAlmeidaDataToTable {

	private static LoadAlmeidaCurramFromFileToTable loader = null;
	private static String logString = "";
	private static final String ONE_SPACE = " ";

	public LoadAlmeidaCurramFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurramFromFileToTable();
		}

		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		int numberTokens = stringTokenizer.countTokens();

		String curso = stringTokenizer.nextToken();
		String ramo = null;
		String orientacao = null;

		if (numberTokens > 2) {
			ramo = stringTokenizer.nextToken();
		}
		if (numberTokens > 3) {
			orientacao = stringTokenizer.nextToken();
		}
		String descricao = stringTokenizer.nextToken();

		long longCurso = 0;
		long longRamo = 0;
		long longOrient = 0;
		try {
			curso = curso.substring(0, 2);
			longCurso = (new Integer(curso)).longValue();

			if ((ramo != null)) {
				if (!ramo.startsWith(ONE_SPACE)) {
					ramo = ramo.substring(0, 1);
					longRamo = (new Integer(ramo)).longValue();
				}
			}
			if (orientacao != null) {
				if (!ramo.startsWith(ONE_SPACE)) {
					orientacao = orientacao.substring(0, 1);
					longOrient = (new Integer(orientacao)).longValue();
				}
			}
		} catch (NumberFormatException e) {
			loader.numberUntreatableElements++;
			logString += "ERRO: Na linha [" + (numberLinesProcessed + 1) + "] os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
		}
	
		loader.setupDAO();
		if (persistentObjectOJB.readAlmeidaCurramByUnique(longCurso, longRamo, longOrient) == null) {
			Almeida_curram almeida_curram = new Almeida_curram();
			almeida_curram.setCodint(loader.numberElementsWritten + 1);
			almeida_curram.setCodcur(longCurso);
			almeida_curram.setCodram(longRamo);
			almeida_curram.setCodorien(longOrient);
			almeida_curram.setDescri(descricao);
			writeElement(almeida_curram);
		} else {
			logString += "ERRO(linha[" + (numberLinesProcessed + 1) + "]): o Ramo com do curso " + longCurso + " do ramo = " + longRamo + " da orientacao = " + longOrient + " ja existe!\n";
		}
		loader.shutdownDAO();

	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/CURRAM.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadAlmeidaCurramFromFileToTable.txt";
	}
	
	protected String getClassName() {
		return "LoadAlmeidaCurramFromFileToTable";
	}
}