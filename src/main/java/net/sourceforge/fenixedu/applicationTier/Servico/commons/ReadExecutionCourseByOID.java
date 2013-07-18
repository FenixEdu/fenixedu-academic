package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCourseByOID {

    @Service
    public static InfoExecutionCourse run(Integer oid) {
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(oid);
        return (executionCourse != null) ? InfoExecutionCourse.newInfoFromDomain(executionCourse) : null;
    }

}