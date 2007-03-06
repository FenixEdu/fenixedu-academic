package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.DateTime;

public class WrittenTestsRoomManager extends HashSet<OldRoom> {

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
        OldRoom oldRoom;
        OccupationPeriod occupationPeriod;
        do {
            dateTime = evaluationRoomManager.getNextDateTime();
            oldRoom = evaluationRoomManager.getNextOldRoom();
            occupationPeriod = new OccupationPeriod(dateTime.toYearMonthDay(), dateTime.plusMinutes(120).toYearMonthDay());
        } while (!oldRoom.isFree(occupationPeriod, dateTime.toCalendar(null), dateTime.plusMinutes(120).getHourOfDay() == 0 ? dateTime.plusMinutes(119).toCalendar(null) : dateTime.plusMinutes(120).toCalendar(null), 
        	new DiaSemana(dateTime.getDayOfWeek() + 1), Integer.valueOf(1), Integer.valueOf(1), Boolean.TRUE, Boolean.TRUE));

        return dateTime;
    }

    public OldRoom getNextOldRoom(final ExecutionPeriod executionPeriod) {
	final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	return evaluationRoomManager.getNextOldRoom();
    }

}
