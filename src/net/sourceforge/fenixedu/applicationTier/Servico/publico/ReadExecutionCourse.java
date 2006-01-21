package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

/**
 * @author João Mota
 */
public class ReadExecutionCourse extends Service {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) throws ExcepcaoPersistencia {
        final IPersistentExecutionCourse executionCourseDAO = persistentSupport.getIPersistentExecutionCourse();

        ExecutionCourse iExecCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(code, infoExecutionPeriod
                        .getIdInternal());

        if (iExecCourse != null) {
            return InfoExecutionCourse.newInfoFromDomain(iExecCourse);
        }
        return null;
    }

}
