package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ReadExecutionCourse {

    public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException {
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(idInternal);

        if (executionCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}