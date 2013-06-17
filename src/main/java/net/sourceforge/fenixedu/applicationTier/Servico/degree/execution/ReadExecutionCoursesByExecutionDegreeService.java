package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionCoursesByExecutionDegreeService {

    public static class NonExistingExecutionDegree extends FenixServiceException {
        public NonExistingExecutionDegree() {
            super();
        }
    }

    @Service
    public static List run(String executionDegreeId, String executionPeriodId) throws FenixServiceException {

        final ExecutionSemester executionSemester;
        if (StringUtils.isEmpty(executionPeriodId)) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            executionSemester = AbstractDomainObject.fromExternalId(executionPeriodId);
        }

        final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);
        if (executionDegree == null) {
            throw new NonExistingExecutionDegree();
        }

        Set<ExecutionCourse> executionCourseList =
                executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(executionSemester);

        List infoExecutionCourseList = (List) CollectionUtils.collect(executionCourseList, new Transformer() {

            @Override
            public Object transform(Object input) {
                ExecutionCourse executionCourse = (ExecutionCourse) input;
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                return infoExecutionCourse;
            }
        });

        return infoExecutionCourseList;

    }
}