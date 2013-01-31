/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionMark;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks extends FenixService {

	public InfoSiteStudentsTestMarks run(Integer executionCourseId, Integer distributedTestId, String path)
			throws FenixServiceException {

		InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();

		DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
		if (distributedTest == null) {
			throw new InvalidArgumentsServiceException();
		}

		List<StudentTestQuestion> studentTestQuestionList =
				distributedTest.getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder();

		HashMap<Integer, InfoStudentTestQuestionMark> infoStudentTestQuestionMarkList =
				new HashMap<Integer, InfoStudentTestQuestionMark>();
		for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
			if (infoStudentTestQuestionMarkList.containsKey(studentTestQuestion.getStudent().getIdInternal())) {
				InfoStudentTestQuestionMark infoStudentTestQuestionMark =
						infoStudentTestQuestionMarkList.get(studentTestQuestion.getStudent().getIdInternal());
				ParseSubQuestion parse = new ParseSubQuestion();
				Question question = studentTestQuestion.getQuestion();
				try {
					question = parse.parseSubQuestion(studentTestQuestion.getQuestion(), path.replace('\\', '/'));
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}
				if (studentTestQuestion.getItemId() != null
						&& !studentTestQuestion.getItemId().equals(question.getSubQuestions().get(0).getItemId())) {
					infoStudentTestQuestionMark.addTestQuestionMark(
							infoStudentTestQuestionMark.getTestQuestionMarks().size() - 1,
							studentTestQuestion.getTestQuestionMark());
				} else {
					infoStudentTestQuestionMark.addTestQuestionMark(studentTestQuestion.getTestQuestionMark());
				}
				infoStudentTestQuestionMark.addToMaximumMark(studentTestQuestion.getTestQuestionValue());
			} else {
				infoStudentTestQuestionMarkList.put(studentTestQuestion.getStudent().getIdInternal(),
						InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion));
			}
		}

		List<InfoStudentTestQuestionMark> infoStudentTestQuestionList =
				new ArrayList<InfoStudentTestQuestionMark>(infoStudentTestQuestionMarkList.values());
		Collections.sort(infoStudentTestQuestionList, new BeanComparator("studentNumber"));
		infoSiteStudentsTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
		infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) distributedTest
				.getTestScope().getDomainObject()));
		infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(distributedTest));

		return infoSiteStudentsTestMarks;
	}
}