package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class DisapproveThesisDiscussion extends Service {

    public void run(Thesis thesis) throws FenixServiceException {
        thesis.rejectEvaluation();
    }
}
