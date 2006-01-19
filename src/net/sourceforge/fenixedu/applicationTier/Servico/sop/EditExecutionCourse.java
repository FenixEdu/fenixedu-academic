package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class EditExecutionCourse extends Service {

    public InfoExecutionCourse run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
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

    private List<InfoCurricularCourse> getInfoCurricularCoursesFrom(final ExecutionCourse executionCourse) {
        final List<InfoCurricularCourse> result = new ArrayList(executionCourse.getAssociatedCurricularCoursesCount());
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            result.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        return result;
    }
}