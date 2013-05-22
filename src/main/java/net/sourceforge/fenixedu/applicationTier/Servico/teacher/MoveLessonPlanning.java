package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import pt.ist.fenixWebFramework.services.Service;

public class MoveLessonPlanning extends FenixService {

    protected void run(Integer executionCourseID, LessonPlanning lessonPlanning, Integer order) {
        lessonPlanning.moveTo(order);
    }

    // Service Invokers migrated from Berserk

    private static final MoveLessonPlanning serviceInstance = new MoveLessonPlanning();

    @Service
    public static void runMoveLessonPlanning(Integer executionCourseID, LessonPlanning lessonPlanning, Integer order)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, lessonPlanning, order);
    }

}