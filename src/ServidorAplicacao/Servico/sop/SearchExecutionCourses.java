/*
 *
 * Created on 2003/08/21
 */

package ServidorAplicacao.Servico.sop;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 **/
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.CurricularYear;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class SearchExecutionCourses implements IServico {

	private static SearchExecutionCourses _servico =
		new SearchExecutionCourses();
	/**
	 * The singleton access method of this class.
	 **/
	public static SearchExecutionCourses getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private SearchExecutionCourses() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "SearchExecutionCourses";
	}

	public List run(
		InfoExecutionPeriod infoExecutionPeriod,
		InfoExecutionDegree infoExecutionDegree,
		InfoCurricularYear infoCurricularYear,
		String executionCourseName)
		throws FenixServiceException {

		List result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				(IExecutionPeriod) sp
					.getIPersistentExecutionPeriod()
					.readByOID(
					ExecutionPeriod.class,
					infoExecutionPeriod.getIdInternal());

			ICursoExecucao executionDegree =
				(ICursoExecucao) sp.getICursoExecucaoPersistente().readByOID(
					CursoExecucao.class,
					infoExecutionDegree.getIdInternal());

			ICurricularYear curricularYear = null;
			if (infoCurricularYear != null) {
				curricularYear =
					(ICurricularYear) sp
						.getIPersistentCurricularYear()
						.readByOID(
						CurricularYear.class,
						infoCurricularYear.getIdInternal());
			}

			List executionCourses =
				sp
					.getIDisciplinaExecucaoPersistente()
					.readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
						executionPeriod,
						executionDegree,
						curricularYear,
						executionCourseName);

			result =
				(
					List) CollectionUtils
						.collect(executionCourses, new Transformer() {
				public Object transform(Object arg0) {
					return Cloner.copyIExecutionCourse2InfoExecutionCourse(
						(IDisciplinaExecucao) arg0);
				}
			});
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}