/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class DeleteExerciseVariation {

    public List<LabelValueBean> run(String executionCourseId, String questionCode) throws InvalidArgumentsServiceException {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        Question question = AbstractDomainObject.fromExternalId(questionCode);

        if (question == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (StudentTestQuestion studentTestQuestion : question.getStudentTestsQuestions()) {
            if (compareDates(studentTestQuestion.getDistributedTest().getEndDate(), studentTestQuestion.getDistributedTest()
                    .getEndHour())) {
                result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle(), studentTestQuestion
                        .getStudent().getNumber().toString()));
            }
        }

        if (result.size() == 0) {
            Question newQuestion = getNewQuestion(question);
            for (TestQuestion testQuestion : question.getTestQuestions()) {
                if (newQuestion == null) {
                    testQuestion.getTest().deleteTestQuestion(testQuestion);
                } else {
                    testQuestion.setQuestion(getNewQuestion(question));
                }
            }
            Metadata metadata = question.getMetadata();
            if (question.getStudentTestsQuestions() == null || question.getStudentTestsQuestions().size() == 0) {
                question.delete();
                if (metadata.getQuestionsCount() <= 1) {
                    metadata.delete();
                } else if (metadata.getVisibleQuestions().size() == 0) {
                    metadata.setVisibility(Boolean.FALSE);
                }
            } else {
                if (metadata.getVisibleQuestions().size() <= 1) {
                    metadata.setVisibility(Boolean.FALSE);
                }
                question.setVisibility(Boolean.FALSE);
            }
        }

        return result;
    }

    private boolean compareDates(Calendar date, Calendar hour) {
        final Calendar calendar = Calendar.getInstance();
        final CalendarDateComparator dateComparator = new CalendarDateComparator();
        final CalendarHourComparator hourComparator = new CalendarHourComparator();
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

    private Question getNewQuestion(Question oldQuestion) {
        Metadata metadata = oldQuestion.getMetadata();
        if (metadata.getVisibleQuestions().size() > 1) {
            for (Question question : metadata.getVisibleQuestions()) {
                if (question.equals(oldQuestion)) {
                    return question;
                }
            }
        }
        return null;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteExerciseVariation serviceInstance = new DeleteExerciseVariation();

    @Service
    public static List<LabelValueBean> runDeleteExerciseVariation(String executionCourseId, String questionCode)
            throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, questionCode);
    }

}