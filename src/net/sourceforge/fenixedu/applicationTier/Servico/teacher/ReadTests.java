/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTests;
import net.sourceforge.fenixedu.dataTransferObject.InfoTest;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTests implements IService {

    public ReadTests() {
    }

    public SiteView run(Integer executionCourseId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            List tests = persistentTest.readByTestScopeObject(executionCourse);
            List result = new ArrayList();
            Iterator iter = tests.iterator();
            while (iter.hasNext()) {
                ITest test = (ITest) iter.next();
                InfoTest infoTest = InfoTest.newInfoFromDomain(test);
                result.add(infoTest);
            }
            InfoSiteTests bodyComponent = new InfoSiteTests();
            bodyComponent.setInfoTests(result);
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}