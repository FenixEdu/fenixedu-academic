/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteDistributedTests;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests implements IService {

    public ReadDistributedTests() {
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
            IPersistentDistributedTest persistentDistrubutedTest = persistentSuport
                    .getIPersistentDistributedTest();
            List distributedTests = persistentDistrubutedTest.readByTestScopeObject(executionCourse);
            List result = new ArrayList();
            Iterator iter = distributedTests.iterator();
            while (iter.hasNext()) {
                IDistributedTest distributedTest = (IDistributedTest) iter.next();
                InfoDistributedTest infoDistributedTest = InfoDistributedTest
                        .newInfoFromDomain(distributedTest);
                result.add(infoDistributedTest);
            }
            InfoSiteDistributedTests bodyComponent = new InfoSiteDistributedTests();
            bodyComponent.setInfoDistributedTests(result);
            bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}