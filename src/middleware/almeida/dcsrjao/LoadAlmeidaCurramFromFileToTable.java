package middleware.almeida.dcsrjao;

import java.util.StringTokenizer;

import middleware.almeida.Almeida_curram;
import middleware.almeida.LoadDataFile;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadAlmeidaCurramFromFileToTable extends LoadDataFile {

	private static LoadAlmeidaCurramFromFileToTable loader = null;
	private static String logString = "";

	public LoadAlmeidaCurramFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadAlmeidaCurramFromFileToTable();
		}

		loader.load();
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
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

		Almeida_curram almeida_curram = new Almeida_curram();
		almeida_curram.setCodint(loader.numberElementsWritten + 1);
		almeida_curram.setCodcur((new Integer(curso)).longValue());
		if (ramo != null) {
			almeida_curram.setCodram((new Integer(ramo)).longValue());
		}
		if (orientacao != null) {
			almeida_curram.setCodorien((new Integer(orientacao)).longValue());
		}
		almeida_curram.setDescri(descricao);

		writeElement(almeida_curram);
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
}