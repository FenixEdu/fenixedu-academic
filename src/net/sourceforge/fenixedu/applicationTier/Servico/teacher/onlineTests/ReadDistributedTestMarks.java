/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionMark;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks implements IService {

    public SiteView run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
            List studentTestQuestionList = persistentStudentTestQuestion.readByDistributedTest(distributedTest.getIdInternal());

            List<InfoStudentTestQuestionMark> infoStudentTestQuestionList = (List<InfoStudentTestQuestionMark>) CollectionUtils.collect(
                    studentTestQuestionList, new Transformer() {

                        public Object transform(Object arg0) {
                            IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) arg0;
                            return InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion);
                        }

                    });

            infoSiteStudentsTestMarks.setMaximumMark(persistentStudentTestQuestion.getMaximumDistributedTestMark(distributedTest.getIdInternal()));
            infoSiteStudentsTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
            infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain((IExecutionCourse) distributedTest.getTestScope()
                    .getDomainObject()));
            infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(distributedTest));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        SiteView siteView = new ExecutionCourseSiteView(infoSiteStudentsTestMarks, infoSiteStudentsTestMarks);
        return siteView;
    }

}