package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

public class LoadStudents extends DataFileLoader {

	private LoadStudents() throws IOException {

	}

	public static void main(String[] args) {
		try {
			new LoadStudents();
		} catch (IOException e) {
			e.printStackTrace(System.out);
			System.out.println("Erro a carregar as propriedades");
			System.exit(1);
		}
		try {

			// Obter as interfaces para o suporte persistente.
			IPessoaPersistente iPessoaPersistente =
				SuportePersistenteOJB.getInstance().getIPessoaPersistente();
//			IPersistentStudent iAlunoPersistente =
//				SuportePersistenteOJB.getInstance().getIPersistentStudent();

			// Ler o ficheiro de entrada.
			BufferedReader bufferedReader =
				new BufferedReader(new FileReader(getDataFilePath()));
			String line = bufferedReader.readLine();
			while (line != null) {
				carregarAluno(line);
				line = bufferedReader.readLine();
			}
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			// Criar o utilizador do SOP.
			Pessoa pessoa = new Pessoa();
			pessoa.setUsername("sop");
			pessoa.setPassword(PasswordEncryptor.encryptPassword("sop123sop"));
			pessoa.setNumeroDocumentoIdentificacao("sop111111");
			pessoa.setTipoDocumentoIdentificacao(
				new TipoDocumentoIdentificacao(
					TipoDocumentoIdentificacao.OUTRO));

			IPessoa tempPerson =
				(IPessoa) iPessoaPersistente.readDomainObjectByCriteria(pessoa);
			if (tempPerson != null) {
				iPessoaPersistente.simpleLockWrite(pessoa);
            }
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
		} catch (Exception e) {
//			System.out.println("ATENCAO: LoadStudents: " + e.toString());

			try {
				SuportePersistenteOJB.getInstance().cancelarTransaccao();
			} catch (Exception x) {
//				System.out.println(
//					"ATENCAO: LoadStudents: cancelarTransaccao: "
//						+ e.toString());
			}
		}
	}

	private static void carregarAluno(String line)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

		// Obter os dados do aluno.
		String numero = stringTokenizer.nextToken();
		String bi = stringTokenizer.nextToken();
		String nome = stringTokenizer.nextToken();
		String chaveLicenciatura = stringTokenizer.nextToken();
		//String chaveRamo = 
		stringTokenizer.nextToken();
		String email = stringTokenizer.nextToken();

		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		// Obter as interfaces para o suporte persistente.
		IPessoaPersistente iPessoaPersistente = sp.getIPessoaPersistente();
		IPersistentStudent iAlunoPersistente = sp.getIPersistentStudent();

		IPersistentDegreeCurricularPlan curricularDegreePlanDAO =
			sp.getIPersistentDegreeCurricularPlan();
		IStudentCurricularPlanPersistente studentCurricularPlanDAO =
			sp.getIStudentCurricularPlanPersistente();

		// Criar pessoa.
		sp.iniciarTransaccao();

		IPessoa pessoaTemp = new Pessoa();
		pessoaTemp.setNumeroDocumentoIdentificacao(bi);

		IPessoa pessoa =
			(IPessoa)
				 iPessoaPersistente.readDomainObjectByCriteria(pessoaTemp);

		if (pessoa == null) {
			pessoa = pessoaTemp;
		}
		iPessoaPersistente.simpleLockWrite(pessoa);
		
		pessoa.setNumeroDocumentoIdentificacao(bi);
		pessoa.setEmail(email);
		pessoa.setNome(nome);
		pessoa.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		pessoa.setUsername("L"+numero);

		//iPessoaPersistente.escreverPessoa(pessoa);

		IStudent studentTemp = new Student(null, null, pessoa, null);

		IStudent student =
			(IStudent) iAlunoPersistente.readDomainObjectByCriteria(
				studentTemp);

		if (student == null) {
			student = studentTemp;
		}
		iAlunoPersistente.simpleLockWrite(student);
		
		student.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
		student.setNumber(new Integer(numero));
		student.setState(new StudentState(0));

		//iAlunoPersistente.lockWrite(student);

		/* Create Student Curricular Plan */

		IStudentCurricularPlan studentCurricularPlanTemp =
			new StudentCurricularPlan(
				student,
				null,
				null,
				null,
				new StudentCurricularPlanState(
					StudentCurricularPlanState.ACTIVE));

		IStudentCurricularPlan studentCurricularPlan =
			(
				IStudentCurricularPlan) studentCurricularPlanDAO
					.readDomainObjectByCriteria(
				studentCurricularPlanTemp);
		if (studentCurricularPlan == null) {
			studentCurricularPlan = studentCurricularPlanTemp;
		}
        studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);
		/* FIXME : BRONCA TEMOS QUE TER ESTA INFORMAÇÂO*/
		studentCurricularPlan.setStartDate(new Date());

		IDegreeCurricularPlan planoCurricularCursoTemp =
			new DegreeCurricularPlan();
		ICurso curso = new Curso();
		curso.setSigla(MiddlewareUtil.gerarSiglaDeLicenciatura(Integer.parseInt(chaveLicenciatura)));
		planoCurricularCursoTemp.setDegree(curso);
		IDegreeCurricularPlan planoCurricularCurso =
			(
				IDegreeCurricularPlan) curricularDegreePlanDAO
					.readDomainObjectByCriteria(
				planoCurricularCursoTemp);

		/**
		 * Supondo que os planos curriculares dos cursos já estão inseridos.
		 * Senão estiverem dá bronca...
		 */

		if (planoCurricularCurso == null) {
			System.out.println("------------- BRONCA --------");
			planoCurricularCurso = planoCurricularCursoTemp;
		}
		
		studentCurricularPlan.setDegreeCurricularPlan(planoCurricularCurso);


		sp.confirmarTransaccao();
	}
}