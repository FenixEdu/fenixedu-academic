/*
 * Created on 12/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTestWithTestScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsTestMarksStatistics;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarksStatistics implements IService {

    public ReadDistributedTestMarksStatistics() {
    }

    public SiteView run(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException {

        ISuportePersistente persistentSuport;
        InfoSiteStudentsTestMarksStatistics infoSiteStudentsTestMarksStatistics = new InfoSiteStudentsTestMarksStatistics();
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                    .getIPersistentStudentTestQuestion();
            List studentTestQuestionList = persistentStudentTestQuestion
                    .readStudentTestQuestionsByDistributedTest(distributedTest);
            Iterator it = studentTestQuestionList.iterator();

            List correctAnswersPercentageList = new ArrayList();
            List partiallyCorrectAnswersPercentage = new ArrayList();
            List wrongAnswersPercentageList = new ArrayList();
            List notAnsweredPercentageList = new ArrayList();
            List answeredPercentageList = new ArrayList();

            DecimalFormat df = new DecimalFormat("#%");
            int numOfStudent = persistentStudentTestQuestion.countNumberOfStudents(distributedTest);
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getCorrectionFormula().getFormula().intValue() == CorrectionFormula.FENIX) {

                    correctAnswersPercentageList.add(df.format(persistentStudentTestQuestion
                            .countCorrectOrIncorrectAnswers(studentTestQuestion.getTestQuestionOrder(),
                                    studentTestQuestion.getTestQuestionValue(), true, distributedTest)
                            * java.lang.Math.pow(numOfStudent, -1)));
                    wrongAnswersPercentageList.add(df.format(persistentStudentTestQuestion
                            .countCorrectOrIncorrectAnswers(studentTestQuestion.getTestQuestionOrder(),
                                    studentTestQuestion.getTestQuestionValue(), false, distributedTest)
                            * java.lang.Math.pow(numOfStudent, -1)));

                    int partially = persistentStudentTestQuestion.countPartiallyCorrectAnswers(
                            studentTestQuestion.getTestQuestionOrder(), studentTestQuestion
                                    .getTestQuestionValue(), distributedTest);
                    if (partially != 0)
                        partiallyCorrectAnswersPercentage.add(df.format(partially
                                * java.lang.Math.pow(numOfStudent, -1)));
                    else
                        partiallyCorrectAnswersPercentage.add(new String("-"));

                }
                int responsed = persistentStudentTestQuestion.countResponsedOrNotResponsed(
                        studentTestQuestion.getTestQuestionOrder(), true, distributedTest);

                notAnsweredPercentageList.add(df.format((numOfStudent - responsed)
                        * java.lang.Math.pow(numOfStudent, -1)));
                answeredPercentageList.add(df.format(responsed * java.lang.Math.pow(numOfStudent, -1)));
            }
            infoSiteStudentsTestMarksStatistics
                    .setCorrectAnswersPercentage(correctAnswersPercentageList);
            infoSiteStudentsTestMarksStatistics
                    .setPartiallyCorrectAnswersPercentage(partiallyCorrectAnswersPercentage);
            infoSiteStudentsTestMarksStatistics.setWrongAnswersPercentage(wrongAnswersPercentageList);
            infoSiteStudentsTestMarksStatistics.setNotAnsweredPercentage(notAnsweredPercentageList);
            infoSiteStudentsTestMarksStatistics.setAnsweredPercentage(answeredPercentageList);
            infoSiteStudentsTestMarksStatistics.setInfoDistributedTest(InfoDistributedTestWithTestScope
                    .newInfoFromDomain(distributedTest));
            infoSiteStudentsTestMarksStatistics
                    .setExecutionCourse((InfoExecutionCourse) infoSiteStudentsTestMarksStatistics
                            .getInfoDistributedTest().getInfoTestScope().getInfoObject());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        SiteView siteView = new ExecutionCourseSiteView(infoSiteStudentsTestMarksStatistics,
                infoSiteStudentsTestMarksStatistics);
        return siteView;
    }
}