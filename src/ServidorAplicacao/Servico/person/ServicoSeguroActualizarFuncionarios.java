package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.LeituraFicheiroFuncionario;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroActualizarFuncionarios {

	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncionarios(String[] args) {
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

	/** Executa a actualizacao da tabela Funcionario na Base de Dados */
	public static void main(String[] args) throws NotExecuteException {
		ServicoSeguroActualizarFuncionarios servico = new ServicoSeguroActualizarFuncionarios(args);		
		
		LeituraFicheiroFuncionario servicoLeitura = new LeituraFicheiroFuncionario();

		lista = servicoLeitura.lerFicheiro(args[0]/*ficheiro*/, delimitador, estrutura, ordem);

		/* ciclo para percorrer a Collection de Funcionarios */
		/* algoritmo */

		/* Recuperar registos */
		System.out.println("ServicoSeguroActualizarFuncionarios.main:Lista de Resultados...");
		Iterator iterador = lista.iterator();
		while (iterador.hasNext()) {
			System.out.println(iterador.next());
		}

		/* Procurar chavePessoa correspondente e criar funcionario */
		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext()) {
			IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
			IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
			Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

			String numeroDocumentoIdentificacao = (String) instanciaTemporaria.get("numeroDocumentoIdentificacao");
			Integer tipoDocumentoIdentificacao = new Integer((String) instanciaTemporaria.get("tipoDocumentoIdentificacao"));
			Integer numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

			Pessoa pessoa = iPessoaPersistente.lerPessoa(numeroDocumentoIdentificacao, tipoDocumentoIdentificacao.intValue());

			if (pessoa != null) {
				pessoa.setUsername(numeroMecanografico.toString());
				//pessoa.setPassword(numeroDocumentoIdentificacao);
				iPessoaPersistente.alterarPessoa(pessoa);

				Funcionario funcionario = new Funcionario(0, /* codigoInterno */
				pessoa.getCodigoInterno().intValue(), numeroMecanografico.intValue(), 0 /* chaveHorarioActual */
				);

				Funcionario funcionarioBD = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(numeroMecanografico.intValue());

				if (funcionarioBD != null) {
					funcionario.setCodigoInterno(funcionarioBD.getCodigoInterno());

					System.out.println(
						"ServicoSeguroActualizarFuncionarios.main:Funcionario " + funcionarioBD.getCodigoInterno() + " existente.");

					if (funcionario.equals(funcionarioBD)) {
						System.out.println(
							"ServicoSeguroActualizarFuncionarios.main:Funcionario "
								+ funcionarioBD.getCodigoInterno()
								+ " nao mudou.");
					} else {
						System.out.println(
							"ServicoSeguroActualizarFuncionarios.main:Funcionario " + funcionarioBD.getCodigoInterno() + " mudou.");
						iFuncionarioPersistente.alterarFuncionario(funcionario);
					}
				} //(funcionarioBD!=null)
				else {
					funcionario.setCodigoInterno(iFuncionarioPersistente.ultimoCodigoInterno() + 1);
					iFuncionarioPersistente.escreverFuncionario(funcionario);

					System.out.println(
						"ServicoSeguroActualizarFuncionarios.main:Funcionario "
							+ numeroMecanografico.intValue()
							+ " tem Pessoa correspondente e nao existe.");

				}

			} //(pessoa!=null)
			else {
				System.out.println(
					"ServicoSeguroActualizarFuncionarios.main:Funcionario "
						+ numeroMecanografico.intValue()
						+ " nao tem Pessoa correspondente.");
			}

		}

	}

}
