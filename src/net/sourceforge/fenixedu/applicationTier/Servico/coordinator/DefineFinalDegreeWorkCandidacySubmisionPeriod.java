/*
 * Created on 2004/04/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class DefineFinalDegreeWorkCandidacySubmisionPeriod extends Service {

    public void run(Integer executionDegreeOID, Date startOfCandidacyPeriod, Date endOfCandidacyPeriod)
            throws ExcepcaoPersistencia {

        if (executionDegreeOID != null && startOfCandidacyPeriod != null && endOfCandidacyPeriod != null) {
            ExecutionDegree cursoExecucao = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

            if (cursoExecucao != null) {
                Scheduleing scheduleing = cursoExecucao.getScheduling();
                if (scheduleing == null) {
                    scheduleing = new Scheduleing();
                    scheduleing.setCurrentProposalNumber(Integer.valueOf(1));
                }

                scheduleing.addExecutionDegrees(cursoExecucao);
                scheduleing.setStartOfCandidacyPeriod(startOfCandidacyPeriod);
                scheduleing.setEndOfCandidacyPeriod(endOfCandidacyPeriod);
            }
        }
    }

}
