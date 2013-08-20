/*
 * Created on 29/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.util.EvaluationType;

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

    @Override
    public String getPublishmentMessage() {
        return publishmentMessage;
    }

    public void setAssociatedExecutionCourse(List list) {
        associatedExecutionCourse = list;
    }

    public void setInfoDistributedTest(InfoDistributedTest test) {
        infoDistributedTest = test;
    }

    @Override
    public void setPublishmentMessage(String string) {
        publishmentMessage = string;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoExam) {
            InfoOnlineTest infoOnlineTest = (InfoOnlineTest) obj;

            result =
                    getExternalId().equals(infoOnlineTest.getExternalId())
                            && getInfoDistributedTest().equals(infoOnlineTest.getInfoDistributedTest())
                            && getPublishmentMessage().equals(infoOnlineTest.getPublishmentMessage())
                            && getAssociatedExecutionCourse().containsAll(infoOnlineTest.getAssociatedExecutionCourse())
                            && infoOnlineTest.getAssociatedExecutionCourse().containsAll(getAssociatedExecutionCourse());
        }
        return result;
    }

    public void copyFromDomain(OnlineTest onlineTest) {
        super.copyFromDomain(onlineTest);
        if (onlineTest != null) {
            setEvaluationType(EvaluationType.ONLINE_TEST_TYPE);
            setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(onlineTest.getDistributedTest()));
        }
    }

    /**
     * @param onlineTest
     * @return
     */
    public static InfoOnlineTest newInfoFromDomain(OnlineTest onlineTest) {
        InfoOnlineTest infoOnlineTest = null;
        if (onlineTest != null) {
            infoOnlineTest = new InfoOnlineTest();
            infoOnlineTest.copyFromDomain(onlineTest);
        }
        return infoOnlineTest;
    }

}