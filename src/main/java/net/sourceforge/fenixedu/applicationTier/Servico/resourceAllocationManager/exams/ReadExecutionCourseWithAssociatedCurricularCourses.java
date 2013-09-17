package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ana e Ricardo
 * 
 */
public class ReadExecutionCourseWithAssociatedCurricularCourses {

    @Atomic
    public static InfoExecutionCourse run(String executionCourseID) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

            CollectionUtils.filter(infoCurricularCourse.getInfoScopes(), new Predicate() {
                @Override
                public boolean evaluate(Object arg0) {
                    InfoCurricularCourseScope scope = (InfoCurricularCourseScope) arg0;
                    return scope.getInfoCurricularSemester().getSemester()
                            .equals(executionCourse.getExecutionPeriod().getSemester());
                }
            });

            infoCurricularCourses.add(infoCurricularCourse);
        }

        infoExecutionCourse.setFilteredAssociatedInfoCurricularCourses(infoCurricularCourses);

        return infoExecutionCourse;
    }
}