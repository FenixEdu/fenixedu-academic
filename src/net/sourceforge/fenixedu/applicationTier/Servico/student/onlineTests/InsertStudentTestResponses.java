/*
 * Created on 3/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestion;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.util.StringAppender;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertStudentTestResponses implements IService {

    private static final Logger logger = Logger.getLogger(InsertStudentTestResponses.class);

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private String path;

    public InfoSiteStudentTestFeedback run(String userName, Integer studentNumber, Integer distributedTestId, Response[] response, String path)
            throws FenixServiceException {

        String logIdString = StringAppender.append("student nº ", studentNumber.toString(), " testId ", distributedTestId.toString());

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();
            if (!student.getPerson().getUsername().equalsIgnoreCase(userName))
                throw new NotAuthorizedStudentToDoTestException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();
            String event = getLogString(response);

            double totalMark = 0;
            int responseNumber = 0;
            int notResponseNumber = 0;
            List<String> errors = new ArrayList<String>();

            if (compareDates(distributedTest.getEndDate(), distributedTest.getEndHour())) {
                List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                        .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());
                if (studentTestQuestionList.size() == 0)
                    throw new FenixServiceException();
                IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
                ParseQuestion parse = new ParseQuestion();

                for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                    persistentStudentTestQuestion.simpleLockWrite(studentTestQuestion);

                    InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestion.newInfoFromDomain(studentTestQuestion);
                    infoStudentTestQuestion.setResponse(response[studentTestQuestion.getTestQuestionOrder().intValue() - 1]);

                    if (logger.isDebugEnabled())
                        logger.debug(StringAppender.append(logIdString, " infoStudentTestQuestion.getResonse()= ",
                                getLogString(new Response[] { infoStudentTestQuestion.getResponse() })));

                    if (infoStudentTestQuestion.getResponse().isResponsed()) {
                        responseNumber++;
                        if (studentTestQuestion.getResponse() != null && distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                            // não pode aceitar nova resposta
                        } else {
                            try {
                                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
                                infoStudentTestQuestion.setQuestion(correctQuestionValues(infoStudentTestQuestion.getQuestion(), new Double(
                                        infoStudentTestQuestion.getTestQuestionValue().doubleValue())));
                            } catch (Exception e) {
                                throw new FenixServiceException(e);
                            }

                            IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory.getInstance();
                            IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                                    .getQuestionCorrectionStrategy(infoStudentTestQuestion);

                            String error = questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
                            if (error == null) {
                                if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                        && infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions().size() != 0) {

                                    infoStudentTestQuestion = questionCorrectionStrategy.getMark(infoStudentTestQuestion);
                                }
                                totalMark += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                XMLEncoder encoder = new XMLEncoder(out);

                                if (logger.isDebugEnabled())
                                    logger.debug(StringAppender.append(logIdString, " order= ", infoStudentTestQuestion.getTestQuestionOrder()
                                            .toString(), " ", dateFormat.format(Calendar.getInstance().getTime()),
                                            " before write: infoStudentTestQuestion.getResonse()= ",
                                            getLogString(new Response[] { infoStudentTestQuestion.getResponse() })));

                                encoder.writeObject(infoStudentTestQuestion.getResponse());
                                encoder.close();

                                final String outString = out.toString();
                                if (logger.isDebugEnabled())
                                    logger.debug(StringAppender.append(logIdString, " out.toString()= ", outString));

                                studentTestQuestion.setResponse(outString);
                                studentTestQuestion.setTestQuestionMark(infoStudentTestQuestion.getTestQuestionMark());

                            } else {
                                notResponseNumber++;
                                responseNumber--;
                                errors.add(error);
                            }
                        }
                    } else {
                        if (studentTestQuestion.getResponse() != null)
                            responseNumber++;
                        else
                            notResponseNumber++;

                    }
                }
                if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
                    IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTest);
                    IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student,
                            (IExecutionCourse) distributedTest.getTestScope().getDomainObject());
                    IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);

                    if (mark == null) {
                        mark = new Mark();
                        mark.setAttend(attend);
                        mark.setEvaluation(onlineTest);
                    }
                    persistentSuport.getIPersistentMark().simpleLockWrite(mark);
                    mark.setMark(new java.text.DecimalFormat("#0.##").format(totalMark));
                }

                IPersistentStudentTestLog persistentStudentTestLog = persistentSuport.getIPersistentStudentTestLog();
                IStudentTestLog studentTestLog = new StudentTestLog();
                persistentStudentTestLog.simpleLockWrite(studentTestLog);
                studentTestLog.setDistributedTest(distributedTest);
                studentTestLog.setStudent(student);
                studentTestLog.setDate(Calendar.getInstance().getTime());
                studentTestLog.setEvent(event);
            } else
                throw new NotAuthorizedException();
            infoSiteStudentTestFeedback.setResponseNumber(new Integer(responseNumber));
            infoSiteStudentTestFeedback.setNotResponseNumber(new Integer(notResponseNumber));
            infoSiteStudentTestFeedback.setErrors(errors);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
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
        for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
            for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue().doubleValue() * difValue));
            }
        }

        return infoQuestion;
    }

    private String getLogString(Response[] response) {
        StringBuffer event = new StringBuffer();
        event.append("Submeter Teste;");
        for (int questionNumber = 0; questionNumber < response.length; questionNumber++) {
            if (response[questionNumber] instanceof ResponseLID) {
                if (((ResponseLID) response[questionNumber]).getResponse() != null) {
                    for (int responseNumber = 0; responseNumber < ((ResponseLID) response[questionNumber]).getResponse().length; responseNumber++) {
                        if (responseNumber != 0)
                            event.append(",");
                        event.append(((ResponseLID) response[questionNumber]).getResponse()[responseNumber]);
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