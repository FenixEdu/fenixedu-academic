/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

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

public class EditExecutionDegree implements IServico {

	private static EditExecutionDegree service = new EditExecutionDegree();

	public static EditExecutionDegree getService() {
		return service;
	}

	private EditExecutionDegree() {
	}

	public final String getNome() {
		return "EditExecutionDegree";
	}
	

	public List run(String executionYearString, String coordenatorIdString, String tempExamMapString, Integer degreeCurricularPlanId, Integer executionDegreeId) throws FenixServiceException {

		ICursoExecucaoPersistente persistentExecutionDegree = null;
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
				CursoExecucao execDegree = new CursoExecucao();
				execDegree.setIdInternal(executionDegreeId);
				ICursoExecucao oldExecutionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(execDegree, false);
				List result = new ArrayList(2);
				
				// first check if the executionDegree still exists - concurrency control
				if(oldExecutionDegree != null) {
					boolean modified = false;
					ICursoExecucao newExecutionDegree = null;
					if(executionYearString.compareTo("") != 0) {
						IPersistentExecutionYear persistentExecutionYear = persistentSuport.getIPersistentExecutionYear();
						IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);
						IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
						IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
						newExecutionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
						if(newExecutionDegree != null) {
							result.add("alreadyExisting");
							result.add(executionYearString);
							return result;
						}
						oldExecutionDegree.setExecutionYear(executionYear);
						modified = true;
					}		
					if(tempExamMapString.compareTo("") != 0) {
						oldExecutionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));
						modified = true;
					}
					if(coordenatorIdString.compareTo("") != 0) {
							IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
							ITeacher teacher = (ITeacher) persistentTeacher.readByOId(new Teacher(new Integer(coordenatorIdString)), false);
							oldExecutionDegree.setCoordinator(teacher);
							modified = true;
					}
					if(modified)
						persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
					return null;
				}
				result.add("desapeared");
				return result;
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}