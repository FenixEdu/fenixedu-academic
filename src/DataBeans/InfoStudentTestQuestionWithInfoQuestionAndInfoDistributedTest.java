/*
 * Created on 28/Ago/2003
 */
package DataBeans;

import Dominio.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
		extends InfoStudentTestQuestionWithInfoQuestion {

	public void copyFromDomain(IStudentTestQuestion studentTestQuestion) {
		super.copyFromDomain(studentTestQuestion);
		if (studentTestQuestion != null) {
			setDistributedTest(InfoDistributedTestWithTestScope
					.newInfoFromDomain(studentTestQuestion.getDistributedTest()));
		}
	}

	public static InfoStudentTestQuestion newInfoFromDomain(
			IStudentTestQuestion studentTestQuestion) {
		InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest infoStudentTestQuestion = null;
		if (studentTestQuestion != null) {
			infoStudentTestQuestion = new InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest();
			infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
		}
		return infoStudentTestQuestion;
	}

}