package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Susana Fernandes
 */

public class Metadata extends Metadata_Base {

    public Calendar getLearningTime() {
        if (getLearningTimeDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getLearningTimeDate());
            return calendar;
        }

        return null;
    }

    public void setLearningTime(Calendar calendar) {
        final Date date = (calendar != null) ? calendar.getTime() : null;
        setLearningTimeDate(date);
    }

}