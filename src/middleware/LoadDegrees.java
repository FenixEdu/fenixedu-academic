package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class LoadDegrees extends DataFileLoader{
	

	private LoadDegrees() throws IOException{
		super();
	}

	public static void main(String[] args) {
	
		try {
			new LoadDegrees();
		} catch (IOException e) {
			e.printStackTrace(System.out);
			System.out.println("Erro a carregar as propriedades");
			System.exit(1);
		}
		try {

			// Ler o ficheiro de entrada.
			BufferedReader bufferedReader =
				new BufferedReader(new FileReader(getDataFilePath()));
			String line = bufferedReader.readLine();
			while (line != null) {
				carregarLicenciatura(line);
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("CarregarLicenciatura: " + e.toString());
		}
	}

	private static void carregarLicenciatura(String line)
		throws ExcepcaoPersistencia {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());
		// Obter os dados da licenciatura.
		String codigo = stringTokenizer.nextToken();
		String nome = stringTokenizer.nextToken();

		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

		ICursoExecucaoPersistente iLicenciaturaExecucaoPersistente =
			sp.getICursoExecucaoPersistente();
		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp.getIPersistentDegreeCurricularPlan(); 
		
		//IRamoPersistente iRamoPersistente = SuportePersistente.getInstance().iRamoPersistente();       

		if (codigo.endsWith("0")) { 

			// Obter código da licenciatura.
			int codigoLicenciatura = Integer.parseInt(codigo) / 10;

			// Criar licenciatura.
			ICurso degree =
				new Curso(
					MiddlewareUtil.gerarSiglaDeLicenciatura(codigoLicenciatura),
					nome,
					new TipoCurso(TipoCurso.LICENCIATURA));

			IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan(nome, degree);
			sp.iniciarTransaccao();

			//iLicenciaturaPersistente.lockWrite(degree);
			degreeCurricularPlanDAO.lockWrite(degreeCurricularPlan);

			// Criar licenciaturas execução.
			IExecutionYear executionYear = new ExecutionYear("2002/03");
			IDegreeCurricularPlan curricularPlan = new DegreeCurricularPlan("planoCurricular",degree);
			ICursoExecucao executionDegree = null;
				new CursoExecucao(executionYear,curricularPlan);

			iLicenciaturaExecucaoPersistente.lockWrite(executionDegree);

			sp.confirmarTransaccao();
		} else {
			/* Ignoramos para já os Ramos */
			// Obter o código da licenciatura.
			//            int codigoLicenciatura = Integer.parseInt(codigo) / 10;
			//            
			//            // Obter o código do ramo.
			//            int codigoRamo = Integer.parseInt(codigo) % 10;
			//            
			//            // Criar ramo.
			//            Ramo ramo = new Ramo(codigoRamo, nome, codigoLicenciatura);
			//            iRamoPersistente.escreverRamo(ramo);
		}

	}

}