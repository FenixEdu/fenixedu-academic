package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.EvaluationType;

public class OnlineTest extends OnlineTest_Base {

    public static List<OnlineTest> readOnlineTests() {
	List<OnlineTest> result = new ArrayList<OnlineTest>();

	for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
	    if (evaluation instanceof OnlineTest) {
		result.add((OnlineTest) evaluation);
	    }
	}

	return result;
    }

    public OnlineTest() {
	super();
    }

    public EvaluationType getEvaluationType() {
	return EvaluationType.ONLINE_TEST_TYPE;
    }

    public void delete() {
	removeDistributedTest();
	super.delete();
    }

}
