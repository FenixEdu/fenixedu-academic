package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonFunctionsAccumulation extends PersonFunctionsAccumulation_Base {

	public PersonFunctionsAccumulation(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
			final LocalDate endDate, final BigDecimal hours, final FunctionsAccumulation functionsAccumulation,
			final String functionsAccumulationGiafId, final ProfessionalRegime professionalRegime,
			final String professionalRegimeGiafId, final DateTime creationDate, final DateTime modifiedDate) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setGiafProfessionalData(giafProfessionalData);
		setBeginDate(beginDate);
		setEndDate(endDate);
		setHours(hours);
		setFunctionsAccumulation(functionsAccumulation);
		setFunctionsAccumulationGiafId(functionsAccumulationGiafId);
		setProfessionalRegime(professionalRegime);
		setProfessionalRegimeGiafId(professionalRegimeGiafId);
		setCreationDate(creationDate);
		setModifiedDate(modifiedDate);
		setImportationDate(new DateTime());
	}

	public boolean isValid() {
		return getFunctionsAccumulation() != null && getBeginDate() != null
				&& (getEndDate() == null || !getBeginDate().isAfter(getEndDate())) && getProfessionalRegime() != null;
	}

}
