/*
 * Created on 9/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoInquiryStatistics;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInquiryStatistics;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadInquiryStatistics implements IService {

    private String path = new String();

    public ReadInquiryStatistics() {
    }

    public SiteView run(Integer executionCourseId, Integer distributedTestId, String path)
            throws FenixServiceException {
        this.path = path.replace('\\', '/');
        InfoSiteInquiryStatistics infoSiteInquiryStatistics = new InfoSiteInquiryStatistics();
        List infoInquiryStatisticsList = new ArrayList();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            infoSiteInquiryStatistics.setExecutionCourse(InfoExecutionCourse
                    .newInfoFromDomain((IExecutionCourse) distributedTest.getTestScope()
                            .getDomainObject()));
            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                    .getIPersistentStudentTestQuestion();

            List testQuestionList = persistentStudentTestQuestion
                    .readStudentTestQuestionsByDistributedTest(distributedTest);
            Iterator it = testQuestionList.iterator();

            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                InfoInquiryStatistics infoInquiryStatistics = new InfoInquiryStatistics();

                InfoStudentTestQuestion infoStudentTestQuestion;
                ParseQuestion parse = new ParseQuestion();
                try {
                    if (studentTestQuestion.getOptionShuffle() == null) {
                        persistentSuport.getIPersistentStudentTestQuestion().simpleLockWrite(
                                studentTestQuestion);
                        boolean shuffle = true;
                        if (distributedTest.getTestType().equals(new TestType(3))) //INQUIRY
                            shuffle = false;
                        studentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(
                                studentTestQuestion.getQuestion().getXmlFile(), shuffle, this.path));
                    }
                    infoStudentTestQuestion = InfoStudentTestQuestionWithAll
                            .newInfoFromDomain(studentTestQuestion);
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
                            this.path);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                List statistics = new ArrayList();
                DecimalFormat df = new DecimalFormat("#%");
                int numOfStudentResponses = persistentStudentTestQuestion.countResponsedOrNotResponsed(
                        studentTestQuestion.getTestQuestionOrder(), true, distributedTest);
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
                    for (int i = 1; i <= infoStudentTestQuestion.getQuestion().getOptionNumber()
                            .intValue(); i++) {

                        String response = new String("%<string>" + i + "</string>%");

                        int mark = persistentStudentTestQuestion.countResponses(studentTestQuestion
                                .getTestQuestionOrder(), response, distributedTest);
                        String percentage = new String("0%");
                        if (mark != 0)
                            percentage = df.format(mark * java.lang.Math.pow(numOfStudentResponses, -1));
                        statistics.add(new LabelValueBean(new Integer(i).toString(), percentage));
                    }
                } else {
                    List responses = persistentStudentTestQuestion.getResponses(studentTestQuestion
                            .getTestQuestionOrder(), distributedTest);

                    String percentage = new String("0%");
                    Iterator responseIt = responses.iterator();
                    while (responseIt.hasNext()) {
                        String response = (String) responseIt.next();

                        percentage = df.format(persistentStudentTestQuestion.countByResponse(response,
                                studentTestQuestion.getTestQuestionOrder(), distributedTest)
                                * java.lang.Math.pow(numOfStudentResponses, -1));

                        XMLDecoder decoder = new XMLDecoder(
                                new ByteArrayInputStream(response.getBytes()));
                        Response r = (Response) decoder.readObject();
                        decoder.close();
                        if (r instanceof ResponseSTR)
                            statistics.add(new LabelValueBean(percentage, ((ResponseSTR) r)
                                    .getResponse()));
                        else
                            statistics.add(new LabelValueBean(percentage, ((ResponseNUM) r)
                                    .getResponse()));

                    }
                }
                statistics.add(new LabelValueBean(new String("Número de alunos que responderam"),
                        new Integer(numOfStudentResponses).toString()));
                infoInquiryStatistics.setInfoStudentTestQuestion(infoStudentTestQuestion);
                infoInquiryStatistics.setOptionStatistics(statistics);
                infoInquiryStatisticsList.add(infoInquiryStatistics);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        infoSiteInquiryStatistics.setInfoInquiryStatistics(infoInquiryStatisticsList);
        SiteView siteView = new ExecutionCourseSiteView(infoSiteInquiryStatistics,
                infoSiteInquiryStatistics);
        return siteView;
    }

}