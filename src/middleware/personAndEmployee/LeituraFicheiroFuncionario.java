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

/**
 * @author Ivo Brandão
 */
public class LeituraFicheiroFuncionario {

	private Collection ordem;
	private ArrayList lista;
	private BufferedReader leitura;

	public LeituraFicheiroFuncionario() {
	}

	public Collection lerFicheiro(String ficheiroValidas, String delimitador, Hashtable estrutura, Collection ordem) throws NotExecuteException {
		this.ordem = ordem;
		this.lista = new ArrayList();
		this.leitura = null;
		
		File file = null;
		InputStream ficheiro = null;
		
		String linhaFicheiro = null;
		Hashtable instancia = null;

		try {
			file =  new  File(ficheiroValidas);
			ficheiro = new FileInputStream(file);
			leitura = new BufferedReader(new InputStreamReader(ficheiro,"8859_1"), new Long(file.length()).intValue());
		} catch (IOException e) {
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}

		// first line with header
		try {
			linhaFicheiro = leitura.readLine();
		} catch (IOException e) {
			throw new NotExecuteException("error.ficheiro.impossivelLer");
		}

		do {
			//read file line by line
			try {
				linhaFicheiro = leitura.readLine();
			} catch (IOException e) {
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}

			if ((linhaFicheiro != null) && (linhaFicheiro.length() > 10)) {
				// buil a instance and add it to the list
				instancia = new Hashtable();

				instancia = recuperarInstancia(linhaFicheiro, delimitador);

				lista.add(instancia);
			}
		} while ((linhaFicheiro != null));
		
		try {			
			leitura.close();
			ficheiro.close();
		} catch (Exception e) {
			throw new NotExecuteException("error.ficheiro.impossivelFechar");
		}
		
		try {			
			file.delete();			
		} catch (Exception e) {
			throw new NotExecuteException("error.ficheiro.impossivelApagar");
		}
		return lista;
	}

	private Hashtable recuperarInstancia(String linha, String delimitador) {
		StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);
		Hashtable instancia = new Hashtable();


		Iterator iterador = ordem.iterator();
		String campo = null;
		while (iterador.hasNext()) {
			campo = (String) iterador.next();
			if (campo.equals("tipoDocumentoIdentificacao")) {
				instancia.put(campo, FormatPersonUtils.formataTipoDocumentoIdentificacao(stringTokenizer.nextToken().trim()));
			} else {
				instancia.put(campo, stringTokenizer.nextToken().trim());
			}
		}

		return instancia;
	}
}
