package middleware.personAndEmployee;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import Dominio.CentroCusto;
import Dominio.Funcionario;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.ICentroCustoPersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author  Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroInicializarLocalTrabalhoFuncionarios {

	private static String ficheiroCorreio = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroInicializarLocalTrabalhoFuncionarios(String[] args) {
		ficheiroCorreio = args[0];
		delimitador = new String(";");

		/* Inicializar Hashtable com atributos a recuperar do ficheiro de texto requeridos */
		estrutura = new Hashtable();
		estrutura.put("numeroDocumentoIdentificacao", new Object()); //String
		estrutura.put("tipoDocumentoIdentificacao", new Object()); //int
		estrutura.put("numeroMecanografico", new Object()); //String
		estrutura.put("sigla", new Object()); //String
		estrutura.put("departamento", new Object()); //String
		estrutura.put("seccao1", new Object()); //String
		estrutura.put("seccao2", new Object()); //String

		/* Inicializar Collection com ordem dos atributos a recuperar do ficheiro de texto */
		ordem = new ArrayList();
		ordem.add("numeroDocumentoIdentificacao");
		ordem.add("tipoDocumentoIdentificacao");
		ordem.add("numeroMecanografico");
		ordem.add("sigla");
		ordem.add("departamento");
		ordem.add("seccao1");
		ordem.add("seccao2");
	}

	/** Executa a actualizacao da tabela funcionario e o preenchimento da tabela centro_custo na Base de Dados */
	public static void main(String[] args) throws NotExecuteException {
		new ServicoSeguroInicializarLocalTrabalhoFuncionarios(args);

		LeituraFicheiroFuncionarioCentroCusto servicoLeitura = new LeituraFicheiroFuncionarioCentroCusto();

		lista = servicoLeitura.lerFicheiro(ficheiroCorreio, delimitador, estrutura, ordem);

		/* ciclo para percorrer a Collection de Correio dos Funcionarios */
		/* algoritmo */

		/* Recuperar registos */
		System.out.println("ServicoSeguroInicializarLocalTrabalhoFuncionarios.main:Lista de Resultados..." + lista.size());
		//		Iterator iterador = lista.iterator();
		//		while (iterador.hasNext()) {
		//			System.out.println(iterador.next());
		//		}

		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext()) {
			ICentroCustoPersistente iCentroCustoPersistente = SuportePersistente.getInstance().iCentroCustoPersistente();
			IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
			Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

			Integer numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));
			String sigla = (String) instanciaTemporaria.get("sigla");
			String departamento = (String) instanciaTemporaria.get("departamento");
			String seccao1 = (String) instanciaTemporaria.get("seccao1");
			String seccao2 = (String) instanciaTemporaria.get("seccao2");
			System.out.println("ServicoSeguroInicializarLocalTrabalhoFuncionarios.main:funcionario..." + numeroMecanografico);

			// verifica se o centro de custo ja existe na tabela. caso contrario escreve.
			if (sigla != null) {
				CentroCusto centroCusto = null;
				if ((centroCusto = iCentroCustoPersistente.lerCentroCusto(sigla)) == null) {
					centroCusto = new CentroCusto(sigla, departamento, seccao1, seccao2);
					if (!iCentroCustoPersistente.escreverCentroCusto(centroCusto)) {
						continue;
					}
					centroCusto = iCentroCustoPersistente.lerCentroCusto(sigla);
				}

				Funcionario funcionario = null;
				if ((funcionario = iFuncionarioPersistente.lerFuncionarioSemHistoricoPorNumMecanografico(numeroMecanografico.intValue()))
					== null) {
					continue;
				}

				//data inicio da assiduidade
				Timestamp dataInicioAssiduidade = new Timestamp(0);
				funcionario.setDataInicio(dataInicioAssiduidade);
				if ((dataInicioAssiduidade = iFuncionarioPersistente.lerInicioAssiduidade(numeroMecanografico.intValue())) != null) {
					funcionario.setDataInicio(new Date(dataInicioAssiduidade.getTime()));
				}

				//agora
				Calendar agora = Calendar.getInstance();
				funcionario.setQuando(new Timestamp(agora.getTimeInMillis()));

				//regista historico para local de trabalho do funcionario
				funcionario.setChaveCCLocalTrabalho(new Integer(centroCusto.getCodigoInterno()));
				if (!iFuncionarioPersistente.existeHistoricoCCLocalTrabalho(funcionario)) {
					if (!iFuncionarioPersistente.escreverCCLocalTrabalho(funcionario)) {
						throw new NotExecuteException("error.centroCusto.impossivelEscrever");
					}
				}
			}
		}
	}
}