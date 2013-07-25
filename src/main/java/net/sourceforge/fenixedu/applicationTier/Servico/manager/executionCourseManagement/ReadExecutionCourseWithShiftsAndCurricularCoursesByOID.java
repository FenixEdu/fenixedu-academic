/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID {

    @Service
    public static InfoExecutionCourse run(final Integer oid) {
        InfoExecutionCourse infoExecutionCourse = null;

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(oid);
        if (executionCourse != null) {
            infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        }

        return infoExecutionCourse;
    }

}