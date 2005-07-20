package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 */
public class ReadExecutionCourse implements IService {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionCourse iExecCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(code, infoExecutionPeriod
                        .getIdInternal());

        if (iExecCourse != null) {
            return (InfoExecutionCourse) Cloner.get(iExecCourse);
        }
        return null;
    }

}
