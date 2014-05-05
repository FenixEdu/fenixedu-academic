package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

public class ExamRoomManager extends HashSet<Space> {

    private final Map<ExecutionSemester, EvaluationRoomManager> evaluationRoomManagerMap =
            new HashMap<ExecutionSemester, EvaluationRoomManager>();

    public DateTime getNextDateTime(final ExecutionSemester executionPeriod) {
        EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
        if (evaluationRoomManager == null) {
            evaluationRoomManager =
                    new EvaluationRoomManager(executionPeriod.getEndDateYearMonthDay().minusDays(31).toDateTimeAtMidnight(),
                            executionPeriod.getEndDateYearMonthDay().toDateTimeAtMidnight(), 180, this);
            evaluationRoomManagerMap.put(executionPeriod, evaluationRoomManager);
        }
        return evaluationRoomManager.getNextDateTime();
    }

    public Space getNextOldRoom(final ExecutionSemester executionPeriod) {
        final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
        return evaluationRoomManager.getNextOldRoom();
    }

}
