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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks implements IService {

	public SiteView run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSuport;
		InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();

		persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		DistributedTest distributedTest = (DistributedTest) persistentSuport
				.getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
		if (distributedTest == null) {
			throw new InvalidArgumentsServiceException();
		}

		IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
				.getIPersistentStudentTestQuestion();
		List studentTestQuestionList = persistentStudentTestQuestion
				.readByDistributedTest(distributedTest.getIdInternal());

		List<InfoStudentTestQuestionMark> infoStudentTestQuestionList = (List<InfoStudentTestQuestionMark>) CollectionUtils
				.collect(studentTestQuestionList, new Transformer() {

					public Object transform(Object arg0) {
						StudentTestQuestion studentTestQuestion = (StudentTestQuestion) arg0;
						return InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion);
					}

				});

		infoSiteStudentsTestMarks.setMaximumMark(persistentStudentTestQuestion
				.getMaximumDistributedTestMark(distributedTest.getIdInternal()));
		infoSiteStudentsTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
		infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse
				.newInfoFromDomain((ExecutionCourse) distributedTest.getTestScope().getDomainObject()));
		infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest
				.newInfoFromDomain(distributedTest));

		SiteView siteView = new ExecutionCourseSiteView(infoSiteStudentsTestMarks,
				infoSiteStudentsTestMarks);
		return siteView;
	}

}