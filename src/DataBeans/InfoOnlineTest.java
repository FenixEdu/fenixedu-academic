/*
 * Created on 29/Jan/2004
 *  
 */
package DataBeans;

import java.util.List;

import Dominio.IOnlineTest;
import Util.EvaluationType;

/**
 * @author Susana Fernandes
 *  
 */

public class InfoOnlineTest extends InfoEvaluation implements ISiteComponent {

	private InfoDistributedTest infoDistributedTest;

	private List associatedExecutionCourse;

	private String publishmentMessage;

	public InfoOnlineTest() {
	}

	public List getAssociatedExecutionCourse() {
		return associatedExecutionCourse;
	}

	public InfoDistributedTest getInfoDistributedTest() {
		return infoDistributedTest;
	}

	public String getPublishmentMessage() {
		return publishmentMessage;
	}

	public void setAssociatedExecutionCourse(List list) {
		associatedExecutionCourse = list;
	}

	public void setInfoDistributedTest(InfoDistributedTest test) {
		infoDistributedTest = test;
	}

	public void setPublishmentMessage(String string) {
		publishmentMessage = string;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoExam) {
			InfoOnlineTest infoOnlineTest = (InfoOnlineTest) obj;

			result = getIdInternal().equals(infoOnlineTest.getIdInternal())
					&& getInfoDistributedTest().equals(
							infoOnlineTest.getInfoDistributedTest())
					&& getPublishmentMessage().equals(
							infoOnlineTest.getPublishmentMessage())
					&& getAssociatedExecutionCourse().containsAll(
							infoOnlineTest.getAssociatedExecutionCourse())
					&& infoOnlineTest.getAssociatedExecutionCourse()
							.containsAll(getAssociatedExecutionCourse());
		}
		return result;
	}

	public void copyFromDomain(IOnlineTest onlineTest) {
		super.copyFromDomain(onlineTest);
		if (onlineTest != null) {
			setEvaluationType(EvaluationType.ONLINE_TEST_TYPE);
			setInfoDistributedTest(InfoDistributedTest
					.newInfoFromDomain(onlineTest.getDistributedTest()));
		}
	}

	/**
	 * @param onlineTest
	 * @return
	 */
	public static InfoOnlineTest newInfoFromDomain(IOnlineTest onlineTest) {
		InfoOnlineTest infoOnlineTest = null;
		if (onlineTest != null) {
			infoOnlineTest = new InfoOnlineTest();
			infoOnlineTest.copyFromDomain(onlineTest);
		}
		return infoOnlineTest;
	}

}