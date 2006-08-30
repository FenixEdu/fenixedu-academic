package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class CreateResultParticipation extends Service {

    public ResultParticipation run(ResultParticipationCreationBean bean) throws FenixServiceException {
	final Result result = bean.getResult();
	final ResultParticipationRole role = bean.getResultParticipationRole();
	final Unit organization = bean.getOrganization();
	Person participator = bean.getParticipator();

	if (participator == null) {
	    final InsertExternalPerson newPerson = new InsertExternalPerson();
	    final String participatorName = bean.getParticipatorName();

	    if (organization != null) {
		participator = (newPerson.run(participatorName, organization)).getPerson();
	    } else {
		final String orgName = bean.getOrganizationName();
		participator = (newPerson.run(participatorName, orgName)).getPerson();
	    }
	}

	return new ResultParticipation(result, participator, role);
    }
}
