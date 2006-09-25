/*
 * Created on 9/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInquiryStatistics;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class ReadInquiryStatistics extends Service {

	private String path = new String();

	public SiteView run(Integer executionCourseId, Integer distributedTestId, String path)
			throws FenixServiceException, ExcepcaoPersistencia {
		this.path = path.replace('\\', '/');
		InfoSiteInquiryStatistics infoSiteInquiryStatistics = new InfoSiteInquiryStatistics();
		List infoInquiryStatisticsList = new ArrayList();
		DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
		if (distributedTest == null)
			throw new InvalidArgumentsServiceException();
		infoSiteInquiryStatistics.setExecutionCourse(InfoExecutionCourse
				.newInfoFromDomain((ExecutionCourse) distributedTest.getTestScope().getDomainObject()));

        final Set<StudentTestQuestion> studentTestQuestions = distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
		for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
			InfoInquiryStatistics infoInquiryStatistics = new InfoInquiryStatistics();

			InfoStudentTestQuestion infoStudentTestQuestion;
			ParseSubQuestion parse = new ParseSubQuestion();
			try {
				infoStudentTestQuestion = InfoStudentTestQuestion.newInfoFromDomain(studentTestQuestion);
//				infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
//						this.path);
				if (studentTestQuestion.getOptionShuffle() == null) {
					studentTestQuestion.setOptionShuffle(infoStudentTestQuestion.getOptionShuffle());
				}
			} catch (Exception e) {
				throw new FenixServiceException(e);
			}
			List<LabelValueBean> statistics = new ArrayList<LabelValueBean>();
			DecimalFormat df = new DecimalFormat("#%");
			int numOfStudentResponses = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), true);
			if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
				for (int i = 1; i <= infoStudentTestQuestion.getQuestion().getOptionNumber().intValue(); i++) {

					String response = new String("<string>" + i + "</string>");

					int mark = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), response);
					String percentage = new String("0%");
					if (mark != 0)
						percentage = df.format(mark * java.lang.Math.pow(numOfStudentResponses, -1));
					statistics.add(new LabelValueBean(new Integer(i).toString(), percentage));
				}
			} else {
				Set<String> responses = distributedTest.findResponses();

				String percentage = new String("0%");
				for (String response : responses) {
					percentage = df.format(distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), response)
							* java.lang.Math.pow(numOfStudentResponses, -1));

					XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(response.getBytes()));
					Response r = (Response) decoder.readObject();
					decoder.close();
					if (r instanceof ResponseSTR)
						statistics.add(new LabelValueBean(percentage, ((ResponseSTR) r).getResponse()));
					else
						statistics.add(new LabelValueBean(percentage, ((ResponseNUM) r).getResponse()));

				}
			}
			statistics.add(new LabelValueBean(new String("Número de alunos que responderam"),
					new Integer(numOfStudentResponses).toString()));
			infoInquiryStatistics.setInfoStudentTestQuestion(infoStudentTestQuestion);
			infoInquiryStatistics.setOptionStatistics(statistics);
			infoInquiryStatisticsList.add(infoInquiryStatistics);
		}
		infoSiteInquiryStatistics.setInfoInquiryStatistics(infoInquiryStatisticsList);
		SiteView siteView = new ExecutionCourseSiteView(infoSiteInquiryStatistics,
				infoSiteInquiryStatistics);
		return siteView;
	}

}