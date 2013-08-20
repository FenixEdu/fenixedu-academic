/*
 * Created on 9/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class ReadInquiryStatistics {

    public List<InfoInquiryStatistics> run(String executionCourseId, String distributedTestId, String path)
            throws FenixServiceException {
        List<InfoInquiryStatistics> infoInquiryStatisticsList = new ArrayList<InfoInquiryStatistics>();
        DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        final Set<StudentTestQuestion> studentTestQuestions =
                distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            InfoInquiryStatistics infoInquiryStatistics = new InfoInquiryStatistics();
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
                if (studentTestQuestion.getOptionShuffle() == null
                        && studentTestQuestion.getSubQuestionByItem().getShuffle() != null) {
                    studentTestQuestion.setOptionShuffle(studentTestQuestion.getSubQuestionByItem().getShuffleString());
                }
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            List<LabelValueBean> statistics = new ArrayList<LabelValueBean>();
            DecimalFormat df = new DecimalFormat("#%");
            int numOfStudentResponses = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), true);
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                for (int i = 1; i <= studentTestQuestion.getSubQuestionByItem().getOptionNumber(); i++) {
                    int mark = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), Integer.toString(i));
                    String percentage = "0%";
                    if (mark != 0) {
                        percentage = df.format(mark * java.lang.Math.pow(numOfStudentResponses, -1));
                    }
                    statistics.add(new LabelValueBean(Integer.toString(i), percentage));
                }
            } else {
                Set<StudentTestQuestion> responses =
                        distributedTest.findStudentTestQuestionsByTestQuestionOrder(studentTestQuestion.getTestQuestionOrder());
                String percentage = "0%";
                for (StudentTestQuestion studentTestQuestion2 : responses) {
                    Response response = studentTestQuestion2.getResponse();
                    if (response != null) {
                        String responseString = null;
                        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                            responseString = ((ResponseSTR) response).getResponse();
                        } else {
                            responseString = ((ResponseNUM) response).getResponse();
                        }
                        if (!StringUtils.isEmpty(responseString)) {
                            percentage =
                                    df.format(distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(),
                                            responseString) * java.lang.Math.pow(numOfStudentResponses, -1));
                            LabelValueBean labelValueBean = new LabelValueBean(percentage, responseString);
                            if (!statistics.contains(labelValueBean)) {
                                statistics.add(labelValueBean);
                            }
                        }
                    }
                }
            }
            statistics.add(new LabelValueBean("Número de alunos que responderam", Integer.toString(numOfStudentResponses)));
            infoInquiryStatistics.setInfoStudentTestQuestion(studentTestQuestion);
            infoInquiryStatistics.setOptionStatistics(statistics);
            infoInquiryStatisticsList.add(infoInquiryStatistics);
        }
        return infoInquiryStatisticsList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadInquiryStatistics serviceInstance = new ReadInquiryStatistics();

    @Service
    public static List<InfoInquiryStatistics> runReadInquiryStatistics(String executionCourseId, String distributedTestId,
            String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, path);
    }

}