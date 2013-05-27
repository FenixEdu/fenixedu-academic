package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import pt.ist.fenixWebFramework.services.Service;

public class EditTest {

    protected void run(Integer executionCourseId, Integer testId, String title, String information) {
        Test test = RootDomainObject.getInstance().readTestByOID(testId);
        test.setTitle(title);
        test.setInformation(information);
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }

    // Service Invokers migrated from Berserk

    private static final EditTest serviceInstance = new EditTest();

    @Service
    public static void runEditTest(Integer executionCourseId, Integer testId, String title, String information)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, title, information);
    }

}