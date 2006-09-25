/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionMark;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks extends Service {

    public InfoSiteStudentsTestMarks run(Integer executionCourseId, Integer distributedTestId, String path)
            throws FenixServiceException, ExcepcaoPersistencia {

        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();

        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestionList = distributedTest
                .getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder();

        HashMap<Integer, InfoStudentTestQuestionMark> infoStudentTestQuestionMarkList = new HashMap<Integer, InfoStudentTestQuestionMark>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (infoStudentTestQuestionMarkList
                    .containsKey(studentTestQuestion.getStudent().getNumber())) {
                InfoStudentTestQuestionMark infoStudentTestQuestionMark = infoStudentTestQuestionMarkList
                        .get(studentTestQuestion.getStudent().getNumber());
                ParseSubQuestion parse = new ParseSubQuestion();
                Question question = studentTestQuestion.getQuestion();
                try {
                    question = parse.parseSubQuestion(studentTestQuestion.getQuestion(), path.replace(
                            '\\', '/'));
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                if (studentTestQuestion.getItemId() != null
                        && !studentTestQuestion.getItemId().equals(
                                question.getSubQuestions().get(0).getItemId())) {
                    infoStudentTestQuestionMark.addTestQuestionMark(infoStudentTestQuestionMark
                            .getTestQuestionMarks().size() - 1, studentTestQuestion
                            .getTestQuestionMark());
                } else {
                    infoStudentTestQuestionMark.addTestQuestionMark(studentTestQuestion
                            .getTestQuestionMark());
                }
                infoStudentTestQuestionMark.addToMaximumMark(studentTestQuestion.getTestQuestionValue());
            } else {
                infoStudentTestQuestionMarkList.put(studentTestQuestion.getStudent().getNumber(),
                        InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion));
            }
        }
        // List<InfoStudentTestQuestionMark> infoStudentTestQuestionList =
        // (List<InfoStudentTestQuestionMark>) CollectionUtils
        // .collect(studentTestQuestionList, new Transformer() {
        //
        // public Object transform(Object arg0) {
        // StudentTestQuestion studentTestQuestion = (StudentTestQuestion) arg0;
        // return
        // InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion);
        // }
        //
        // });

        // infoSiteStudentsTestMarks.setMaximumMark(distributedTest.calculateMaximumDistributedTestMark());
        infoSiteStudentsTestMarks
                .setInfoStudentTestQuestionList(new ArrayList<InfoStudentTestQuestionMark>(
                        infoStudentTestQuestionMarkList.values()));
        infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse
                .newInfoFromDomain((ExecutionCourse) distributedTest.getTestScope().getDomainObject()));
        infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest
                .newInfoFromDomain(distributedTest));

        return infoSiteStudentsTestMarks;
    }
}