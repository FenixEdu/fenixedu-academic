
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

import org.odmg.QueryException;



import Dominio.ICountry;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.PersonRole;
import Dominio.Student;
import Dominio.StudentGroupInfo;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RoleType;
import Util.Sexo;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;


public class SuperConverter extends ObjectFenixOJB {

	SuportePersistenteOJB sp = null;
	
	public SuperConverter() throws ExcepcaoPersistencia{
		sp = SuportePersistenteOJB.getInstance();
	};



public static void main(String args[]) throws Exception{
	// Start Converting
 

	SuperConverter superConverter = new SuperConverter();

	// Nao esquecer os Roles
	// Nao esquecer as quantias das guias e a tabela de precos
	
	
	// Converter as Nacionalidades
	
	
	
	// Converter Alunos de Licenciatura em Persons e em Students
	superConverter.migrateAluno2Fenix();
		
	
	// Converter Pessoas de Pos Graduacao em Persons
	
	// Converter Alunos de Pos Graduacao em Students 
	
	
	
	// Converte Areas Cientificas
	
	
	// Converte Alunos
	
	
	// Converte Disciplinas
	
	
	// Inscricoes do Alunos em Disciplinas
	
	
	// Inscricoes de Alunos em Areas Cientificas
	
	
	// Converte Guias de Pagamento
	
	
	}



   
	public void migrateAluno2Fenix() throws Exception {
		IPessoa person2Write = null;
		try {
			List alunosG = getAlunosLIC();
			
			System.out.println("A Converter " + alunosG.size() + " alunos de Licenciatura para o Fenix ...");

			// Cria informacao sobre um grupo de alunos 
			StudentGroupInfo studentGroupInfo = new StudentGroupInfo();
			studentGroupInfo.setIdInternal(new Integer(1));
			studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
			studentGroupInfo.setMaxNACToEnrol(new Integer(10));
			studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));

			sp.iniciarTransaccao();
			super.lockWrite(studentGroupInfo);
			sp.confirmarTransaccao();


			Iterator iterator = alunosG.iterator();
			while(iterator.hasNext()){
				person2Write = new Dominio.Pessoa();
				Almeida_aluno student2Convert = (Almeida_aluno) iterator.next();
				
				// Cria uma nova Pessoa				
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
				
				
				// Escrever a pessoa para Obter o Codigo Interno
				
				try {
					sp.iniciarTransaccao();
					sp.getIPessoaPersistente().escreverPessoa(person2Write);
					sp.confirmarTransaccao();
					
					givePersonRole(person2Write);
				} catch (ExistingPersistentException e) {
					// Verificar se a Pessoa ja existe (Se ja existe lanca excepcao)
					
					sp.iniciarTransaccao();
					person2Write = this.sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(student2Convert.getBi(), new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
					sp.confirmarTransaccao();
					System.out.println("A Pessoa " + student2Convert.getNome() + " já existe.");
				}
				
				// Criar o Aluno
				
				IStudent student2Write = new Student();
				student2Write.setNumber(new Integer(String.valueOf(student2Convert.getNumero())));
				student2Write.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
				student2Write.setPerson(person2Write);
				student2Write.setState(new StudentState(StudentState.INSCRITO));
				student2Write.setStudentGroupInfo(studentGroupInfo);
				
				sp.iniciarTransaccao();
				super.lockWrite(student2Write);
				sp.confirmarTransaccao();
				
				
				giveStudentRole(student2Write);
				
			}
		
			System.out.println(" Success ");
		} catch (QueryException ex) {
			System.out.println("Pessoa : " + person2Write.getNome());

			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
    

	public void givePersonRole(IPessoa person) throws Exception {
		sp.iniciarTransaccao();
		IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PERSON);
			
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(person);
		newRole.setRole(role);

		sp.getIPersistentPersonRole().lockWrite(newRole);
		sp.confirmarTransaccao();
	}

	public void giveStudentRole(IStudent student) throws Exception {
		sp.iniciarTransaccao();

		IRole role = sp.getIPersistentRole().readByRoleType(RoleType.STUDENT);
			
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(student.getPerson());
		newRole.setRole(role);


		sp.getIPersistentPersonRole().lockWrite(newRole);
		sp.confirmarTransaccao();
	}
    
    
   	public List getAlunosLIC() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Almeida_aluno.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}


    
    public List getAlunosPOS() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_aluno_mestrado.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
    }

	public List getAreasCientificas() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_area_cientifica.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}


	public List getCursosMestrado() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_curso_mestrado.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}

	public List getDiscAreaAluno() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_disc_area_aluno.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
    
	public List getDiscArea() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_disc_area.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
  
	public List getDisciplinas() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_disciplina.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
	
	public List getDocentes() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_docente.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
	
	public List getGuia() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_guia.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
	
	public List getGuiaTabela() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_guia_tabela.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
    
    
	public List getPagamentoGuia() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_pagamento_guia.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
	
	public List getPessoas() throws Exception {
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			String oqlQuery = "select all from " + Posgrad_pessoa.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
			return result;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
	
	public ICountry convertCountry(String countryCode) throws ExcepcaoPersistencia {
		ICountry country = null;
		sp.iniciarTransaccao();
		
		if(	countryCode.equals("01") ||
			countryCode.equals("02") ||
			countryCode.equals("03") ||
			countryCode.equals("04") ||
			countryCode.equals("05") ||
			countryCode.equals("06")){
			country = sp.getIPersistentCountry().readCountryByName("PORTUGAL");
		}else if (countryCode.equals("10")){
			country = sp.getIPersistentCountry().readCountryByName("ANGOLA");
		}else if (countryCode.equals("11")){
			country = sp.getIPersistentCountry().readCountryByName("BRASIL");
		}else if (countryCode.equals("12")){
			country = sp.getIPersistentCountry().readCountryByName("CABO VERDE");
		}else if (countryCode.equals("13")){
			country = sp.getIPersistentCountry().readCountryByName("GUINE-BISSAO");
		}else if (countryCode.equals("14")){
			country = sp.getIPersistentCountry().readCountryByName("MOCAMBIQUE");
		}else if (countryCode.equals("15")){
			country = sp.getIPersistentCountry().readCountryByName("SAO TOME E PRINCIPE");
		}else if (countryCode.equals("16")){
			country = sp.getIPersistentCountry().readCountryByName("TIMOR LORO SAE");
		}else if (countryCode.equals("20")){
			country = sp.getIPersistentCountry().readCountryByName("BELGICA");
		}else if (countryCode.equals("21")){
			country = sp.getIPersistentCountry().readCountryByName("DINAMARCA");
		}else if (countryCode.equals("22")){
			country = sp.getIPersistentCountry().readCountryByName("ESPANHA");
		}else if (countryCode.equals("23")){
			country = sp.getIPersistentCountry().readCountryByName("FRANCA");
		}else if (countryCode.equals("24")){
			country = sp.getIPersistentCountry().readCountryByName("HOLANDA");
		}else if (countryCode.equals("25")){
			country = sp.getIPersistentCountry().readCountryByName("IRLANDA");
		}else if (countryCode.equals("26")){
			country = sp.getIPersistentCountry().readCountryByName("ITALIA");
		}else if (countryCode.equals("27")){
			country = sp.getIPersistentCountry().readCountryByName("LUXEMBURGO");
		}else if (countryCode.equals("28")){
			country = sp.getIPersistentCountry().readCountryByName("ALEMANHA");
		}else if (countryCode.equals("29")){
			country = sp.getIPersistentCountry().readCountryByName("REINO UNIDO");
		}else if (countryCode.equals("30")){
			country = sp.getIPersistentCountry().readCountryByName("SUECIA");
		}else if (countryCode.equals("31")){
			country = sp.getIPersistentCountry().readCountryByName("NORUEGA");
		}else if (countryCode.equals("32")){
			country = sp.getIPersistentCountry().readCountryByName("POLONIA");
		}else if (countryCode.equals("33")){
			country = sp.getIPersistentCountry().readCountryByName("AFRICA DO SUL");
		}else if (countryCode.equals("34")){
			country = sp.getIPersistentCountry().readCountryByName("ARGENTINA");
		}else if (countryCode.equals("35")){
			country = sp.getIPersistentCountry().readCountryByName("CANADA");
		}else if (countryCode.equals("36")){
			country = sp.getIPersistentCountry().readCountryByName("CHILE");
		}else if (countryCode.equals("37")){
			country = sp.getIPersistentCountry().readCountryByName("EQUADOR");
		}else if (countryCode.equals("38")){
			country = sp.getIPersistentCountry().readCountryByName("ESTADOS UNIDOS DA AMERICA");
		}else if (countryCode.equals("39")){
			country = sp.getIPersistentCountry().readCountryByName("IRAO");
		}else if (countryCode.equals("40")){
			country = sp.getIPersistentCountry().readCountryByName("MARROCOS");
		}else if (countryCode.equals("41")){
			country = sp.getIPersistentCountry().readCountryByName("VENEZUELA");
		}else if (countryCode.equals("42")){
			country = sp.getIPersistentCountry().readCountryByName("AUSTRALIA");
		}else if (countryCode.equals("43")){
			country = sp.getIPersistentCountry().readCountryByName("PAQUISTAO");
		}else if (countryCode.equals("44")){
			country = sp.getIPersistentCountry().readCountryByName("REPUBLICA DO ZAIRE");
		}else if (countryCode.equals("45")){
			country = sp.getIPersistentCountry().readCountryByName("COREIA");
		}else if (countryCode.equals("46")){
			country = sp.getIPersistentCountry().readCountryByName("QUENIA");
		}else if (countryCode.equals("47")){
			country = sp.getIPersistentCountry().readCountryByName("LIBIA");
		}else if (countryCode.equals("48")){
			country = sp.getIPersistentCountry().readCountryByName("PALESTINA");
		}else if (countryCode.equals("49")){
			country = sp.getIPersistentCountry().readCountryByName("ZIMBABUE");
		}else if (countryCode.equals("50")){
			country = sp.getIPersistentCountry().readCountryByName("MEXICO");
		}else if (countryCode.equals("51")){
			country = sp.getIPersistentCountry().readCountryByName("RUSSIA");
		}else if (countryCode.equals("52")){
			country = sp.getIPersistentCountry().readCountryByName("AUSTRIA");
		}else if (countryCode.equals("53")){
			country = sp.getIPersistentCountry().readCountryByName("IRAQUE");
		}else if (countryCode.equals("54")){
			country = sp.getIPersistentCountry().readCountryByName("PERU");
		}else if (countryCode.equals("60")){
			country = sp.getIPersistentCountry().readCountryByName("ROMENIA");		
		}else if (countryCode.equals("61")){
			country = sp.getIPersistentCountry().readCountryByName("REPUBLICA CHECA");
		}
			
		sp.confirmarTransaccao();
		return country;
	}
    
}
