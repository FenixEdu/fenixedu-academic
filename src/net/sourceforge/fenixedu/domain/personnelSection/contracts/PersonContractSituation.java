package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonContractSituation extends PersonContractSituation_Base {

    public PersonContractSituation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
	    final LocalDate endDate, final String step, final ContractSituation contractSituation,
	    final String contractSituationGiafId, final ProfessionalCategory professionalCategory,
	    final String professionalCategoryGiafId, final DateTime creationDate, final DateTime modifiedDate) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafProfessionalData(giafProfessionalData);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setStep(step);
	setContractSituation(contractSituation);
	setContractSituationGiafId(contractSituationGiafId);
	setProfessionalCategory(professionalCategory);
	setProfessionalCategoryGiafId(professionalCategoryGiafId);
	setCreationDate(creationDate);
	setModifiedDate(modifiedDate);
	setImportationDate(new DateTime());
    }

    public boolean hasValidDates() {
	return getBeginDate() != null && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    public boolean isValid() {
	return getProfessionalCategory() != null && hasValidDates();
    }

}
