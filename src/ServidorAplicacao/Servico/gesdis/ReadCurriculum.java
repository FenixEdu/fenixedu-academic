package ServidorAplicacao.Servico.gesdis;

import DataBeans.InfoCurriculum;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadCurriculum implements IServico {

	private static ReadCurriculum service = new ReadCurriculum();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadCurriculum getService() {

		return service;

	}

	/**
	 * The ctor of this class.
	 **/

	private ReadCurriculum() {

	}

	/**
	 * Returns the name of this service.
	 **/

	public final String getNome() {

		return "ReadCurriculum";

	}

	public InfoCurriculum run(InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		try {
			ICurriculum curriculum = null;
			IDisciplinaExecucao executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoExecutionCourse);

			ISuportePersistente sp;

			sp = SuportePersistenteOJB.getInstance();

			curriculum =
				sp.getIPersistentCurriculum().readCurriculumByExecutionCourse(
					executionCourse);

			

			InfoCurriculum infoCurriculum =null;
			if (curriculum!=null){
				infoCurriculum =Cloner.copyICurriculum2InfoCurriculum(curriculum);
			}

			return infoCurriculum;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}
