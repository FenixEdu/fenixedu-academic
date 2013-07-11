package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import pt.ist.fenixframework.Atomic;

public class ChangeConvokeStatus {

    @Atomic
    public static void run(Vigilancy vigilancy, AttendingStatus status) {
        vigilancy.setStatus(status);
        for (ExecutionCourse ec : vigilancy.getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.evaluation.generic.edited.vigilancy", vigilancy.getWrittenEvaluation()
                            .getPresentationName(), ec.getName(), ec.getDegreePresentationString());
        }
    }
}