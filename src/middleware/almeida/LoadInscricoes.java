/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.StringTokenizer;

import Dominio.Enrolment;
import Dominio.Frequenta;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadInscricoes extends LoadDataFile {

	private static LoadInscricoes loader = null;

	private LoadInscricoes() {
	}

	public static void main(String[] args) {
		loader = new LoadInscricoes();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeperator());

		String numero = stringTokenizer.nextToken();
		String ano = stringTokenizer.nextToken();
		String semestre = stringTokenizer.nextToken();
		String codigoDisciplina = stringTokenizer.nextToken();
		String anoInscricao = stringTokenizer.nextToken();
		String curso = stringTokenizer.nextToken();
		String ramo = stringTokenizer.nextToken();
		//String nota = stringTokenizer.nextToken();

		Almeida_inscricoes almeida_inscricoes = new Almeida_inscricoes();
		almeida_inscricoes.setCodint(loader.numberElementsWritten + 1);
		almeida_inscricoes.setNumero((new Integer(numero)).longValue());
		almeida_inscricoes.setAno((new Integer(ano)).longValue());
		almeida_inscricoes.setSemestre((new Integer(semestre)).longValue());
		almeida_inscricoes.setCoddis(codigoDisciplina);
		almeida_inscricoes.setAnoinscricao((new Integer(anoInscricao)).longValue());
		almeida_inscricoes.setCurso((new Integer(curso)).longValue());
		almeida_inscricoes.setRamo(ramo);

		//writeElement(almeida_inscricoes);
		processEnrolement(almeida_inscricoes);
	}

	private void processEnrolement(Almeida_inscricoes almeida_inscricoes) {

	}

	protected String getFilename() {
		return "migration/INSCRICOES.TXT";
	}

	protected String getFieldSeperator() {
		return "\t";
	}

}