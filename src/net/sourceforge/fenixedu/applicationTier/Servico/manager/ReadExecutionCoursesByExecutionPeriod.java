/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                    ExecutionPeriod.class, executionPeriodId);

            if (executionPeriod == null) {
                throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
            }
            allExecutionCoursesFromExecutionPeriod = executionPeriod.getAssociatedExecutionCourses();

            if (allExecutionCoursesFromExecutionPeriod == null
                    || allExecutionCoursesFromExecutionPeriod.isEmpty()) {
                return allExecutionCoursesFromExecutionPeriod;
            }
            InfoExecutionCourse infoExecutionCourse = null;
            allExecutionCourses = new ArrayList(allExecutionCoursesFromExecutionPeriod.size());
            Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
            while (iter.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
                Boolean hasSite;
                if(executionCourse.getSite() != null)
                    hasSite = true;
                else
                    hasSite = false;

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