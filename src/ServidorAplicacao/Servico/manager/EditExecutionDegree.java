/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
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
	

	public List run(InfoExecutionDegree infoExecutionDegree, Integer executionDegreeId) throws FenixServiceException {

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
					ICursoExecucao newExecutionDegree = null;
					InfoExecutionYear infoExecutionYear = infoExecutionDegree.getInfoExecutionYear();
					Integer degreeCurricularPlanId = infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal();
					
					// check if the execution year has been modified
					if(infoExecutionYear != null) {
						String executionYearString = infoExecutionYear.getYear();
						IPersistentExecutionYear persistentExecutionYear = persistentSuport.getIPersistentExecutionYear();
						IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);
						IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
						IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
						newExecutionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
						
						// check if the new executionDegree already exists...
						if(newExecutionDegree != null) {
							result.add("alreadyExisting");
							result.add(executionYearString);
							return result;
						}
						
						oldExecutionDegree.setExecutionYear(executionYear);
					}
					
					Boolean tempExamMap = infoExecutionDegree.getTemporaryExamMap();		
					if(tempExamMap != null)
						oldExecutionDegree.setTemporaryExamMap(tempExamMap);

					IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
					InfoTeacher infoTeacher = infoExecutionDegree.getInfoCoordinator();
					// check if the teacher has been modified
					if(infoTeacher != null) {
						ITeacher teacher = (ITeacher) persistentTeacher.readByOId(new Teacher(infoExecutionDegree.getInfoCoordinator().getIdInternal()), false);
						oldExecutionDegree.setCoordinator(teacher);
					}
					
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