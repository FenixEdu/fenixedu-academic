
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
import Dominio.ICountry;
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
 

	SuperConverter superConverter = new SuperConverter();

	// Nao esquecer os Roles
	
	
	// Converter Alunos de Licenciatura em Persons e em Students
	superConverter.migrateAluno2Fenix();
		
	
	// Converter Pessoas de Pos Graduacao em Persons
	superConverter.migratePosgradPessoa2Fenix();
	
	
	// Converter Alunos de Pos Graduacao em Students 
	
	
	
	// Converte Areas Cientificas
	
	
	// Converte Disciplinas
	
	
	// Inscricoes do Alunos em Disciplinas
	
	
	// Inscricoes de Alunos em Areas Cientificas
	
	
	
	}

	public void migratePosgradPessoa2Fenix(){
		
//		try {
//			System.out.print("A Ler Pessoas de Pos-Graduacao ...");
//			List alunosG = get();
//			System.out.println("  Done !");
//
//			System.out.println("A Converter " + alunosG.size() + " alunos de Licenciatura para o Fenix ...");
//			
//			
//		
//		} catch(Exception e) {
//			System.out.println("Erro na Pessoa de Pos-Graduacao : " + person2Convert.getNome());
//			throw new Exception(e);
//		}	
					
	}


   
	public void migrateAluno2Fenix() throws Exception {
		IPessoa person2Write = null;
		boolean newPerson = false;
		boolean newStudent = false;
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
				newPerson = false;
				newStudent = false;
				student2Convert = (Almeida_aluno) iterator.next();

				Criteria criteria = new Criteria();
				criteria.addEqualTo("numeroDocumentoIdentificacao",student2Convert.getBi());
				criteria.addEqualTo("tipoDocumentoIdentificacao",new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
				query = new QueryByCriteria(Pessoa.class,criteria);
				result=(List)broker.getCollectionByQuery(query);
				
				if (result.size() == 0) {
					// Cria uma nova Pessoa		
						
					newPerson = true;
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
	
				} else {
					person2Write = (IPessoa) result.get(0);
					System.out.println("A Pessoa " + student2Convert.getNome() + " já existe.");
				}
				
				// Se for uma nova pessoa dar o novo Role
				if (newPerson) {
					givePersonRole(person2Write);
				}
				
				
				// Criar o Aluno
				IStudent student2Write = null;

				criteria = new Criteria();
				criteria.addEqualTo("number",new Integer(String.valueOf(student2Convert.getNumero())));
				criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.LICENCIATURA));
				query = new QueryByCriteria(Student.class, criteria);
				result = (List)broker.getCollectionByQuery(query);

				if (result.size() == 0){
					newStudent = true;
					student2Write = new Student();
					student2Write.setNumber(new Integer(String.valueOf(student2Convert.getNumero())));
					student2Write.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
					student2Write.setPerson(person2Write);
					student2Write.setState(new StudentState(StudentState.INSCRITO));
					student2Write.setStudentGroupInfo(studentGroupInfo);
				
					if (newStudent){				
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
