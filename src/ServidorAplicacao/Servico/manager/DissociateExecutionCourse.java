/*
 * Created on 11/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DissociateExecutionCourse implements IServico {

	private static DissociateExecutionCourse service = new DissociateExecutionCourse();

	public static DissociateExecutionCourse getService() {
		return service;
	}

	private DissociateExecutionCourse() {
	}

	public final String getNome() {
		return "DissociateExecutionCourse";
	}
	
//	dissociate an execution course
	public void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {

		 try {

			 	ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			 	
			 	IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
				
				if(curricularCourse == null)
					throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
				
				IPersistentExecutionCourse persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
				IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseId), false);
				
				if(executionCourse == null)
					throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
				
			 	List executionCourses = curricularCourse.getAssociatedExecutionCourses();
			 	List curricularCourses = executionCourse.getAssociatedCurricularCourses();
			 	
			 	if(!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
					persistentCurricularCourse.simpleLockWrite(curricularCourse);
			 		executionCourses.remove(executionCourse);
			 		curricularCourses.remove(curricularCourse);
			 		executionCourse.setAssociatedCurricularCourses(curricularCourses);
			 		curricularCourse.setAssociatedExecutionCourses(executionCourses);
			 	}
		 } catch (ExcepcaoPersistencia e) {
			 throw new FenixServiceException(e);
		 }
	}
}