package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncNaoDocentePersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.LeituraFicheiroFuncNaoDocente;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroActualizarFuncsNaoDocentes {

	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncsNaoDocentes(String[] args) {
		ficheiro = args[0];
		delimitador = new String(";");

		/* Inicializar Hashtable com atributos a recuperar do ficheiro de texto requeridos */
		estrutura = new Hashtable();
		estrutura.put("numeroDocumentoIdentificacao", new Object()); //String
		estrutura.put("tipoDocumentoIdentificacao", new Object()); //int
		estrutura.put("numeroMecanografico", new Object()); //String

		/* Inicializar Collection com ordem dos atributos a recuperar do ficheiro de texto */
		ordem = new ArrayList();
		ordem.add("numeroDocumentoIdentificacao");
		ordem.add("tipoDocumentoIdentificacao");
		ordem.add("numeroMecanografico");
	}

	/** Executa a actualizacao da tabela FuncNaoDocente na Base de Dados */
	public static void main(String[] args) throws NotExecuteException {
		ServicoSeguroActualizarFuncsNaoDocentes servico = new ServicoSeguroActualizarFuncsNaoDocentes(args);
		LeituraFicheiroFuncNaoDocente servicoLeitura = new LeituraFicheiroFuncNaoDocente();

		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		/* ciclo para percorrer a Collection de Funcionarios */
		/* algoritmo */

		/* Recuperar registos */
		System.out.println("ServicoSeguroActualizarFuncsNaoDocentes.main:Lista de Resultados...");
		Iterator iterador = lista.iterator();
		while (iterador.hasNext()) {
			System.out.println(iterador.next());
		}

		/* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext()) {
			IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
			IFuncNaoDocentePersistente iFuncNaoDocentePersistente = SuportePersistente.getInstance().iFuncNaoDocentePersistente();
			Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

			Integer numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

			//ler funcionario correspondente ao nao docente
			Funcionario funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(numeroMecanografico.intValue());
			System.out.println("ServicoSeguroActualizarFuncsNaoDocentes.main:FuncNaoDocente " + numeroMecanografico.intValue());

			if (funcionario != null) {
				System.out.println(
					"ServicoSeguroActualizarFuncsNaoDocentes.main:FuncNaoDocente "
						+ numeroMecanografico.intValue()
						+ " tem Funcionario correspondente.");

				FuncNaoDocente funcNaoDocenteBD =
					iFuncNaoDocentePersistente.lerFuncNaoDocentePorNumMecanografico(numeroMecanografico.intValue());

				if (funcNaoDocenteBD != null) {
					System.out.println(
						"ServicoSeguroActualizarFuncsNaoDocentes.main:FuncNaoDocente "
							+ numeroMecanografico.intValue()
							+ " tem FuncNaoDocente correspondente.");
				} else {
					FuncNaoDocente funcNaoDocente =
						new FuncNaoDocente(iFuncNaoDocentePersistente.ultimoCodigoInterno() + 1, funcionario.getCodigoInterno());

					iFuncNaoDocentePersistente.escreverFuncNaoDocente(funcNaoDocente);

					System.out.println(
						"ServicoSeguroActualizarFuncsNaoDocentes.main:FuncNaoDocente "
							+ numeroMecanografico.intValue()
							+ " nao tem FuncNaoDocente correspondente.");
				}

			} //(funcionario!=null)
			else {
				System.out.println(
					"ServicoSeguroActualizarFuncsNaoDocentes.main:FuncNaoDocente "
						+ numeroMecanografico.intValue()
						+ " nao tem Funcionario correspondente.");
			} //(funcionario==null)

		}

	}

}