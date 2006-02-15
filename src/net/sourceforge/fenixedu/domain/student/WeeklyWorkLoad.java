package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Attends;

import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class WeeklyWorkLoad extends WeeklyWorkLoad_Base {

    public static final Period PERIOD = Period.weeks(1);

    private transient Interval interval = null;

    public WeeklyWorkLoad(final Attends attends, final Integer contact, final Integer autonomousStudy, final Integer other) {
        super();
        setAttends(attends);
        setContact(contact);
        setAutonomousStudy(autonomousStudy);
        setOther(other);
        // TODO... set the correct date.
        setStartDate(new YearMonthDay());
    }

    public Interval getInterval() {
        // This does not need to be syncronized... the result value will always be the same...
        // not syncing may duplicate the work on a first call... but down the line, and in general
        // not syncing will actually be better performence wise.
        if (interval == null) {
            final YearMonthDay startDate = getStartDate();
            final YearMonthDay endDate = startDate.plus(PERIOD);
            interval = new Interval(startDate.toDateMidnight(), endDate.toDateMidnight());
        }
        return interval;
    }

}
