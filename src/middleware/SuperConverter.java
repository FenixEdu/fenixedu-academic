 
package middleware;


import java.util.Iterator;
import java.util.List;

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

import Dominio.Country;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.Funcionario;
import Dominio.ICountry;
import Dominio.ICurricularCourse;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentGroupInfo;
import Dominio.PersonRole;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.Student;
import Dominio.StudentGroupInfo;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


public class SuperConverter extends ObjectFenixOJB {

	PersistenceBroker broker = null;
	public SuperConverter() throws ExcepcaoPersistencia{
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	};



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
	
	
	// Converte Disciplinas
	superConverter.migratePosGradDisciplina2Fenix();
	
	// Inscricoes do Alunos em Disciplinas
	
	
	// Inscricoes de Alunos em Areas Cientificas
	
	
	
	}




	public void migratePosGradDisciplina2Fenix() throws Exception{
		ICurricularCourse curricularCourse2Write = null;
		Posgrad_disciplina curricularCourse2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		QueryByCriteria queryByCriteria = null;
		try {
			System.out.print("Reading PosGrad Curricular Courses ...");
			List curricularCoursesPG = getDisciplinas();
			System.out.println("  Done !");
			
			System.out.println("Migrating " + curricularCoursesPG.size() + " PosGrad Curricular Courses to Fenix ...");
			Iterator iterator = curricularCoursesPG.iterator();
			while(iterator.hasNext()){
				curricularCourse2Convert = (Posgrad_disciplina) iterator.next();
				
				// Get the old degree corresponding to this Curricular Course
				
				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(curricularCourse2Convert.getCodigocursomestrado())));
				query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
		
				if (result.size() == 0){
					System.out.println("Error Reading OLD Degree (" + curricularCourse2Convert.getNome() + ")");
					throw new Exception("Cannot Read PosGrad Degree");
				}

				Posgrad_curso_mestrado oldDegre = (Posgrad_curso_mestrado) result.get(0); 

				// Get the new Degree

				criteria = new Criteria();
				criteria.addEqualTo("nome", oldDegre.getNomemestrado());
				query = new QueryByCriteria(Curso.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() == 0){
					System.out.println("Error Reading NEW Degree (" + oldDegre.getNomemestrado() + ")");
					throw new Exception("Cannot Read Fenix Degree");
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
				
				// Check the Curricular Course type
				
				
				
				
				
				
				
			}
			System.out.println("  Done !");

		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Curricular Course " + curricularCourse2Convert.getNome() , e);
			
		}
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
			IStudentGroupInfo studentGroupInfo = new StudentGroupInfo();
			
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
			queryByCriteria = new QueryByCriteria(studentGroupInfo);
			
			result = (List) broker.getCollectionByQuery(queryByCriteria);
			
			if (result.size() == 0){
			
				studentGroupInfo = new StudentGroupInfo();			
				studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
				studentGroupInfo.setMaxNACToEnrol(new Integer(10));
				studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
				studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
				broker.store(studentGroupInfo);
			} else {
				studentGroupInfo = (IStudentGroupInfo) result.get(0);
			}

			Iterator iterator = studentsPG.iterator();
			while(iterator.hasNext()){
				student2Convert = (Posgrad_aluno_mestrado) iterator.next();
		
				criteria = new Criteria();
				criteria.addEqualTo("number", new Integer(String.valueOf(student2Convert.getNumero())));
				criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.MESTRADO));
				query = new QueryByCriteria(Student.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
		
				if (result.size() == 0){
					
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
							
					// Create a new Student
					student2Write = new Student();
					student2Write.setNumber(new Integer(String.valueOf(student2Convert.getNumero())));
					student2Write.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
					student2Write.setPerson(person);
					student2Write.setState(new StudentState(StudentState.INSCRITO));
					student2Write.setStudentGroupInfo(studentGroupInfo);
					broker.store(student2Write);
					
					// Give the Student Role (This student may not exist but the person may already be a 
					// Graduate Student) 
					
					IPersonRole personRole = readPersonRole((Pessoa) person, RoleType.STUDENT);
					if (personRole == null){
						giveStudentRole(student2Write);												
					}
					
				} else System.out.println("O Aluno " + student2Convert.getNumero() + " ja existe. Nenhuma alteracao efectuada");
		
			}
			System.out.println("  Done !");
		} catch(Exception e) {;
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
		criteria.addEqualTo("keyPerson", person.getCodigoInterno());
		
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
					criteria.addEqualTo("chavePessoa", personTemp.getCodigoInterno());
					query = new QueryByCriteria(Funcionario.class,criteria);
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
			IStudentGroupInfo studentGroupInfo = new StudentGroupInfo();
			
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
			queryByCriteria = new QueryByCriteria(studentGroupInfo);
			
			result = (List) broker.getCollectionByQuery(queryByCriteria);
			
			if (result.size() == 0){
			
				studentGroupInfo = new StudentGroupInfo();			
				studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
				studentGroupInfo.setMaxNACToEnrol(new Integer(10));
				studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
				studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
				broker.store(studentGroupInfo);
			} else {
				studentGroupInfo = (IStudentGroupInfo) result.get(0);
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
					student2Write.setStudentGroupInfo(studentGroupInfo);
				
					IPersonRole personRole = readPersonRole((Pessoa) person2Write, RoleType.STUDENT);
					if (personRole == null){
						giveStudentRole(student2Write);												
					}
					
				} else {
					student2Write = (IStudent) result.get(0);
					student2Write.setStudentGroupInfo(studentGroupInfo);
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
		Query query = new QueryByCriteria(Posgrad_area_cientifica.class,criteria);
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
	
	public ICountry convertCountry(String countryCode) throws ExcepcaoPersistencia {

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
