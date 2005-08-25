package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExecutionCourse implements IService {

    public InfoExecutionCourse run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, infoExecutionCourse.getIdInternal());
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<InfoCurricularCourse> infoCurricularCourses = getInfoCurricularCoursesFrom(executionCourse);
        infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);

        executionCourse.edit(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(),
                infoExecutionCourse.getTheoreticalHours(), infoExecutionCourse.getTheoPratHours(),
                infoExecutionCourse.getPraticalHours(), infoExecutionCourse.getLabHours(),
                infoExecutionCourse.getComment());

        return infoExecutionCourse;
    }

    private List<InfoCurricularCourse> getInfoCurricularCoursesFrom(final IExecutionCourse executionCourse) {
        final List<InfoCurricularCourse> result = new ArrayList(executionCourse.getAssociatedCurricularCoursesCount());
        for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            result.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        return result;
    }
}