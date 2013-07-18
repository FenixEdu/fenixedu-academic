package net.sourceforge.fenixedu.applicationTier.Servico.publico;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 */
public class ReadExecutionCourse {

    @Service
    public static Object run(InfoExecutionPeriod infoExecutionPeriod, String code) {
        final ExecutionSemester executionSemester =
                RootDomainObject.getInstance().readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());
        ExecutionCourse iExecCourse = executionSemester.getExecutionCourseByInitials(code);

        if (iExecCourse != null) {
            return InfoExecutionCourse.newInfoFromDomain(iExecCourse);
        }
        return null;
    }

}