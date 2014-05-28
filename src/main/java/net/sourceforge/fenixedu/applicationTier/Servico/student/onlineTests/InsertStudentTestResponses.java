/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
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
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertStudentTestResponses {

    private static final Logger logger = LoggerFactory.getLogger(InsertStudentTestResponses.class);

    private static String path;

    @Atomic
    public static InfoSiteStudentTestFeedback run(Registration registration, Integer studentNumber,
            final String distributedTestId, Response[] response) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        ServiceMonitoring.logService(InsertStudentTestResponses.class, registration, studentNumber, distributedTestId, response,
                path);

        String logIdString = "student num" + studentNumber.toString() + " testId " + distributedTestId.toString();
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new InfoSiteStudentTestFeedback();
        if (registration == null) {
            throw new FenixServiceException();
        }
        if (registration.getNumber().compareTo(studentNumber) != 0) {
            throw new NotAuthorizedStudentToDoTestException();
        }

        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new FenixServiceException();
        }
        String event = getLogString(response);

        double totalMark = 0;
        int responseNumber = 0;
        int notResponseNumber = 0;
        List<String> errors = new ArrayList<String>();

        if (compareDates(distributedTest.getEndDate(), distributedTest.getEndHour())) {
            Set<StudentTestQuestion> studentTestQuestionList =
                    StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
            if (studentTestQuestionList.size() == 0) {
                throw new FenixServiceException();
            }
            ParseSubQuestion parse = new ParseSubQuestion();
            List<StudentTestQuestion> newTestQuestions = new ArrayList<StudentTestQuestion>();
            for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                Response thisResponse = response[studentTestQuestion.getTestQuestionOrder().intValue() - 1];

                if (logger.isDebugEnabled()) {
                    logger.debug(logIdString + " infoStudentTestQuestion.getResonse()= "
                            + getLogString(new Response[] { thisResponse }));
                }

                if (thisResponse.isResponsed()) {
                    responseNumber++;
                    if (studentTestQuestion.getResponse() != null
                            && distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                        totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                        // n�o pode aceitar nova resposta
                    } else {
                        try {
                            studentTestQuestion = parse.parseStudentTestQuestion(studentTestQuestion);
                            studentTestQuestion
                                    .setSubQuestionByItem(correctQuestionValues(studentTestQuestion.getSubQuestionByItem(),
                                            new Double(studentTestQuestion.getTestQuestionValue().doubleValue())));
                        } catch (Exception e) {
                            throw new FenixServiceException(e);
                        }

                        IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory =
                                QuestionCorrectionStrategyFactory.getInstance();
                        IQuestionCorrectionStrategy questionCorrectionStrategy =
                                questionCorrectionStrategyFactory.getQuestionCorrectionStrategy(studentTestQuestion);

                        String error = questionCorrectionStrategy.validResponse(studentTestQuestion, thisResponse);
                        if (error == null) {
                            studentTestQuestion.setResponse(thisResponse);
                            if ((!distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
                                    && studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions().size() != 0) {

                                studentTestQuestion = questionCorrectionStrategy.getMark(studentTestQuestion);

                                if (studentTestQuestion.getSubQuestionByItem().getNextItemId() != null) {
                                    Integer newOrder = studentTestQuestion.getTestQuestionOrder() + 1;
                                    StudentTestQuestion nextStudentTestQuestion = new StudentTestQuestion();
                                    nextStudentTestQuestion.setStudent(studentTestQuestion.getStudent());
                                    nextStudentTestQuestion.setDistributedTest(distributedTest);
                                    nextStudentTestQuestion.setTestQuestionOrder(newOrder);
                                    nextStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
                                    nextStudentTestQuestion.setCorrectionFormula(studentTestQuestion.getCorrectionFormula());
                                    nextStudentTestQuestion.setResponse(null);
                                    nextStudentTestQuestion.setItemId(studentTestQuestion.getSubQuestionByItem().getNextItemId());
                                    nextStudentTestQuestion.setQuestion(studentTestQuestion.getQuestion());
                                    newTestQuestions.add(nextStudentTestQuestion);
                                    nextStudentTestQuestion.setTestQuestionValue(getNextQuestionValue(studentTestQuestion,
                                            nextStudentTestQuestion));
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

            studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
            for (StudentTestQuestion stq : newTestQuestions) {
                for (StudentTestQuestion question : studentTestQuestionList) {
                    if (!question.getExternalId().equals(stq.getExternalId())
                            && stq.getTestQuestionOrder().compareTo(question.getTestQuestionOrder()) <= 0) {
                        question.setTestQuestionOrder(new Integer(question.getTestQuestionOrder() + 1));
                    }
                }
            }

            if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))
                    && !Double.valueOf(Math.max(0, totalMark)).equals(new Double(0.0))) {
                OnlineTest onlineTest = distributedTest.getOnlineTest();
                Attends attend =
                        registration.getStudent().readAttendByExecutionCourse(
                                (distributedTest.getTestScope().getExecutionCourse()));
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
                mark.setMark(grade);
            }
            StudentTestLog studentTestLog = new StudentTestLog(distributedTest, registration, event);
            infoSiteStudentTestFeedback.setStudentTestLog(studentTestLog);
        } else {
            throw new NotAuthorizedException();
        }
        infoSiteStudentTestFeedback.setResponseNumber(new Integer(responseNumber));
        infoSiteStudentTestFeedback.setNotResponseNumber(new Integer(notResponseNumber));
        infoSiteStudentTestFeedback.setErrors(errors);
        return infoSiteStudentTestFeedback;
    }

    private static boolean compareDates(Calendar date, Calendar hour) {
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

    private static SubQuestion correctQuestionValues(SubQuestion subQuestion, Double questionValue) {
        Double maxValue = subQuestion.getMaxValue();
        if (maxValue.compareTo(questionValue) != 0) {
            if (maxValue == 0.0) {
                for (ResponseProcessing responseProcessing : subQuestion.getResponseProcessingInstructions()) {
                    if (responseProcessing.getResponseValue() == maxValue) {
                        responseProcessing.setResponseValue(questionValue);
                    } else {
                        responseProcessing.setResponseValue(maxValue);
                    }
                }
            } else {
                double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
                for (ResponseProcessing responseProcessing : subQuestion.getResponseProcessingInstructions()) {
                    responseProcessing.setResponseValue(Double.valueOf(responseProcessing.getResponseValue() * difValue));
                }
            }
        }

        return subQuestion;
    }

    private static Double getNextQuestionValue(StudentTestQuestion thisStudentTestQuestion,
            StudentTestQuestion nextStudentTestQuestion) throws FenixServiceException {
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            parse.parseStudentTestQuestion(nextStudentTestQuestion);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }

        Double oldValue = thisStudentTestQuestion.getSubQuestionByItem().getMaxValue();
        double diff = -1;

        if (thisStudentTestQuestion.getTestQuestionValue().doubleValue() != 0) {
            if (oldValue.doubleValue() != 0) {
                diff = thisStudentTestQuestion.getTestQuestionValue().doubleValue() * Math.pow(oldValue.doubleValue(), -1);
            } else {
                diff = 1;
            }

        }
        if (nextStudentTestQuestion.getSubQuestionByItem().getMaxValue() == 0) {
            return diff;
        }
        return nextStudentTestQuestion.getSubQuestionByItem().getMaxValue() * diff;
    }

    private static String getLogString(Response[] response) {
        StringBuilder event = new StringBuilder();
        event.append("Submeter Teste;");
        for (Response element : response) {
            if (element instanceof ResponseLID) {
                if (((ResponseLID) element).getResponse() != null) {
                    for (int responseNumber = 0; responseNumber < ((ResponseLID) element).getResponse().length; responseNumber++) {
                        if (responseNumber != 0) {
                            event.append(",");
                        }
                        event.append(((ResponseLID) element).getResponse()[responseNumber]);
                    }
                }
            } else if (element instanceof ResponseSTR) {
                if (((ResponseSTR) element).getResponse() != null) {
                    event.append(((ResponseSTR) element).getResponse());
                }

            } else if (element instanceof ResponseNUM) {
                if (((ResponseNUM) element).getResponse() != null) {
                    event.append(((ResponseNUM) element).getResponse());
                }

            }
            event.append(";");
        }

        return event.toString();
    }

}