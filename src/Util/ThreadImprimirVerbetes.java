package Util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroPessoasGestaoAssiduidade;

/**
 * @author Fernanda Quitério & Tânia Pousão
 *
 */
public class ThreadImprimirVerbetes extends Thread {
	private HttpServletResponse response = null;
	private HttpSession session = null;
	private ArrayList listaFuncionarios = null;
	private Timestamp dataInicio = null;
	private Timestamp dataFim = null;
	private Locale locale = null;

	public ThreadImprimirVerbetes(
		HttpServletResponse response,
		HttpSession session,
		ArrayList listaFuncionarios,
		Timestamp dataInicio,
		Timestamp dataFim,
		Locale locale) {
		this.response = response;
		this.session = session;
		this.listaFuncionarios = listaFuncionarios;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.locale = locale;
	}

	public void run() {
		// vão ser criados 5 ficheiros html com os verbetes dos funcionários
		// enquanto estiverem os verbetes em impressao nao é possivel fazer download do ficheiro zipado
		this.session.setAttribute("verbetesEmImpressao", "true");

		//TODO: corrigir atributos da sessao

		// calcular o número de funcionários que cada thread vai tratar

		int numeroFuncionarios = this.listaFuncionarios.size() / 5;
		int restantesFuncionarios =
			new BigInteger(String.valueOf(this.listaFuncionarios.size())).mod(new BigInteger(String.valueOf(5))).intValue();

		ArrayList listaAlgunsFuncionarios = null;
		ListIterator iterador = this.listaFuncionarios.listIterator();
		ThreadImprimirNVerbetes nVerbetes = null;
		ArrayList listaThreads = new ArrayList();
		//lança 5 threads no maximo que vão criar os ficheiros dos verbetes consoante o numero de funcionarios
		int limiteThreads = 0;
		if(numeroFuncionarios == 0 && restantesFuncionarios != 0){
			limiteThreads = 1;
		} else if(numeroFuncionarios != 0){
			limiteThreads = 5;
		}
		
		for (int j = 1; j <= limiteThreads; j++) {
			// junta os restantes funcionarios da lista ao último ficheiro de verbetes
			if (j == limiteThreads) {
				numeroFuncionarios = numeroFuncionarios + restantesFuncionarios;
			}

			listaAlgunsFuncionarios = new ArrayList();
			for (int n = 0; n < numeroFuncionarios; n++) {
				listaAlgunsFuncionarios.add((Integer) iterador.next());
			}
			// cria a thread que vai construir o ficheiro de verbetes com n funcionarios
			nVerbetes = new ThreadImprimirNVerbetes(this.response, listaAlgunsFuncionarios, this.dataInicio, this.dataFim, locale);
			nVerbetes.setName("verbetes" + "-" + j + "-" + dataInicio.toString().substring(0, dataInicio.toString().indexOf(" ")));
			nVerbetes.start();
			listaThreads.add(nVerbetes);
		}
		/*
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException ie) {
					// nao faz nada
				}*/
		// espera que as threads terminem e introduz o ficheiro criado pela thread num ficheiro zipado,
		// que estará depois disponivel para download
		if (listaThreads.size() > 0) {

			System.out.println("TTTTTTTT: antes de esperar pelas threads");
			try {

				comprimirFicheiros(listaThreads);
			} catch (Exception e) {
				return;
			}
			System.out.println("TTTTTTTT: depois de esperar pelas threads");

			// copia do ficheiro para o servidor de FTP
			//		FtpFicheiro.enviarFicheiro("verbetes-" + dataInicio.toString().substring(0, dataInicio.toString().indexOf(" ")) + ".zip");

			// apartir de agora já podem fazer download do ficheiro mesmo sem receberem o mail		
			this.session.setAttribute("verbetesEmImpressao", "false");

			// apagar os ficheiros criados pelas threads
			File ficheiroApagar = null;
			for (int j = 0; j < 5; j++) {
				try {
					ficheiroApagar =
						new File(
							"verbetes" + "-" + (j + 1) + "-" + dataInicio.toString().substring(0, dataInicio.toString().indexOf(" ")) + ".html");
					ficheiroApagar.delete();
				} catch (Exception e) {
					continue;
				}
			}
			//apagar o ficheiro zipado criado pela thread
			//new File("verbetes-" + dataInicio.toString().substring(0, dataInicio.toString().indexOf(" ")) + ".zip");
			//ficheiroApagar.delete();

			System.out.println("TTTTTTTT: antes de enviar o mail");
			ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
			ServicoSeguroPessoasGestaoAssiduidade servicoSeguroPessoasGestaoAssiduidade =
				new ServicoSeguroPessoasGestaoAssiduidade(servicoAutorizacaoLer);
			boolean erros = false;
			try {

				Executor.getInstance().doIt(servicoSeguroPessoasGestaoAssiduidade);

			} catch (NotAuthorizeException nae) {
				erros = true;
				nae.printStackTrace();
			} catch (NotExecuteException nee) {
				erros = true;
				nee.printStackTrace();
			} catch (PersistenceException pe) {
				erros = true;
				pe.printStackTrace();
			} finally {
				if (erros) {
					this.session.setAttribute("erroThreads", "true");
					System.out.println("TTTTTTTTT - " + this.getName() + ": erro ao ler as pessoas de gestao de assiduidade");
					return;
				}
			}

			try {
				ListIterator iteradorPessoas = servicoSeguroPessoasGestaoAssiduidade.getListaPessoas().listIterator();
				while (iteradorPessoas.hasNext()) {
					Pessoa pessoa = (Pessoa) iteradorPessoas.next();
					//	ciistr1.ist.utl.pt
					if (EMail
						.send(
						//"mail.adm"
					"mail.rnl.ist.utl.pt",
							"assiduidade",
							pessoa.getEmail(),
							"Verbetes de Assiduidade",
							"O ficheiro de verbetes já está disponível no menu Listagens->Ficheiro Verbetes.\n"
								+ "Tem apenas que carregar no botão e salvar o ficheiro no computador.\n"
								+ "O ficheiro encontra-se comprimido, pelo que tem que descomprimi-lo e depois imprimir todos os ficheiros dos verbetes.")) {

						this.session.removeAttribute("erroThreads");

					} else {
						//System.out.println("TTTTTTTTT - " + this.getName() + ": Nao conseguiu enviar o mail.");
						this.session.setAttribute("erroThreads", "true");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("TTTTTTTTT - " + this.getName() + ": Nao conseguiu enviar os mails.");
			}
			System.out.println("valor do atributo da sessao: " + session.getAttribute("verbetesEmImpressao"));
			System.out.println("TTTTTTTTT - " + this.getName() + ": fim da thread");
			return;
		}
	}

	public void comprimirFicheiros(ArrayList listaThreads) throws Exception {
		FileOutputStream nomeFicheiro = null;
		CheckedOutputStream csum = null;
		ZipOutputStream ficheiroZipado = null;

		try {
			nomeFicheiro =
				new FileOutputStream(new File("verbetes-" + dataInicio.toString().substring(0, dataInicio.toString().indexOf(" ")) + ".zip"));
			csum = new CheckedOutputStream(nomeFicheiro, new Adler32());
			ficheiroZipado = new ZipOutputStream(new BufferedOutputStream(csum));

			ListIterator iteradorThreads = listaThreads.listIterator();
			while (iteradorThreads.hasNext()) {
				ThreadImprimirNVerbetes threadNVerbetes = (ThreadImprimirNVerbetes) iteradorThreads.next();
				System.out.println("TTTTTTTT: " + this.getName() + "nome da thread que eu espero: " + threadNVerbetes.getName());
				try {
					if (threadNVerbetes.isAlive()) {
						threadNVerbetes.join();
					}
					// escreve no ficheiro de zip para juntar todos os ficheiros criados pelas threads
					BufferedReader in = new BufferedReader(new FileReader(threadNVerbetes.getName() + ".html"));
					ficheiroZipado.putNextEntry(new ZipEntry(threadNVerbetes.getName() + ".html"));
					int c;
					while ((c = in.read()) != -1)
						ficheiroZipado.write(c);
					in.close();

				} catch (Exception e) {
					continue;
				}
			}
			ficheiroZipado.close();
		} catch (Exception e) {
			// enviar um mail a avisar que correu alguma coisa mal
			return;
		}
	} /* comprimirFicheiros */
}