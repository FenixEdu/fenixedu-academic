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
public class LoadStudents extends LoadDataFile {

	private static LoadStudents loader = null;

	private LoadStudents() {
	}

	public static void main(String[] args) {
		loader = new LoadStudents();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeperator());

		// Obter os dados do aluno.
		String numero = stringTokenizer.nextToken();
		String nome = stringTokenizer.nextToken();
		String dataNascimento = stringTokenizer.nextToken();
		String bi = stringTokenizer.nextToken();
		String curso = stringTokenizer.nextToken();
		String ramo = stringTokenizer.nextToken();
		String sexo = stringTokenizer.nextToken();
		String nacionalidade = stringTokenizer.nextToken();
		String freguesiaNaturalidade = stringTokenizer.nextToken();
		String concelhoNaturalidade = stringTokenizer.nextToken();
		String distritoNaturalidade = stringTokenizer.nextToken();
		String nomePai = stringTokenizer.nextToken();
		String nomeMae = stringTokenizer.nextToken();
		String morada = stringTokenizer.nextToken();
		String localidadeMorada = stringTokenizer.nextToken();
		String codigoPostal = stringTokenizer.nextToken();
		String localidadeCodigoPostal = stringTokenizer.nextToken();
		String telefone = stringTokenizer.nextToken();
		String email = stringTokenizer.nextToken();

		Almeida_aluno almeida_aluno = new Almeida_aluno();
		almeida_aluno.setNumero((new Integer(numero)).longValue());
		almeida_aluno.setNome(nome);
		almeida_aluno.setNascimento(convertToJavaDate(dataNascimento));
		almeida_aluno.setBi(bi);
		almeida_aluno.setCurso((new Integer(curso)).longValue());
		almeida_aluno.setRamo((new Integer(ramo)).longValue());
		almeida_aluno.setSexo(sexo);
		almeida_aluno.setNacionalidade(nacionalidade);
		almeida_aluno.setFreguesia(freguesiaNaturalidade);
		almeida_aluno.setConcelho(concelhoNaturalidade);
		almeida_aluno.setDistrito(distritoNaturalidade);
		almeida_aluno.setNomepai(nomePai);
		almeida_aluno.setNomemae(nomeMae);
		almeida_aluno.setMorada(morada);
		almeida_aluno.setLocalidademorada(localidadeMorada);
		almeida_aluno.setCp(codigoPostal);
		almeida_aluno.setLocalidadecp(localidadeCodigoPostal);
		almeida_aluno.setTelefone(telefone);
		almeida_aluno.setEmail(email);

		loader.writeElement(almeida_aluno);
	}

	protected String getFilename() {
		return "etc/migration/ALUNOS.TXT";
	}

	protected String getFieldSeperator() {
		return "\t";
	}

}