/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.util.Calendar;
import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ExamsMapSlot {

    private Calendar day;

    private List exams;

    public ExamsMapSlot(Calendar day, List exams) {
        setDay(day);
        setExams(exams);
    }

    public Calendar getDay() {
        return day;
    }

    public List getExams() {
        return exams;
    }

    public void setDay(Calendar calendar) {
        day = calendar;
    }

    public void setExams(List list) {
        exams = list;
    }
}