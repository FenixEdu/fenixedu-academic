package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LoadCourses extends DataFileLoader {
	static int i = 0;
	private LoadCourses() throws IOException {
	
	}

	public static void main(String[] args) {
	
		try {
			new LoadCourses();
		} catch (IOException e) {
			e.printStackTrace(System.out);
			System.out.println("Erro a carregar as propriedades");
			System.exit(1);
		}
		
		try {

			// Obter as interfaces para o suporte persistente.
//			IPersistentCurricularCourse iDisciplinaCurricularPersistente =
//				SuportePersistenteOJB
//					.getInstance()
//					.getIPersistentCurricularCourse();
//			IDisciplinaExecucaoPersistente iDisciplinaExecucaoPersistente =
//				SuportePersistenteOJB
//					.getInstance()
//					.getIDisciplinaExecucaoPersistente();

			// Apagar todas as disciplinas execucao.
			//iDisciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();

			// Apagar as associações de disciplinas curriculares a disciplinas execucao.
			//iDisciplinaCurricularPersistente.apagarAssociacoesDeDisciplinaCurricularADisciplinaExecucao();

			// Ler o ficheiro de entrada.
			BufferedReader bufferedReader =
				new BufferedReader(new FileReader(getDataFilePath()));
			String line = bufferedReader.readLine();
			while (line != null) {
				carregarDisciplinaCurricular(line);
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

	private static void carregarDisciplinaCurricular(String line)
		throws ExcepcaoPersistencia {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

		// Obter os dados da disciplina.
		//codigo composto
		stringTokenizer.nextToken();
		String chaveLicenciatura = stringTokenizer.nextToken();
		// chave ramo
		stringTokenizer.nextToken();
		// ano curricular
		stringTokenizer.nextToken();
		// semestre
		stringTokenizer.nextToken();
		String codigoDisciplina = stringTokenizer.nextToken();
		String cargaHorariaTeorica = stringTokenizer.nextToken();
		String cargaHorariaPratica = stringTokenizer.nextToken();
		String cargaHorariaLaboratorial = stringTokenizer.nextToken();
		String cargaHorariaTeoricoPratica = stringTokenizer.nextToken();
		String nomeDisciplina = stringTokenizer.nextToken();

		//String sigla = gerarSiglaDeDisciplina(nomeDisciplina);
		//String sigla = "";
		String sigla = codigoDisciplina;

		// Obter as interfaces para o suporte persistente.
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		
		IPersistentCurricularCourse iDisciplinaCurricularPersistente =
			sp
				.getIPersistentCurricularCourse();
		IPersistentDegreeCurricularPlan curricularDegreePlanDAO =
			sp
				.getIPersistentDegreeCurricularPlan();
		//        IDisciplinaExecucaoPersistente iDisciplinaExecucaoPersistente = SuportePersistente.getInstance().iDisciplinaExecucaoPersistente();

		// Criar a disciplina curricular.
		ICurricularCourse disciplinaCurricular = new CurricularCourse();
		disciplinaCurricular.setCode(sigla);
		disciplinaCurricular.setCredits(null);
//		disciplinaCurricular.setCurricularYear(new Integer(anoCurricular));

		disciplinaCurricular.setDepartmentCourse(null);
		disciplinaCurricular.setLabHours(new Double(cargaHorariaLaboratorial));
		disciplinaCurricular.setName(nomeDisciplina);
		disciplinaCurricular.setPraticalHours(new Double(cargaHorariaPratica));
//		disciplinaCurricular.setSemester(new Integer(semestre));
		disciplinaCurricular.setTheoPratHours(
			new Double(cargaHorariaTeoricoPratica));
		disciplinaCurricular.setTheoreticalHours(
			new Double(cargaHorariaTeorica));

		String degreeCode =
			MiddlewareUtil.gerarSiglaDeLicenciatura(
				Integer.parseInt(chaveLicenciatura));

		IDegreeCurricularPlan planoCurricularCursoTemp =
			new DegreeCurricularPlan();
		ICurso curso = new Curso();
		curso.setSigla(degreeCode);
		planoCurricularCursoTemp.setDegree(curso);
		
		sp.iniciarTransaccao();
		
		IDegreeCurricularPlan planoCurricularCurso =
			(
				IDegreeCurricularPlan) curricularDegreePlanDAO
					.readDomainObjectByCriteria(
				planoCurricularCursoTemp);

		if (planoCurricularCurso == null) {
			planoCurricularCurso = planoCurricularCursoTemp;
		}

		disciplinaCurricular.setDegreeCurricularPlan(planoCurricularCurso);

		
		ICurricularCourse criteriaCurricularCourse = new CurricularCourse();
//		criteriaCurricularCourse.setSemester(disciplinaCurricular.getSemester());
		criteriaCurricularCourse.setName(disciplinaCurricular.getName());
		criteriaCurricularCourse.setCode(disciplinaCurricular.getCode());
		
		criteriaCurricularCourse = (ICurricularCourse) iDisciplinaCurricularPersistente.readDomainObjectByCriteria(criteriaCurricularCourse);
		
		if (criteriaCurricularCourse == null ){
			iDisciplinaCurricularPersistente.lockWrite(disciplinaCurricular);
		}else{
//			System.out.println("Course already in database:");
//			System.out.println("\t"+line);
			i++;
		}
			
		
		
		
		
		sp.confirmarTransaccao();
		
	}
}