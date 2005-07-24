package net.sourceforge.fenixedu.domain;


public class DegreeInfoTest extends DomainTestBase {

	private IDegreeInfo degreeInfoWithDegree;
	private IDegreeInfo degreeInfoSimple;
	
	private IDegreeInfo degreeInfoToDelete = null;

	protected void setUp() throws Exception {
		super.setUp();
		
		setUpCreate();
		setUpDelete();
	}

	private void setUpDelete() {
		degreeInfoToDelete = new DegreeInfo();
		degreeInfoToDelete.setDegree(new Degree());
	}

	private void setUpCreate() {
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
	
	public void testDelete() {
		degreeInfoToDelete.delete();
		
		assertFalse(degreeInfoToDelete.hasDegree());
	}
}
