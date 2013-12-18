package net.sourceforge.fenixedu.domain.cardGeneration;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Person;

public class SantanderProblem extends SantanderProblem_Base {

    public SantanderProblem() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SantanderProblem(SantanderBatch batch, Person person, String cause) {
        this();
        setArg(person.getUsername() + "\t" + cause);
        setSantanderBatch(batch);
    }

    public void delete() {
        setSantanderBatch(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasArg() {
        return getArg() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDescriptionKey() {
        return getDescriptionKey() != null;
    }

    @Deprecated
    public boolean hasSantanderBatch() {
        return getSantanderBatch() != null;
    }

}
