package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PersonProfessionalRegime extends PersonProfessionalRegime_Base {

    public PersonProfessionalRegime(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ProfessionalRegime professionalRegime, final String professionalRegimeGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setProfessionalRegime(professionalRegime);
        setProfessionalRegimeGiafId(professionalRegimeGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean isValid() {
        return getProfessionalRegime() != null && getBeginDate() != null
                && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
    }

    public boolean overlaps(final Interval interval) {
        Interval regimeInterval = getInterval();
        return getBeginDate() != null
                && ((regimeInterval != null && regimeInterval.overlaps(interval)) || (regimeInterval == null && !getBeginDate()
                        .isAfter(interval.getEnd().toLocalDate())));
    }

    public boolean isActive(LocalDate date) {
        return !getBeginDate().isAfter(date) && (getEndDate() == null || !getEndDate().isBefore(date));
    }

    public int getDaysInInterval(Interval interval) {
        LocalDate beginDate =
                getBeginDate().isBefore(interval.getStart().toLocalDate()) ? interval.getStart().toLocalDate() : getBeginDate();
        LocalDate endDate =
                getEndDate() == null || getEndDate().isAfter(interval.getEnd().toLocalDate()) ? interval.getEnd().toLocalDate() : getEndDate();
        return Days.daysBetween(beginDate, endDate).getDays();
    }

    public boolean betweenDates(LocalDate beginDate, LocalDate endDate) {
        if (isValid()) {
            if (getEndDate() == null) {
                return endDate == null || !getBeginDate().isAfter(endDate);
            }
            if (endDate == null) {
                return !beginDate.isAfter(getEndDate());
            }
            Interval dateInterval =
                    new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
            return getInterval().overlaps(dateInterval);
        }
        return false;
    }

    public boolean isAfter(PersonProfessionalRegime otherPersonProfessionalRegime) {
        if (!isValid()) {
            return false;
        }
        if (!otherPersonProfessionalRegime.isValid()) {
            return true;
        }
        if (getEndDate() == null) {
            return otherPersonProfessionalRegime.getEndDate() == null ? getBeginDate().isAfter(
                    otherPersonProfessionalRegime.getBeginDate()) : true;
        } else {
            return otherPersonProfessionalRegime.getEndDate() == null ? false : getEndDate().isAfter(
                    otherPersonProfessionalRegime.getEndDate());
        }
    }

    @Deprecated
    public boolean hasModifiedDate() {
        return getModifiedDate() != null;
    }

    @Deprecated
    public boolean hasProfessionalRegimeGiafId() {
        return getProfessionalRegimeGiafId() != null;
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
    public boolean hasProfessionalRegime() {
        return getProfessionalRegime() != null;
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
    public boolean hasGiafProfessionalData() {
        return getGiafProfessionalData() != null;
    }

}
