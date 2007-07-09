package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.Room;

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
        Room room;
        OccupationPeriod occupationPeriod;
        do {
            dateTime = evaluationRoomManager.getNextDateTime();
            room = evaluationRoomManager.getNextOldRoom();
            occupationPeriod = new OccupationPeriod(dateTime.toYearMonthDay(), dateTime.plusMinutes(120).toYearMonthDay());
        } while ( false /*!room.isFree(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(), dateTime.toCalendar(null), dateTime.plusMinutes(120).getHourOfDay() == 0 ? dateTime.plusMinutes(119).toCalendar(null) : dateTime.plusMinutes(120).toCalendar(null), 
        	new DiaSemana(dateTime.getDayOfWeek() + 1), Integer.valueOf(1), Integer.valueOf(1), Boolean.TRUE, Boolean.TRUE)*/);

        return dateTime;
    }

    public Room getNextOldRoom(final ExecutionPeriod executionPeriod) {
	final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	return evaluationRoomManager.getNextOldRoom();
    }

}
