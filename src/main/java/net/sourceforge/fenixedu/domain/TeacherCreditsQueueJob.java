package net.sourceforge.fenixedu.domain;

import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public static TeacherCreditsQueueJob createTeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        return new TeacherCreditsQueueJob(executionSemester);
    }

}
