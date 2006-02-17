package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Attends;

public class WeeklyWorkLoad extends WeeklyWorkLoad_Base implements Comparable<WeeklyWorkLoad> {

    public WeeklyWorkLoad(final Attends attends, final Integer weekOffset,
            final Integer contact, final Integer autonomousStudy, final Integer other) {
        super();

        if (attends == null || weekOffset == null) {
            throw new NullPointerException();
        }

        setAttends(attends);
        setContact(contact);
        setAutonomousStudy(autonomousStudy);
        setOther(other);
        setWeekOffset(weekOffset);
    }

    public int compareTo(final WeeklyWorkLoad weeklyWorkLoad) {
        if (weeklyWorkLoad == null) {
            throw new NullPointerException("Cannot compare weekly work load with null");
        }
        if (getAttends() != weeklyWorkLoad.getAttends()) {
            throw new IllegalArgumentException("Cannot compare weekly work loads of different attends.");
        }

        return getWeekOffset().compareTo(weeklyWorkLoad.getWeekOffset());
    }

}
