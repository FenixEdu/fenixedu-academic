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

import Dominio.CentroCusto;
import Dominio.Employee;
import Dominio.EmployeeHistoric;
import Dominio.ICostCenter;
import Dominio.IEmployee;
import Dominio.IEmployeeHistoric;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroInicializarLocalTrabalhoFuncionarios
{

	private static String ficheiroCorreio = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroInicializarLocalTrabalhoFuncionarios(String[] args)
	{
		ficheiroCorreio = "E:/Projectos/_carregamentos/centro-custo-afecto.dat"; //args[0];
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

		LeituraFicheiroFuncionarioCentroCusto servicoLeitura =
			new LeituraFicheiroFuncionarioCentroCusto();

		lista = servicoLeitura.lerFicheiro(ficheiroCorreio, delimitador, estrutura, ordem);

		System.out.println(
			"ServicoSeguroInicializarLocalTrabalhoFuncionarios.main:Lista de Resultados..."
				+ lista.size());

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();

		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext())
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

			criteria.addEqualTo("sigla", sigla);
			query = new QueryByCriteria(CentroCusto.class, criteria);
			List resultCC = (List) broker.getCollectionByQuery(query);
			if (resultCC.size() == 0)
			{
				costCenter = new CentroCusto(sigla, departamento, seccao1, seccao2);
				broker.store(costCenter);
				
				//read the new cost center
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("sigla", sigla);
				query = new QueryByCriteria(CentroCusto.class, criteria);
				List resultCCNew = (List) broker.getCollectionByQuery(query);
				if(resultCCNew.size() == 0) {
					throw new Exception("Erro a Ler centro de custo do Funcionario " + numeroMecanografico);
				}
				else
				{
					costCenter = (CentroCusto) resultCCNew.get(0);
				}
			}
			else
			{
				costCenter = (CentroCusto) resultCC.get(0);
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
				continue;
			}
			else
			{
				employee = (IEmployee) resultEmployee.get(0);
			}

			//Read hisctoric employee
			IEmployeeHistoric employeeHistoric = employee.getEmployeeHistoric();
			if(employeeHistoric == null || employeeHistoric.getWorkingPlaceCostCenter() == null
			|| !employeeHistoric.getWorkingPlaceCostCenter().equals(costCenter)){
				employeeHistoric = new EmployeeHistoric();
				employeeHistoric.setEmployee(employee);
				employeeHistoric.setWorkingPlaceCostCenter(costCenter);
				employeeHistoric.setBeginDate(Calendar.getInstance().getTime());
				employeeHistoric.setWhen(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				employeeHistoric.setWho(new Integer(0));
				
				broker.store(employeeHistoric);
			} 		
		}
	}
}