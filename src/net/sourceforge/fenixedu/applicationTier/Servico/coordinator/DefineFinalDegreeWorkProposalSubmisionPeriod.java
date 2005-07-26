/*
 * Created on 2004/03/10
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class DefineFinalDegreeWorkProposalSubmisionPeriod implements IService {

    public void run(Integer executionDegreeOID, Date startOfProposalPeriod, Date endOfProposalPeriod)
            throws ExcepcaoPersistencia {

        if (executionDegreeOID != null && startOfProposalPeriod != null && endOfProposalPeriod != null) {

            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            IExecutionDegree cursoExecucao = (IExecutionDegree) persistentFinalDegreeWork.readByOID(
                    ExecutionDegree.class, executionDegreeOID);

            if (cursoExecucao != null) {
                IScheduleing scheduleing = persistentFinalDegreeWork
                        .readFinalDegreeWorkScheduleing(executionDegreeOID);

                if (scheduleing == null) {
                    scheduleing = DomainFactory.makeScheduleing();
                    scheduleing.setCurrentProposalNumber(new Integer(1));
                }

                scheduleing.setExecutionDegree(cursoExecucao);
                scheduleing.setStartOfProposalPeriod(startOfProposalPeriod);
                scheduleing.setEndOfProposalPeriod(endOfProposalPeriod);
            }

        }

    }

}