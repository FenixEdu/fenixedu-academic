package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public abstract class PersonIdentificationDocumentExtraInfo extends PersonIdentificationDocumentExtraInfo_Base {

    public PersonIdentificationDocumentExtraInfo() {
        super();
        setRegisteredInSystemTimestamp(new DateTime());
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRegisteredInSystemTimestamp() {
        return getRegisteredInSystemTimestamp() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
