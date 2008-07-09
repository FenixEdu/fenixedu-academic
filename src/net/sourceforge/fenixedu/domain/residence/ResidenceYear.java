package net.sourceforge.fenixedu.domain.residence;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public class ResidenceYear extends ResidenceYear_Base {

    public ResidenceYear(ResidenceManagementUnit residenceManagementUnit) {
	this(getNextYear(), residenceManagementUnit);
    }

    public ResidenceYear(Integer year, ResidenceManagementUnit residenceManagementUnit) {
	super();
	setYear(year);
	setUnit(residenceManagementUnit);
	setRootDomainObject(RootDomainObject.getInstance());
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
	for (ResidenceYear year : RootDomainObject.getInstance().getResidenceYears()) {
	    if (next == null || year.getYear() > next) {
		next = year.getYear();
	    }
	}

	return next != null ? next + 1 : new DateTime().getYear();
    }
    
}
