/*
 * Created on 12/Fev/2004
 *  
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
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarksStatistics;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarksStatistics {

    protected SiteView run(String executionCourseId, String distributedTestId) throws FenixServiceException {

        InfoSiteStudentsTestMarksStatistics infoSiteStudentsTestMarksStatistics = new InfoSiteStudentsTestMarksStatistics();

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions =
                distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();

        List<String> correctAnswersPercentageList = new ArrayList<String>();
        List<String> partiallyCorrectAnswersPercentage = new ArrayList<String>();
        List<String> wrongAnswersPercentageList = new ArrayList<String>();
        List<String> notAnsweredPercentageList = new ArrayList<String>();
        List<String> answeredPercentageList = new ArrayList<String>();

        DecimalFormat df = new DecimalFormat("#%");
        int numOfStudent = distributedTest.countNumberOfStudents();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            if (studentTestQuestion.getCorrectionFormula().getFormula().intValue() == CorrectionFormula.FENIX) {

                correctAnswersPercentageList.add(df.format(distributedTest.countAnsweres(
                        studentTestQuestion.getTestQuestionOrder(), studentTestQuestion.getTestQuestionValue(), true)
                        * java.lang.Math.pow(numOfStudent, -1)));
                wrongAnswersPercentageList.add(df.format(distributedTest.countAnsweres(
                        studentTestQuestion.getTestQuestionOrder(), studentTestQuestion.getTestQuestionValue(), false)
                        * java.lang.Math.pow(numOfStudent, -1)));

                int partially =
                        distributedTest.countPartiallyCorrectAnswers(studentTestQuestion.getTestQuestionOrder(),
                                studentTestQuestion.getTestQuestionValue());
                if (partially != 0) {
                    partiallyCorrectAnswersPercentage.add(df.format(partially * java.lang.Math.pow(numOfStudent, -1)));
                } else {
                    partiallyCorrectAnswersPercentage.add("-");
                }

            }
            int responsed = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), true);

            notAnsweredPercentageList.add(df.format((numOfStudent - responsed) * java.lang.Math.pow(numOfStudent, -1)));
            answeredPercentageList.add(df.format(responsed * java.lang.Math.pow(numOfStudent, -1)));
        }
        infoSiteStudentsTestMarksStatistics.setCorrectAnswersPercentage(correctAnswersPercentageList);
        infoSiteStudentsTestMarksStatistics.setPartiallyCorrectAnswersPercentage(partiallyCorrectAnswersPercentage);
        infoSiteStudentsTestMarksStatistics.setWrongAnswersPercentage(wrongAnswersPercentageList);
        infoSiteStudentsTestMarksStatistics.setNotAnsweredPercentage(notAnsweredPercentageList);
        infoSiteStudentsTestMarksStatistics.setAnsweredPercentage(answeredPercentageList);
        infoSiteStudentsTestMarksStatistics.setDistributedTest(distributedTest);
        SiteView siteView = new ExecutionCourseSiteView(infoSiteStudentsTestMarksStatistics, infoSiteStudentsTestMarksStatistics);
        return siteView;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTestMarksStatistics serviceInstance = new ReadDistributedTestMarksStatistics();

    @Atomic
    public static SiteView runReadDistributedTestMarksStatistics(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}