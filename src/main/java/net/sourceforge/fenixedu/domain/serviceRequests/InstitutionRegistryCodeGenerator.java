package net.sourceforge.fenixedu.domain.serviceRequests;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {
    public InstitutionRegistryCodeGenerator() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected Integer getNextNumber(CycleType cycle) {
        switch (cycle) {
        case FIRST_CYCLE:
            super.setCurrentFirstCycle((super.getCurrentFirstCycle() != null ? super.getCurrentFirstCycle() : 0) + 1);
            return super.getCurrentFirstCycle();
        case SECOND_CYCLE:
            super.setCurrentSecondCycle((super.getCurrentSecondCycle() != null ? super.getCurrentSecondCycle() : 0) + 1);
            return super.getCurrentSecondCycle();
        case THIRD_CYCLE:
            super.setCurrentThirdCycle((super.getCurrentThirdCycle() != null ? super.getCurrentThirdCycle() : 0) + 1);
            return super.getCurrentThirdCycle();
        default:
            throw new DomainException("error.InstitutionRegistryCodeGenerator.unsupportedCycle");
        }
    }

    public RegistryCode createRegistryFor(IRegistryDiplomaRequest request) {
        return new RegistryCode(this, request);
    }

    public RegistryCode createRegistryFor(IDiplomaSupplementRequest request) {
        return new RegistryCode(this, request);
    }

    public RegistryCode createRegistryFor(IDiplomaRequest request) {
        return new RegistryCode(this, request);
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode> getRegistryCode() {
        return getRegistryCodeSet();
    }

    @Deprecated
    public boolean hasAnyRegistryCode() {
        return !getRegistryCodeSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCurrentSecondCycle() {
        return getCurrentSecondCycle() != null;
    }

    @Deprecated
    public boolean hasCurrentThirdCycle() {
        return getCurrentThirdCycle() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasCurrentFirstCycle() {
        return getCurrentFirstCycle() != null;
    }

}
