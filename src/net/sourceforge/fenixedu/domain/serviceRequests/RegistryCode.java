package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

import org.joda.time.LocalDate;

public class RegistryCode extends RegistryCode_Base {
    public static Comparator<RegistryCode> COMPARATOR_BY_CODE = new Comparator<RegistryCode>() {
	@Override
	public int compare(RegistryCode o1, RegistryCode o2) {
	    if (o1.getCode().compareTo(o2.getCode()) != 0) {
		return o1.getCode().compareTo(o2.getCode());
	    }
	    return COMPARATOR_BY_ID.compare(o1, o2);
	}
    };

    private RegistryCode(InstitutionRegistryCodeGenerator generator, DocumentRequest request, CycleType cycle) {
	setRegistryCodeGenerator(generator);
	addDocumentRequest(request);
	String type = null;
	if (cycle == null) {
	    cycle = getCycle(request);
	}
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
	setCode(generator.getNextNumber(cycle) + "/ISTC" + type + "/" + new LocalDate().toString("yy"));
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, RegistryDiplomaRequest request) {
	this(generator, request, request.getRequestedCycle());
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, DiplomaSupplementRequest request) {
	this(generator, request, request.getRequestedCycle());
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, DiplomaRequest request) {
	this(generator, request, request.getWhatShouldBeRequestedCycle());
    }

    public CycleType getCycle(DocumentRequest request) {
	switch (request.getDegreeType()) {
	case DEGREE:
	    return CycleType.FIRST_CYCLE;
	case MASTER_DEGREE:
	    return CycleType.SECOND_CYCLE;
	default:
	    throw new DomainException("error.registryCode.unableToGuessCycleTypeToGenerateCode");
	}
    }

    public Integer getCodeNumber() {
	return Integer.parseInt(getCode().substring(0, getCode().indexOf('/')));
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getRegistryCodeGenerator().getRootDomainObject();
    }
}
