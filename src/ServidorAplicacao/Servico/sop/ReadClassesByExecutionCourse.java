package ServidorAplicacao.Servico.sop;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse implements IServico {
	private static ReadClassesByExecutionCourse serviceInstance =
		new ReadClassesByExecutionCourse();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadClassesByExecutionCourse getService() {
		return serviceInstance;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadClassesByExecutionCourse() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadClassesByExecutionCourse";
	}

	public List run(InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		List infoClasses = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionCourse executionCourse =
				(IExecutionCourse) sp
					.getIPersistentExecutionCourse()
					.readByOID(
					ExecutionCourse.class,
					infoExecutionCourse.getIdInternal());

			List classes =
				sp.getITurmaPersistente().readByExecutionCourse(
					executionCourse);

			infoClasses =
				(List) CollectionUtils.collect(classes, new Transformer() {
				public Object transform(Object arg0) {
					return Cloner.copyClass2InfoClass((ITurma) arg0);
				}
			});
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return infoClasses;
	}
}
