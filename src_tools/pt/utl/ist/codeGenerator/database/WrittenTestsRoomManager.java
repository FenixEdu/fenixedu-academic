package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;

public class WrittenTestsRoomManager extends HashSet<Room> {

    private final Map<ExecutionPeriod, EvaluationRoomManager> evaluationRoomManagerMap = new HashMap<ExecutionPeriod, EvaluationRoomManager>();

    public DateTime getNextDateTime(final ExecutionPeriod executionPeriod) {
	EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	if (evaluationRoomManager == null) {
	    evaluationRoomManager = new EvaluationRoomManager(
		    executionPeriod.getBeginDateYearMonthDay().plusMonths(1).toDateTimeAtMidnight(),
		    executionPeriod.getEndDateYearMonthDay().minusDays(31).toDateTimeAtMidnight(), 120, this);
	    evaluationRoomManagerMap.put(executionPeriod, evaluationRoomManager);
	}

        DateTime dateTime;
        Room oldRoom;
        
        do {
            dateTime = evaluationRoomManager.getNextDateTime();
            oldRoom = evaluationRoomManager.getNextOldRoom();
            
        } while (!oldRoom.isFree(dateTime.toYearMonthDay(), dateTime.plusMinutes(120).toYearMonthDay(), 
        	new HourMinuteSecond(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute()),
        	dateTime.plusMinutes(120).getHourOfDay() == 0 ?
        		new HourMinuteSecond(dateTime.plusMinutes(119).getHourOfDay(), dateTime.plusMinutes(119).getMinuteOfHour(), dateTime.plusMinutes(119).getSecondOfMinute()) :
        		    new HourMinuteSecond(dateTime.plusMinutes(120).getHourOfDay(),dateTime.plusMinutes(120).getMinuteOfHour(), dateTime.plusMinutes(120).getSecondOfMinute()),  
        	new DiaSemana(dateTime.getDayOfWeek() + 1), FrequencyType.DAILY, Integer.valueOf(1), Boolean.TRUE, Boolean.TRUE));

        return dateTime;
    }

    public Room getNextOldRoom(final ExecutionPeriod executionPeriod) {
	final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	return evaluationRoomManager.getNextOldRoom();
    }
}
