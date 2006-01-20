package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadExecutionCoursesByExecutionDegreeService extends Service {

    public class NonExistingExecutionDegree extends FenixServiceException {
        public NonExistingExecutionDegree() {
            super();
        }
    }

    public List run(Integer executionDegreeId, Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = persistentSupport.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        ExecutionPeriod executionPeriod = null;

        if (executionPeriodId == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) executionCourseDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }

        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();

        ExecutionDegree executionDegree = (ExecutionDegree) executionDegreeDAO.readByOID(
                ExecutionDegree.class, executionDegreeId);

        if (executionDegree == null) {
            throw new NonExistingExecutionDegree();
        }

        List executionCourseList = executionCourseDAO.readByExecutionDegreeAndExecutionPeriod(
                executionDegree.getDegreeCurricularPlan().getIdInternal(), executionPeriod
                        .getIdInternal());

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