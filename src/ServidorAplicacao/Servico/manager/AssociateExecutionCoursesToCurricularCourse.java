/*
 * Created on 9/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
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
				
				IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
				IDisciplinaExecucao executionCourse;
				
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
				
				if(curricularCourse == null)
					throw new NonExistingServiceException("b", null);
					
				List executionCourses = curricularCourse.getAssociatedExecutionCourses();
				
				Iterator iter = executionCourses.iterator();
				System.out.println("executionCoursesAssociados"+executionCourses);
				while(iter.hasNext()){
				Integer associatedExecutionPeriodId = ((IDisciplinaExecucao) iter.next()).getExecutionPeriod().getIdInternal();
				if(associatedExecutionPeriodId.equals(executionPeriodId))
					throw new ExistingServiceException("message.unavailable.execution.period",null);
				}
				
				executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseId), false);
				if(executionCourse == null)
					throw new NonExistingServiceException("c", null);
				else {
						List curricularCourses = executionCourse.getAssociatedCurricularCourses();
						if(!executionCourses.contains(executionCourse) && !curricularCourses.contains(curricularCourse)) {
							persistentCurricularCourse.simpleLockWrite(curricularCourse);
							executionCourses.add(executionCourse);
							curricularCourses.add(curricularCourse);
							curricularCourse.setAssociatedExecutionCourses(executionCourses);
							executionCourse.setAssociatedCurricularCourses(curricularCourses);
						}
				}	 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}