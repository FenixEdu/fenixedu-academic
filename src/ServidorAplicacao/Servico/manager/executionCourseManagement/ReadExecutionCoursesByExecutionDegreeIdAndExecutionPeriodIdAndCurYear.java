package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ExecutionDegree;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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