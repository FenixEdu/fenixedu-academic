package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExercise {
    protected Metadata run(String executionCourseId, String metadataId) throws FenixServiceException {
        return FenixFramework.getDomainObject(metadataId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExercise serviceInstance = new ReadExercise();

    @Atomic
    public static Metadata runReadExercise(String executionCourseId, String metadataId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId);
    }

}