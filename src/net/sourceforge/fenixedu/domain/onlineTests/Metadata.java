package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Calendar;

/**
 * @author Susana Fernandes
 */

public class Metadata extends Metadata_Base {

    private Calendar learningTime;

    public Calendar getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(Calendar calendar) {
        learningTime = calendar;
    }

}