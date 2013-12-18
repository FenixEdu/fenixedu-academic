package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Collection;
import java.util.Collections;

import org.fenixedu.bennu.core.domain.Bennu;

public class AcademicServiceRequestYear extends AcademicServiceRequestYear_Base {
    private AcademicServiceRequestYear(final int year) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        setYear(Integer.valueOf(year));
        setLatestServiceRequestNumber(Integer.valueOf(0));
    }

    static final public AcademicServiceRequestYear readByYear(final int year, boolean create) {
        for (final AcademicServiceRequestYear requestYear : Bennu.getInstance().getAcademicServiceRequestYearsSet()) {
            if (requestYear.getYear().intValue() == year) {
                return requestYear;
            }
        }
        if (create) {
            return new AcademicServiceRequestYear(year);
        }
        return null;
    }

    public static Collection<AcademicServiceRequest> getAcademicServiceRequests(int year) {
        AcademicServiceRequestYear requestYear = readByYear(year, false);
        if (requestYear == null) {
            return Collections.emptySet();
        }
        return requestYear.getAcademicServiceRequests();
    }

    protected Integer generateServiceRequestNumber() {
        setLatestServiceRequestNumber(Integer.valueOf(getLatestServiceRequestNumber().intValue() + 1));
        return getLatestServiceRequestNumber();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest> getAcademicServiceRequests() {
        return getAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequests() {
        return !getAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLatestServiceRequestNumber() {
        return getLatestServiceRequestNumber() != null;
    }

}
