package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */

public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear implements IService {

    public Object run(Integer executionDegreeId, Integer executionPeriodId, Integer curricularYear)
            throws FenixServiceException {

        List infoExecutionCourseList = new ArrayList();
        try {
            List executionCourseList = null;
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            if (executionPeriodId == null) {
                throw new FenixServiceException("nullExecutionPeriodId");
            }

            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                    ExecutionPeriod.class, executionPeriodId);

            if (executionDegreeId == null && curricularYear == null) {
                executionCourseList = executionCourseDAO
                        .readByExecutionPeriodWithNoCurricularCourses(executionPeriod);

            } else {
                IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                        ExecutionDegree.class, executionDegreeId);

                executionCourseList = executionCourseDAO
                        .readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
                                executionPeriod, executionDegree);
            }

            CollectionUtils.collect(executionCourseList, new Transformer() {
                public Object transform(Object input) {
                    IExecutionCourse executionCourse = (IExecutionCourse) input;
                    return Cloner.get(executionCourse);
                }
            }, infoExecutionCourseList);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }

        return infoExecutionCourseList;
    }
}