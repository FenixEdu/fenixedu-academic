/*
 * Created on 2004/03/10
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Scheduleing;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class DefineFinalDegreeWorkProposalSubmisionPeriod implements IService {

	public DefineFinalDegreeWorkProposalSubmisionPeriod() {
		super();
	}

	public void run(
		Integer executionDegreeOID,
		Date startOfProposalPeriod,
		Date endOfProposalPeriod)
		throws FenixServiceException {

		if (executionDegreeOID != null
			&& startOfProposalPeriod != null
			&& endOfProposalPeriod != null) {

			try {
				ISuportePersistente persistentSupport =
					SuportePersistenteOJB.getInstance();
				IPersistentFinalDegreeWork persistentFinalDegreeWork =
					persistentSupport.getIPersistentFinalDegreeWork();

				ICursoExecucao cursoExecucao =
					(ICursoExecucao) persistentFinalDegreeWork.readByOID(
						CursoExecucao.class,
						executionDegreeOID);

				if (cursoExecucao != null) {
					IScheduleing scheduleing =
						(IScheduleing) persistentFinalDegreeWork.readFinalDegreeWorkScheduleing(
							executionDegreeOID);

					if (scheduleing == null) {
						scheduleing = new Scheduleing();
						scheduleing.setCurrentProposalNumber(new Integer(1));
					}

					persistentFinalDegreeWork.simpleLockWrite(scheduleing);
					scheduleing.setExecutionDegree(cursoExecucao);
					scheduleing.setStartOfProposalPeriod(startOfProposalPeriod);
					scheduleing.setEndOfProposalPeriod(endOfProposalPeriod);
				}
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}

		}

	}

}
