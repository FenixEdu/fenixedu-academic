package net.sourceforge.fenixedu.domain;


public class DegreeInfoTest extends DomainTestBase {

	private IDegreeInfo degreeInfoWithDegree;
	private IDegreeInfo degreeInfoSimple;

	protected void setUp() throws Exception {
		super.setUp();
		
		IDegree degree = new Degree();
		degree.setIdInternal(1);
		
		degreeInfoWithDegree = new DegreeInfo(new Degree());
		degreeInfoWithDegree.setIdInternal(1);
		
		degreeInfoSimple = new DegreeInfo();
		degreeInfoSimple.setIdInternal(2);
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
