/*
 * Created on 14/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionDegree;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertExecutionDegreeAtDegreeCurricularPlan implements IServico {

	private static InsertExecutionDegreeAtDegreeCurricularPlan service = new InsertExecutionDegreeAtDegreeCurricularPlan();

	public static InsertExecutionDegreeAtDegreeCurricularPlan getService() {
		return service;
	}

	private InsertExecutionDegreeAtDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "InsertExecutionDegreeAtDegreeCurricularPlan";
	}
	

	public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal()), false);
				
				IPersistentExecutionYear persistentExecutionYear = persistentSuport.getIPersistentExecutionYear();
				IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(infoExecutionDegree.getInfoExecutionYear().getYear());
				
				ICursoExecucaoPersistente persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
				
				ICursoExecucao executionDegree = new CursoExecucao();
				IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
				ITeacher coordinator = (ITeacher) persistentTeacher.readByOId(new Teacher(infoExecutionDegree.getInfoCoordinator().getIdInternal()), false);
				executionDegree.setCoordinator(coordinator);
				executionDegree.setCurricularPlan(degreeCurricularPlan);
				executionDegree.setExecutionYear(executionYear);
				executionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
				
				persistentExecutionDegree.lockWrite(executionDegree);
	
		} catch(ExistingPersistentException existingException) {
			throw new ExistingServiceException("O curso em execução referente ao ano lectivo em execução " + infoExecutionDegree.getInfoExecutionYear().getYear(), existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}