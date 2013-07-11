package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertTestQuestion {

    private String path = new String();

    protected void run(String executionCourseId, String testId, String[] metadataId, Integer questionOrder, Double questionValue,
            CorrectionFormula formula, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');

        for (String element : metadataId) {
            Metadata metadata = FenixFramework.getDomainObject(element);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            Question question = null;
            if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0) {
                question = metadata.getVisibleQuestions().get(0);
            } else {
                throw new InvalidArgumentsServiceException();
            }
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            Test test = FenixFramework.getDomainObject(testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }
            List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
            Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
            if (testQuestionList != null) {
                if (questionOrder == null || questionOrder.equals(Integer.valueOf(-1))) {
                    questionOrder = Integer.valueOf(testQuestionList.size() + 1);
                } else {
                    questionOrder = Integer.valueOf(questionOrder.intValue() + 1);
                }
                for (TestQuestion iterTestQuestion : testQuestionList) {
                    Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                    if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                        iterTestQuestion.setTestQuestionOrder(Integer.valueOf(iterQuestionOrder.intValue() + 1));
                    }
                }
            }
            Double thisQuestionValue = questionValue;
            if (questionValue == null) {
                ParseSubQuestion parseQuestion = new ParseSubQuestion();
                if (thisQuestionValue == null) {
                    thisQuestionValue = new Double(0);
                }
                try {
                    InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                    question = parseQuestion.parseSubQuestion(question, this.path);
                    if (infoQuestion.getQuestionValue() != null) {
                        thisQuestionValue = infoQuestion.getQuestionValue();
                    } else {
                        for (SubQuestion subQuestion : question.getSubQuestions()) {
                            if (subQuestion.getQuestionValue() != null) {
                                thisQuestionValue =
                                        new Double(thisQuestionValue.doubleValue() + subQuestion.getQuestionValue().doubleValue());
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }

            }
            TestQuestion testQuestion = new TestQuestion();
            test.setLastModifiedDate(Calendar.getInstance().getTime());
            testQuestion.setQuestion(question);
            testQuestion.setTest(test);
            testQuestion.setTestQuestionOrder(questionOrder);
            testQuestion.setTestQuestionValue(thisQuestionValue);
            testQuestion.setCorrectionFormula(formula);
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertTestQuestion serviceInstance = new InsertTestQuestion();

    @Atomic
    public static void runInsertTestQuestion(String executionCourseId, String testId, String[] metadataId, Integer questionOrder,
            Double questionValue, CorrectionFormula formula, String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, metadataId, questionOrder, questionValue, formula, path);
    }

}