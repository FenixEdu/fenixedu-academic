package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionCourseByOID {

    @Service
    public static InfoExecutionCourse run(String oid) {
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(oid);
        return (executionCourse != null) ? InfoExecutionCourse.newInfoFromDomain(executionCourse) : null;
    }

}