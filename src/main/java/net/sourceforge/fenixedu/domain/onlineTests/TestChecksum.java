package net.sourceforge.fenixedu.domain.onlineTests;

public class TestChecksum extends TestChecksum_Base {

    public TestChecksum() {
        super();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasChecksumCode() {
        return getChecksumCode() != null;
    }

}
