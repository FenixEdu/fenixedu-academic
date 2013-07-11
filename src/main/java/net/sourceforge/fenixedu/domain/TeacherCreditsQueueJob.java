package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.Atomic;

public class TeacherCreditsQueueJob extends TeacherCreditsQueueJob_Base {

    public TeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        super();
        setExecutionSemester(executionSemester);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        TeacherCredits.closeAllTeacherCredits(getExecutionSemester());
        QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setDone(true);
        return queueJobResult;
    }

    @Atomic
    public static TeacherCreditsQueueJob createTeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        return new TeacherCreditsQueueJob(executionSemester);
    }

}
