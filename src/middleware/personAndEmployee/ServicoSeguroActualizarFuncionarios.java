package middleware.personAndEmployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Employee;
import Dominio.IEmployee;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Util.RoleType;

/**
 * @author Ivo Brandão
 */
public class ServicoSeguroActualizarFuncionarios
{

	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncionarios(String[] args)
	{
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
	public static void main(String[] args) throws Exception
	{
		new ServicoSeguroActualizarFuncionarios(args);
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();

		LeituraFicheiroFuncionario servicoLeitura = new LeituraFicheiroFuncionario();

		String ficheiro = args[0];
		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		Integer numeroMecanografico = null;

		int newEmployees = 0;
		int newRoles = 0;

		List usedPersons = new ArrayList();
		List unusedPersons = new ArrayList();

		broker.beginTransaction();

		/* Find the correspond person and create the employee */
		Iterator iteradorNovo = lista.iterator();

		System.out.println("Migrating " + lista.size() + " Employees ...");

		while (iteradorNovo.hasNext())
		{
			try
			{
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

				String numeroDocumentoIdentificacao =
					(String) instanciaTemporaria.get("numeroDocumentoIdentificacao");
				numeroMecanografico =
					new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

				// Check if the Employee Exists
				Criteria criteria = new Criteria();
				Query query = null;

				criteria.addEqualTo("employeeNumber", numeroMecanografico);
				query = new QueryByCriteria(Employee.class, criteria);
				List resultEmployee = (List) broker.getCollectionByQuery(query);

				// Read The Corresponding Person
				IPessoa person =
					PersonUtils.getPerson(
						numeroMecanografico.toString(),
						numeroDocumentoIdentificacao,
						"F",
						broker);
				if (person == null)
				{
					person =
						PersonUtils.getPerson(
							numeroMecanografico.toString(),
							numeroDocumentoIdentificacao,
							"D",
							broker);
					if (person == null)
					{
						throw new Exception("Erro a Ler a pessoa do Funcionario " + numeroMecanografico);
					}
				}

				IEmployee employee2Write = null;
				if (resultEmployee.size() == 0)
				{
					// The Employee doesn't exists but the person hasn't a employee associated
					query = null;
					criteria = new Criteria();
					criteria.addEqualTo("keyPerson", person.getIdInternal());
					query = new QueryByCriteria(Employee.class, criteria);
					resultEmployee = (List) broker.getCollectionByQuery(query);
					if (resultEmployee.size() == 0)
					{
						employee2Write = new Employee();
						employee2Write.setEmployeeNumber(numeroMecanografico);
						employee2Write.setPerson(person);
						employee2Write.setWorkingHours(new Integer(0));
						employee2Write.setAntiquity(new Date());
						employee2Write.setActive(Boolean.TRUE);
						System.out.println("New employee without person: " + numeroMecanografico + "-" + employee2Write.getPerson().getNome() + "-" + employee2Write.getPerson().getNumeroDocumentoIdentificacao());						
					}
					else
					{
						// The Employee exists but the person already has a employee associated
						// Decision Time ... Keep The Old NumeroMecanografico or Put The new ?
						employee2Write = (IEmployee) resultEmployee.get(0);
						// Keep the higher
						if (employee2Write.getEmployeeNumber().intValue()
							< numeroMecanografico.intValue())
						{
							employee2Write.setEmployeeNumber(numeroMecanografico);
						}
						else
						{
							numeroMecanografico = employee2Write.getEmployeeNumber();
						}

						employee2Write.setActive(Boolean.TRUE);
						System.out.println("New employee with person: " + numeroMecanografico + "-" + employee2Write.getPerson().getNome() + "-" + employee2Write.getPerson().getNumeroDocumentoIdentificacao());												
					}
					broker.store(employee2Write);
					usedPersons.add(person);
					if (unusedPersons.contains(person))
					{
						unusedPersons.remove(person);
					}
					newEmployees++;
				}
				else if (resultEmployee.size() == 1)
				{
					IEmployee employee = (IEmployee) resultEmployee.get(0);
					employee.setActive(Boolean.TRUE);
					if (!employee.getPerson().equals(person))
					{
						if (!usedPersons.contains(employee.getPerson()))
						{
							unusedPersons.add(employee.getPerson());
						}
						employee.setPerson(person);
					}
					broker.store(employee);
				}

				// Change the person Username
				person.setUsername("F" + String.valueOf(numeroMecanografico));

				IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.EMPLOYEE, broker);
				if (personRole == null)
				{

					IRole role = RoleFunctions.readRole(RoleType.EMPLOYEE, broker);
					if (role == null)
					{
						throw new Exception("Role Desconhecido !!!");
					}
					else
					{
						person.getPersonRoles().add(role);
						System.out.println("New role: " + numeroMecanografico + "-" + person.getNome() + "-" + person.getNumeroDocumentoIdentificacao());
						newRoles++;
					}					
				}
				broker.store(person);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("\nError Migrating Employee " + numeroMecanografico + "\n");
				//broker.abortTransaction();
				//throw new Exception("\nError Migrating Employee " + numeroMecanografico + "\n" + e);
				continue;
			}
		}
		broker.commitTransaction();

		broker.beginTransaction();
		for (int i = 0; i < unusedPersons.size(); i++)
		{
			IPessoa person = (IPessoa) unusedPersons.get(i);
			System.out.println(
				"PERSON username: "
					+ person.getUsername()
					+ "- bi:"
					+ person.getNumeroDocumentoIdentificacao());
			broker.delete(person);
		}
		broker.commitTransaction();
		broker.clearCache();

		System.out.println("Persons removed : " + unusedPersons.size());
		System.out.println("New Funcionarios added : " + newEmployees);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");

	}
}
