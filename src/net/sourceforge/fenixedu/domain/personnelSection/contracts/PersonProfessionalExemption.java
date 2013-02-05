package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import org.joda.time.Interval;
import org.joda.time.PeriodType;

public class PersonProfessionalExemption extends PersonProfessionalExemption_Base {

    public PersonProfessionalExemption() {
        super();
    }

    public boolean isValid() {
        return false;
    }

    private Interval getInterval() {
        return getBeginDate() != null && getEndDate() != null ? new Interval(getBeginDate().toDateTimeAtStartOfDay(),
                getEndDate().plusDays(1).toDateTimeAtStartOfDay()) : null;
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

    public boolean getIsSabaticalOrEquivalent() {
        return false;
    }

    public boolean getHasMandatoryCredits() {
        return false;
    }

    public boolean getGiveCredits() {
        return false;
    }

    public boolean isLongDuration() {
        Integer daysBetween =
                new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().toDateTimeAtStartOfDay().plusDays(1))
                        .toPeriod(PeriodType.days()).getDays();
        return (daysBetween == null || daysBetween >= 90);
    }
}
