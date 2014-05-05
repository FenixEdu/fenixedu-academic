package net.sourceforge.fenixedu.domain.util.icalendar;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Shift;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

public class ClassEventBean extends EventBean {

    private Shift classShift;

    public ClassEventBean(DateTime begin, DateTime end, boolean allDay, Set<Space> rooms, String url, String note,
            Shift classShift) {
        super(null, begin, end, allDay, rooms, url, note);
        setClassShift(classShift);
    }

    public Shift getClassShift() {
        return classShift;
    }

    public void setClassShift(Shift classShift) {
        this.classShift = classShift;
    }

    @Override
    public String getTitle() {
        return getClassShift().getExecutionCourse().getNome() + " : " + getClassShift().getShiftTypesCapitalizedPrettyPrint();
    }

}
