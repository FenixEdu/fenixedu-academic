package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import Dominio.Paises;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPaisesPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author Ivo Brandão
 * 
 */
public class LeituraFicheiroPessoa {

	/** construtor por defeito */
	public LeituraFicheiroPessoa() {
	}

	/** retorna uma Collection (ArrayList) com instancias de Pessoa */
	public static Collection lerFicheiro(String ficheiroValidas, String delimitador) throws NotExecuteException {
		ArrayList listaPessoas = new ArrayList();
		File ficheiro = null;
		BufferedReader leitura = null;
		String linhaFicheiro = null;
		Pessoa pessoa = null;
		int contadorErros = 0;

		File erros = null;
		BufferedWriter escritor = null;

		System.out.println("-->LeituraFicheiroPessoa.lerFicheiro");

		//para escrita dos dados de pessoas invalidas num ficheiro
		try {
			erros = new File("pessoasInvalidas.txt");
			escritor = new BufferedWriter(new FileWriter(erros));
		} catch (Exception exception) {
			System.out.println("LeituraFicheiroPessoa.lerFicheiro:Erro ao abrir o ficheiro de erros.");
		}

		try {
			/* ficheiro com dados de pessoa validos */
			ficheiro = new File(ficheiroValidas);
			leitura = new BufferedReader(new FileReader(ficheiro), new Long(ficheiro.length()).intValue());
		} catch (IOException e) {
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}

		/* primeira linha contem os cabecalhos */
		try {
			linhaFicheiro = leitura.readLine();
		} catch (IOException e) {
			throw new NotExecuteException("error.ficheiro.impossivelLer");
		}

		do {
			/* leitura do ficheiro com dados de pessoa linha a linha */
			try {
				linhaFicheiro = leitura.readLine();
			} catch (IOException e) {
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}

			if ((linhaFicheiro != null) && (linhaFicheiro.length() > 10)) {
				/* aqui construir uma instancia de pessoa e adiciona-la a lista */
				pessoa = new Pessoa();

				try {
					pessoa = recuperarPessoa(linhaFicheiro, delimitador);
					listaPessoas.add(pessoa);
				} catch (Exception exception) {
					System.out.println("LeituraFicheiroPessoa.lerFicheiro: " + exception.toString());
					//recuperar dados de numeroDocumentoIdentificacao, tipoDocumentoIdentificacao e nome
					//ou fazer dump de linhaFicheiro
					contadorErros++;
					System.out.println("LeituraFicheiroPessoa.lerFicheiro:Erro a recuperar: " + exception.toString());
					try {
						escritor.write(linhaFicheiro);
						escritor.newLine();
					} catch (Exception exception2) {
						System.out.println(
							"LeituraFicheiroPessoa.lerFicheiro:Erro a escrever ficheiro de erros " + exception2.toString());
					}
				}

			}
		} while ((linhaFicheiro != null));

		try {
			escritor.close();
		} catch (Exception exception) {
			System.out.println("LeituraFicheiroPessoa.lerFicheiro:Erro ao fechar o ficheiro de erros.");
		}

		System.out.println("Número de registos inválidos: " + contadorErros);

		return listaPessoas;
	}

	/** recuperar os atributos para construir a instancia de pessoa */
	private static Pessoa recuperarPessoa(String linha, String delimitador) {
		StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);
		Pessoa pessoa = new Pessoa();

		/* atributos */
		String numeroDocumentoIdentificacao = new String();
		Integer tipoDocumentoIdentificacao = new Integer(0);
		String localEmissaoDocumentoIdentificacao = new String();
		Date dataEmissaoDocumentoIdentificacao = null;
		Date dataValidadeDocumentoIdentificacao = null;
		String nome = new String();
		Integer sexo = null;
		Integer estadoCivil = new Integer(0);
		Date nascimento = null;
		String nomePai = new String();
		String nomeMae = new String();
		String nacionalidade = new String();
		String freguesiaNaturalidade = new String();
		String concelhoNaturalidade = new String();
		String distritoNaturalidade = new String();
		String morada = new String();
		String localidade = new String();
		String codigoPostal = new String();
		String localidadeCodigoPostal = new String();
		String freguesiaMorada = new String();
		String concelhoMorada = new String();
		String distritoMorada = new String();
		String telefone = new String();
		String telemovel = new String();
		String email = new String();
		String enderecoWeb = new String();
		String numContribuinte = new String();
		String profissao = new String();
		String username = new String();
		String password = new String();
		Integer nacionalidadeCompleta = new Integer(0);
		String codigoFiscal = new String();

		System.out.println("-->LeituraFicheiroPessoa.recuperarPessoa");

		//parsing do primeiro ;
		//stringTokenizer.nextToken();

		/* codigo de parsing dos atributos */

		/* campo a formatar */
		numeroDocumentoIdentificacao = new String(stringTokenizer.nextToken().trim());
		pessoa.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);

		tipoDocumentoIdentificacao = formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim());
		pessoa.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(tipoDocumentoIdentificacao.intValue()));

		localEmissaoDocumentoIdentificacao = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);

		dataEmissaoDocumentoIdentificacao = formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);

		nome = new String(stringTokenizer.nextToken().trim());
		pessoa.setNome(nome);

		/* campo a formatar */
		sexo = formataSexo(stringTokenizer.nextToken().trim());
		pessoa.setSexo(new Sexo(sexo.intValue()));

		/* campo a formatar */
		estadoCivil = formataEstadoCivil(stringTokenizer.nextToken().trim());
		pessoa.setEstadoCivil(new EstadoCivil(estadoCivil.intValue()));

		nascimento = formataData(stringTokenizer.nextToken().trim());
		pessoa.setNascimento(nascimento);

		nomePai = new String(stringTokenizer.nextToken().trim());
		pessoa.setNomePai(nomePai);

		nomeMae = new String(stringTokenizer.nextToken().trim());
		pessoa.setNomeMae(nomeMae);

		nacionalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setNacionalidade(nacionalidade);

		freguesiaNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setFreguesiaNaturalidade(freguesiaNaturalidade);

		concelhoNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setConcelhoNaturalidade(concelhoNaturalidade);

		distritoNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setDistritoNaturalidade(distritoNaturalidade);

		morada = new String(stringTokenizer.nextToken().trim());
		pessoa.setMorada(morada);

		localidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalidade(localidade);

		codigoPostal = new String(stringTokenizer.nextToken().trim());
		pessoa.setCodigoPostal(codigoPostal);

		freguesiaMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setFreguesiaMorada(freguesiaMorada);

		concelhoMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setConcelhoMorada(concelhoMorada);

		distritoMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setDistritoMorada(distritoMorada);

		telefone = new String(stringTokenizer.nextToken().trim());
		pessoa.setTelefone(telefone);

		telemovel = new String(stringTokenizer.nextToken().trim());
		pessoa.setTelemovel(telemovel);

		email = new String(stringTokenizer.nextToken().trim());
		pessoa.setEmail(email);

		enderecoWeb = new String(stringTokenizer.nextToken().trim());
		pessoa.setEnderecoWeb(enderecoWeb);

		numContribuinte = new String(stringTokenizer.nextToken().trim());
		pessoa.setNumContribuinte(numContribuinte);

		profissao = new String(stringTokenizer.nextToken().trim());
		pessoa.setProfissao(profissao);

		username = new String(stringTokenizer.nextToken().trim());
		//pessoa.setUsername(username);
		pessoa.setUsername(numeroDocumentoIdentificacao);

		password = new String(stringTokenizer.nextToken().trim());
		//pessoa.setPassword(password);
		pessoa.setPassword(numeroDocumentoIdentificacao);

		/* campo a formatar */
		nacionalidadeCompleta = formataNacionalidadeCompleta(stringTokenizer.nextToken().trim());
		//pessoa.setNacionalidadeCompleta(nacionalidadeCompleta.intValue());

		localidadeCodigoPostal = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalidadeCodigoPostal(localidadeCodigoPostal);

		codigoFiscal = new String(stringTokenizer.nextToken().trim());
		pessoa.setCodigoFiscal(codigoFiscal);

		/* este atributo esta invalido, contem zeros no ficheiro */
		dataValidadeDocumentoIdentificacao = formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);

		codigoFiscal = new String(stringTokenizer.nextToken().trim());
		pessoa.setCodigoFiscal(codigoFiscal);

		//ignorar localidade codigo fiscal
		stringTokenizer.nextToken();

		//teste a pessoa lida
		System.out.println("Valores lidos para instancia de pessoa");
		System.out.println(pessoa.toString());

		return pessoa;
	}

	/** constroi uma Date a partir de uma String com o formato:0AAAAMMDD 
	 * retornar uma data invalida para os casos de 000000000
	 * data invalida -> 1/1/1900
	 */
	private static Date formataData(String naoFormatada) {
		System.out.println("Data a formatar: " + naoFormatada);
		Date resultado = null;

		Integer todaData = new Integer(naoFormatada);

		//teste para verificar se se retorna data invalida
		if (todaData.intValue() == 0) {
			resultado = null;
			System.out.println("Data inválida: " + todaData);
			return resultado;
		}
		
		try {
			Integer anoTexto = new Integer(todaData.toString().substring(0, 4));
			Integer mesTexto = new Integer(todaData.toString().substring(4, 6));
			Integer diaTexto = new Integer(todaData.toString().substring(6, 8));

			//construir data com zeros nas horas
			Calendar calendario = Calendar.getInstance();
			calendario.setLenient(false);
			calendario.clear();
			calendario.set(anoTexto.intValue(), mesTexto.intValue() - 1, diaTexto.intValue());
			//      resultado = new Date(anoTexto.intValue()-1900, mesTexto.intValue()-1, diaTexto.intValue());
			resultado = new Date(calendario.getTimeInMillis());
		} catch (IllegalArgumentException iae) {
			resultado = null;
		}
		return resultado;
	}

	/** retorna um Integer representando o sexo,
	 * null em caso de String invalida
	 */
	private static Integer formataSexo(String naoFormatado) {
		Integer resultado = null;

		//trocar estes valores 8 e 9 por constantes
		if (naoFormatado.equals(Sexo.FEMININO_STRING.toLowerCase()))
			resultado = new Integer(Sexo.FEMININO);
		if (naoFormatado.equals(Sexo.MASCULINO_STRING.toLowerCase()))
			resultado = new Integer(Sexo.MASCULINO);

		return resultado;
	}

	/** retorna um Integer representando o estado civil,
	 * null em caso de String invalida
	 */
	private static Integer formataEstadoCivil(String naoFormatado) {
		Integer resultado = null;

		//trocar estes valores por constantes
		if (naoFormatado.equals(EstadoCivil.SOLTEIRO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.SOLTEIRO);
		if (naoFormatado.equals(EstadoCivil.CASADO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.CASADO);
		if (naoFormatado.equals(EstadoCivil.DIVORCIADO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.DIVORCIADO);
		if (naoFormatado.equals(EstadoCivil.VIUVO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.VIUVO);
		if (naoFormatado.equals(EstadoCivil.SEPARADO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.SEPARADO);
		if (naoFormatado.equals(EstadoCivil.UNIAO_DE_FACTO_STRING.toLowerCase()))
			resultado = new Integer(EstadoCivil.UNIAO_DE_FACTO);
		if (naoFormatado.equals("desconhecido"))
			resultado = new Integer(0);

		System.out.println("Estado Civil nao formatado: " + naoFormatado + " formatado: " + resultado);
		return resultado;

	}

	/** retorna um Integer representando o tipo de documento identificacao,
	 * null em caso de String invalida
	 */
	private static Integer formataTipoDocumentoIdentificacao(String naoFormatado) {
		Integer resultado = null;

		//trocar estes valores por constantes
		if (naoFormatado.equals("00"))
			resultado = new Integer(7);
		if (naoFormatado.equals("01"))
			resultado = new Integer(1);
		if (naoFormatado.equals("02"))
			resultado = new Integer(2);
		if (naoFormatado.equals("03"))
			resultado = new Integer(5);
		if (naoFormatado.equals("04"))
			resultado = new Integer(3);
		if (naoFormatado.equals("05"))
			resultado = new Integer(4);
		if (naoFormatado.equals("06"))
			resultado = new Integer(6);

		System.out.println("tipo documento nao formatado: " + naoFormatado + " formatado: " + resultado);

		return resultado;
	}

	/** retorna um Integer com o codigoInterno do pais */
	private static Integer formataNacionalidadeCompleta(String naoFormatada) {
		Integer resultado = null;
		SuportePersistente suportePersistente = SuportePersistente.getInstance();
		IPaisesPersistente paisesPersistente = suportePersistente.iPaisesPersistente();
		Paises pais = null;
		Integer chavePais = new Integer(naoFormatada);

		//ler pais com este codigoPais
		pais = paisesPersistente.lerPaisesCodigoPais(chavePais.intValue());

		//tirar codigoInterno
		if (pais != null) {
			resultado = new Integer(pais.getCodigoInterno());
		}

		System.out.println("pais nao formatado: " + naoFormatada + " chavePais: " + chavePais + " codigoInterno " + resultado);

		return resultado;
	}

}
