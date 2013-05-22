package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExercise extends FenixService {
    protected Metadata run(Integer executionCourseId, Integer metadataId) throws FenixServiceException {
        return rootDomainObject.readMetadataByOID(metadataId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExercise serviceInstance = new ReadExercise();

    @Service
    public static Metadata runReadExercise(Integer executionCourseId, Integer metadataId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId);
    }

}