/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.StringTokenizer;

import middleware.LoadDataFile;
import middleware.almeida.Almeida_curram;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadRamos extends LoadDataFile {

	PersistentObjectOJBReader persistentObjectOJB = null;

	static String bufferToWrite = new String();

	private static LoadRamos loader = null;

	private LoadRamos() {
		super();
		persistentObjectOJB = new PersistentObjectOJBReader();
	}

	public static void main(String[] args) {
		loader = new LoadRamos();
		loader.load();

		//loader.writeToFile(bufferToWrite);
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

		return "";
	}

}