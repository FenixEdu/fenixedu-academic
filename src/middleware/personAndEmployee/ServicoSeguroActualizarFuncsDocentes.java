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
import Dominio.IEmployeeNotTeacher;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.teacher.Category;
import Dominio.teacher.ICategory;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ServicoSeguroActualizarFuncsDocentes
{

	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncsDocentes(String[] args)
	{
		ficheiro = args[0];
		delimitador = new String("\t");

		/* Inicializar Hashtable com atributos a recuperar do ficheiro de texto requeridos */
		estrutura = new Hashtable();
		estrutura.put("numeroDocumentoIdentificacao", new Object()); //String
		estrutura.put("tipoDocumentoIdentificacao", new Object()); //int
		estrutura.put("numeroMecanografico", new Object()); //String
		estrutura.put("grauAcademico", new Object()); //String
		estrutura.put("categoria", new Object()); //String

		/* Inicializar Collection com ordem dos atributos a recuperar do ficheiro de texto */
		ordem = new ArrayList();
		ordem.add("numeroDocumentoIdentificacao");
		ordem.add("tipoDocumentoIdentificacao");
		ordem.add("numeroMecanografico");
		ordem.add("grauAcademico");
		ordem.add("categoria");
	}

	/** Executa a actualizacao da tabela FuncNaoDocente na Base de Dados */
	public static void main(String[] args) throws Exception
	{
		new ServicoSeguroActualizarFuncsDocentes(args);
		
		LeituraFicheiroFuncDocente servicoLeitura = new LeituraFicheiroFuncDocente();
		
		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		System.out.println("A converter " + lista.size() + " Docentes ... ");
		int newTeachers = 0;
		int newRoles = 0;

		List persons = new ArrayList();
		
		Iterator iteradorNovo = lista.iterator();
		Integer numeroMecanografico = null;
		String categoria = null;

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
				categoria = (String) instanciaTemporaria.get("categoria");

				//Read The Employee
				Criteria criteria = new Criteria();
				Query query = null;
				IEmployee employee = getEmployee(broker, numeroMecanografico, criteria);

				// Check if Teacher Exists
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("teacherNumber", numeroMecanografico);
				//criteria.addEqualTo("keyPerson", person.getIdInternal());
				query = new QueryByCriteria(Teacher.class, criteria);
				List resultTeacher = (List) broker.getCollectionByQuery(query);

				if (employee == null && resultTeacher.size() == 0)
				{
					throw new Exception(
						"Não encontro o funcionário para o professor " + numeroMecanografico);
				}
				if (employee == null && resultTeacher.size() != 0)
				{
					throw new Exception(
						"Erro ao Ler o Funcionario "
							+ numeroMecanografico
							+ " do docente "
							+ ((ITeacher) resultTeacher.get(0)).getTeacherNumber());
				}

				// Check Category
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("code", categoria);
				query = new QueryByCriteria(Category.class, criteria);
				List resultCategory = (List) broker.getCollectionByQuery(query);
				
				
				ITeacher teacher = null;
				if (resultTeacher.size() == 0)
				{
					teacher = new Teacher();
					teacher.setPerson(employee.getPerson());
					teacher.setTeacherNumber(numeroMecanografico);
					if(resultCategory != null){
						teacher.setCategory((ICategory) resultCategory.get(0));
					}
					System.out.println("New teacher with person: " + numeroMecanografico + "-" + teacher.getPerson().getNome() + "-" + teacher.getPerson().getNumeroDocumentoIdentificacao());
					newTeachers++;
				}
				else if (resultTeacher.size() == 1)
				{
					teacher = (ITeacher) resultTeacher.get(0);
					if ((teacher.getPerson() == null)
						|| (!teacher.getPerson().equals(employee.getPerson())))
					{
						teacher.setPerson(employee.getPerson());
					}
					if ((teacher.getCategory() == null || teacher.getCategory().getCode() == null)
						|| (!teacher.getCategory().getCode().equals(categoria)))
					{
						
						if (resultCategory != null)
						{
							teacher.setCategory((ICategory) resultCategory.get(0));
						}
					}
				}
				else
				{
					System.out.println(
						"\nMais que um professor com o numero mecanografico 	"
							+ numeroMecanografico
							+ "\n");
					continue;
				}
				broker.store(teacher);

				IPersonRole personRole =
					RoleFunctions.readPersonRole(teacher.getPerson(), RoleType.TEACHER, broker);
				if (personRole == null)
				{
					IRole role = RoleFunctions.readRole(RoleType.TEACHER, broker);
					if (role == null)
					{
						throw new Exception("Role Desconhecido !!!");
					}
					else
					{
						teacher.getPerson().getPersonRoles().add(role);
						System.out.println("New role: " + numeroMecanografico + "-" + teacher.getPerson().getNome() + "-" + teacher.getPerson().getNumeroDocumentoIdentificacao());
						newRoles++;
					}
				}

				teacher.getPerson().setUsername("D" + numeroMecanografico);
				broker.store(teacher.getPerson());

				persons.add(teacher.getPerson());
			}
			catch (Exception e)
			{
				System.out.println("\nError Migrating Employee " + numeroMecanografico + "\n");
				e.printStackTrace(System.out);
			}
		}
		broker.commitTransaction();

		broker.beginTransaction();
		Iterator iterator = persons.listIterator();
		while (iterator.hasNext())
		{
			IPessoa pessoa = (IPessoa) iterator.next();

			Criteria criteria = new Criteria();
			Query query = null;

			criteria.addEqualTo("employee.person.idInternal", pessoa.getIdInternal());
			query = new QueryByCriteria(EmployeeNotTeacher.class, criteria);
			List resultEmployeeNotTeacher = (List) broker.getCollectionByQuery(query);

			Iterator iterator2 = resultEmployeeNotTeacher.listIterator();
			while (iterator2.hasNext())
			{
				IEmployeeNotTeacher employeeNotTeacher = (IEmployeeNotTeacher) iterator2.next();
				broker.delete(employeeNotTeacher);
			}
		}
		broker.commitTransaction();
		broker.clearCache();

		System.out.println("New Teachers added : " + newTeachers);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");

	}

	private static IEmployee getEmployee(
		PersistenceBroker broker,
		Integer numeroMecanografico,
		Criteria criteria)
		throws Exception
	{
		Query query;
		criteria.addEqualTo("employeeNumber", numeroMecanografico);
		query = new QueryByCriteria(Employee.class, criteria);
		List resultEmployee = (List) broker.getCollectionByQuery(query);

		if (resultEmployee.size() == 0)
		{
			return null;
			//throw new Exception("Error Reading Existing Employee " + numeroMecanografico);
		}
		return (IEmployee) resultEmployee.get(0);
	}
}