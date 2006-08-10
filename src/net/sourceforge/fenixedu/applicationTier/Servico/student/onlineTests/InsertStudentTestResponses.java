package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
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
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.log4j.Logger;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class InsertStudentTestResponses extends Service {
    private static final Logger logger = Logger.getLogger(InsertStudentTestResponses.class);

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private String path;

    public InfoSiteStudentTestFeedback run(String userName, Integer studentNumber,
            final Integer distributedTestId, Response[] response, String path)
            throws FenixServiceException, ExcepcaoPersistencia {

        String logIdString = StringAppender.append("student n� ", studentNumber.toString(),
                " testId ", distributedTestId.toString());

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        this.path = path.replace('\\', '/');
        Registration student = Registration.readByUsername(userName);
        if (student == null)
            throw new FenixServiceException();
        if (student.getNumber().compareTo(studentNumber) != 0)
            throw new NotAuthorizedStudentToDoTestException();

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
        	Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(student, distributedTest);
            if (studentTestQuestionList.size() == 0)
                throw new FenixServiceException();
            ParseQuestion parse = new ParseQuestion();

            for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                        .newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion.setResponse(response[studentTestQuestion.getTestQuestionOrder()
                        .intValue() - 1]);

                if (logger.isDebugEnabled())
                    logger.debug(StringAppender.append(logIdString,
                            " infoStudentTestQuestion.getResonse()= ",
                            getLogString(new Response[] { infoStudentTestQuestion.getResponse() })));

                if (infoStudentTestQuestion.getResponse().isResponsed()) {
                    responseNumber++;
                    if (studentTestQuestion.getResponse() != null
                            && distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                        totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
                        // n�o pode aceitar nova resposta
                    } else {
                        try {
                            infoStudentTestQuestion = parse.parseStudentTestQuestion(
                                    infoStudentTestQuestion, this.path);
                            infoStudentTestQuestion.setQuestion(correctQuestionValues(
                                    infoStudentTestQuestion.getQuestion(),
                                    new Double(infoStudentTestQuestion.getTestQuestionValue()
                                            .doubleValue())));
                        } catch (Exception e) {
                            throw new FenixServiceException(e);
                        }

                        IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory
                                .getInstance();
                        IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                                .getQuestionCorrectionStrategy(infoStudentTestQuestion);

                        String error = questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
                        if (error == null) {
                            if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                    && infoStudentTestQuestion.getQuestion()
                                            .getResponseProcessingInstructions().size() != 0) {

                                infoStudentTestQuestion = questionCorrectionStrategy
                                        .getMark(infoStudentTestQuestion);
                            }
                            totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            XMLEncoder encoder = new XMLEncoder(out);

                            if (logger.isDebugEnabled())
                                logger.debug(StringAppender.append(logIdString, " order= ",
                                        infoStudentTestQuestion.getTestQuestionOrder().toString(), " ",
                                        dateFormat.format(Calendar.getInstance().getTime()),
                                        " before write: infoStudentTestQuestion.getResonse()= ",
                                        getLogString(new Response[] { infoStudentTestQuestion
                                                .getResponse() })));

                            encoder.writeObject(infoStudentTestQuestion.getResponse());
                            encoder.close();

                            final String outString = out.toString();
                            if (logger.isDebugEnabled())
                                logger.debug(StringAppender.append(logIdString, " out.toString()= ",
                                        outString));

                            studentTestQuestion.setResponse(outString);
                            studentTestQuestion.setTestQuestionMark(infoStudentTestQuestion
                                    .getTestQuestionMark());

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
            if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
                OnlineTest onlineTest = distributedTest.getOnlineTest();
                Attends attend = student.readAttendByExecutionCourse(((ExecutionCourse) distributedTest
                        .getTestScope().getDomainObject()));
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
                System.out.println("GRADE ----------------------> " + student.getNumber() + " " + grade);
                mark.setMark(grade);
            }

            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(distributedTest);
            studentTestLog.setStudent(student);
            studentTestLog.setDate(Calendar.getInstance().getTime());
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

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = new Double(0);
        for (ResponseProcessing responseProcessing : infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
            for (ResponseProcessing responseProcessing : infoQuestion
                    .getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(Double.valueOf(responseProcessing.getResponseValue()
                        * difValue));
            }
        }

        return infoQuestion;
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