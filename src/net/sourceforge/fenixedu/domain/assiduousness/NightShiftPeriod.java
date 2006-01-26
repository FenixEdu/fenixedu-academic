package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;

import org.joda.time.TimeOfDay;


public class NightShiftPeriod {

    private TimeInterval nightShift;

    public void setNightShift(TimeInterval nightShift) {
        this.nightShift = nightShift;
    }

    // TODO this must be defined from a prefs file
    public NightShiftPeriod() {
        super();
        TimeInterval nightShift = new TimeInterval(new TimeOfDay(20,0), new TimeOfDay(7,0), true);
        this.setNightShift(nightShift);
    }

    
}
