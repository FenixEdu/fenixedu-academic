package middleware.personAndEmployee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Ivo Brandão
 */
public class LeituraFicheiroFuncNaoDocente
{
	private Collection ordem;
	private ArrayList lista;
	private BufferedReader leitura;

	public LeituraFicheiroFuncNaoDocente()
	{
	}

	public Collection lerFicheiro(
		String ficheiroValidas,
		String delimitador,
		Hashtable estrutura,
		Collection ordem)
		throws NotExecuteException
	{

		this.ordem = ordem;
		this.lista = new ArrayList();
		this.leitura = null;

		File file = null;
		InputStream ficheiro = null;

		String linhaFicheiro = null;
		Hashtable instancia = null;

		try
		{
			file = new File(ficheiroValidas);
			ficheiro = new FileInputStream(file);
			leitura =
				new BufferedReader(
					new InputStreamReader(ficheiro, "8859_1"),
					new Long(file.length()).intValue());
		}
		catch (IOException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}

		// first line with header
		try
		{
			linhaFicheiro = leitura.readLine();
		}
		catch (IOException e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelLer");
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
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}

			if ((linhaFicheiro != null) && (linhaFicheiro.length() > 10))
			{
				// buil a instance and add it to the list
				instancia = new Hashtable();

				instancia = recuperarInstancia(linhaFicheiro, delimitador);

				lista.add(instancia);
			}
		}
		while ((linhaFicheiro != null));

		try
		{
			leitura.close();
			ficheiro.close();
		}
		catch (Exception e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelFechar");
		}

		try
		{
			file.delete();
		}
		catch (Exception e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelApagar");
		}

		return lista;
	}

	private Hashtable recuperarInstancia(String linha, String delimitador)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);
		Hashtable instancia = new Hashtable();

		Iterator iterador = ordem.iterator();

		String campo = null;
		while (iterador.hasNext())
		{
			campo = (String) iterador.next();
			if (campo.equals("tipoDocumentoIdentificacao"))
			{
				instancia.put(
					campo,
						FormatPersonUtils.formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim()));
			}
			else
			{
				instancia.put(campo, stringTokenizer.nextToken().trim());
			}
		}

		return instancia;
	}

	private static Integer formataTipoDocumentoIdentificacao(String naoFormatado)
	{
		Integer resultado = null;

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
}
