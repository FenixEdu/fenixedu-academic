/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DegreeCurricularPlan;
import Dominio.Funcionario;
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
import Util.RoleType;
import Util.Sexo;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadStudents extends LoadDataFile {

	private static LoadStudents loader = null;

	private LoadStudents() {
	}

	public static void main(String[] args) {
		loader = new LoadStudents();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeperator());

		// Obter os dados do aluno.
		String numero = stringTokenizer.nextToken();
		String nome = stringTokenizer.nextToken();
		String dataNascimento = stringTokenizer.nextToken();
		String bi = stringTokenizer.nextToken();
		String curso = stringTokenizer.nextToken();
		String ramo = stringTokenizer.nextToken();
		String sexo = stringTokenizer.nextToken();
		String nacionalidade = stringTokenizer.nextToken();
		String freguesiaNaturalidade = stringTokenizer.nextToken();
		String concelhoNaturalidade = stringTokenizer.nextToken();
		String distritoNaturalidade = stringTokenizer.nextToken();
		String nomePai = stringTokenizer.nextToken();
		String nomeMae = stringTokenizer.nextToken();
		String morada = stringTokenizer.nextToken();
		String localidadeMorada = stringTokenizer.nextToken();
		String codigoPostal = stringTokenizer.nextToken();
		String localidadeCodigoPostal = stringTokenizer.nextToken();
		String telefone = stringTokenizer.nextToken();
		String email = stringTokenizer.nextToken();

		Almeida_aluno almeida_aluno = new Almeida_aluno();
		almeida_aluno.setNumero((new Integer(numero)).longValue());
		almeida_aluno.setNome(nome);
		almeida_aluno.setNascimento(convertToJavaDate(dataNascimento));
		almeida_aluno.setBi(bi);
		almeida_aluno.setCurso((new Integer(curso)).longValue());
		almeida_aluno.setRamo((new Integer(ramo)).longValue());
		almeida_aluno.setSexo(sexo);
		almeida_aluno.setNacionalidade(nacionalidade);
		almeida_aluno.setFreguesia(freguesiaNaturalidade);
		almeida_aluno.setConcelho(concelhoNaturalidade);
		almeida_aluno.setDistrito(distritoNaturalidade);
		almeida_aluno.setNomepai(nomePai);
		almeida_aluno.setNomemae(nomeMae);
		almeida_aluno.setMorada(morada);
		almeida_aluno.setLocalidademorada(localidadeMorada);
		almeida_aluno.setCp(codigoPostal);
		almeida_aluno.setLocalidadecp(localidadeCodigoPostal);
		almeida_aluno.setTelefone(telefone);
		almeida_aluno.setEmail(email);

		//loader.writeElement(almeida_aluno);
		try {
			processStudent(almeida_aluno);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error processing student: " + almeida_aluno.getNumero());
		}
	}

	protected String getFilename() {
		return "migration/ALUNOS.TXT";
	}

	protected String getFieldSeperator() {
		return "\t";
	}

	protected void processStudent(Almeida_aluno almeida_aluno) throws Exception {
		IPessoa person2Write = null;
		List result = null;

		// Cria informacao sobre um grupo de alunos
		IStudentGroupInfo studentGroupInfo = new StudentGroupInfo();
		studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
		result = query(studentGroupInfo);
			
		if (result.size() == 0){
			
			studentGroupInfo = new StudentGroupInfo();			
			studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
			studentGroupInfo.setMaxNACToEnrol(new Integer(10));
			studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
			studentGroupInfo.setStudentType(new StudentType (StudentType.NORMAL));
			writeElement(studentGroupInfo);
		} else {
			studentGroupInfo = (IStudentGroupInfo) result.get(0);
		}
		
		Criteria criteria = new Criteria();
		criteria.addEqualTo("numeroDocumentoIdentificacao",almeida_aluno.getBi());
		criteria.addEqualTo("tipoDocumentoIdentificacao",new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		result = query(Pessoa.class, criteria);

		if (result.size() == 0) {
			// Cria uma nova Pessoa
			person2Write = new Pessoa();	
			person2Write.setNumeroDocumentoIdentificacao(almeida_aluno.getBi());
			person2Write.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
			person2Write.setNome(almeida_aluno.getNome());
			person2Write.setNascimento(almeida_aluno.getNascimento());
					
			// Username vai ser o Numero
			person2Write.setUsername(String.valueOf(almeida_aluno.getNumero()));
					
			// PAssword o Numero do BI 
			person2Write.setPassword(PasswordEncryptor.encryptPassword(almeida_aluno.getBi()));
					
			if (almeida_aluno.getSexo().equalsIgnoreCase("M"))
				person2Write.setSexo(new Sexo(Sexo.MASCULINO));
			else if (almeida_aluno.getSexo().equalsIgnoreCase("F"))
				person2Write.setSexo(new Sexo(Sexo.FEMININO));
			else {
				System.out.println();
				System.out.println("Erro a converter Aluno n " + almeida_aluno.getNumero() + ". Erro no SEXO.");
			} 
		
			// Converter a Nacionalidade
			person2Write.setPais(convertCountry(almeida_aluno.getNacionalidade()));
					
			person2Write.setFreguesiaNaturalidade(almeida_aluno.getFreguesia());
			person2Write.setConcelhoNaturalidade(almeida_aluno.getConcelho());
			person2Write.setDistritoNaturalidade(almeida_aluno.getDistrito());
			person2Write.setNomePai(almeida_aluno.getNomepai());
			person2Write.setNomeMae(almeida_aluno.getNomemae());
			person2Write.setMorada(almeida_aluno.getMorada());
			person2Write.setLocalidade(almeida_aluno.getLocalidademorada());
			person2Write.setCodigoPostal(almeida_aluno.getCp());
			person2Write.setLocalidadeCodigoPostal(almeida_aluno.getLocalidadecp());
					
					
			// pequena verificacao para dividir os numeros de telefone
			if ((almeida_aluno.getTelefone().length() == 9) && (almeida_aluno.getTelefone().charAt(0) == '9'))	
				person2Write.setTelemovel(almeida_aluno.getTelefone());
			else person2Write.setTelefone(almeida_aluno.getTelefone());
					
			person2Write.setEmail(almeida_aluno.getEmail());

			loader.writeElement(person2Write);
			givePersonRole(person2Write);
	
		} else {
			person2Write = (IPessoa) result.get(0);
			//System.out.println("A Pessoa " + almeida_aluno.getNome() + " já existe.");
						
			criteria = new Criteria();
			criteria.addEqualTo("codigoInterno",((Pessoa)person2Write).getCodigoInterno());
			result = query(Funcionario.class, criteria);

			if (result.size() == 0) {
				criteria = new Criteria();
				criteria.addEqualTo("internalCode",((Pessoa)person2Write).getCodigoInterno());
				criteria.addEqualTo("degreeType",new TipoCurso(TipoCurso.MESTRADO));				
				result = query(Student.class, criteria);
				if (result.size() == 0) {
					// Since person is not employee nor post-gradute student
					// information must be updated
					person2Write.setMorada(almeida_aluno.getMorada());
					person2Write.setLocalidade(almeida_aluno.getLocalidademorada());
					person2Write.setCodigoPostal(almeida_aluno.getCp());
					person2Write.setLocalidadeCodigoPostal(almeida_aluno.getLocalidadecp());
				}
			}
		}
				

		// Criar o Aluno
		IStudent student2Write = null;

		criteria = new Criteria();
		criteria.addEqualTo("number",new Integer(String.valueOf(almeida_aluno.getNumero())));
		criteria.addEqualTo("degreeType", new TipoCurso(TipoCurso.LICENCIATURA));

		result = query(Student.class, criteria);

		if (result.size() == 0){
			student2Write = new Student();
			student2Write.setNumber(new Integer(String.valueOf(almeida_aluno.getNumero())));
			student2Write.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
			student2Write.setPerson(person2Write);
			student2Write.setState(new StudentState(StudentState.INSCRITO));
			student2Write.setStudentGroupInfo(studentGroupInfo);
				
			giveStudentRole(student2Write);
		} else {
			student2Write = (IStudent) result.get(0);
			student2Write.setStudentGroupInfo(studentGroupInfo);
			//System.out.println("O Aluno " + almeida_aluno.getNumero() + " já existe.");
		}
		writeElement(student2Write);

	}

	public void giveStudentRole(IStudent student) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.STUDENT);
		
		List result = query(Role.class, criteria);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(student.getPerson());
		newRole.setRole(role);
		
		writeElement(newRole);
	}

}