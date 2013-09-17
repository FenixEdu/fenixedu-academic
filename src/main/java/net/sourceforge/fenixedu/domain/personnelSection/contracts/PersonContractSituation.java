package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

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
        Interval situationInterval = getInterval();
        return getBeginDate() != null
                && ((situationInterval != null && situationInterval.overlaps(interval)) || (situationInterval == null && !getBeginDate()
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

    public Double getWeeklyLessonHours(Interval interval) {
        if (getContractSituation().getEndSituation() || (getContractSituation().getServiceExemption() && !hasMandatoryCredits())
                || !getContractSituation().getInExercise()) {
            return Double.valueOf(0);
        }
        ProfessionalCategory professionalCategory = getProfessionalCategory();
        ProfessionalRegime professionalRegime = getDominantProfessionalRegime(interval);
        if (professionalCategory != null) {
            if (professionalRegime == null) {
                if (professionalCategory.isTeacherMonitorCategory()) {
                    return Double.valueOf(4);
                } else if (professionalCategory.isTeacherInvitedCategory()) {
                    return Double.valueOf(12);
                } else {
                    return Double.valueOf(9);
                }
            }
            BigDecimal fullTimeEquivalent = professionalRegime.getFullTimeEquivalent();
            if (fullTimeEquivalent == null) {
                Integer weighting = professionalRegime.getWeighting();
                if (weighting != null) {
                    if (weighting.compareTo(100) >= 0) {
                        fullTimeEquivalent = BigDecimal.ONE;
                    } else {
                        fullTimeEquivalent = new BigDecimal(weighting).divide(BigDecimal.valueOf(100));
                    }
                }
            }
            if (fullTimeEquivalent != null) {
                if (fullTimeEquivalent.equals(BigDecimal.ONE)) {
                    if (professionalCategory.isTeacherMonitorCategory()) {
                        return Double.valueOf(4);
                    } else if (professionalCategory.isTeacherInvitedCategory()) {
                        return Double.valueOf(12);
                    } else {
                        return Double.valueOf(9);
                    }
                } else {
                    return fullTimeEquivalent.multiply(new BigDecimal(12)).doubleValue();
                }
            }
        }
        return Double.valueOf(12);
    }

    private ProfessionalRegime getDominantProfessionalRegime(Interval interval) {
        return getGiafProfessionalData().getPersonProfessionalData().getDominantProfessionalRegime(getGiafProfessionalData(),
                interval, CategoryType.TEACHER);
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
                int personProfessionalExemptionDays =
                        Days.daysBetween(personProfessionalExemption.getBeginDate(), personProfessionalExemption.getEndDate())
                                .getDays();
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
        return getContractSituation().getMustHaveAssociatedExemption() && personProfessionalExemption != null ? personProfessionalExemption
                .getGiveCredits() : (getContractSituation().getServiceExemption() && getContractSituation().getGiveCredits() && isLongDuration(interval));
    }

    private boolean hasMandatoryCredits() {
        PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
        return getContractSituation().getMustHaveAssociatedExemption() && personProfessionalExemption != null ? personProfessionalExemption
                .getHasMandatoryCredits() : (getContractSituation().getServiceExemption() && getContractSituation()
                .getHasMandatoryCredits());
    }

    public LocalDate getServiceExemptionEndDate() {
        PersonProfessionalExemption personProfessionalExemption = getPersonProfessionalExemption();
        return getContractSituation().getMustHaveAssociatedExemption() ? getEndDate() != null ? getEndDate() : personProfessionalExemption != null ? personProfessionalExemption
                .getEndDate() : null : getEndDate();
    }

    public int getDaysInInterval(Interval intervalWithNextPeriods) {
        LocalDate beginDate =
                getBeginDate().isBefore(intervalWithNextPeriods.getStart().toLocalDate()) ? intervalWithNextPeriods.getStart()
                        .toLocalDate() : getBeginDate();
        LocalDate endDate =
                getEndDate() == null || getEndDate().isAfter(intervalWithNextPeriods.getEnd().toLocalDate()) ? intervalWithNextPeriods
                        .getEnd().toLocalDate() : getEndDate();
        return Days.daysBetween(beginDate, endDate).getDays();
    }

    public boolean isLongDuration(Interval interval) {
        Integer daysBetween = interval.toPeriod(PeriodType.days()).getDays();
        return (daysBetween == null || daysBetween >= 90);
    }
    @Deprecated
    public boolean hasModifiedDate() {
        return getModifiedDate() != null;
    }

    @Deprecated
    public boolean hasStep() {
        return getStep() != null;
    }

    @Deprecated
    public boolean hasContractSituation() {
        return getContractSituation() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategoryGiafId() {
        return getProfessionalCategoryGiafId() != null;
    }

    @Deprecated
    public boolean hasAnulationDate() {
        return getAnulationDate() != null;
    }

    @Deprecated
    public boolean hasImportationDate() {
        return getImportationDate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategory() {
        return getProfessionalCategory() != null;
    }

    @Deprecated
    public boolean hasContractSituationGiafId() {
        return getContractSituationGiafId() != null;
    }

    @Deprecated
    public boolean hasGiafProfessionalData() {
        return getGiafProfessionalData() != null;
    }

}
