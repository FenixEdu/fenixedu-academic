package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonServiceExemption extends PersonServiceExemption_Base {

    public PersonServiceExemption(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final ServiceExemption serviceExemption, final String serviceExemptionGiafId,
	    final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafProfessionalData(giafProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setServiceExemption(serviceExemption);
	setServiceExemptionGiafId(serviceExemptionGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean isValid() {
	return getServiceExemption() != null && getBeginDate() != null
		&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

}
