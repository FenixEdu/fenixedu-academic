package middleware.personAndEmployee;

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

import Dominio.Employee;
import Dominio.EmployeeNotTeacher;
import Dominio.IEmployee;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import Util.RoleType;

/**
 * @author Ivo Brandão
 */
public class ServicoSeguroActualizarFuncsNaoDocentes extends ServicoSeguroSuperMigracaoPessoas
{
	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncsNaoDocentes(String[] args) throws NotExecuteException
	{
		String path;
		try
		{
			path = readPathFile();
		} catch (NotExecuteException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		ficheiro = path.concat(args[0]);
		
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
	public static void main(String[] args) throws Exception
	{
		new ServicoSeguroActualizarFuncsNaoDocentes(args);
		LeituraFicheiroFuncNaoDocente servicoLeitura = new LeituraFicheiroFuncNaoDocente();

		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		System.out.println("Migrating " + lista.size() + " Funcionarios Nao Docentes ... ");

		int newEmployees = 0;
		int newRoles = 0;

		List persons = new ArrayList();
		EmployeeNotTeacher employeeNotTeacher = null;
		Integer numeroMecanografico = null;

		/* Find the correspond employee and create the employee not teacher */
		Iterator iteradorNovo = lista.iterator();

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();
		while (iteradorNovo.hasNext())
		{
			try
			{
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

				numeroMecanografico =
					new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

				Criteria criteria = new Criteria();
				Query query = null;

				// Check if a Employee exists
				criteria = new Criteria();
				query = null;

				criteria.addEqualTo("employeeNumber", numeroMecanografico);
				query = new QueryByCriteria(Employee.class, criteria);
				List resultEmployee = (List) broker.getCollectionByQuery(query);
				if (resultEmployee.size() == 0)
				{
					throw new Exception("Error Reading Existing Employee");
				}

				//Employee
				IEmployee employee = (IEmployee) resultEmployee.get(0);

				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("keyEmployee", employee.getIdInternal());
				query = new QueryByCriteria(EmployeeNotTeacher.class, criteria);
				List resultEmployeeNotTeacher = (List) broker.getCollectionByQuery(query);

				if (resultEmployeeNotTeacher.size() == 0)
				{
					//Employee not Teacher doesn't exists
					employeeNotTeacher = new EmployeeNotTeacher();
					employeeNotTeacher.setEmployee(employee);
					broker.store(employeeNotTeacher);
					System.out.println("New not teacher employee: " + numeroMecanografico + "-" + employee.getPerson().getNome() + "-" + employee.getPerson().getNumeroDocumentoIdentificacao());
					newEmployees++;

					IPersonRole personRole =
						RoleFunctions.readPersonRole(employee.getPerson(), RoleType.EMPLOYEE, broker);
					if (personRole == null)
					{
						IRole role = RoleFunctions.readRole(RoleType.EMPLOYEE, broker);
						if (role == null)
						{
							throw new Exception("Role Desconhecido !!!");
						}
						else
						{
							employee.getPerson().getPersonRoles().add(role);
							broker.store(employee.getPerson());
							System.out.println("New role: " + numeroMecanografico + "-" + employeeNotTeacher.getEmployee().getPerson().getNome() + "-" + employeeNotTeacher.getEmployee().getPerson().getNumeroDocumentoIdentificacao());						
							newRoles++;
						}
					}
				}
				else if (resultEmployeeNotTeacher.size() > 0)
				{
					//Employee not Teacher exists, then verify the correspond employee
					employeeNotTeacher = (EmployeeNotTeacher) resultEmployeeNotTeacher.get(0);
					if (!employeeNotTeacher.getEmployee().equals(employee))
					{
						employeeNotTeacher.setEmployee(employee);
					}
					broker.store(employeeNotTeacher);
				}

				persons.add(employeeNotTeacher.getEmployee().getPerson());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Error migrating employee " + numeroMecanografico + "\n");
				continue;
				//broker.commitTransaction();
				//throw new Exception("Error migrating employee " + numeroMecanografico + "\n" + e);
			}
		}
		broker.commitTransaction();

		broker.beginTransaction();
		try
		{
			Iterator iterator = persons.listIterator();
			while (iterator.hasNext())
			{
				IPessoa pessoa = (IPessoa) iterator.next();

				Criteria criteria = new Criteria();
				Query query = null;

				criteria.addEqualTo("keyPerson", pessoa.getIdInternal());
				query = new QueryByCriteria(Teacher.class, criteria);
				List resultTeacher = (List) broker.getCollectionByQuery(query);
				Iterator iterator2 = resultTeacher.listIterator();
				while (iterator2.hasNext())
				{
					ITeacher teacher = (ITeacher) iterator2.next();
					broker.delete(teacher);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		broker.commitTransaction();
		broker.clearCache();

		System.out.println("New Funcionarios Nao Docentes added : " + newEmployees);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");
		System.exit(0);
	}
}