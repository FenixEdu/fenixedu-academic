package net.sourceforge.fenixedu.domain.residence;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.Bennu;

public class ResidenceYear extends ResidenceYear_Base {

    public ResidenceYear(ResidenceManagementUnit residenceManagementUnit) {
        this(getNextYear(), residenceManagementUnit);
    }

    public ResidenceYear(Integer year, ResidenceManagementUnit residenceManagementUnit) {
        super();
        setYear(year);
        setUnit(residenceManagementUnit);
        setRootDomainObject(Bennu.getInstance());
        for (Month month : Month.values()) {
            new ResidenceMonth(month, this);
        }
    }

    public Set<ResidenceMonth> getSortedMonths() {
        TreeSet<ResidenceMonth> months = new TreeSet<ResidenceMonth>(new BeanComparator("month"));
        months.addAll(getMonths());
        return months;
    }

    private static Integer getNextYear() {
        Integer next = null;
        for (ResidenceYear year : Bennu.getInstance().getResidenceYearsSet()) {
            if (next == null || year.getYear() > next) {
                next = year.getYear();
            }
        }

        return next != null ? next + 1 : new DateTime().getYear();
    }

    public static ResidenceYear getCurrentYear() {
        Integer currentYear = new LocalDate().getYear();
        for (ResidenceYear year : Bennu.getInstance().getResidenceYearsSet()) {
            if (year.getYear().equals(currentYear)) {
                return year;
            }
        }
        return null;
    }

    public static boolean hasCurrentYear() {
        return getCurrentYear() != null;
    }

    public boolean isFor(int year) {
        return getYear().intValue() == year;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidenceMonth> getMonths() {
        return getMonthsSet();
    }

    @Deprecated
    public boolean hasAnyMonths() {
        return !getMonthsSet().isEmpty();
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
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
