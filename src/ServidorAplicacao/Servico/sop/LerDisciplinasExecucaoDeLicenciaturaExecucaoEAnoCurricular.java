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
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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

	public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer curricularYear) {

		List listDCDE = null;
		List listInfoDE = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

	
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
				

			ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

			listDCDE =
				executionCourseDAO
					.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
					curricularYear,
					executionPeriod, executionDegree);

			Iterator iterator = listDCDE.iterator();
			listInfoDE = new ArrayList();
			while (iterator.hasNext()) {
				IDisciplinaExecucao elem =
					(IDisciplinaExecucao) iterator.next();

				listInfoDE.add(Cloner.copyIExecutionCourse2InfoExecutionCourse(elem));

			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return listInfoDE;
	}

}