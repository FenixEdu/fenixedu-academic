package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;

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
	return evaluationRoomManager.getNextDateTime();
    }

    public OldRoom getNextOldRoom(final ExecutionPeriod executionPeriod) {
	final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
	return evaluationRoomManager.getNextOldRoom();
    }

}
