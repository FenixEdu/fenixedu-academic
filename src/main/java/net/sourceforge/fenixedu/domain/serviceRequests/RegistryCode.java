package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;

import org.joda.time.LocalDate;

public class RegistryCode extends RegistryCode_Base {
    public static Comparator<RegistryCode> COMPARATOR_BY_CODE = new Comparator<RegistryCode>() {
        @Override
        public int compare(RegistryCode o1, RegistryCode o2) {
            if (o1.getCode().compareTo(o2.getCode()) != 0) {
                return o1.getCode().compareTo(o2.getCode());
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }
    };

    private RegistryCode(InstitutionRegistryCodeGenerator generator, AcademicServiceRequest request, CycleType cycle) {
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

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, IRegistryDiplomaRequest request) {
        this(generator, (AcademicServiceRequest) request, request.getRequestedCycle());
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, IDiplomaSupplementRequest request) {
        this(generator, (AcademicServiceRequest) request, request.getRequestedCycle());
    }

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, IDiplomaRequest request) {
        this(generator, (AcademicServiceRequest) request, request.getWhatShouldBeRequestedCycle());
    }

    public CycleType getCycle(AcademicServiceRequest request) {
        if (request.isRequestForPhd()) {
            return CycleType.THIRD_CYCLE;
        } else if (request.isRequestForRegistration()) {
            RegistrationAcademicServiceRequest registrationRequest = (RegistrationAcademicServiceRequest) request;
            switch (registrationRequest.getDegreeType()) {
            case DEGREE:
                return CycleType.FIRST_CYCLE;
            case MASTER_DEGREE:
                return CycleType.SECOND_CYCLE;
            default:
                throw new DomainException("error.registryCode.unableToGuessCycleTypeToGenerateCode");
            }
        }

        throw new DomainException("error.registryCode.request.neither.is.phd.nor.registration.request");
    }

    public Integer getCodeNumber() {
        return Integer.parseInt(getCode().substring(0, getCode().indexOf('/')));
    }

    public Integer getYear() {
        return Integer.parseInt(getCode().substring(getCode().length() - 2, getCode().length()));
    }

    protected RootDomainObject getRootDomainObject() {
        return getRegistryCodeGenerator().getRootDomainObject();
    }

    public Set<CycleType> getAssociatedCycles() {
        Set<CycleType> cycleTypes = new HashSet<CycleType>();

        for (AcademicServiceRequest request : getDocumentRequest()) {
            IRectorateSubmissionBatchDocumentEntry entry = (IRectorateSubmissionBatchDocumentEntry) request;

            cycleTypes.add(entry.getRequestedCycle());
        }

        return cycleTypes;
    }

    public static RegistryCode findRegistryCodeForNumberAndYear(CycleType cycleType, Integer codeNumber, Integer year) {
        if (year < 0 || year > 99) {
            throw new DomainException("error.RegistryCode.invalid.year");
        }

        for (InstitutionRegistryCodeGenerator generator : RootDomainObject.getInstance().getRegistryCodeGenerator()) {
            if (!generator.hasInstitution()) {
                continue;
            }

            for (RegistryCode registryCode : generator.getRegistryCode()) {
                if (!registryCode.getAssociatedCycles().contains(cycleType)) {
                    continue;
                }

                if (year == registryCode.getYear() && codeNumber == registryCode.getCodeNumber()) {
                    return registryCode;
                }
            }
        }

        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest> getDocumentRequest() {
        return getDocumentRequestSet();
    }

    @Deprecated
    public boolean hasAnyDocumentRequest() {
        return !getDocumentRequestSet().isEmpty();
    }

    @Deprecated
    public boolean hasRegistryCodeGenerator() {
        return getRegistryCodeGenerator() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
