package pt.utl.ist.codeGenerator.database;

import java.util.Stack;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class LessonRoomManager extends Stack<Space> {

    private int nextWeekDay = 2;

    public int getNextWeekDay() {
        return nextWeekDay;
    }

    private HourMinuteSecond nextHourMinuteSecond = new HourMinuteSecond(8, 0, 0);
    private HourMinuteSecond limitHourMinuteSecond = new HourMinuteSecond(20, 0, 0);

    public HourMinuteSecond getNextHourMinuteSecond(final int durationInMinutes) {
        final HourMinuteSecond nextHourMinuteSecond = this.nextHourMinuteSecond;
        if (nextHourMinuteSecond.isAfter(limitHourMinuteSecond)) {
            this.nextHourMinuteSecond = new HourMinuteSecond(8, 0, 0);
            if (nextWeekDay == 7) {
                this.nextWeekDay = 2;
                pop();
            } else {
                this.nextWeekDay = nextWeekDay + 1;
            }
        } else {
            this.nextHourMinuteSecond = nextHourMinuteSecond.plusMinutes(durationInMinutes);
        }
        return nextHourMinuteSecond;
    }

    public Space getNextOldRoom() {
        return peek();
    }

}
