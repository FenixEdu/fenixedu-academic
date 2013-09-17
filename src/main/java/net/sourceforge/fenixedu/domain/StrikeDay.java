package net.sourceforge.fenixedu.domain;

public class StrikeDay extends StrikeDay_Base {

    public StrikeDay() {
        super();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDate() {
        return getDate() != null;
    }

}
