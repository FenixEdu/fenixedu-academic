package middleware.personAndEmployee;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CostCenter;
import Dominio.Employee;
import Dominio.EmployeeHistoric;
import Dominio.ICostCenter;
import Dominio.IEmployee;
import Dominio.IEmployeeHistoric;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroInicializarLocalTrabalhoFuncionarios extends ServicoSeguroSuperMigracaoPessoas
{

	private static String ficheiroCorreio = null;

	private static String delimitador;

	private static Hashtable estrutura;

	private static Collection ordem;

	private static Collection lista;

	/** Construtor */
	public ServicoSeguroInicializarLocalTrabalhoFuncionarios(String[] args) throws NotExecuteException
	{

		String path;
		try
		{
			path = readPathFile();
		} catch (NotExecuteException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		ficheiroCorreio = path.concat(args[0]);

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

	/**
	 * Executa a actualizacao da tabela funcionario e o preenchimento da tabela centro_custo na Base de
	 * Dados
	 */
	public static void main(String[] args) throws Exception
	{

		new ServicoSeguroInicializarLocalTrabalhoFuncionarios(args);

		LeituraFicheiroFuncionarioCentroCusto servicoLeitura = new LeituraFicheiroFuncionarioCentroCusto();

		lista = servicoLeitura.lerFicheiro(ficheiroCorreio, delimitador, estrutura, ordem);

		System.out
				.println("ServicoSeguroInicializarLocalTrabalhoFuncionarios.main:Lista de Resultados..."
						+ lista.size());

		int newEmployees = 0;
		Iterator iteradorNovo = lista.iterator();

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();
		while (iteradorNovo.hasNext())
		{
			try
			{
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

				Integer numeroMecanografico = new Integer((String) instanciaTemporaria
						.get("numeroMecanografico"));
				String sigla = (String) instanciaTemporaria.get("sigla");
				String departamento = (String) instanciaTemporaria.get("departamento");
				String seccao1 = (String) instanciaTemporaria.get("seccao1");
				String seccao2 = (String) instanciaTemporaria.get("seccao2");

				ICostCenter costCenter = null;

				// verify if the cost center already exists in data base. If not write it.
				Criteria criteria = new Criteria();
				Query query = null;

				criteria.addEqualTo("code", sigla);
				query = new QueryByCriteria(CostCenter.class, criteria);
				List resultCC = (List) broker.getCollectionByQuery(query);
				if (resultCC.size() == 0)
				{
					costCenter = new CostCenter(sigla, departamento, seccao1, seccao2);
					broker.store(costCenter);
				} else
				{
					costCenter = (CostCenter) resultCC.get(0);
				}

				IEmployee employee = null;
				// Read the employee
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("employeeNumber", numeroMecanografico);
				query = new QueryByCriteria(Employee.class, criteria);
				List resultEmployee = (List) broker.getCollectionByQuery(query);

				if (resultEmployee.size() == 0)
				{
					throw new Exception("Erro Funcionario Não Existe" + numeroMecanografico);
				} else
				{
					employee = (IEmployee) resultEmployee.get(0);
				}

				// Read the employee historic about Working Cost Center
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("keyEmployee", employee.getIdInternal());
				criteria.addNotNull("workingPlaceCostCenter");
				criteria.addIsNull("endDate");
				criteria.addOrderBy("beginDate", false);
				query = new QueryByCriteria(EmployeeHistoric.class, criteria);
				List resultEmployeeHitoric = (List) broker.getCollectionByQuery(query);

				if (resultEmployeeHitoric.size() == 0)
				{
					IEmployeeHistoric employeeHistoric = new EmployeeHistoric();
					employeeHistoric.setEmployee(employee);
					employeeHistoric.setWorkingPlaceCostCenter(costCenter);
					employeeHistoric.setBeginDate(Calendar.getInstance().getTime());
					employeeHistoric.setWhen(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					employeeHistoric.setWho(new Integer(0));

					broker.store(employeeHistoric);

					newEmployees++;
				} else if (resultEmployeeHitoric.size() == 1)
				{
					IEmployeeHistoric employeeHistoric = (IEmployeeHistoric) resultEmployeeHitoric
							.get(0);
					if (!employeeHistoric.getWorkingPlaceCostCenter().equals(costCenter))
					{
						Calendar lastDay = Calendar.getInstance();
						lastDay.add(Calendar.DAY_OF_MONTH, -1);
						employeeHistoric.setEndDate(lastDay.getTime());
						broker.store(employeeHistoric);

						//new cc for mailing cost center
						IEmployeeHistoric newEmployeeHistoric = new EmployeeHistoric();
						newEmployeeHistoric.setEmployee(employee);
						newEmployeeHistoric.setWorkingPlaceCostCenter(costCenter);
						newEmployeeHistoric.setBeginDate(Calendar.getInstance().getTime());
						newEmployeeHistoric.setWhen(new Timestamp(Calendar.getInstance()
								.getTimeInMillis()));
						newEmployeeHistoric.setWho(new Integer(0));

						broker.store(newEmployeeHistoric);

						newEmployees++;
					}
				} else if (resultEmployeeHitoric.size() > 1)
				{
					Date beginDate = null;
					for (int i = 0; i < resultEmployeeHitoric.size(); i++)
					{
						IEmployeeHistoric employeeHistoric = (IEmployeeHistoric) resultEmployeeHitoric
								.get(i);

						//if it's the most recent register, then it isn't valid put end date
						if (i > 0 && employeeHistoric.getEndDate() == null)
						{
							Calendar lastDay = Calendar.getInstance();
							lastDay.setTimeInMillis(beginDate.getTime());
							lastDay.add(Calendar.DAY_OF_MONTH, -1);

							employeeHistoric.setEndDate(lastDay.getTime());
							broker.store(employeeHistoric);
						}

						//keep begin date for next historic put in end date
						beginDate = employeeHistoric.getBeginDate();
					}
				}
			} catch (Exception exception)
			{
				exception.printStackTrace();
				continue;
			}
		}
		broker.commitTransaction();
		broker.clearCache();

		System.out.println("New Employees with  Working Place Cost Center: " + newEmployees);
		System.out.println("  Done !");
	}
}