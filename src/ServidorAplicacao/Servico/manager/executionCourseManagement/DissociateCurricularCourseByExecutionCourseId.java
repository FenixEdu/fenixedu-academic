package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.List;

import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério
 * 23/Dez/2003
 */

public class DissociateCurricularCourseByExecutionCourseId implements IServico {

	private static DissociateCurricularCourseByExecutionCourseId _servico = new DissociateCurricularCourseByExecutionCourseId();

	/**
	  * The actor of this class.
	  **/

	private DissociateCurricularCourseByExecutionCourseId() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "DissociateCurricularCourseByExecutionCourseId";
	}

	/**
	 * Returns the _servico.
	 * @return DissociateCurricularCourseByExecutionCourseId
	 */
	public static DissociateCurricularCourseByExecutionCourseId getService() {
		return _servico;
	}

	public Object run(
		Integer executionCourseId,
		Integer curricularCourseId) throws FenixServiceException {

		try {
			List executionCourseList = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
			IPersistentCurricularCourse persistentCurricularCourse  =sp.getIPersistentCurricularCourse();
			
			if(executionCourseId == null) {
				throw new FenixServiceException("nullExecutionCourseId");
			}
			if(curricularCourseId == null) {
				throw new FenixServiceException("nullCurricularCourseId");
			}
			
			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(executionCourseId);
			executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, true);
			if(executionCourse == null) {
				throw new NonExistingServiceException("noExecutionCourse");
			}
			
			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseId);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
			if(curricularCourse == null) {
				throw new NonExistingServiceException("noCurricularCourse");
			}
			
			List curricularCourses = executionCourse.getAssociatedCurricularCourses();
			List executionCourses = curricularCourse.getAssociatedExecutionCourses();
			
			if(!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
				executionCourses.remove(executionCourse);
				curricularCourses.remove(curricularCourse);
				executionCourse.setAssociatedCurricularCourses(curricularCourses);
				curricularCourse.setAssociatedExecutionCourses(executionCourses);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		
		return Boolean.TRUE;
	}
}