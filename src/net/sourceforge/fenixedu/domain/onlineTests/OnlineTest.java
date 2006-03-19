/*
 * Created on 29/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class OnlineTest extends OnlineTest_Base {

    public OnlineTest() {
    	super();
        this.setOjbConcreteClass(OnlineTest.class.getName());
    }
    
	public EvaluationType getEvaluationType() {
		return EvaluationType.ONLINE_TEST_TYPE;
	}
}