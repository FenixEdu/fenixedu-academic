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
import Dominio.IRole;
import Dominio.ITeacher;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.Teacher;
import Util.LeituraFicheiroFuncDocente;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ServicoSeguroActualizarFuncsDocentes {

	private static String ficheiro = null;
	private static String delimitador;
	private static Hashtable estrutura;
	private static Collection ordem;
	private static Collection lista;

	/** Construtor */
	public ServicoSeguroActualizarFuncsDocentes(String[] args) {
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
		new ServicoSeguroActualizarFuncsDocentes(args);
		LeituraFicheiroFuncDocente servicoLeitura = new LeituraFicheiroFuncDocente();
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();

		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		/* ciclo para percorrer a Collection de Funcionarios */
		/* algoritmo */

		System.out.println("A converter " + lista.size() + " Docentes ... ");
		int newTeachers = 0;
		int newRoles = 0;

		/* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
		Iterator iteradorNovo = lista.iterator();
		Integer numeroMecanografico = null;

		broker.beginTransaction();
		while (iteradorNovo.hasNext()) {
			try {

				Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

				numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

				//Read The Employee
				Criteria criteria = new Criteria();
				Query query = null;
				Funcionario funcionario = getFuncionario(broker, numeroMecanografico, criteria);

				// Check if Teacher Exists
				criteria = new Criteria();
				query = null;
				criteria.addEqualTo("teacherNumber", numeroMecanografico);
				//criteria.addEqualTo("keyPerson", person.getIdInternal());
				query = new QueryByCriteria(Teacher.class, criteria);
				List resultTeacher = (List) broker.getCollectionByQuery(query);

				if (funcionario == null && resultTeacher.size() == 0) {
					throw new Exception("Erro ao Ler o Funcionario " + numeroMecanografico);
				}
				if (funcionario == null && resultTeacher.size() != 0) {
					throw new Exception(
						"Erro ao Ler o Funcionario "
							+ numeroMecanografico
							+ " do docente "
							+ ((ITeacher) resultTeacher.get(0)).getTeacherNumber());
				}

				ITeacher teacher = null;
				if (resultTeacher.size() == 0) {
					teacher = new Teacher();
					teacher.setPerson(funcionario.getPerson());
					teacher.setTeacherNumber(numeroMecanografico);
					broker.store(teacher);
					newTeachers++;
				} else if (resultTeacher.size() == 1) {
					teacher = (Teacher) resultTeacher.get(0);
					if (!teacher.getPerson().equals(funcionario.getPerson())) {
						teacher.setPerson(funcionario.getPerson());
					}
					broker.store(teacher);
				}

				IPersonRole personRole = RoleFunctions.readPersonRole((Pessoa) teacher.getPerson(), RoleType.TEACHER, broker);
				if (personRole == null) {
					criteria = new Criteria();
					criteria.addEqualTo("roleType", RoleType.TEACHER);

					query = new QueryByCriteria(Role.class, criteria);
					List result = (List) broker.getCollectionByQuery(query);

					if (result.size() == 0) {
						throw new Exception("Unknown Role !!!");
					} else {
						teacher.getPerson().getPersonRoles().add((IRole) result.get(0));
					}
					newRoles++;
				}
				broker.store(teacher.getPerson());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("\nError Migrating Employee " + numeroMecanografico + "\n");
				//broker.abortTransaction();
				//throw new Exception("\nError Migrating Employee " + numeroMecanografico + "\n" + e);
				continue;
			}
		}
		broker.commitTransaction();

		System.out.println("New Teachers added : " + newTeachers);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");

	}

	private static Funcionario getFuncionario(PersistenceBroker broker, Integer numeroMecanografico, Criteria criteria)
		throws Exception {
		Query query;
		criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
		query = new QueryByCriteria(Funcionario.class, criteria);
		List resultFuncionario = (List) broker.getCollectionByQuery(query);

		if (resultFuncionario.size() == 0) {
			return null;
			//throw new Exception("Error Reading Existing Employee " + numeroMecanografico);
		}
		return (Funcionario) resultFuncionario.get(0);
	}
}