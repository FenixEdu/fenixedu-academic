package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 */
public class ReadExecutionCourse {

    @Atomic
    public static Object run(InfoExecutionPeriod infoExecutionPeriod, String code) {
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        ExecutionCourse iExecCourse = executionSemester.getExecutionCourseByInitials(code);

        if (iExecCourse != null) {
            return InfoExecutionCourse.newInfoFromDomain(iExecCourse);
        }
        return null;
    }

}