package middleware.personAndEmployee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Country;
import Dominio.ICountry;
import Dominio.Pessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.OJB.ObjectFenixOJB;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Ivo Brandão
 *  
 */
public class LeituraFicheiroPessoa extends ObjectFenixOJB
{

	/** construtor por defeito */
	public LeituraFicheiroPessoa()
	{
	}

	/** retorna uma Collection (ArrayList) com instancias de Pessoa */
	public static Collection lerFicheiro(String ficheiroValidas, String delimitador)
	{
		ArrayList listaPessoas = new ArrayList();
		BufferedReader leitura = null;
		String linhaFicheiro = null;
		Pessoa pessoa = null;
		int contadorErros = 0;

		//para escrita dos dados de pessoas invalidas num ficheiro
		File erros = null;
		BufferedWriter escritorErros = null;
		try
		{
			erros = new File(System.getProperty("java.io.tmpdir") + File.separator + "pessoasInvalidas.txt");
			escritorErros = new BufferedWriter(new FileWriter(erros));
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}

		// ficheiro com dados de pessoa validos
		try
		{
			File file = new File(ficheiroValidas);
			InputStream inputStream = new FileInputStream(file);
			leitura =
				new BufferedReader(
					new InputStreamReader(inputStream, "8859_1"),
					new Long(file.length()).intValue());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		//primeira linha contem os cabecalhos, logo ignora-se-a
		try
		{
			linhaFicheiro = leitura.readLine();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		do
		{
			// leitura do ficheiro com dados de pessoa linha a linha
			try
			{
				linhaFicheiro = leitura.readLine();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}

			if ((linhaFicheiro != null) && (linhaFicheiro.length() > 10))
			{
				//construir uma instancia de pessoa e adiciona-la a lista
				pessoa = new Pessoa();

				try
				{
					pessoa = recuperarPessoa(linhaFicheiro, delimitador);
					System.out.println("-->Adiciona Pessoa à lista: " + pessoa.getNome());
					listaPessoas.add(pessoa);
				}
				catch (Exception exception)
				{
					System.out.println("LeituraFicheiroPessoa.lerFicheiro: " + exception.toString());
					exception.printStackTrace(System.out);

					//recuperar dados de numeroDocumentoIdentificacao, tipoDocumentoIdentificacao e
					// nome
					//ou fazer dump de linhaFicheiro
					contadorErros++;
					try
					{
						escritorErros.write(linhaFicheiro);
						escritorErros.newLine();
					}
					catch (Exception exception2)
					{
						System.out.println(
							"LeituraFicheiroPessoa.lerFicheiro:Erro a escrever ficheiro de erros "
								+ exception2.toString());
					}
				}
			}
		}
		while ((linhaFicheiro != null));

		try
		{
			escritorErros.close();
		}
		catch (Exception exception)
		{
			System.out.println("LeituraFicheiroPessoa.lerFicheiro:Erro ao fechar o ficheiro de erros.");
		}

		return listaPessoas;
	}

	/** recuperar os atributos para construir a instancia de pessoa */
	private static Pessoa recuperarPessoa(String linha, String delimitador) throws Exception
	{
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
		//		String username = null;
		//		String password = null;
		String codigoFiscal = new String();

		// codigo de parsing dos atributos com as respectivas formatacoes
		numeroDocumentoIdentificacao = new String(stringTokenizer.nextToken().trim());
		pessoa.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);

		tipoDocumentoIdentificacao =
			formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim());
		pessoa.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(tipoDocumentoIdentificacao.intValue()));

		localEmissaoDocumentoIdentificacao = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalEmissaoDocumentoIdentificacao(
			WordUtils.capitalize(StringUtils.lowerCase(localEmissaoDocumentoIdentificacao)));

		dataEmissaoDocumentoIdentificacao = formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);

		nome = new String(stringTokenizer.nextToken().trim());
		pessoa.setNome(WordUtils.capitalize(StringUtils.lowerCase(nome)));

		sexo = formataSexo(stringTokenizer.nextToken().trim());
		pessoa.setSexo(new Sexo(sexo.intValue()));

		estadoCivil = formataEstadoCivil(stringTokenizer.nextToken().trim());
		pessoa.setEstadoCivil(new EstadoCivil(estadoCivil.intValue()));

		nascimento = formataData(stringTokenizer.nextToken().trim());
		pessoa.setNascimento(nascimento);

		nomePai = new String(stringTokenizer.nextToken().trim());
		pessoa.setNomePai(WordUtils.capitalize(StringUtils.lowerCase(nomePai)));
	
		nomeMae = new String(stringTokenizer.nextToken().trim());
		pessoa.setNomeMae(WordUtils.capitalize(StringUtils.lowerCase(nomeMae)));
	
		nacionalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setNacionalidade(WordUtils.capitalize(StringUtils.lowerCase(nacionalidade)));
	
		freguesiaNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setFreguesiaNaturalidade(
			WordUtils.capitalize(StringUtils.lowerCase(freguesiaNaturalidade)));
	
		concelhoNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setConcelhoNaturalidade(
			WordUtils.capitalize(StringUtils.lowerCase(concelhoNaturalidade)));
	
		distritoNaturalidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setDistritoNaturalidade(
			WordUtils.capitalize(StringUtils.lowerCase(distritoNaturalidade)));
	
		morada = new String(stringTokenizer.nextToken().trim());
		pessoa.setMorada(WordUtils.capitalize(StringUtils.lowerCase(morada)));
	
		localidade = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalidade(WordUtils.capitalize(StringUtils.lowerCase(localidade)));
	
		codigoPostal = new String(stringTokenizer.nextToken().trim());
		pessoa.setCodigoPostal(codigoPostal);

		freguesiaMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setFreguesiaMorada(WordUtils.capitalize(StringUtils.lowerCase(freguesiaMorada)));
	
		concelhoMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setConcelhoMorada(WordUtils.capitalize(StringUtils.lowerCase(concelhoMorada)));
	
		distritoMorada = new String(stringTokenizer.nextToken().trim());
		pessoa.setDistritoMorada(WordUtils.capitalize(StringUtils.lowerCase(distritoMorada)));
	
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
		pessoa.setProfissao(WordUtils.capitalize(StringUtils.lowerCase(profissao)));
	
		pessoa.setUsername(numeroDocumentoIdentificacao);

		pessoa.setPassword(PasswordEncryptor.encryptPassword(numeroDocumentoIdentificacao));

		ICountry country = formataNacionalidadeCompleta(stringTokenizer.nextToken().trim());
		pessoa.setPais(country);
		if(country != null) {
			pessoa.setChavePais(country.getIdInternal());
			pessoa.setNacionalidade(WordUtils.capitalize(StringUtils.lowerCase(country.getNationality())));
		}

		pessoa.getPais().setName(
			WordUtils.capitalize(StringUtils.lowerCase(pessoa.getPais().getName())));
		pessoa.getPais().setNationality(
			WordUtils.capitalize(StringUtils.lowerCase(pessoa.getPais().getNationality())));
	
				localidadeCodigoPostal = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalidadeCodigoPostal(
			WordUtils.capitalize(StringUtils.lowerCase(localidadeCodigoPostal)));
	
		codigoFiscal = new String(stringTokenizer.nextToken().trim());
		pessoa.setCodigoFiscal(WordUtils.capitalize(StringUtils.lowerCase(codigoFiscal)));
	
		// este atributo esta invalido, contem zeros no ficheiro
		dataValidadeDocumentoIdentificacao = formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);

		//ignorar localidade codigo fiscal
		//		codigoFiscal = new String(stringTokenizer.nextToken().trim());
		//		pessoa.setCodigoFiscal(
		//			StringUtils.capitaliseAllWords(
		//				StringUtils.lowerCase(codigoFiscal)));
		stringTokenizer.nextToken();

		return pessoa;
	}

	/**
	 * constroi uma Date a partir de uma String com o formato:0AAAAMMDD retornar uma data invalida para
	 * os casos de 000000000 data invalida -> 1/1/1900
	 */
	private static Date formataData(String naoFormatada)
	{
		Date resultado = null;
		if (naoFormatada != null && !naoFormatada.equals("") && naoFormatada.length() == 8)
		{

			Integer todaData = null;
			try
			{
				todaData = new Integer(naoFormatada);
				if (todaData.intValue() == 0) {
					return null;
				}
			} catch (NumberFormatException nfe) {
				return null;
			}

			try
			{
				Integer anoTexto = new Integer(todaData.toString().substring(0, 4));
				Integer mesTexto = new Integer(todaData.toString().substring(4, 6));
				Integer diaTexto = new Integer(todaData.toString().substring(6, 8));

				Calendar calendario = Calendar.getInstance();
				calendario.setLenient(false);
				calendario.clear();
				calendario.set(anoTexto.intValue(), mesTexto.intValue() - 1, diaTexto.intValue());
				
				resultado = new Date(calendario.getTimeInMillis());
			}
			catch (IllegalArgumentException iae)
			{
				resultado = null;
			}
		}
		return resultado;
	}

	/**
	 * retorna um Integer representando o sexo, null em caso de String invalida
	 */
	private static Integer formataSexo(String naoFormatado)
	{
		Integer resultado = null;

		//trocar estes valores 8 e 9 por constantes
		if (naoFormatado.equalsIgnoreCase(Sexo.FEMININO_STRING) || naoFormatado.equalsIgnoreCase("F"))
			resultado = new Integer(Sexo.FEMININO);
		else if (
			naoFormatado.equalsIgnoreCase(Sexo.MASCULINO_STRING) || naoFormatado.equalsIgnoreCase("M"))
			resultado = new Integer(Sexo.MASCULINO);

		return resultado;
	}

	/**
	 * retorna um Integer representando o estado civil, null em caso de String invalida
	 */
	private static Integer formataEstadoCivil(String naoFormatado)
	{
		Integer resultado = null;

		//trocar estes valores por constantes
		if (naoFormatado == null || naoFormatado.equals(""))
		{
			resultado = new Integer(EstadoCivil.SOLTEIRO);
		}
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.SOLTEIRO_STRING))
			resultado = new Integer(EstadoCivil.SOLTEIRO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.CASADO_STRING))
			resultado = new Integer(EstadoCivil.CASADO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.DIVORCIADO_STRING))
			resultado = new Integer(EstadoCivil.DIVORCIADO);
		else if (
			(naoFormatado.equalsIgnoreCase(EstadoCivil.VIUVO_STRING))
				|| (naoFormatado.equalsIgnoreCase("viuvo")))
			resultado = new Integer(EstadoCivil.VIUVO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.SEPARADO_STRING))
			resultado = new Integer(EstadoCivil.SEPARADO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.UNIAO_DE_FACTO_STRING))
			resultado = new Integer(EstadoCivil.UNIAO_DE_FACTO);
		else
			resultado = new Integer(EstadoCivil.DESCONHECIDO);

		return resultado;

	}

	/**
	 * retorna um Integer representando o tipo de documento identificacao, null em caso de String
	 * invalida
	 */
	private static Integer formataTipoDocumentoIdentificacao(String naoFormatado)
	{
		Integer resultado = null;

		//trocar estes valores por constantes
		if (naoFormatado.equals("00"))
			resultado = new Integer(TipoDocumentoIdentificacao.OUTRO);
		else if (naoFormatado.equals("01"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
		else if (naoFormatado.equals("02"))
			resultado = new Integer(TipoDocumentoIdentificacao.PASSAPORTE);
		else if (naoFormatado.equals("03"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA);
		else if (naoFormatado.equals("04"))
			resultado =
				new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO);
		else if (naoFormatado.equals("05"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM);
		else if (naoFormatado.equals("06"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA);
		else
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		return resultado;
	}

	/** retorna um Integer com o codigoInterno do pais */
	private static ICountry formataNacionalidadeCompleta(String naoFormatada) throws Exception
	{

		if (naoFormatada == null || naoFormatada.equals(""))
		{
			naoFormatada = "1";
		}

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

		Integer chavePais = new Integer(naoFormatada);

		Criteria criteria = new Criteria();
		Query query = null;

		if (chavePais.equals(new Integer(1)))
		{
			criteria.addEqualTo("name", "PORTUGAL");
		}
		else
		{
			criteria.addEqualTo("name", "ESTRANGEIRO");
		}

		query = new QueryByCriteria(Country.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		broker.close();

		if (result.size() == 0)
			throw new Exception("Error Reading Country");
		else
			return (ICountry) result.get(0);

	}

}
