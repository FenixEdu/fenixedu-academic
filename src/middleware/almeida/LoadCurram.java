/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.StringTokenizer;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadCurram extends LoadDataFile {

	private static LoadCurram loader = null;

	private LoadCurram() {
	}

	public static void main(String[] args) {
		loader = new LoadCurram();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

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
		return "etc/migration/CURRAM.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	/* (non-Javadoc)
	 * @see middleware.almeida.LoadDataFile#getFilenameOutput()
	 */
	protected String getFilenameOutput() {
		
		return null;
	}

}