package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadExecutionCoursesByExecutionDegreeService extends Service {

    public class NonExistingExecutionDegree extends FenixServiceException {
        public NonExistingExecutionDegree() {
            super();
        }
    }

    public List run(Integer executionDegreeId, Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod;
        if (executionPeriodId == null) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        }

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
        if (executionDegree == null) {
            throw new NonExistingExecutionDegree();
        }

        Set<ExecutionCourse> executionCourseList =  executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(executionPeriod);

        List infoExecutionCourseList = (List) CollectionUtils.collect(executionCourseList, new Transformer() {

            public Object transform(Object input) {
                ExecutionCourse executionCourse = (ExecutionCourse) input;
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                return infoExecutionCourse;
            }
        });

        return infoExecutionCourseList;

    }
}