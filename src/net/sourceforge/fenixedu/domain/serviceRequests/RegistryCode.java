package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

import org.joda.time.LocalDate;

public class RegistryCode extends RegistryCode_Base {
    private RegistryCode(InstitutionRegistryCodeGenerator generator, DocumentRequest request, CycleType cycle) {
	setRegistryCodeGenerator(generator);
	addDocumentRequest(request);
	String type = null;
	switch (cycle) {
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
	setCode(generator.getNextNumber() + "/ISTC" + type + "/" + new LocalDate().toString("yy"));
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, RegistryDiplomaRequest request) {
	this(generator, request, request.getRequestedCycle());
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, DiplomaRequest request) {
	this(generator, request, request.getWhatShouldBeRequestedCycle());
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getRegistryCodeGenerator().getRootDomainObject();
    }
}
