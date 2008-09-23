package net.sourceforge.fenixedu.applicationTier.Servico.candidacy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public class ExecuteStateOperation extends FenixService {

    public void run(final Operation operation, final Person person) throws FenixServiceException {
	operation.execute(person);
    }
}