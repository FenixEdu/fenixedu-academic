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
import Dominio.ITeacher;
import Dominio.Pessoa;
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
		ServicoSeguroActualizarFuncsDocentes servico = new ServicoSeguroActualizarFuncsDocentes(args);
		LeituraFicheiroFuncDocente servicoLeitura = new LeituraFicheiroFuncDocente();
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();


		lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

		/* ciclo para percorrer a Collection de Funcionarios */
		/* algoritmo */

		System.out.println("A converter " + lista.size() + " Docentes ... ");
		int newTeachers = 0;
		int newRoles = 0;
		
		broker.beginTransaction();

		/* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
		Iterator iteradorNovo = lista.iterator();
		while (iteradorNovo.hasNext()) {
			Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

			Integer numeroMecanografico = new Integer((String) instanciaTemporaria.get("numeroMecanografico"));
			String numeroDocumentoIdentificacao = (String) instanciaTemporaria.get("numeroDocumentoIdentificacao");
			Integer tipoDocumentoIdentificacao = (Integer) instanciaTemporaria.get("tipoDocumentoIdentificacao");
				

			Criteria criteria = new Criteria();
			Query query = null;
			
			criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
			query = new QueryByCriteria(Funcionario.class,criteria);
			List resultFuncionario = (List) broker.getCollectionByQuery(query);	


			if (resultFuncionario.size() == 0){
				throw new Exception("Error Reading Existing Employee " + numeroMecanografico);
			}
			
			// Read The Corresponding Person
			criteria = new Criteria();
			query = null;
			
			criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
			criteria.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao);
			query = new QueryByCriteria(Pessoa.class,criteria);
			List resultPerson = (List) broker.getCollectionByQuery(query);	

			if (resultPerson.size() == 0){
				throw new Exception("Error Reading Existing Person");
			}
			
			Pessoa person = (Pessoa) resultPerson.get(0);
			
			
			// Check if Teacher Exists

			criteria = new Criteria();
			query = null;
			
			criteria.addEqualTo("teacherNumber", numeroMecanografico);
			criteria.addEqualTo("keyPerson", person.getCodigoInterno());
			query = new QueryByCriteria(Teacher.class,criteria);
			List resultTeacher = (List) broker.getCollectionByQuery(query);	

			if (resultTeacher.size() == 0){
				ITeacher teacher = new Teacher();
				teacher.setPerson(person);
				teacher.setTeacherNumber(numeroMecanografico); 
				broker.store(teacher);
				newTeachers++;
				
				IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.TEACHER, broker);
				if (personRole == null){
					RoleFunctions.giveRole(person, RoleType.TEACHER, broker);
					newRoles++;
				}
			}
		}
		broker.commitTransaction();
		System.out.println("New Teachers added : " + newTeachers);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");

	}

}