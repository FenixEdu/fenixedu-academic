package net.sourceforge.fenixedu.domain.institutionalRelations.academic;

public class Program extends Program_Base {

    public Program() {
        super();
    }

    public boolean isMobility() {
        return false;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRegistrationAgreement() {
        return getRegistrationAgreement() != null;
    }

}
