/*
 * Created on Oct 14, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteStudentsTestMarks;
import DataBeans.InfoStudentTestQuestionMark;
import DataBeans.SiteView;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarks implements IService {

	public ReadDistributedTestMarks() {
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException {

		ISuportePersistente persistentSuport;
		InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOID(
							DistributedTest.class, distributedTestId);
			if (distributedTest == null) {
				throw new InvalidArgumentsServiceException();
			}

			IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
					.getIPersistentStudentTestQuestion();
			List studentTestQuestionList = persistentStudentTestQuestion
					.readByDistributedTest(distributedTest);

			List infoStudentTestQuestionList = null;

			infoStudentTestQuestionList = (List) CollectionUtils.collect(
					studentTestQuestionList, new Transformer() {

						public Object transform(Object arg0) {
							IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) arg0;
							return InfoStudentTestQuestionMark
									.newInfoFromDomain(studentTestQuestion);
						}

					});

			infoSiteStudentsTestMarks
					.setMaximumMark(persistentStudentTestQuestion
							.getMaximumDistributedTestMark(distributedTest));
			infoSiteStudentsTestMarks
					.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
			infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse
					.newInfoFromDomain((IExecutionCourse) distributedTest
							.getTestScope().getDomainObject()));
			infoSiteStudentsTestMarks
					.setInfoDistributedTest(InfoDistributedTest
							.newInfoFromDomain(distributedTest));

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		SiteView siteView = new ExecutionCourseSiteView(
				infoSiteStudentsTestMarks, infoSiteStudentsTestMarks);
		return siteView;
	}

}