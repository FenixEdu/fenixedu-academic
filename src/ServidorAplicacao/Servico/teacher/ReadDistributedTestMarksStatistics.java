/*
 * Created on 12/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteStudentsTestMarksStatistics;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionFormula;

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
            persistentSuport = SuportePersistenteOJB.getInstance();
            IDistributedTest distributedTest = new DistributedTest(
                    distributedTestId);
            distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOId(distributedTest,
                            false);
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
            int numOfStudent = persistentStudentTestQuestion
                    .countNumberOfStudents(distributedTest);
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
                        .next();
                if (studentTestQuestion.getCorrectionFormula().getFormula()
                        .intValue() == CorrectionFormula.FENIX) {

                    correctAnswersPercentageList.add(df
                            .format(persistentStudentTestQuestion
                                    .countCorrectOrIncorrectAnswers(
                                            studentTestQuestion
                                                    .getTestQuestionOrder(),
                                            studentTestQuestion
                                                    .getTestQuestionValue(),
                                            true, distributedTest)
                                    * java.lang.Math.pow(numOfStudent, -1)));
                    wrongAnswersPercentageList.add(df
                            .format(persistentStudentTestQuestion
                                    .countCorrectOrIncorrectAnswers(
                                            studentTestQuestion
                                                    .getTestQuestionOrder(),
                                            studentTestQuestion
                                                    .getTestQuestionValue(),
                                            false, distributedTest)
                                    * java.lang.Math.pow(numOfStudent, -1)));

                    int partially = persistentStudentTestQuestion
                            .countPartiallyCorrectAnswers(studentTestQuestion
                                    .getTestQuestionOrder(),
                                    studentTestQuestion.getTestQuestionValue(),
                                    distributedTest);
                    if (partially != 0)
                        partiallyCorrectAnswersPercentage
                                .add(df.format(partially
                                        * java.lang.Math.pow(numOfStudent, -1)));
                    else
                        partiallyCorrectAnswersPercentage.add(new String("-"));

                }
                int responsed = persistentStudentTestQuestion
                        .countResponsedOrNotResponsed(studentTestQuestion
                                .getTestQuestionOrder(), true, distributedTest);

                notAnsweredPercentageList.add(df
                        .format((numOfStudent - responsed)
                                * java.lang.Math.pow(numOfStudent, -1)));
                answeredPercentageList.add(df.format(responsed
                        * java.lang.Math.pow(numOfStudent, -1)));
            }
            infoSiteStudentsTestMarksStatistics
                    .setCorrectAnswersPercentage(correctAnswersPercentageList);
            infoSiteStudentsTestMarksStatistics
                    .setPartiallyCorrectAnswersPercentage(partiallyCorrectAnswersPercentage);
            infoSiteStudentsTestMarksStatistics
                    .setWrongAnswersPercentage(wrongAnswersPercentageList);
            infoSiteStudentsTestMarksStatistics
                    .setNotAnsweredPercentage(notAnsweredPercentageList);
            infoSiteStudentsTestMarksStatistics
                    .setAnsweredPercentage(answeredPercentageList);
            infoSiteStudentsTestMarksStatistics.setInfoDistributedTest(Cloner
                    .copyIDistributedTest2InfoDistributedTest(distributedTest));
            infoSiteStudentsTestMarksStatistics
                    .setExecutionCourse((InfoExecutionCourse) infoSiteStudentsTestMarksStatistics
                            .getInfoDistributedTest().getInfoTestScope()
                            .getInfoObject());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        SiteView siteView = new ExecutionCourseSiteView(
                infoSiteStudentsTestMarksStatistics,
                infoSiteStudentsTestMarksStatistics);
        return siteView;
    }
}