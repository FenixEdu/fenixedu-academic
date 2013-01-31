package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonProfessionalContract extends PersonProfessionalContract_Base {

	public PersonProfessionalContract(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
			final LocalDate endDate, final ContractSituation contractSituation, final String contractSituationGiafId,
			final DateTime creationDate, final DateTime modifiedDate) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setGiafProfessionalData(giafProfessionalData);
		setBeginDate(beginDate);
		setEndDate(endDate);
		setContractSituation(contractSituation);
		setContractSituationGiafId(contractSituationGiafId);
		setCreationDate(creationDate);
		setModifiedDate(modifiedDate);
		setImportationDate(new DateTime());
	}

	public boolean isValid() {
		return getContractSituation() != null && getBeginDate() != null
				&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
	}
}
