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
public class LoadNacionalidades extends LoadDataFile {

	private static LoadNacionalidades loader = null;

	private LoadNacionalidades() {
	}

	public static void main(String[] args) {
		loader = new LoadNacionalidades();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

		String codigo = stringTokenizer.nextToken();
		String descricao = stringTokenizer.nextToken();

		Almeida_nacionalidade almeida_nacionalidade = new Almeida_nacionalidade();
		almeida_nacionalidade.setNome(descricao);
		almeida_nacionalidade.setNumero((new Integer(codigo)).longValue());

		writeElement(almeida_nacionalidade);
	}

	protected String getFilename() {
		return "etc/migration/NACIONALIDADES.TXT";
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