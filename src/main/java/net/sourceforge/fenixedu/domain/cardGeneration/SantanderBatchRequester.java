package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;

public class SantanderBatchRequester extends SantanderBatchRequester_Base {

    private SantanderBatchRequester() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SantanderBatchRequester(Person requester) {
        this();
        setPerson(requester);
    }

    public void delete() {
        setPerson(null);
        setSantanderBatch(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSantanderBatch() {
        return getSantanderBatch() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
