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
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import Dominio.ICountry;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
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

	public LeituraFicheiroPessoa()
	{
	}

	public static Collection lerFicheiro(String ficheiroValidas, String delimitador)
		throws NotExecuteException
	{
		ArrayList listaPessoas = new ArrayList();
		BufferedReader leitura = null;
		String linhaFicheiro = null;
		Pessoa pessoa = null;
		int contadorErros = 0;

		//write persons with errors in data
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

		// file with persons data
		File file = null;
		InputStream inputStream = null;
		try
		{
			file = new File(ficheiroValidas);
			inputStream = new FileInputStream(file);
			leitura =
				new BufferedReader(
					new InputStreamReader(inputStream, "8859_1"),
					new Long(file.length()).intValue());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		// first line with header
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
			//read file line by line
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
				// buil a instance and add it to the list
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

		try {			
			leitura.close();
			inputStream.close();
		} catch (Exception e) {
			throw new NotExecuteException("error.ficheiro.impossivelFechar");
		}
		
		try {			
			file.delete();			
		} catch (Exception e) {
			throw new NotExecuteException("error.ficheiro.impossivelApagar");
		}
		
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

	private static Pessoa recuperarPessoa(String linha, String delimitador) throws Exception
	{
		StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);
		Pessoa pessoa = new Pessoa();

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
		FormatPersonUtils.formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim());
		pessoa.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(tipoDocumentoIdentificacao.intValue()));

		localEmissaoDocumentoIdentificacao = new String(stringTokenizer.nextToken().trim());
		pessoa.setLocalEmissaoDocumentoIdentificacao(
			WordUtils.capitalize(StringUtils.lowerCase(localEmissaoDocumentoIdentificacao)));

		dataEmissaoDocumentoIdentificacao = FormatPersonUtils.formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);

		nome = new String(stringTokenizer.nextToken().trim());
		pessoa.setNome(WordUtils.capitalize(StringUtils.lowerCase(nome)));

		sexo = FormatPersonUtils.formataSexo(stringTokenizer.nextToken().trim());
		pessoa.setSexo(new Sexo(sexo.intValue()));

		estadoCivil = FormatPersonUtils.formataEstadoCivil(stringTokenizer.nextToken().trim());
		pessoa.setEstadoCivil(new EstadoCivil(estadoCivil.intValue()));

		nascimento = FormatPersonUtils.formataData(stringTokenizer.nextToken().trim());
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

		ICountry country = FormatPersonUtils.formataNacionalidadeCompleta(stringTokenizer.nextToken().trim());
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
		dataValidadeDocumentoIdentificacao = FormatPersonUtils.formataData(stringTokenizer.nextToken().trim());
		pessoa.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);

		//ignorar localidade codigo fiscal
		//		codigoFiscal = new String(stringTokenizer.nextToken().trim());
		//		pessoa.setCodigoFiscal(
		//			StringUtils.capitaliseAllWords(
		//				StringUtils.lowerCase(codigoFiscal)));
		stringTokenizer.nextToken();

		return pessoa;
	}




}
