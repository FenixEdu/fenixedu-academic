package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExercise {
    protected Metadata run(String executionCourseId, String metadataId) throws FenixServiceException {
        return AbstractDomainObject.fromExternalId(metadataId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExercise serviceInstance = new ReadExercise();

    @Service
    public static Metadata runReadExercise(String executionCourseId, String metadataId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId);
    }

}