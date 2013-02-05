package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SantanderBatchRequester extends SantanderBatchRequester_Base {

    private SantanderBatchRequester() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public SantanderBatchRequester(Person requester) {
        this();
        setPerson(requester);
    }

    public void delete() {
        removePerson();
        removeSantanderBatch();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
