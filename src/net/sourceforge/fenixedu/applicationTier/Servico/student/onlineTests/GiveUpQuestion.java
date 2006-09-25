/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

import org.joda.time.DateTime;

/**
 * @author Susana Fernandes
 */
public class GiveUpQuestion extends Service {

    public void run(Registration registration, DistributedTest distributedTest, Integer exerciseCode,
            Integer itemCode, String path) throws FenixServiceException, ExcepcaoPersistencia {
        if (distributedTest == null) {
            throw new FenixServiceException();
        }
        Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(
                registration, distributedTest);
        StudentTestQuestion thisStudentTestQuestion = null;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getQuestion().getIdInternal().equals(exerciseCode)) {
                if (studentTestQuestion.getStudentSubQuestions() != null
                        && studentTestQuestion.getStudentSubQuestions().size() >= itemCode) {
                    SubQuestion subQuestion = studentTestQuestion.getStudentSubQuestions().get(itemCode);
                    if (subQuestion.getItemId().equals(studentTestQuestion.getItemId())) {
                        thisStudentTestQuestion = studentTestQuestion;
                        break;
                    }
                } else {
                    throw new FenixServiceException();
                }
            }
        }
        SubQuestion subQuestion = thisStudentTestQuestion.getStudentSubQuestions().get(itemCode);
        Response response = null;
        if (subQuestion.getQuestionType().getType().intValue() == QuestionType.LID) {
            response = new ResponseLID(new String[] { null });
        } else if (subQuestion.getQuestionType().getType().intValue() == QuestionType.STR) {
            response = new ResponseSTR("");
        } else if (subQuestion.getQuestionType().getType().intValue() == QuestionType.NUM) {
            response = new ResponseNUM("");
        }
        response.setResponsed();
        thisStudentTestQuestion.setResponse(response);
        thisStudentTestQuestion.setTestQuestionMark(new Double(0));
        String nextItem = getNextItem(subQuestion.getResponseProcessingInstructions());
        Integer newOrder = new Integer(thisStudentTestQuestion.getTestQuestionOrder().intValue() + 1);
        if (nextItem != null) {
            for (StudentTestQuestion question : studentTestQuestionList) {
                if (question.getTestQuestionOrder().compareTo(newOrder) >= 0) {
                    question.setTestQuestionOrder(new Integer(
                            question.getTestQuestionOrder().intValue() + 1));
                }
            }
            StudentTestQuestion nextStudentTestQuestion = new StudentTestQuestion();
            nextStudentTestQuestion.setStudent(registration);
            nextStudentTestQuestion.setDistributedTest(distributedTest);
            nextStudentTestQuestion.setTestQuestionOrder(newOrder);
            nextStudentTestQuestion.setCorrectionFormula(thisStudentTestQuestion.getCorrectionFormula());
            nextStudentTestQuestion.setResponse(null);
            nextStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
            nextStudentTestQuestion.setItemId(nextItem);
            nextStudentTestQuestion.setQuestion(thisStudentTestQuestion.getQuestion());
            nextStudentTestQuestion.setTestQuestionValue(getNextQuestionValue(thisStudentTestQuestion,
                    nextStudentTestQuestion, path.replace('\\', '/')));
        }

        StudentTestLog studentTestLog = new StudentTestLog();
        studentTestLog.setDistributedTest(distributedTest);
        studentTestLog.setStudent(registration);
        studentTestLog.setDateDateTime(new DateTime());
        studentTestLog.setEvent("Desistiu da pergunta/alínea: "
                + thisStudentTestQuestion.getTestQuestionOrder());
        return;
    }

    private Double getNextQuestionValue(StudentTestQuestion thisStudentTestQuestion,
            StudentTestQuestion nextStudentTestQuestion, String path) throws FenixServiceException {
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            parse.parseStudentTestQuestion(nextStudentTestQuestion, path);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }

        Double oldValue = thisStudentTestQuestion.getSubQuestionByItem().getMaxValue();
        double diff = 0;

        if (thisStudentTestQuestion.getTestQuestionValue().doubleValue() != 0) {
            if (oldValue.doubleValue() != 0) {
                diff = thisStudentTestQuestion.getTestQuestionValue().doubleValue()
                        * Math.pow(oldValue.doubleValue(), -1);
            } else {
                diff = 1;
            }

        }
        if (nextStudentTestQuestion.getSubQuestionByItem().getMaxValue() == 0) {
            return diff;
        }
        return nextStudentTestQuestion.getSubQuestionByItem().getMaxValue() * diff;
    }

    protected String getNextItem(List<ResponseProcessing> responseProcessingList) {
        String nextItem = null;
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (!responseProcessing.isFenixCorrectResponse()) {
                return responseProcessing.getNextItem();
            } else {
                nextItem = responseProcessing.getNextItem();
            }
        }
        return nextItem;
    }
}