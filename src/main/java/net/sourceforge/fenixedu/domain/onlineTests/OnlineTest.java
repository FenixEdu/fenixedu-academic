package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.EvaluationType;

public class OnlineTest extends OnlineTest_Base {

    public static List<OnlineTest> readOnlineTests() {
        List<OnlineTest> result = new ArrayList<OnlineTest>();

        for (Evaluation evaluation : Bennu.getInstance().getEvaluationsSet()) {
            if (evaluation instanceof OnlineTest) {
                result.add((OnlineTest) evaluation);
            }
        }

        return result;
    }

    public OnlineTest() {
        super();
        setGradeScale(GradeScale.TYPE20);
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.ONLINE_TEST_TYPE;
    }

    @Override
    public void delete() {
        logRemove();
        setDistributedTest(null);
        super.delete();
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.online.test") + " "
                + getDistributedTest().getEvaluationTitle();
    }

    @Deprecated
    public boolean hasDistributedTest() {
        return getDistributedTest() != null;
    }

}
