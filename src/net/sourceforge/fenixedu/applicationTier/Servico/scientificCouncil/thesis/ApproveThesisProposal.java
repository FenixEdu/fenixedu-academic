package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ApproveThesisProposal extends Service {

    public void run(Thesis thesis) throws FenixServiceException {

        if (thesis == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        thesis.approveProposal();
    }
}
