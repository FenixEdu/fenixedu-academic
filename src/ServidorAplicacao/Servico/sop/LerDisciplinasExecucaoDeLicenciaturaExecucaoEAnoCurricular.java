package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
 *
 * @author tfc130
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
	implements IServico {

	private static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular _servico =
		new LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular";
	}

	public List run(
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod,
		Integer curricularYear) {

		List listDCDE = null;
		List listInfoDE = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);

			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					infoExecutionDegree);

			ICurricularYear curricularYearFromDB =
					(ICurricularYear) sp
						.getIPersistentCurricularYear()
						.readByOID(
						CurricularYear.class,
						curricularYear);


			listDCDE =
				executionCourseDAO
					.readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
					executionPeriod,
					executionDegree,
					curricularYearFromDB,
					"");

			Iterator iterator = listDCDE.iterator();
			listInfoDE = new ArrayList();
			while (iterator.hasNext()) {
				IExecutionCourse elem =
					(IExecutionCourse) iterator.next();

				listInfoDE.add(
					Cloner.copyIExecutionCourse2InfoExecutionCourse(elem));

			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return listInfoDE;
	}

}