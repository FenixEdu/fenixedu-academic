package middleware.personAndEmployee;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
public class ServicoSeguroInicializarCorreioFuncionarios
{

	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroInicializarCorreioFuncionarios(String[] args)
	{
		ficheiro = "E:/Projectos/_carregamentos/funcionario-correio.dat"; //args[0];
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
	public static void main(String[] args) throws NotExecuteException
	{
		new ServicoSeguroInicializarCorreioFuncionarios(args);

		LeituraFicheiroFuncionarioCentroCusto servicoLeitura =
			new LeituraFicheiroFuncionarioCentroCusto();

		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		System.out.println("ServicoSeguroInicializarCorreioFuncionarios.main:Lista de Resultados...");
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();

		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext())
		{
			try
			{
				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

				Integer numeroMecanografico =
					new Integer((String) instanciaTemporaria.get("numeroMecanografico"));
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
				}
				else
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
					throw new Exception(
						"Erro ao Ler centro de custo do Funcionario " + numeroMecanografico);
				}
				else
				{
					employee = (IEmployee) resultEmployee.get(0);
				}

				//Read hisctoric employee
				employee.fillEmployeeHistoric();
				IEmployeeHistoric employeeHistoric = employee.getEmployeeHistoric();
				if (employeeHistoric == null
					|| employeeHistoric.getMailingCostCenter() == null
					|| !employeeHistoric.getMailingCostCenter().equals(costCenter))
				{
					employeeHistoric = new EmployeeHistoric();
					employeeHistoric.setEmployee(employee);
					employeeHistoric.setMailingCostCenter(costCenter);
					employeeHistoric.setBeginDate(Calendar.getInstance().getTime());
					employeeHistoric.setWhen(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					employeeHistoric.setWho(new Integer(0));

					broker.store(employeeHistoric);
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				continue;
			}
		}
		broker.commitTransaction();
	}
}