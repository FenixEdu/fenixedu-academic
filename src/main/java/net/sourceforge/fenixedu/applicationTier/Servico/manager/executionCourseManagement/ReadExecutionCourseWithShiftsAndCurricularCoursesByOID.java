/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID {

    @Service
    public static InfoExecutionCourse run(final String oid) {
        InfoExecutionCourse infoExecutionCourse = null;

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(oid);
        if (executionCourse != null) {
            infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        }

        return infoExecutionCourse;
    }

}