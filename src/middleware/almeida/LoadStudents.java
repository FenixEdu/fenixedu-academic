/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import middleware.personAndEmployee.PersonUtils;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IBranch;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.PersonRole;
import Dominio.Role;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.StudentKind;
import Util.RoleType;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;

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
			new StringTokenizer(line, getFieldSeparator());

		// Obter os dados do aluno.
		// Estrutura Original
		//		String numero = stringTokenizer.nextToken();
		//		String nome = stringTokenizer.nextToken();
		//		String dataNascimento = stringTokenizer.nextToken();
		//		String bi = stringTokenizer.nextToken();
		//		String curso = stringTokenizer.nextToken();
		//		String ramo = stringTokenizer.nextToken();
		//		String sexo = stringTokenizer.nextToken();
		//		String nacionalidade = stringTokenizer.nextToken();
		//		String freguesiaNaturalidade = stringTokenizer.nextToken();
		//		String concelhoNaturalidade = stringTokenizer.nextToken();
		//		String distritoNaturalidade = stringTokenizer.nextToken();
		//		String nomePai = stringTokenizer.nextToken();
		//		String nomeMae = stringTokenizer.nextToken();
		//		String morada = stringTokenizer.nextToken();
		//		String localidadeMorada = stringTokenizer.nextToken();
		//		String codigoPostal = stringTokenizer.nextToken();
		//		String localidadeCodigoPostal = stringTokenizer.nextToken();
		//		String telefone = stringTokenizer.nextToken();
		//		String email = stringTokenizer.nextToken();

		// Estrutura Actual
		String bi = stringTokenizer.nextToken();
		String numero = stringTokenizer.nextToken();
		String curso = stringTokenizer.nextToken();
		String ramo = stringTokenizer.nextToken();
		String ano = stringTokenizer.nextToken(); // new
		String polo = stringTokenizer.nextToken(); // new

		Almeida_aluno almeida_aluno = new Almeida_aluno();
		almeida_aluno.setNumero((new Integer(numero)).longValue());
		almeida_aluno.setBi(bi);
		almeida_aluno.setCurso((new Integer(curso)).longValue());
		almeida_aluno.setRamo((new Integer(ramo)).longValue());
		almeida_aluno.setAno(new Integer(ano));
		almeida_aluno.setPolo(polo);

		//		almeida_aluno.setNome(nome);
		//		almeida_aluno.setNascimento(convertToJavaDate(dataNascimento));		
		//		almeida_aluno.setSexo(sexo);
		//		almeida_aluno.setNacionalidade(nacionalidade);
		//		almeida_aluno.setFreguesia(freguesiaNaturalidade);
		//		almeida_aluno.setConcelho(concelhoNaturalidade);
		//		almeida_aluno.setDistrito(distritoNaturalidade);
		//		almeida_aluno.setNomepai(nomePai);
		//		almeida_aluno.setNomemae(nomeMae);
		//		almeida_aluno.setMorada(morada);
		//		almeida_aluno.setLocalidademorada(localidadeMorada);
		//		almeida_aluno.setCp(codigoPostal);
		//		almeida_aluno.setLocalidadecp(localidadeCodigoPostal);
		//		almeida_aluno.setTelefone(telefone);
		//		almeida_aluno.setEmail(email);

		//loader.writeElement(almeida_aluno);
		try {
			processStudent(almeida_aluno);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
				"Error processing student: " + almeida_aluno.getNumero());
		}
	}

	protected String getFilename() {
		return "data/alunos.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected void processStudent(Almeida_aluno almeida_aluno)
		throws Exception {

		List result = null;

		// Cria informacao sobre um grupo de alunos
		IStudentKind studentGroupInfo = new StudentKind();
		studentGroupInfo.setStudentType(new StudentType(StudentType.NORMAL));
		result = query(studentGroupInfo);

		if (result.size() == 0) {
			studentGroupInfo = new StudentKind();
			studentGroupInfo.setMaxCoursesToEnrol(new Integer(7));
			studentGroupInfo.setMaxNACToEnrol(new Integer(10));
			studentGroupInfo.setMinCoursesToEnrol(new Integer(3));
			studentGroupInfo.setStudentType(
				new StudentType(StudentType.NORMAL));
			writeElement(studentGroupInfo);
		} else {
			studentGroupInfo = (IStudentKind) result.get(0);
		}

//		Criteria criteriaByBi = new Criteria();
//		criteriaByBi.addEqualTo(
//			"numeroDocumentoIdentificacao",
//			almeida_aluno.getBi());
//
//		List resultByBi = query(Pessoa.class, criteriaByBi);
//
//		Criteria criteriaByUsername = new Criteria();
//		criteriaByUsername.addEqualTo(
//			"username",
//			"L" + almeida_aluno.getNumero());
//
//		List resultByUsername = query(Pessoa.class, criteriaByUsername);

		IPessoa studentPerson =
			PersonUtils.getPerson(
				String.valueOf(almeida_aluno.getNumero()),
				almeida_aluno.getBi(),
				"L",
				getBroker());
		if (studentPerson != null)	 {	
		//if (resultByBi.size() == 1) {
			studentPerson.setUsername("L" + almeida_aluno.getNumero());
			IBranch branch = getBranch(almeida_aluno);
			if (branch == null) {
				// Unable to match branch
				// TODO : log the error
				System.out.println(
					"Unable to match branch: ["
						+ almeida_aluno.getRamo()
						+ "]  for degree: ["
						+ almeida_aluno.getCurso()
						+ "]");
				return;
			}
			writeElement(studentPerson);
			// Criar o Aluno
			IStudent student2Write = null;
			student2Write =
				writeStudent(
					almeida_aluno,
					studentGroupInfo,
					studentPerson,
					student2Write);
			readStudentCurricularPlan(almeida_aluno, student2Write);
		} else {
			System.out.println(
				"Student: ["
					+ almeida_aluno.getNumero()
					+ "] is associated to ["
					+ result.size()
					+ "] people.");
		}
	}

	private IStudent writeStudent(
		Almeida_aluno almeida_aluno,
		IStudentKind studentGroupInfo,
		IPessoa studentPerson,
		IStudent student2Write)
		throws NumberFormatException, Exception {
		List result;
		Criteria studentCriteria = new Criteria();
		studentCriteria.addEqualTo(
			"number",
			new Integer(String.valueOf(almeida_aluno.getNumero())));
		studentCriteria.addEqualTo(
			"degreeType",
			new TipoCurso(TipoCurso.LICENCIATURA));

		result = query(Student.class, studentCriteria);

		if (result.size() == 0) {
			// Student is not yet in the database
			// Create new student
			student2Write = new Student();
			student2Write.setNumber(
				new Integer(String.valueOf(almeida_aluno.getNumero())));
			student2Write.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));

			student2Write.setPerson(studentPerson);

			student2Write.setState(new StudentState(StudentState.INSCRITO));
			student2Write.setStudentKind(studentGroupInfo);

			giveStudentRole(student2Write);
		} else {
			// Student is already in the database
			// Just update the students information.
			writeElement(student2Write);
			student2Write = (IStudent) result.get(0);
			student2Write.setStudentKind(studentGroupInfo);
			//System.out.println("O Aluno " + almeida_aluno.getNumero() + " já existe.");
		}
		writeElement(student2Write);
		return student2Write;
	}

	public void giveStudentRole(IStudent student) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.STUDENT);

		List result = query(Role.class, criteria);

		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else
			role = (Role) result.get(0);

		IPersonRole newRole = new PersonRole();
		newRole.setPerson(student.getPerson());
		newRole.setRole(role);

		writeElement(newRole);
	}

	/* (non-Javadoc)
	 * @see middleware.almeida.LoadDataFile#getFilenameOutput()
	 */
	protected String getFilenameOutput() {

		return null;
	}

	private IStudentCurricularPlan readStudentCurricularPlan(
		Almeida_aluno almeida_aluno,
		IStudent student) {

		IBranch branch = getBranch(almeida_aluno);
		if (branch == null) {
			return null;
		}

		IStudentCurricularPlan studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlanByStudentNumber(
				student.getNumber());

		if (studentCurricularPlan == null) {
			studentCurricularPlan =
				new StudentCurricularPlan(
					student,
					persistentObjectOJB.readDegreeCurricularPlanByDegreeID(
						new Integer("" + almeida_aluno.getCurso())),
					getBranch(almeida_aluno),
					new Date(),
					new StudentCurricularPlanState(
						StudentCurricularPlanState.ACTIVE));

			studentCurricularPlan.setBranch(branch);
			studentCurricularPlan.setCurrentState(
				new StudentCurricularPlanState(
					StudentCurricularPlanState.ACTIVE));
			studentCurricularPlan.setDegreeCurricularPlan(
				persistentObjectOJB.readDegreeCurricularPlanByDegreeID(
					new Integer("" + almeida_aluno.getCurso())));
			studentCurricularPlan.setEnrolments(new ArrayList());
			studentCurricularPlan.setGivenCredits(new Double(0));
			studentCurricularPlan.setIdInternal(null);
			studentCurricularPlan.setSpecialization(null);
			studentCurricularPlan.setStartDate(new Date());
			studentCurricularPlan.setStudent(student);

			writeElement(studentCurricularPlan);
		}

		return studentCurricularPlan;
	}

	/**
	 * @param string
	 */
	private IBranch getBranch(Almeida_aluno almeida_aluno) {
		String branchCode = "";

		if (almeida_aluno.getRamo() != 0) {
			branchCode =
				""
					+ new Integer("" + almeida_aluno.getCurso())
					+ almeida_aluno.getRamo()
					+ "0";
			//+ almeida_aluno.getCodigoOrientacao();
		}

		return persistentObjectOJB.readBranchByCodeAndDegree(
			branchCode,
			new Integer("" + almeida_aluno.getCurso()));
	}

}