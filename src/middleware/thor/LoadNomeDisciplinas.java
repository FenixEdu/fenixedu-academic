/*
 * Created on May 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.StringTokenizer;

import middleware.LoadDataFile;
import middleware.almeida.Almeida_coddisc;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadNomeDisciplinas extends LoadDataFile {

	private static LoadNomeDisciplinas loader = null;

	private LoadNomeDisciplinas() {
		super();
		persistentObjectOJB = new PersistentObjectOJBReader ();
	}

	public static void main(String[] args) {
		loader = new LoadNomeDisciplinas();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

		String codigoDisciplina = stringTokenizer.nextToken();
		String nomeDisciplina = stringTokenizer.nextToken();

		Almeida_coddisc almeida_coddisc = new Almeida_coddisc();
		almeida_coddisc.setCodint(loader.numberElementsWritten + 1);
		almeida_coddisc.setCoddis(codigoDisciplina);
		almeida_coddisc.setNomedis(nomeDisciplina);

		writeElement(almeida_coddisc);
	}

	protected String getFilename() {
		return "etc/migrationNextSemester/NOMEDIS_2003.TXT";
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