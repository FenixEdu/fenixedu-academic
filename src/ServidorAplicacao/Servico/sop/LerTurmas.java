package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurmas
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
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurmas implements IServico {

	private static LerTurmas _servico = new LerTurmas();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerTurmas getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerTurmas() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerTurmas";
	}

	public Object run(
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod,
		Integer curricularYear) {

		List classesList = null;
		List infoClassesList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurmaPersistente classDAO = sp.getITurmaPersistente();


			System.out.println("1-"+infoExecutionPeriod.getInfoExecutionYear().getYear());
			System.out.println("2-"+infoExecutionPeriod.getName());

			System.out.println("3-"+curricularYear);
			System.out.println("4-"+infoExecutionDegree.getInfoExecutionYear().getYear());
			System.out.println("5-"+infoExecutionDegree.getInfoDegreeCurricularPlan().getName());
			System.out.println("6-"+infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());			





			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);

			System.out.println("1-"+infoExecutionPeriod.getInfoExecutionYear().getYear());
			System.out.println("2-"+infoExecutionPeriod.getName());


			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					infoExecutionDegree);

			classesList =
				classDAO
					.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
					executionPeriod,
					curricularYear,
					executionDegree);

			Iterator iterator = classesList.iterator();
			infoClassesList = new ArrayList();
			while (iterator.hasNext()) {
				ITurma elem = (ITurma) iterator.next();
				infoClassesList.add(Cloner.copyClass2InfoClass(elem));
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new RuntimeException(ex);
		}
		return infoClassesList;
	}

}