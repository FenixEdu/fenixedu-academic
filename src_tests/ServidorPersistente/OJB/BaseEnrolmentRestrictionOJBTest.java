/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;


/**
 * @author jpvl
 */
abstract public class BaseEnrolmentRestrictionOJBTest extends TestCaseOJB {

	/**
	 * @param testName
	 */
	public BaseEnrolmentRestrictionOJBTest(String testName) {
		super(testName);
	}
	
	/* (non-Javadoc)
	 * @see ServidorPersistente.OJB.TestCaseOJB#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForRestrictionsTests.xml";
	}

}
