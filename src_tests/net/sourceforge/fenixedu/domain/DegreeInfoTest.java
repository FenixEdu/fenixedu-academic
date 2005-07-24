package net.sourceforge.fenixedu.domain;


public class DegreeInfoTest extends DomainTestBase {

	private IDegreeInfo degreeInfoWithDegree;
	private IDegreeInfo degreeInfoSimple;

	protected void setUp() throws Exception {
		super.setUp();
		
		degreeInfoWithDegree = new DegreeInfo(new Degree());

		degreeInfoSimple = new DegreeInfo();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreate() {
		
		assertTrue(degreeInfoWithDegree.hasDegree());
		assertNotNull(degreeInfoWithDegree.getLastModificationDate());
		
		assertFalse(degreeInfoSimple.hasDegree());
		assertNotNull(degreeInfoSimple.getLastModificationDate());
	}
}
