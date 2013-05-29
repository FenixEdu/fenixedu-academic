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

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Ana e Ricardo
 * 
 */
public class ReadExecutionCourseWithAssociatedCurricularCourses {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static InfoExecutionCourse run(String executionCourseID) throws FenixServiceException {
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
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