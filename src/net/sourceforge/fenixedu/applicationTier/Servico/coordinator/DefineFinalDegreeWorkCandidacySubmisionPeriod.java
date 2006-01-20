/*
 * Created on 2004/04/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 * 
 */
public class DefineFinalDegreeWorkCandidacySubmisionPeriod extends Service {

    public void run(Integer executionDegreeOID, Date startOfCandidacyPeriod, Date endOfCandidacyPeriod)
            throws ExcepcaoPersistencia {

        if (executionDegreeOID != null && startOfCandidacyPeriod != null && endOfCandidacyPeriod != null) {
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            ExecutionDegree cursoExecucao = (ExecutionDegree) persistentFinalDegreeWork.readByOID(
                    ExecutionDegree.class, executionDegreeOID);

            if (cursoExecucao != null) {
                Scheduleing scheduleing = persistentFinalDegreeWork
                        .readFinalDegreeWorkScheduleing(executionDegreeOID);

                if (scheduleing == null) {
                    scheduleing = DomainFactory.makeScheduleing();
                    scheduleing.setCurrentProposalNumber(new Integer(1));
                }

                scheduleing.setExecutionDegree(cursoExecucao);
                scheduleing.setStartOfCandidacyPeriod(startOfCandidacyPeriod);
                scheduleing.setEndOfCandidacyPeriod(endOfCandidacyPeriod);
            }

        }

    }

}