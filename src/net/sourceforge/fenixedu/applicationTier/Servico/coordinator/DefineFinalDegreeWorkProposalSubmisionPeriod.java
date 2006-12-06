/*
 * Created on 2004/03/10
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
public class DefineFinalDegreeWorkProposalSubmisionPeriod extends Service {

    public void run(Integer executionDegreeOID, Date startOfProposalPeriod, Date endOfProposalPeriod)
            throws ExcepcaoPersistencia {
        if (executionDegreeOID != null && startOfProposalPeriod != null && endOfProposalPeriod != null) {
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

            if (executionDegree != null) {
                Scheduleing scheduleing = executionDegree.getScheduling();

                if (scheduleing == null) {
                    scheduleing = new Scheduleing();
                    scheduleing.setCurrentProposalNumber(new Integer(1));
                    scheduleing.setAttributionByTeachers(Boolean.FALSE);
                }

                scheduleing.addExecutionDegrees(executionDegree);
                scheduleing.setStartOfProposalPeriod(startOfProposalPeriod);
                scheduleing.setEndOfProposalPeriod(endOfProposalPeriod);
            }
        }

    }

}