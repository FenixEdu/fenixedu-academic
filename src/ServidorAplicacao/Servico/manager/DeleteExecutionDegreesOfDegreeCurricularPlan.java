/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlan implements IServico {

	private static DeleteExecutionDegreesOfDegreeCurricularPlan service = new DeleteExecutionDegreesOfDegreeCurricularPlan();

	public static DeleteExecutionDegreesOfDegreeCurricularPlan getService() {
		return service;
	}

	private DeleteExecutionDegreesOfDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "DeleteExecutionDegreesOfDegreeCurricularPlan";
	}
	
	// delete a set of executionDegrees
	public List run(List executionDegreesIds) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

			Iterator iter = executionDegreesIds.iterator();

			Boolean result = new Boolean(true);
			List undeletedExecutionDegreesYears = new ArrayList();
			Integer executionDegreeId;
			ICursoExecucao executionDegree;

			while (iter.hasNext()) {

				executionDegreeId = (Integer) iter.next();
				CursoExecucao execDegree = new CursoExecucao();
				execDegree.setIdInternal(executionDegreeId);
				executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(execDegree, false);
				if (executionDegree != null)
					result = persistentExecutionDegree.delete(executionDegree);			
				if(result.equals(new Boolean(false))) {
					undeletedExecutionDegreesYears.add((String) executionDegree.getExecutionYear().getYear());	
				}
			}	
			return undeletedExecutionDegreesYears;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}