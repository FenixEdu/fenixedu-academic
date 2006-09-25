package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.NotAuthorizedStudentToDoTestException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class InsertStudentTestResponses extends Service {
    private static final Logger logger = Logger.getLogger(InsertStudentTestResponses.class);

    private String path;

    public InfoSiteStudentTestFeedback run(Registration registration, Integer studentNumber,
            final Integer distributedTestId, Response[] response, String path)
            throws FenixServiceException, ExcepcaoPersistencia {

        String logIdString = StringAppender.append("student num", studentNumber.toString(), " testId ",
                distributedTestId.toString());
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        this.path = path.replace('\\', '/');
        if (registration == null) {
            throw new FenixServiceException();
        }
        if (registration.getNumber().compareTo(studentNumber) != 0) {
            throw new NotAuthorizedStudentToDoTestException();
        }

        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        if (distributedTest == null)
            throw new FenixServiceException();
        String event = getLogString(response);

        double totalMark = 0;
        int responseNumber = 0;
        int notResponseNumber = 0;
        List<String> errors = new ArrayList<String>();

        if (compareDates(distributedTest.getEndDate(), distributedTest.getEndHour())) {
            Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion
                    .findStudentTestQuestions(registration, distributedTest);
            if (studentTestQuestionList.size() == 0)
                throw new FenixServiceException();
            ParseSubQuestion parse = new ParseSubQuestion();
            List<StudentTestQuestion> newTestQuestions = new ArrayList<StudentTestQuestion>();
            for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                Response thisResponse = response[studentTestQuestion.getTestQuestionOrder().intValue() - 1];

                if (logger.isDebugEnabled())
                    logger.debug(StringAppender.append(logIdString,
                            " infoStudentTestQuestion.getResonse()= ",
                            getLogString(new Response[] { thisResponse })));

                if (thisResponse.isResponsed()) {
                    responseNumber++;
                    if (studentTestQuestion.getResponse() != null
                            && distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                        totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                        // n�o pode aceitar nova resposta
                    } else {
                        try {
                            studentTestQuestion = parse.parseStudentTestQuestion(studentTestQuestion,
                                    this.path);
                            studentTestQuestion.setSubQuestionByItem(correctQuestionValues(
                                    studentTestQuestion.getSubQuestionByItem(), new Double(
                                            studentTestQuestion.getTestQuestionValue().doubleValue())));
                        } catch (Exception e) {
                            throw new FenixServiceException(e);
                        }

                        IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory
                                .getInstance();
                        IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                                .getQuestionCorrectionStrategy(studentTestQuestion);

                        String error = questionCorrectionStrategy.validResponse(studentTestQuestion,
                                thisResponse);
                        if (error == null) {
                            studentTestQuestion.setResponse(thisResponse);
                            if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                    && studentTestQuestion.getSubQuestionByItem()
                                            .getResponseProcessingInstructions().size() != 0) {

                                studentTestQuestion = questionCorrectionStrategy
                                        .getMark(studentTestQuestion);

                                if (studentTestQuestion.getSubQuestionByItem().getNextItemId() != null) {
                                    Integer newOrder = studentTestQuestion.getTestQuestionOrder() + 1;
                                    StudentTestQuestion nextStudentTestQuestion = new StudentTestQuestion();
                                    nextStudentTestQuestion.setStudent(registration);
                                    nextStudentTestQuestion.setDistributedTest(distributedTest);
                                    nextStudentTestQuestion.setTestQuestionOrder(newOrder);
                                    nextStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
                                    nextStudentTestQuestion.setCorrectionFormula(studentTestQuestion
                                            .getCorrectionFormula());
                                    nextStudentTestQuestion.setResponse(null);
                                    nextStudentTestQuestion.setItemId(studentTestQuestion
                                            .getSubQuestionByItem().getNextItemId());
                                    nextStudentTestQuestion.setQuestion(studentTestQuestion
                                            .getQuestion());
                                    newTestQuestions.add(nextStudentTestQuestion);
                                    nextStudentTestQuestion.setTestQuestionValue(getNextQuestionValue(
                                            studentTestQuestion, nextStudentTestQuestion));
                                }
                            }
                            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                        } else {
                            notResponseNumber++;
                            responseNumber--;
                            errors.add(error);
                        }
                    }
                } else {
                    if (studentTestQuestion.getResponse() != null) {
                        responseNumber++;
                        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                        }
                    } else {
                        notResponseNumber++;
                    }

                }
            }

            studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(registration,
                    distributedTest);
            for (StudentTestQuestion stq : newTestQuestions) {
                for (StudentTestQuestion question : studentTestQuestionList) {
                    if (!question.getIdInternal().equals(stq.getIdInternal())
                            && stq.getTestQuestionOrder().compareTo(question.getTestQuestionOrder()) <= 0) {
                        question.setTestQuestionOrder(new Integer(question.getTestQuestionOrder() + 1));
                    }
                }
            }

            if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
                OnlineTest onlineTest = distributedTest.getOnlineTest();
                Attends attend = registration
                        .readAttendByExecutionCourse(((ExecutionCourse) distributedTest.getTestScope()
                                .getDomainObject()));
                Mark mark = onlineTest.getMarkByAttend(attend);

                if (mark == null) {
                    mark = new Mark();
                    mark.setAttend(attend);
                    mark.setEvaluation(onlineTest);
                }
                DecimalFormat df = new DecimalFormat("#0.##");
                DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
                decimalFormatSymbols.setDecimalSeparator('.');
                df.setDecimalFormatSymbols(decimalFormatSymbols);
                String grade = df.format(Math.max(0, totalMark));
                System.out.println("GRADE ----------------------> " + registration.getNumber() + " "
                        + grade);
                mark.setMark(grade);
            }

            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(distributedTest);
            studentTestLog.setStudent(registration);
            studentTestLog.setDateDateTime(new DateTime());
            studentTestLog.setEvent(event);
        } else
            throw new NotAuthorizedException();
        infoSiteStudentTestFeedback.setResponseNumber(new Integer(responseNumber));
        infoSiteStudentTestFeedback.setNotResponseNumber(new Integer(notResponseNumber));
        infoSiteStudentTestFeedback.setErrors(errors);
        return infoSiteStudentTestFeedback;
    }

    private boolean compareDates(Calendar date, Calendar hour) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(calendar, date) <= 0) {
            if (dateComparator.compare(calendar, date) == 0) {
                if (hourComparator.compare(calendar, hour) <= 0) {
                    return true;
                }

                return false;
            }
            return true;
        }
        return false;
    }

    private SubQuestion correctQuestionValues(SubQuestion subQuestion, Double questionValue) {
        Double maxValue = subQuestion.getMaxValue();
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
            for (ResponseProcessing responseProcessing : subQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(Double.valueOf(responseProcessing.getResponseValue()
                        * difValue));
            }
        }

        return subQuestion;
    }

    private Double getNextQuestionValue(StudentTestQuestion thisStudentTestQuestion,
            StudentTestQuestion nextStudentTestQuestion) throws FenixServiceException {
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            parse.parseStudentTestQuestion(nextStudentTestQuestion, path.replace('\\', '/'));
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }

        Double oldValue = thisStudentTestQuestion.getSubQuestionByItem().getMaxValue();
        double diff = -1;

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

    private String getLogString(Response[] response) {
        StringBuilder event = new StringBuilder();
        event.append("Submeter Teste;");
        for (int questionNumber = 0; questionNumber < response.length; questionNumber++) {
            if (response[questionNumber] instanceof ResponseLID) {
                if (((ResponseLID) response[questionNumber]).getResponse() != null) {
                    for (int responseNumber = 0; responseNumber < ((ResponseLID) response[questionNumber])
                            .getResponse().length; responseNumber++) {
                        if (responseNumber != 0)
                            event.append(",");
                        event
                                .append(((ResponseLID) response[questionNumber]).getResponse()[responseNumber]);
                    }
                }
            } else if (response[questionNumber] instanceof ResponseSTR) {
                if (((ResponseSTR) response[questionNumber]).getResponse() != null)
                    event.append(((ResponseSTR) response[questionNumber]).getResponse());

            } else if (response[questionNumber] instanceof ResponseNUM) {
                if (((ResponseNUM) response[questionNumber]).getResponse() != null)
                    event.append(((ResponseNUM) response[questionNumber]).getResponse());

            }
            event.append(";");
        }

        return event.toString();
    }

}