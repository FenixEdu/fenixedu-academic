package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import Dominio.IPersonRole;
import Dominio.Pessoa;
import Util.LeituraFicheiroFuncNaoDocente;
import Util.RoleType;

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
	public static void main(String[] args) throws Exception {
		ServicoSeguroActualizarFuncsNaoDocentes servico = new ServicoSeguroActualizarFuncsNaoDocentes(args);
		LeituraFicheiroFuncNaoDocente servicoLeitura = new LeituraFicheiroFuncNaoDocente();
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

		broker.clearCache();

		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		System.out.println("Migrating " + lista.size() + " Funcionarios Nao Docentes ... ");

		FuncNaoDocente funcNaoDocente = null;
		Integer numeroMecanografico = null; 
		
		/* ciclo para percorrer a Collection de Funcionarios */
		/* algoritmo */


		/* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
		Iterator iteradorNovo = lista.iterator();
		
		try {
			broker.beginTransaction();
			while (iteradorNovo.hasNext()) {
	
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();
	
				numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));
				String numeroDocumentoIdentificacao = (String) instanciaTemporaria.get("numeroDocumentoIdentificacao");
				Integer tipoDocumentoIdentificacao = (Integer) instanciaTemporaria.get("tipoDocumentoIdentificacao");
	
				Criteria criteria = new Criteria();
				Query query = null;
				
				// Read The Person
				criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
				criteria.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao);
				query = new QueryByCriteria(Pessoa.class,criteria);
				List resultPerson = (List) broker.getCollectionByQuery(query);	
	
				if (resultPerson.size() == 0){
					throw new Exception("Error Reading Existing Person");
				}
				
				Pessoa person = (Pessoa) resultPerson.get(0);
				
				
				// Check if a Employee exists
	
				criteria = new Criteria();
				query = null;
				
				criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
				query = new QueryByCriteria(Funcionario.class,criteria);
				List resultFuncionario = (List) broker.getCollectionByQuery(query);	
	
				if (resultFuncionario.size() == 0){
					throw new Exception("Error Reading Existing Employee");
				}
				
				Funcionario funcionario = (Funcionario) resultFuncionario.get(0);
				
				criteria = new Criteria();
				query = null;
				
				criteria.addEqualTo("chaveFuncionario", new Integer(funcionario.getCodigoInterno()));
				query = new QueryByCriteria(FuncNaoDocente.class,criteria);
				List resultFuncNaoDocente = (List) broker.getCollectionByQuery(query);	
	
	
				if (resultFuncNaoDocente.size() == 0){
					funcNaoDocente = new FuncNaoDocente();
					funcNaoDocente.setChaveFuncionario(funcionario.getCodigoInterno());
					broker.store(funcNaoDocente);
					
					IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.EMPLOYEE, broker);
					if (personRole == null){
						RoleFunctions.giveRole(person, RoleType.EMPLOYEE, broker);
					}
					
				}
			}
			broker.commitTransaction();
		} catch(Exception e) {
			broker.commitTransaction();
			throw new Exception("Error migrating employee " + numeroMecanografico + "\n"+ e);
		}
		System.out.println("  Done !");
	}
}