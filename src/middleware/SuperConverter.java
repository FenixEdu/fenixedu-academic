 
package middleware;


import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.Almeida_aluno;
import middleware.posgrad.Posgrad_aluno_mestrado;
import middleware.posgrad.Posgrad_area_cientifica;
import middleware.posgrad.Posgrad_curso_mestrado;
import middleware.posgrad.Posgrad_disc_area;
import middleware.posgrad.Posgrad_disc_area_aluno;
import middleware.posgrad.Posgrad_disciplina;
import middleware.posgrad.Posgrad_docente;
import middleware.posgrad.Posgrad_guia;
import middleware.posgrad.Posgrad_guia_tabela;
import middleware.posgrad.Posgrad_pagamento_guia;
import middleware.posgrad.Posgrad_pessoa;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.odmg.QueryException;

import Dominio.Branch;
import Dominio.Country;
import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.Employee;
import Dominio.IBranch;
import Dominio.ICountry;
import Dominio.ICurricularCourse;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentKind;
import Dominio.PersonRole;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.Student;
import Dominio.StudentKind;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


public class SuperConverter extends ObjectFenixOJB {

	PersistenceBroker broker = null;
	public SuperConverter(){
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}



public static void main(String args[]) throws Exception{
	// Start Converting
 
 
 	// TODO: Quando se le as pessoas para ver se elas ja existem temos de ler tambem por Username
 	// Para garantir que nao vao haver colisoes

	SuperConverter superConverter = new SuperConverter();

	// Nao esquecer os Roles
	
	
	// Converter Alunos de Licenciatura em Persons e em Students
//	superConverter.migrateAluno2Fenix();
		
	
	// Converter Pessoas de Pos Graduacao em Persons
//	superConverter.migratePosgradPessoa2Fenix();
	
	
	// Converter Alunos de Pos Graduacao em Students 
//	superConverter.migratePosgradAluno2Fenix();
	
	
	// Converte Areas Cientificas
//	superConverter.migratePosgradAreaCientifica2FenixBrach();
	
	
	// Converte Disciplinas
//	superConverter.migratePosGradDisciplina2Fenix();
	
	// Inscricoes do Alunos em Disciplinas
	
	
	// Inscricoes de Alunos em Areas Cientificas
	
	
//	superConverter.updatePasswords();
//	superConverter.updateGraduationStudentsUsernames();
	
	superConverter.updateEmployesRoleAndUsername();
	
	
	}


	public void updatePasswords() throws Exception{
//		List result = null;
//		Query query = null;
//		Criteria criteria = null;
//		QueryByCriteria queryByCriteria = null;
		IPessoa person = null;
		try {
			System.out.print("Reading Persons ...");
			List persons = getFenixPersons();
			System.out.println("  Done !");
			
			System.out.println("Updating " + persons.size() + " Passwords ...");
			Iterator iterator = persons.iterator();
			while(iterator.hasNext()){
				person = (IPessoa) iterator.next();

				person.setPassword(PasswordEncryptor.encryptPassword(person.getNumeroDocumentoIdentificacao()));

				broker.store(person);
				
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Updating " + person.getNome() , e);
		}
	}
	
	
	public void updateGraduationStudentsUsernames() throws Exception{
//		List result = null;
//		Query query = null;
//		Criteria criteria = null;
//		QueryByCriteria queryByCriteria = null;
		IStudent student = null;
		try {
			System.out.print("Reading Students ...");
			List students = getFenixStudents();
			System.out.println("  Done !");
		
			System.out.println("Updating " + students.size() + " Usernames ...");
			Iterator iterator = students.iterator();
			while(iterator.hasNext()){
				student = (IStudent) iterator.next();

				student.getPerson().setUsername("L" + student.getNumber());
				broker.store(student.getPerson());
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Updating " + student.getPerson().getNome() , e);
		}
	}



	public void migratePosgradAreaCientifica2FenixBrach() throws Exception{
		IBranch branch2Write = null;
		Posgrad_area_cientifica areaCientifica = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
//		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("Reading PosGrad Areas Cientificas ...");
			List areasCientificasPG = getAreasCientificas();
			System.out.println("  Done !");
			
			System.out.println("Migrating " + areasCientificasPG.size() + " PosGrad Areas Cientificas to Fenix Branch ...");
			Iterator iterator = areasCientificasPG.iterator();
			while(iterator.hasNext()){
				areaCientifica = (Posgrad_area_cientifica) iterator.next();
				
				// Delete unwanted Courses
				if (areaCientifica.getNome().equals("DISCIPLINAS DE ESCOLHA LIVRE") ||
					areaCientifica.getNome().equals("DISCIPLINAS PROPEDÊUTICAS") ||
					(areaCientifica.getCodigocursomestrado() == 15) ||
					(areaCientifica.getCodigocursomestrado() == 31) ||
					(areaCientifica.getCodigocursomestrado() == 50)) {
						continue;
				}
				branch2Write = new Branch();
				branch2Write.setName(areaCientifica.getNome());
				
				// Read Corresponding Posgrad Degree
				criteria = new Criteria();
				criteria.addEqualTo("codigoInterno", new Integer(String.valueOf(areaCientifica.getCodigocursomestrado())));
				query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() != 1) {
					throw new Exception("Error Reading PosGrad-Curso Mestrado [" + result.size() + "]");
				}
				Posgrad_curso_mestrado posgrad_curso_mestrado = (Posgrad_curso_mestrado) result.get(0);
				
				
				// Read Fenix Degree
				
				criteria = new Criteria();
				criteria.addEqualTo("name", posgrad_curso_mestrado.getNomemestrado());
				criteria.addEqualTo("tipoCurso", new TipoCurso(TipoCurso.MESTRADO));
				query = new QueryByCriteria(Curso.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Fenix Degree [" + result.size() + "]");
				}
				
				Curso degree = (Curso) result.get(0); 
				
				// Read Fenix Degree Curricular Plan
				
				criteria = new Criteria();
				criteria.addEqualTo("degreeKey", degree.getIdInternal());
				query = new QueryByCriteria(DegreeCurricularPlan.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1) {
					throw new Exception("Error Reading Fenix Degree Curricular Plan");
				}
				
				DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0); 
								
				branch2Write.setDegreeCurricularPlan(degreeCurricularPlan);					

				int numOfChars = 1;
				
				// Check if Branch Exists
				
				boolean writableBranch = false;
				while(writableBranch == false){

					// Check if Branch Exists				
										
					criteria = new Criteria();
					branch2Write.setCode(generateCode(areaCientifica.getNome(), ++numOfChars));
					criteria.addEqualTo("name", branch2Write.getName());
					criteria.addEqualTo("code", branch2Write.getCode());
					criteria.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
					query = new QueryByCriteria(Branch.class,criteria);
					result = (List) broker.getCollectionByQuery(query);

					if (result.size() == 0)
						writableBranch = true;
				}
				broker.store(branch2Write);
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Area Cientifica " + areaCientifica.getNome() , e);
			
		}
	}



	public void updateEmployesRoleAndUsername() throws Exception{
		Employee employee = null;
		IPessoa person = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
//		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("A Ler Funcionarios ...");
			List employees = getEmployes();
			System.out.println("  Done !");
	
			System.out.println("A Actualizar Roles e Usernames a " + employees.size() + " Empregados ...");

			Iterator iterator = employees.iterator();
			while(iterator.hasNext()){
				employee = (Employee) iterator.next();


				criteria = new Criteria();
				criteria.addEqualTo("roleType", RoleType.EMPLOYEE);
		
				query = new QueryByCriteria(Role.class, criteria);
				result = (List)broker.getCollectionByQuery(query);
		
				Role role = null;
				if (result.size() == 0)
					throw new Exception("Unknown Role !!!");
				else role = (Role) result.get(0);

				criteria = new Criteria();
				criteria.addEqualTo("codigoInterno", employee.getKeyPerson());
				query = new QueryByCriteria(Pessoa.class,criteria);
				List resultPerson = (List) broker.getCollectionByQuery(query);		
			
				
				
			
				if (resultPerson.size() == 0){
					throw new Exception("Error Reading Person");
				}
			
				person = (IPessoa) resultPerson.get(0);
				person.setUsername("F" + String.valueOf(employee.getEmployeeNumber().intValue()));

				IPersonRole personRole = readPersonRole((Pessoa) person, RoleType.EMPLOYEE);
				if (personRole == null){
					person.getPersonRoles().add(role);												
				}
				broker.store(person);
			
			}
		} catch (Exception e){
			throw new Exception("Error Employee " + employee.getEmployeeNumber().intValue() , e);
		}
	}




	public void migratePosGradDisciplina2Fenix() throws Exception{
		ICurricularCourse curricularCourse2Write = null;
		Posgrad_disciplina curricularCourse2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
//		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("Reading PosGrad Curricular Courses ...");
			List curricularCoursesPG = getDisciplinas();
			System.out.println("  Done !");
			
			System.out.println("Migrating " + curricularCoursesPG.size() + " PosGrad Curricular Courses to Fenix ...");
			Iterator iterator = curricularCoursesPG.iterator();
			while(iterator.hasNext()){
				curricularCourse2Convert = (Posgrad_disciplina) iterator.next();
				

				
				// Delete unwanted courses
				if ((curricularCourse2Convert.getNome().indexOf("CRÉDITOS") != -1) ||
				    (curricularCourse2Convert.getNome().indexOf("CURRICULAR") != -1) ||
				    (curricularCourse2Convert.getCodigocursomestrado() == 15) ||
					(curricularCourse2Convert.getCodigocursomestrado() == 31) ||
					(curricularCourse2Convert.getCodigocursomestrado() == 50)){
					continue;
				}
				
				
				
				// Get the old degree corresponding to this Curricular Course
				
				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(curricularCourse2Convert.getCodigocursomestrado())));
				query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
		
				if (result.size() == 0){
					throw new Exception("Error Reading OLD Degree (" + curricularCourse2Convert.getNome() + ")");
				}

				Posgrad_curso_mestrado oldDegree = (Posgrad_curso_mestrado) result.get(0);
				
				criteria.addEqualTo("nome", oldDegree.getNomemestrado());
				query = new QueryByCriteria(Curso.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() == 0){
					throw new Exception("Error Reading NEW Degree (" + oldDegree.getNomemestrado() + ")");
				}
				
				Curso degree = (Curso) result.get(0);
				

				// Get the Degree Curricular Plan for the new Degree
				
				criteria = new Criteria();
				criteria.addEqualTo("degreeKey", degree.getIdInternal());
				query = new QueryByCriteria(DegreeCurricularPlan.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() == 0){
					System.out.println("Error Reading Degree Curricular Plan (" + degree.getNome() + ")");
					throw new Exception("Cannot Read Fenix Degree Curricular Plan");
				}	
				
				DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0);			
				
				
				// Check to see if the Curricular Course Already Exists
				
				criteria = new Criteria();
				
				// Values By Default
				criteria.addEqualTo("name", curricularCourse2Convert.getNome());
				criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
				query = new QueryByCriteria(CurricularCourse.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() != 0) {
					continue;
				}
				
				curricularCourse2Write = new CurricularCourse();

				
				// Read The Credits

				curricularCourse2Write.setCredits(curricularCourse2Convert.getCreditos());
				
				curricularCourse2Write.setCurricularCourseExecutionScope(CurricularCourseExecutionScope.SEMESTRIAL_OBJ);
				curricularCourse2Write.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse2Write.setDepartmentCourse(null);
				curricularCourse2Write.setLabHours(new Double(0.0));
				curricularCourse2Write.setMandatory(Boolean.TRUE);
				curricularCourse2Write.setName(curricularCourse2Convert.getNome());
				curricularCourse2Write.setPraticalHours(new Double(0.0));
				curricularCourse2Write.setTheoPratHours(new Double(0.0));
				curricularCourse2Write.setTheoreticalHours(new Double(0.0));
				
				// Check the Curricular Course type
				if (curricularCourse2Convert.getNome().indexOf("(M)") != -1){
					curricularCourse2Write.setType(CurricularCourseType.M_TYPE_COURSE_OBJ);
				} else if (curricularCourse2Convert.getNome().indexOf("(P)") != -1){
					curricularCourse2Write.setType(CurricularCourseType.P_TYPE_COURSE_OBJ);
				} else if (curricularCourse2Convert.getNome().indexOf("(D/M)") != -1){
					curricularCourse2Write.setType(CurricularCourseType.DM_TYPE_COURSE_OBJ);
				} else if (curricularCourse2Convert.getNome().indexOf("(A)") != -1){
					curricularCourse2Write.setType(CurricularCourseType.A_TYPE_COURSE_OBJ);
				} else if (curricularCourse2Convert.getNome().indexOf("(M/L)") != -1){
					curricularCourse2Write.setType(CurricularCourseType.ML_TYPE_COURSE_OBJ);
				} else curricularCourse2Write.setType(CurricularCourseType.NORMAL_COURSE_OBJ);
				
				

				// Check if the Curricular Course Exists
				if (curricularCourse2Convert.getSigla() != null){
					curricularCourse2Write.setCode(curricularCourse2Convert.getSigla());
				} else {
					curricularCourse2Write.setCode(generateCode(curricularCourse2Convert.getNome(), 1));
				}
				
				boolean writableCourse = false;
				int i = 1;
				while(!writableCourse){
					
					criteria = new Criteria();
					criteria.addEqualTo("code", curricularCourse2Write.getCode());
					criteria.addEqualTo("name", curricularCourse2Write.getName());
					criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
					query = new QueryByCriteria(CurricularCourse.class,criteria);
					result = (List) broker.getCollectionByQuery(query);
					
					
					
					if (result.size() == 0){
						writableCourse = true;
					} else {
						curricularCourse2Write.setCode(generateCode(curricularCourse2Convert.getNome(), i++));
					}
				}
				
				broker.store(curricularCourse2Write);
				
				
				// Create Scopes
				
//				ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
				
	
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Curricular Course " + curricularCourse2Convert.getNome() , e);
			
		}
	}
	
	
	private String removePontuation(String name){
		String result = new String();
		for (int i = 0; i< name.length(); i++){
			if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Á")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("À")) ||
				(String.valueOf(name.charAt(i)).equalsIgnoreCase("Ã"))){
				result += "A";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Ó")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ò")) ||
			    (String.valueOf(name.charAt(i)).equalsIgnoreCase("Õ"))){
				result += "O";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("É")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("È"))){
				result += "E";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Í")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ì"))){
				result += "I";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Ú")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ù"))){
				result += "U";
			} else {
				result += name.charAt(i);
			}
		}
		return result;
		
	}
	
	
	
	private String generateCode(String name, int numOfChars){
		String result = new String();
		
		String aux = null;

		// Fix the String
		name = removePontuation(name);

		StringTokenizer stringTokenizer = new StringTokenizer(name, " ");
				
		while(stringTokenizer.hasMoreTokens()){
			aux = stringTokenizer.nextToken();
			if ((aux.charAt(0) != '(') && (aux.charAt(0) != '-') && (aux.charAt(0) != '\'') && 
			    (aux.charAt(0) != '´') && (aux.charAt(0) != '`')){
			    	if (aux.length() <= numOfChars)
			    		result += aux;
			    	else
						result += String.valueOf(aux.substring(0, numOfChars));
			}
		}
		return result;
	}
	
	

	public void migratePosgradAluno2Fenix() throws Exception{
		IStudent student2Write = null;
		Posgrad_aluno_mestrado student2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("A Ler Alunos de Pos-Graduacao ...");
			List studentsPG = getAlunosPOS();
			System.out.println("  Done !");
			
			System.out.println("A Converter " + studentsPG.size() + " alunos de Pos-Graduacao para o Fenix ...");

			// Cria informacao sobre um grupo de alunos
			IStudentKind studentGroupInfo = new StudentKind();
			
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
			queryByCriteria = new QueryByCriteria(studentGroupInfo);
			
			result = (List) broker.getCollectionByQuery(queryByCriteria);
			
			if (result.size() == 0){
			
				studentGroupInfo = new StudentKind();			
				studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
				studentGroupInfo.setMaxNACToEnrol(new Integer(10));
				studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
				studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
				broker.store(studentGroupInfo);
			} else {
				studentGroupInfo = (IStudentKind) result.get(0);
			}

			Iterator iterator = studentsPG.iterator();
			while(iterator.hasNext()){
				student2Convert = (Posgrad_aluno_mestrado) iterator.next();
		
				criteria = new Criteria();
				criteria.addEqualTo("number", new Integer(String.valueOf(student2Convert.getNumero())));
				criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.MESTRADO));
				query = new QueryByCriteria(Student.class,criteria);
				List resultStudent = (List) broker.getCollectionByQuery(query);		
		
					
				// Read the person old person
				
				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(student2Convert.getCodigopessoa())));

				query = new QueryByCriteria(Posgrad_pessoa.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		

				if (result.size() != 1)
					throw new Exception("Erro a ler a Pessoa da Pos-Graduacao!");
				
				Posgrad_pessoa personOld = (Posgrad_pessoa) result.get(0);
				
				// Verificar o Tipo de Documento
				TipoDocumentoIdentificacao identificationDocumentType = null;
				if (personOld.getTipodocumentoidentificacao().equalsIgnoreCase("BILHETE DE IDENTIDADE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
				} else if (personOld.getTipodocumentoidentificacao().equalsIgnoreCase("PASSAPORTE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.PASSAPORTE);
				} else identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.OUTRO);

				criteria = new Criteria();
				criteria.addEqualTo("numeroDocumentoIdentificacao",personOld.getNumerodocumentoidentificacao());
				criteria.addEqualTo("tipoDocumentoIdentificacao", identificationDocumentType);
				query = new QueryByCriteria(Pessoa.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() != 1)
					throw new Exception("Erro a ler a Pessoa do Fenix ! BI: " + personOld.getNumerodocumentoidentificacao());
				
				IPessoa person = (IPessoa) result.get(0);


				if (resultStudent.size() == 0){

							
					// Create a new Student
					student2Write = new Student();
					student2Write.setNumber(new Integer(String.valueOf(student2Convert.getNumero())));
					student2Write.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
					student2Write.setPerson(person);
					student2Write.setState(new StudentState(StudentState.INSCRITO));
					student2Write.setStudentKind(studentGroupInfo);
					broker.store(student2Write);
					
					// Give the Student Role (This student may not exist but the person may already be a 
					// Graduate Student) 
					
					IPersonRole personRole = readPersonRole((Pessoa) person, RoleType.STUDENT);
					if (personRole == null){
						giveStudentRole(student2Write);												
					}
					
				} else System.out.println("O Aluno " + student2Convert.getNumero() + " ja existe. ");

				// Update The Username
					
				person.setUsername("M" + student2Convert.getNumero());
				broker.store(person);
		
			}
			System.out.println("  Done !");
		} catch(Exception e) {
			System.out.println("Error converting Student " + student2Convert.getNumero());
			throw new Exception(e);
		}
		
		
	}
	
	
	public IPersonRole readPersonRole(Pessoa person, RoleType roleType) throws Exception {
		
		// Read The Role
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", roleType);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List)broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Unknown Role !!!");
		else role = (Role) result.get(0);
		
		
		criteria = new Criteria();
		criteria.addEqualTo("keyRole", role.getIdInternal());
		criteria.addEqualTo("keyPerson", person.getIdInternal());
		
		query = new QueryByCriteria(PersonRole.class, criteria);
		result = (List) broker.getCollectionByQuery(query);
		
		if (result.size() == 0) return null;
		return (IPersonRole) result.get(0);
	}




	public void migratePosgradPessoa2Fenix() throws Exception{
		IPessoa person2Write = null;
		Posgrad_pessoa person2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		try {
			System.out.print("A Ler Pessoas de Pos-Graduacao ...");
			List pessoasPG = getPessoas();
			System.out.println("  Done !");
			
			System.out.println("A Converter " + pessoasPG.size() + " pessoas de Pos-Graduacao para o Fenix ...");

			Iterator iterator = pessoasPG.iterator();
			while(iterator.hasNext()){
				person2Convert = (Posgrad_pessoa) iterator.next();
				person2Write = new Pessoa();
				// Remoe the PosGrad User
				
				if ((person2Convert.getUsername().equalsIgnoreCase("posgrad2002")) && (person2Convert.getPassword().equalsIgnoreCase("aplica2002")))
					continue;

				// Verificar o Tipo de Documento
				TipoDocumentoIdentificacao identificationDocumentType = null;
				if (person2Convert.getTipodocumentoidentificacao().equalsIgnoreCase("BILHETE DE IDENTIDADE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
				} else if (person2Convert.getTipodocumentoidentificacao().equalsIgnoreCase("PASSAPORTE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.PASSAPORTE);
				} else identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.OUTRO);

				
				criteria = new Criteria();
				criteria.addEqualTo("numeroDocumentoIdentificacao",person2Convert.getNumerodocumentoidentificacao());
				criteria.addEqualTo("tipoDocumentoIdentificacao", identificationDocumentType);
				query = new QueryByCriteria(Pessoa.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() == 0){
					copyPessoaPG2Person(person2Write, person2Convert, identificationDocumentType);
					broker.store(person2Write);
					givePersonRole(person2Write);

				} else {
					person2Write = (IPessoa) result.get(0);
					
					System.out.println("A Pessoa " + person2Write.getNome() + " ja existe.");
					
					// Verificar se a Pessoa e um Funcionario
					criteria = new Criteria();
					
					// A classe funcionario ainda usa os codigos internos por isso temos de procurar pelos codigos internos
					Pessoa personTemp = (Pessoa) person2Write;
					criteria.addEqualTo("keyPerson", personTemp.getIdInternal());
					query = new QueryByCriteria(Employee.class,criteria);
					result = (List) broker.getCollectionByQuery(query);
					
					if (result.size() == 0){
						// A pessoa nao e um funcionario. Entao vamos fazer update dos campos todos
						copyPessoaPG2Person(person2Write, person2Convert, identificationDocumentType);
						broker.store(person2Write);
					}
				}				
			}
			System.out.println("   Success !");
		} catch(Exception e) {
			System.out.println("Erro na Pessoa de Pos-Graduacao : " + person2Convert.getNome());
			throw new Exception(e);
		}	
					
	}
	
	private void copyPessoaPG2Person(IPessoa person2Write, Posgrad_pessoa person2Convert, TipoDocumentoIdentificacao identificationDocumentType) throws Exception{

		try{
			person2Write.setNumeroDocumentoIdentificacao(person2Convert.getNumerodocumentoidentificacao());
			person2Write.setTipoDocumentoIdentificacao(identificationDocumentType);
			person2Write.setLocalEmissaoDocumentoIdentificacao(person2Convert.getLocalemissaodocumentoidentificacao());
			person2Write.setDataEmissaoDocumentoIdentificacao(person2Convert.getDataemissaodocumentoidentificacao());
			person2Write.setNome(person2Convert.getNome());
			person2Write.setNomePai(person2Convert.getNomepai());
			person2Write.setNomeMae(person2Convert.getNomemae());
						
			// Verificar o Sexo
			
			if (person2Convert.getSexo() != null){			
				if (person2Convert.getSexo().equalsIgnoreCase("masculino"))
					person2Write.setSexo(new Sexo(Sexo.MASCULINO));
				else if (person2Convert.getSexo().equalsIgnoreCase("feminino"))
					person2Write.setSexo(new Sexo(Sexo.FEMININO));
				else {
					System.out.println("Erro a converter Pessoa  " + person2Convert.getNome() + ". Erro no SEXO. (Encontrado: " + person2Convert.getSexo() + ")");
				} 
			}						
			// Verificar o Estado Civil
			if (person2Convert.getEstadocivil() != null) {
				if (person2Convert.getEstadocivil().equalsIgnoreCase("solteiro")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.SOLTEIRO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("casado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.CASADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("divorciado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.DIVORCIADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("viúvo")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.VIUVO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("separado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.SEPARADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("união de facto")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.UNIAO_DE_FACTO));
				} else {
					System.out.println("Erro a converter Pessoa  " + person2Convert.getNome() + ". Erro no ESTADO CIVIL. (encontrado: " + person2Convert.getEstadocivil() + ")");
				} 
			}
			
			person2Write.setNascimento(person2Convert.getNascimento());
			person2Write.setNacionalidade(person2Convert.getNacionalidade());
			person2Write.setFreguesiaNaturalidade(person2Convert.getFreguesianaturalidade());
			person2Write.setConcelhoNaturalidade(person2Convert.getConcelhonaturalidade());
			person2Write.setDistritoNaturalidade(person2Convert.getDistritonaturalidade());
			person2Write.setMorada(person2Convert.getMorada());
			person2Write.setLocalidade(person2Convert.getLocalidade());
			person2Write.setCodigoPostal(person2Convert.getCodigopostal());
			person2Write.setFreguesiaMorada(person2Convert.getFreguesiamorada());
			person2Write.setConcelhoMorada(person2Convert.getConcelhomorada());
			person2Write.setDistritoMorada(person2Convert.getDistritomorada());
			person2Write.setTelefone(person2Convert.getTelefone());
			person2Write.setTelemovel(person2Convert.getTelemovel());
			person2Write.setEmail(person2Convert.getEmail());
			person2Write.setEnderecoWeb(person2Convert.getEnderecoweb());
			person2Write.setNumContribuinte(person2Convert.getNumcontribuinte());
			person2Write.setProfissao(person2Convert.getProfissao());
			person2Write.setUsername(person2Convert.getUsername());
			person2Write.setPassword(PasswordEncryptor.encryptPassword(person2Convert.getPassword()));
		
		} catch(Exception e){
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}


   
	public void migrateAluno2Fenix() throws Exception {
		IPessoa person2Write = null;
		List result = null;
		Query query = null;
		QueryByCriteria queryByCriteria = null;
		Almeida_aluno student2Convert = null;
		try {
			System.out.print("A Ler Alunos de Licenciatura ...");
			List alunosG = getAlunosLIC();
			System.out.println("  Done !");

			System.out.println("A Converter " + alunosG.size() + " alunos de Licenciatura para o Fenix ...");

			// Cria informacao sobre um grupo de alunos
			IStudentKind studentGroupInfo = new StudentKind();
			
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
			queryByCriteria = new QueryByCriteria(studentGroupInfo);
			
			result = (List) broker.getCollectionByQuery(queryByCriteria);
			
			if (result.size() == 0){
			
				studentGroupInfo = new StudentKind();			
				studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
				studentGroupInfo.setMaxNACToEnrol(new Integer(10));
				studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
				studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
				broker.store(studentGroupInfo);
			} else {
				studentGroupInfo = (IStudentKind) result.get(0);
			}

			Iterator iterator = alunosG.iterator();
			while(iterator.hasNext()){
				student2Convert = (Almeida_aluno) iterator.next();

				Criteria criteria = new Criteria();
				criteria.addEqualTo("numeroDocumentoIdentificacao",student2Convert.getBi());
				criteria.addEqualTo("tipoDocumentoIdentificacao",new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
				query = new QueryByCriteria(Pessoa.class,criteria);
				result=(List)broker.getCollectionByQuery(query);
				
				if (result.size() == 0) {
					// Cria uma nova Pessoa		
						
					person2Write = new Pessoa();	
					person2Write.setNumeroDocumentoIdentificacao(student2Convert.getBi());
					person2Write.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
					
					person2Write.setNome(student2Convert.getNome());
					person2Write.setNascimento(student2Convert.getNascimento());
					
					// Username vai ser o Numero
					person2Write.setUsername(String.valueOf(student2Convert.getNumero()));
					
					// PAssword o Numero do BI 
					person2Write.setPassword(PasswordEncryptor.encryptPassword(student2Convert.getBi()));
					
					if (student2Convert.getSexo().equalsIgnoreCase("M"))
						person2Write.setSexo(new Sexo(Sexo.MASCULINO));
					else if (student2Convert.getSexo().equalsIgnoreCase("F"))
						person2Write.setSexo(new Sexo(Sexo.FEMININO));
					else {
						System.out.println();
						System.out.println("Erro a converter Aluno n " + student2Convert.getNumero() + ". Erro no SEXO.");
					} 
		
					// Converter a Nacionalidade
					person2Write.setPais(convertCountry(student2Convert.getNacionalidade()));
					
					person2Write.setFreguesiaNaturalidade(student2Convert.getFreguesia());
					person2Write.setConcelhoNaturalidade(student2Convert.getConcelho());
					person2Write.setDistritoNaturalidade(student2Convert.getDistrito());
					person2Write.setNomePai(student2Convert.getNomepai());
					person2Write.setNomeMae(student2Convert.getNomemae());
					person2Write.setMorada(student2Convert.getMorada());
					person2Write.setLocalidade(student2Convert.getLocalidademorada());
					person2Write.setCodigoPostal(student2Convert.getCp());
					person2Write.setLocalidadeCodigoPostal(student2Convert.getLocalidadecp());
					
					
					// pequena verificacao para dividir os numeros de telefone
					if ((student2Convert.getTelefone().length() == 9) && (student2Convert.getTelefone().charAt(0) == '9'))	
						person2Write.setTelemovel(student2Convert.getTelefone());
					else person2Write.setTelefone(student2Convert.getTelefone());
					
					
					person2Write.setEmail(student2Convert.getEmail());
					broker.store(person2Write);
					givePersonRole(person2Write);
	
				} else {
					person2Write = (IPessoa) result.get(0);
					System.out.println("A Pessoa " + student2Convert.getNome() + " já existe.");
				}
				
		
				
				// Criar o Aluno
				IStudent student2Write = null;

				criteria = new Criteria();
				criteria.addEqualTo("number",new Integer(String.valueOf(student2Convert.getNumero())));
				criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.LICENCIATURA));
				query = new QueryByCriteria(Student.class, criteria);
				result = (List)broker.getCollectionByQuery(query);

				if (result.size() == 0){
					student2Write = new Student();
					student2Write.setNumber(new Integer(String.valueOf(student2Convert.getNumero())));
					student2Write.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
					student2Write.setPerson(person2Write);
					student2Write.setState(new StudentState(StudentState.INSCRITO));
					student2Write.setStudentKind(studentGroupInfo);
				
					IPersonRole personRole = readPersonRole((Pessoa) person2Write, RoleType.STUDENT);
					if (personRole == null){
						giveStudentRole(student2Write);												
					}
					
				} else {
					student2Write = (IStudent) result.get(0);
					student2Write.setStudentKind(studentGroupInfo);
					System.out.println("O Aluno " + student2Convert.getNumero() + " já existe.");
				}
				broker.store(student2Write);
			
			}
			
			System.out.println(" Done ! ");
		} catch (QueryException ex) {
			System.out.println("Erro no Aluno de Licenciatura numero : " + student2Convert.getNome());

			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
    

	public void givePersonRole(IPessoa person) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.PERSON);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List)broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(person);
		newRole.setRole(role);
		
		broker.store(newRole);
	}

	public void giveStudentRole(IStudent student) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.STUDENT);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List)broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(student.getPerson());
		newRole.setRole(role);
		
		broker.store(newRole);
	}
    
    
   	public List getAlunosLIC() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Almeida_aluno.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}


    
    public List getAlunosPOS() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_aluno_mestrado.class,criteria);
		return (List) broker.getCollectionByQuery(query);
    }

	public List getAreasCientificas() throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("anoLectivo", "2002/2003");
		QueryByCriteria query = new QueryByCriteria(Posgrad_area_cientifica.class, criteria);
		return (List) broker.getCollectionByQuery(query);
	}


	public List getCursosMestrado() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}

	public List getDiscAreaAluno() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_disc_area_aluno.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
    
	public List getDiscArea() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_disc_area.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
  
	public List getDisciplinas() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_disciplina.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	public List getDocentes() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_docente.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	public List getGuia() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_guia.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	public List getGuiaTabela() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_guia_tabela.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
    
    
	public List getPagamentoGuia() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_pagamento_guia.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	public List getPessoas() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_pessoa.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	public List getFenixPersons() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Pessoa.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}

	public List getEmployes() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Employee.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}


	public List getFenixStudents() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Student.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}

	
	public ICountry convertCountry(String countryCode) {

		Criteria criteria = new Criteria();
		
		if(	countryCode.equals("01") ||
			countryCode.equals("02") ||
			countryCode.equals("03") ||
			countryCode.equals("04") ||
			countryCode.equals("05") ||
			countryCode.equals("06")){
				criteria.addEqualTo("name","PORTUGAL");
		}else if (countryCode.equals("10")){
			criteria.addEqualTo("name","ANGOLA");
		}else if (countryCode.equals("11")){
			criteria.addEqualTo("name","BRASIL");
		}else if (countryCode.equals("12")){
			criteria.addEqualTo("name","CABO VERDE");
		}else if (countryCode.equals("13")){
			criteria.addEqualTo("name","GUINE-BISSAO");
		}else if (countryCode.equals("14")){
			criteria.addEqualTo("name","MOCAMBIQUE");
		}else if (countryCode.equals("15")){
			criteria.addEqualTo("name","SAO TOME E PRINCIPE");
		}else if (countryCode.equals("16")){
			criteria.addEqualTo("name","TIMOR LORO SAE");
		}else if (countryCode.equals("20")){
			criteria.addEqualTo("name","BELGICA");
		}else if (countryCode.equals("21")){
			criteria.addEqualTo("name","DINAMARCA");
		}else if (countryCode.equals("22")){
			criteria.addEqualTo("name","ESPANHA");
		}else if (countryCode.equals("23")){
			criteria.addEqualTo("name","FRANCA");
		}else if (countryCode.equals("24")){
			criteria.addEqualTo("name","HOLANDA");
		}else if (countryCode.equals("25")){
			criteria.addEqualTo("name","IRLANDA");
		}else if (countryCode.equals("26")){
			criteria.addEqualTo("name","ITALIA");
		}else if (countryCode.equals("27")){
			criteria.addEqualTo("name","LUXEMBURGO");
		}else if (countryCode.equals("28")){
			criteria.addEqualTo("name","ALEMANHA");
		}else if (countryCode.equals("29")){
			criteria.addEqualTo("name","REINO UNIDO");
		}else if (countryCode.equals("30")){
			criteria.addEqualTo("name","SUECIA");
		}else if (countryCode.equals("31")){
			criteria.addEqualTo("name","NORUEGA");
		}else if (countryCode.equals("32")){
			criteria.addEqualTo("name","POLONIA");
		}else if (countryCode.equals("33")){
			criteria.addEqualTo("name","AFRICA DO SUL");
		}else if (countryCode.equals("34")){
			criteria.addEqualTo("name","ARGENTINA");
		}else if (countryCode.equals("35")){
			criteria.addEqualTo("name","CANADA");
		}else if (countryCode.equals("36")){
			criteria.addEqualTo("name","CHILE");
		}else if (countryCode.equals("37")){
			criteria.addEqualTo("name","EQUADOR");
		}else if (countryCode.equals("38")){
			criteria.addEqualTo("name","ESTADOS UNIDOS DA AMERICA");
		}else if (countryCode.equals("39")){
			criteria.addEqualTo("name","IRAO");
		}else if (countryCode.equals("40")){
			criteria.addEqualTo("name","MARROCOS");
		}else if (countryCode.equals("41")){
			criteria.addEqualTo("name","VENEZUELA");
		}else if (countryCode.equals("42")){
			criteria.addEqualTo("name","AUSTRALIA");
		}else if (countryCode.equals("43")){
			criteria.addEqualTo("name","PAQUISTAO");
		}else if (countryCode.equals("44")){
			criteria.addEqualTo("name","REPUBLICA DO ZAIRE");
		}else if (countryCode.equals("47")){
			criteria.addEqualTo("name","LIBIA");
		}else if (countryCode.equals("48")){
			criteria.addEqualTo("name","PALESTINA");
		}else if (countryCode.equals("49")){
			criteria.addEqualTo("name","ZIMBABUE");
		}else if (countryCode.equals("50")){
			criteria.addEqualTo("name","MEXICO");
		}else if (countryCode.equals("51")){
			criteria.addEqualTo("name","RUSSIA");
		}else if (countryCode.equals("52")){
			criteria.addEqualTo("name","AUSTRIA");
		}else if (countryCode.equals("53")){
			criteria.addEqualTo("name","IRAQUE");
		}else if (countryCode.equals("54")){
			criteria.addEqualTo("name","PERU");
		}else if (countryCode.equals("60")){
			criteria.addEqualTo("name","ROMENIA");
		}else if (countryCode.equals("61")){
			criteria.addEqualTo("name","REPUBLICA CHECA");
		} else return null;

		Query query = new QueryByCriteria(Country.class,criteria);
		List result = (List) broker.getCollectionByQuery(query);

		if (result.size() == 0)
			return null;
		else return (ICountry) result.get(0);
	}
    
}
