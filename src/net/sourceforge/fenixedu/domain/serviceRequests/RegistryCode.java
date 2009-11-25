package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

import org.joda.time.LocalDate;

public class RegistryCode extends RegistryCode_Base {
    protected RegistryCode(InstitutionRegistryCodeGenerator generator, RegistryDiplomaRequest request) {
	super();
	setRegistryCodeGenerator(generator);
	setRectorateSubmissionBatch(generator.getCurrentRectorateSubmissionBatch());
	addDocumentRequest(request);
	String type = null;
	switch (request.getRequestedCycle()) {
	case FIRST_CYCLE:
	    type = "L";
	    break;
	case SECOND_CYCLE:
	    type = "M";
	    break;
	case THIRD_CYCLE:
	    type = "D";
	    break;
	}
	setCode(generator.getNextNumber() + "/ISTc" + type + "/" + new LocalDate().toString("yy"));
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getRegistryCodeGenerator().getRootDomainObject();
    }
}
