package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
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
	return getContractSituation() != null && hasValidDates();
    }

    public boolean contains(final LocalDate date) {
	return getBeginDate() != null
		&& (!getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date)));
    }

    public boolean overlaps(final Interval interval) {
	Interval categoryInterval = getInterval();
	return getBeginDate() != null
		&& ((categoryInterval != null && categoryInterval.overlaps(interval)) || (categoryInterval == null && !getBeginDate()
			.isAfter(interval.getEnd().toLocalDate())));
    }

    private Interval getInterval() {
	return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
		getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean isAfter(PersonContractSituation otherPersonContractSituation) {
	if (!isValid()) {
	    return false;
	}
	if (!otherPersonContractSituation.isValid()) {
	    return true;
	}
	if (getEndDate() == null) {
	    return otherPersonContractSituation.getEndDate() == null ? getBeginDate().isAfter(
		    otherPersonContractSituation.getBeginDate()) : true;
	} else {
	    return otherPersonContractSituation.getEndDate() == null ? false : getEndDate().isAfter(
		    otherPersonContractSituation.getEndDate());
	}
    }

    public boolean isActive(LocalDate date) {
	return !getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date));
    }

    public boolean betweenDates(LocalDate begin, LocalDate end) {
	Interval dateInterval = new Interval(begin.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay().plusDays(1));
	return betweenDates(dateInterval);
    }

    public boolean betweenDates(Interval interval) {
	if (isValid()) {
	    if (getEndDate() == null) {
		return !getBeginDate().isAfter(interval.getEnd().toLocalDate());
	    }
	    return getInterval().overlaps(interval);
	}
	return false;
    }

    public Integer getWeeklyLessonHours(Interval interval) {
	if (getContractSituation().getEndSituation() || (getContractSituation().getServiceExemption() && !hasMandatoryCredits())
		|| !getContractSituation().getInExercise()) {
	    return 0;
	}
	ProfessionalCategory professionalCategory = getProfessionalCategory(interval);
	ProfessionalRegime professionalRegime = getProfessionalRegime(interval);
	if (professionalCategory == null || professionalRegime == null) {
	    return Integer.MAX_VALUE;
	}
	BigDecimal fullTimeEquivalent = professionalRegime.getFullTimeEquivalent();
	if (fullTimeEquivalent != null) {
	    if (fullTimeEquivalent.equals(BigDecimal.ZERO)) {
		return Integer.valueOf(0);
	    } else if (fullTimeEquivalent.equals(BigDecimal.ONE)) {
		Integer weighting = professionalRegime.getWeighting();
		if (weighting != null && weighting > 100) {
		    if (professionalCategory.isTeacherInvitedAssistantCategory()) {
			return Integer.valueOf(12);
		    } else if (professionalCategory.isTeacherInvitedProfessorCategory()) {
			return Integer.valueOf(6);
		    }
		} else if (professionalCategory.isTeacherInvitedCategory()) {
		    return Integer.valueOf(12);
		}
		if (professionalCategory.isTeacherMonitorCategory()) {
		    return Integer.valueOf(0);
		} else if (professionalCategory.isTeacherAssistantCategory()) {
		    return Integer.valueOf(9);
		} else if (professionalCategory.isTeacherProfessorCategory()) {
		    return Integer.valueOf(6);
		}
	    } else if (fullTimeEquivalent.compareTo(new BigDecimal(0.5)) > 0) {
		return fullTimeEquivalent.multiply(new BigDecimal(10)).intValue() + 2;
	    } else {
		return fullTimeEquivalent.multiply(new BigDecimal(10)).intValue() + 1;
	    }
	}
	return 0;
    }

    private ProfessionalCategory getProfessionalCategory(Interval interval) {
	return getGiafProfessionalData().getPersonProfessionalData().getLastProfessionalCategory(getGiafProfessionalData(),
		interval.getStart().toLocalDate(), interval.getEnd().toLocalDate());
    }

    private ProfessionalRegime getProfessionalRegime(Interval interval) {
	return getGiafProfessionalData().getPersonProfessionalData().getLastProfessionalRegime(getGiafProfessionalData(),
		interval.getStart().toLocalDate(), interval.getEnd().toLocalDate());
    }

    public PersonProfessionalExemption getPersonProfessionalExemption() {
	PersonProfessionalExemption exemption = null;
	int exemptionDays = 0;
	for (PersonProfessionalExemption personProfessionalExemption : getGiafProfessionalData()
		.getValidPersonProfessionalExemption()) {
	    if (personProfessionalExemption.getBeginDate().equals(getBeginDate())) {
		if (personProfessionalExemption instanceof PersonSabbatical) {
		    return personProfessionalExemption;
		}
		int personProfessionalExemptionDays = Days.daysBetween(personProfessionalExemption.getBeginDate(),
			personProfessionalExemption.getEndDate()).getDays();
		if (personProfessionalExemptionDays > exemptionDays) {
		    exemption = personProfessionalExemption;
		    exemptionDays = personProfessionalExemptionDays;
		}
	    }
	}
	return exemption;
    }

    public boolean countForCredits(Interval interval) {
	PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
	return getContractSituation().getMustHaveAssociatedExemption() ? personProfessionalExemption != null
		&& personProfessionalExemption.countForCredits(getProfessionalCategory(interval)) : (getContractSituation()
		.getServiceExemption() && getContractSituation().getGiveCredits());
    }

    private boolean hasMandatoryCredits() {
	PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
	return getContractSituation().getMustHaveAssociatedExemption() ? personProfessionalExemption != null
		&& personProfessionalExemption.getHasMandatoryCredits() : getContractSituation().getServiceExemption()
		&& getContractSituation().getHasMandatoryCredits();
    }

    public LocalDate getServiceExemptionEndDate() {
	PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
	return getContractSituation().getMustHaveAssociatedExemption() ? getEndDate() != null ? getEndDate()
		: personProfessionalExemption != null ? personProfessionalExemption.getEndDate() : null : getEndDate();
    }

    public int getDaysInInterval(Interval intervalWithNextPeriods) {
	LocalDate beginDate = getBeginDate().isBefore(intervalWithNextPeriods.getStart().toLocalDate()) ? intervalWithNextPeriods
		.getStart().toLocalDate() : getBeginDate();
	LocalDate endDate = getEndDate() == null || getEndDate().isAfter(intervalWithNextPeriods.getEnd().toLocalDate()) ? intervalWithNextPeriods
		.getEnd().toLocalDate() : getEndDate();
	return Days.daysBetween(beginDate, endDate).getDays();
    }
}
