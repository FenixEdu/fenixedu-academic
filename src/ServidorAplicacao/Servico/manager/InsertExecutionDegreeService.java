/*
 * Created on 14/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class InsertExecutionDegreeService implements IServico {

	private static InsertExecutionDegreeService service = new InsertExecutionDegreeService();

	public static InsertExecutionDegreeService getService() {
		return service;
	}

	private InsertExecutionDegreeService() {
	}

	public final String getNome() {
		return "InsertExecutionDegreeService";
	}
	

	public String run(String executionYearString, String coordenatorIdString, String tempExamMapString, Integer degreeCurricularPlanId) throws FenixServiceException {

		ICursoExecucaoPersistente persistentExecutionDegree = null;
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
				
				IPersistentExecutionYear persistentExecutionYear = persistentSuport.getIPersistentExecutionYear();
				IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);
				
				persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
				ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
				// if it doesn´t exist in the database yet
				if(executionDegree == null) {
					
					executionDegree = new CursoExecucao();			
					executionDegree.setExecutionYear(executionYear);
					executionDegree.setCurricularPlan(degreeCurricularPlan);
					
					IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
					ITeacher teacher = (ITeacher) persistentTeacher.readByOId(new Teacher(new Integer(coordenatorIdString)), false);
					executionDegree.setCoordinator(teacher);
					
					if(tempExamMapString.compareTo("") != 0)
						executionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));

					persistentExecutionDegree.simpleLockWrite(executionDegree);
					return null;
				}
				//if already exists
				else 
					return executionYearString;
				
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}