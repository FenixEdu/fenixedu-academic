package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério
 * 22/Dez/2003
 * 
 */
public class ReadExecutionDegreesByExecutionPeriodId implements IServico {

	private static ReadExecutionDegreesByExecutionPeriodId service = new ReadExecutionDegreesByExecutionPeriodId();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionDegreesByExecutionPeriodId getService() {
		return service;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExecutionDegreesByExecutionPeriodId() {
	}


	public final String getNome() {
		return "ReadExecutionDegreesByExecutionPeriodId";
	}

	public List run(Integer executionPeriodId) throws FenixServiceException {

		ArrayList infoExecutionDegreeList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			
			if (executionPeriodId == null) {
				throw new FenixServiceException("nullId");
			}
			
			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			executionPeriod.setIdInternal(executionPeriodId);
			executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);

			List executionDegrees = executionDegreeDAO.readByExecutionYear(executionPeriod.getExecutionYear());

			Iterator iterator = executionDegrees.iterator();
			infoExecutionDegreeList = new ArrayList();

			while (iterator.hasNext()) {
				ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
				infoExecutionDegreeList.add(Cloner.get(executionDegree));
			}

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
		return infoExecutionDegreeList;
	}
}