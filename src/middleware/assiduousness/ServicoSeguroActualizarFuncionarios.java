package middleware.assiduousness;

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

import Dominio.Funcionario;
import Dominio.IPersonRole;
import Dominio.Pessoa;
import Dominio.Role;
import Util.LeituraFicheiroFuncionario;
import Util.RoleType;

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
	public static void main(String[] args) throws Exception {
		ServicoSeguroActualizarFuncionarios servico = new ServicoSeguroActualizarFuncionarios(args);		
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		
		LeituraFicheiroFuncionario servicoLeitura = new LeituraFicheiroFuncionario();

		lista = servicoLeitura.lerFicheiro(args[0]/*ficheiro*/, delimitador, estrutura, ordem);

		Funcionario funcionario2Write = null;
		Integer numeroMecanografico = null;
		IPersonRole newRole = null;
		
		int newEmployees = 0;
		int newRoles = 0;
		
		broker.beginTransaction();

		try {


			/* ciclo para percorrer a Collection de Funcionarios */
			/* algoritmo */
	
			/* Recuperar registos */
			
	
			/* Procurar chavePessoa correspondente e criar funcionario */
			Iterator iteradorNovo = lista.iterator();

			System.out.println("Migrating " + lista.size() + " Employees ...");

			while (iteradorNovo.hasNext()) {
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();
	
				String numeroDocumentoIdentificacao = (String) instanciaTemporaria.get("numeroDocumentoIdentificacao");
				Integer tipoDocumentoIdentificacao = (Integer) instanciaTemporaria.get("tipoDocumentoIdentificacao");
				numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));
	
				// Check if the Employee Exists
				
				Criteria criteria = new Criteria();
				Query query = null;
			
				criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
				query = new QueryByCriteria(Funcionario.class,criteria);
				List resultFuncionario = (List) broker.getCollectionByQuery(query);	
	
				// Read The Corresponding Person
				criteria = new Criteria();
				query = null;
			
				criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
				criteria.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao);
				query = new QueryByCriteria(Pessoa.class,criteria);
				List resultPerson = (List) broker.getCollectionByQuery(query);	

	
				if (resultPerson.size() == 0){
					throw new Exception("Erro a Ler a pessoa do Funcionario " + numeroMecanografico);
				}
				Pessoa person = (Pessoa) resultPerson.get(0);

				if (resultFuncionario.size() == 0){
					
					
					// Check If the person already has a employee associated
					
					criteria = new Criteria();
					query = null;
			
					criteria.addEqualTo("chavePessoa", person.getIdInternal());
					query = new QueryByCriteria(Funcionario.class,criteria);
					resultFuncionario = (List) broker.getCollectionByQuery(query);	
					
					if (resultFuncionario.size() == 0){
						funcionario2Write = new Funcionario();
						funcionario2Write.setNumeroMecanografico(numeroMecanografico.intValue());
						funcionario2Write.setChavePessoa(person.getIdInternal().intValue());

					} else {
						// Decision Time ... Keep The Old NumeroMecanografico or Put The new ?
						funcionario2Write = (Funcionario) resultFuncionario.get(0);
						
						// Keep the higher 
						if (funcionario2Write.getNumeroMecanografico() < numeroMecanografico.intValue()){
							funcionario2Write.setNumeroMecanografico(numeroMecanografico.intValue());
						}
					}

					broker.store(funcionario2Write);
					newEmployees++;

				} 
				
				// Change the person Username
				person.setUsername("F" + String.valueOf(numeroMecanografico));

				IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.EMPLOYEE, broker);
				if (personRole == null){
						
					criteria = new Criteria();
					criteria.addEqualTo("roleType", RoleType.EMPLOYEE);
			
					query = new QueryByCriteria(Role.class, criteria);
					List result = (List) broker.getCollectionByQuery(query);
			
					Role roleBD = null;
			
					if (result.size() == 0)
						throw new Exception("Unknown Role !!!");
					else roleBD = (Role) result.get(0);
			 
					person.getPersonRoles().add(roleBD);
					newRoles++;
				}
				broker.store(person);					
			}
			
		} catch(Exception e) {
			throw new Exception("\nError Migrating Employee " + numeroMecanografico + "\n" + e);
		}
		broker.commitTransaction();
		System.out.println("New Funcionarios added : " + newEmployees);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");

	}
}
