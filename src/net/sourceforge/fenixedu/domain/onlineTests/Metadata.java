package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public List<IQuestion> getVisibleQuestions() {
        final List<IQuestion> visibleQuestions = new ArrayList<IQuestion>();
        for (final IQuestion question : getQuestions()) {
            if (question.getVisibility()) {
                visibleQuestions.add(question);
            }
        }
        return visibleQuestions;
    }


}