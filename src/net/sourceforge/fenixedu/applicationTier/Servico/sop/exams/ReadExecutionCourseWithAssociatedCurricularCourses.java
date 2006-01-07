package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeAndScopes;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ana e Ricardo
 * 
 */
public class ReadExecutionCourseWithAssociatedCurricularCourses implements IService {

    public InfoExecutionCourse run(Integer executionCourseID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                .newInfoFromDomain(executionCourse);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegreeAndScopes
                    .newInfoFromDomain(curricularCourse);

            CollectionUtils.filter(infoCurricularCourse.getInfoScopes(), new Predicate() {
                public boolean evaluate(Object arg0) {
                    InfoCurricularCourseScope scope = (InfoCurricularCourseScope) arg0;
                    return scope.getInfoCurricularSemester().getSemester().equals(
                            executionCourse.getExecutionPeriod().getSemester());
                }
            });
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoExecutionCourse;
    }
}