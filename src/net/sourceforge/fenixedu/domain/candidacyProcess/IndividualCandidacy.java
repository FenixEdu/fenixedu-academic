package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.RootDomainObject;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {
    
    protected IndividualCandidacy() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
