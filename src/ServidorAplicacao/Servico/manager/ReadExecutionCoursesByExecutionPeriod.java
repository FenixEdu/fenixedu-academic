/*
 * Created on 8/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByExecutionPeriod implements IService {

    /**
     * Executes the service. Returns the current collection of
     * infoExecutionCourses.
     */
    public List run(Integer executionPeriodId) throws FenixServiceException {
        List allExecutionCoursesFromExecutionPeriod = null;
        List allExecutionCourses = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                    ExecutionPeriod.class, executionPeriodId);

            if (executionPeriod == null) {
                throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
            }
            allExecutionCoursesFromExecutionPeriod = persistentExecutionCourse
                    .readByExecutionPeriod(executionPeriod);

            if (allExecutionCoursesFromExecutionPeriod == null
                    || allExecutionCoursesFromExecutionPeriod.isEmpty()) {
                return allExecutionCoursesFromExecutionPeriod;
            }
            InfoExecutionCourse infoExecutionCourse = null;
            allExecutionCourses = new ArrayList(allExecutionCoursesFromExecutionPeriod.size());
            Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
            while (iter.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
                Boolean hasSite = persistentExecutionCourse.readSite(executionCourse.getIdInternal());

                infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                        .newInfoFromDomain(executionCourse);
                infoExecutionCourse.setHasSite(hasSite);
                allExecutionCourses.add(infoExecutionCourse);
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return allExecutionCourses;
    }
}