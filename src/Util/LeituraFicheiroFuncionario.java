package Util;

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

/**
 * @author Ivo Brandão
 */
public class LeituraFicheiroFuncionario {

	/* sera' que posso ter aqui um Map? */
//	private Hashtable estrutura;
	private Collection ordem;
	private ArrayList lista;
//	private File ficheiro;
	private BufferedReader leitura;
//	private FileWriter escrita;

	/** construtor por defeito */
	public LeituraFicheiroFuncionario() {
	}

	/** retorna uma Collection (ArrayList) */
	/* a ideia e' ter uma ArrayList com Hashtables representando a estrutura necessaria */
	/* os valores para cada chave da Hashtable sao Strings */
	public Collection lerFicheiro(String ficheiroValidas, String delimitador, Hashtable estrutura, Collection ordem) throws NotExecuteException {
//		this.estrutura = estrutura;
		this.ordem = ordem;
		this.lista = new ArrayList();
//		this.ficheiro = null;
		this.leitura = null;
//		this.escrita = null;

		String linhaFicheiro = null;
		Hashtable instancia = null;

		try {
			/* ficheiro com dados de funcionario validos */
			File file =  new  File(ficheiroValidas);
			InputStream ficheiro = new FileInputStream(file);
			leitura = new BufferedReader(new InputStreamReader(ficheiro,"8859_1"), new Long(file.length()).intValue());
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
			/* leitura do ficheiro com dados de funcionario linha a linha */
			try {
				linhaFicheiro = leitura.readLine();
			} catch (IOException e) {
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}

			if ((linhaFicheiro != null) && (linhaFicheiro.length() > 10)) {
				/* aqui construir uma instancia de pessoa e adiciona-la a lista*/
				instancia = new Hashtable();

				instancia = recuperarInstancia(linhaFicheiro, delimitador);

				lista.add(instancia);
			}
		} while ((linhaFicheiro != null));

		return lista;
	}

	/** recuperar os atributos para construir a instancia */
	private Hashtable recuperarInstancia(String linha, String delimitador) {
		StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);
		Hashtable instancia = new Hashtable();


		/* codigo de parsing dos atributos */
		Iterator iterador = ordem.iterator();
		String campo = null;
		while (iterador.hasNext()) {
			/* modificado Fernanda e Tânia */
			campo = (String) iterador.next();
			if (campo.equals("tipoDocumentoIdentificacao")) {
				instancia.put(campo, formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim()));
			} else {
				instancia.put(campo, stringTokenizer.nextToken().trim());
			}
			//instancia.put(iterador.next(), stringTokenizer.nextToken().trim());
		}

		return instancia;
	}

	/** retorna um Integer representando o tipo de documento identificacao,
		 * null em caso de String invalida
		 */
	private static Integer formataTipoDocumentoIdentificacao(String naoFormatado) {
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
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO);
		else if (naoFormatado.equals("05"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM);
		else if (naoFormatado.equals("06"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA);
		else resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		return resultado;
	}
}
