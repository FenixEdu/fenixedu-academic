package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_escola;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadAlmeidaEscolasFromFileToTable extends LoadAlmeidaDataToTable {

	private static LoadAlmeidaEscolasFromFileToTable loader = null;
	private static String logString = "";
	private static final String ONE_SPACE = " ";
	private static final String THREE_SPACES = "   ";
//	private static final String FIVE_SPACES = "      ";
	private static final String FOUR_SPACES = "     ";

	public LoadAlmeidaEscolasFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaEscolasFromFileToTable();
		}

		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		//int numberTokens = stringTokenizer.countTokens();
		String codigo = stringTokenizer.nextToken();
		String nome = null;

		nome = stringTokenizer.nextToken("\n");
//		if (numberTokens > 2) {
//			nome = stringTokenizer.nextToken("\n");
//		} else {
//			nome = stringTokenizer.nextToken();
//		}

		if (nome.startsWith(FOUR_SPACES)) {
			nome = nome.substring(5);
		} else if (nome.startsWith(THREE_SPACES)) {
			nome = nome.substring(4);
		}

		loader.setupDAO();
		if (persistentObjectOJB.readAlmeidaEscolaByUnique(codigo, nome) == null) {
			Almeida_escola almeida_escola = new Almeida_escola();
			almeida_escola.setId_internal(loader.numberElementsWritten + 1);
			almeida_escola.setCode(codigo);
			almeida_escola.setName(nome);
			writeElement(almeida_escola);
		} else {
			logString += "ERRO(linha[" + (numberLinesProcessed + 1) + "]): a universidade com o codigo " + codigo + " e nome = " + nome + " ja existe!\n";
		}
		loader.shutdownDAO();

	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/ESCOLAS.TXT";
	}

	protected String getFieldSeparator() {
		return ONE_SPACE;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}
	protected String getClassName() {
		return "LoadAlmeidaEscolasFromFileToTable";
	}
}