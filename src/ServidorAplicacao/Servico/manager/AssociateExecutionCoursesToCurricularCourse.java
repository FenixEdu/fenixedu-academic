/*
 * Created on 9/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class AssociateExecutionCoursesToCurricularCourse implements IServico {
	
	private static AssociateExecutionCoursesToCurricularCourse service = new AssociateExecutionCoursesToCurricularCourse();

	public static AssociateExecutionCoursesToCurricularCourse getService() {
		return service;
	}

	private AssociateExecutionCoursesToCurricularCourse() {
	}

	public final String getNome() {
		return "AssociateExecutionCoursesToCurricularCourse";
	}
	

	public void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId) throws FenixServiceException {
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport.getIPersistentExecutionPeriod();
				IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOId(new ExecutionPeriod(executionPeriodId), false);
				
				if(executionPeriod == null)
					throw new NonExistingServiceException();
				
				IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
				IDisciplinaExecucao executionCourse;
				
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
				
				if(curricularCourse == null)
					throw new NonExistingServiceException();
					
				List executionCourses = curricularCourse.getAssociatedExecutionCourses();
		
				
				executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseId), false);
				if(executionCourse == null)
					throw new NonExistingServiceException();
				else {
						if(!executionCourses.contains(executionCourse)) {
							persistentCurricularCourse.simpleLockWrite(curricularCourse);
							executionCourses.add(executionCourse);
							curricularCourse.setAssociatedExecutionCourses(executionCourses);
						}
				}	 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}