package net.sourceforge.fenixedu.domain;


public class DegreeInfoTest extends DomainTestBase {

	private IDegreeInfo degreeInfoWithDegree;
	private IDegreeInfo degreeInfoSimple;
	
	private IDegreeInfo degreeInfoToDelete = null;

	private void setUpDelete() {
		degreeInfoToDelete = new DegreeInfo();
		degreeInfoToDelete.setDegree(new Degree());
	}

	private void setUpCreate() {
		degreeInfoWithDegree = new DegreeInfo(new Degree());
		degreeInfoSimple = new DegreeInfo();
	}

	public void testCreate() {

		setUpCreate();
		
		assertTrue("Failed to assign Degree on creation", degreeInfoWithDegree.hasDegree());
		assertNotNull("LastModificationDate should be set for newly created DegreeInfo", degreeInfoWithDegree.getLastModificationDate());
		
		assertFalse("DegreeInfo should not have Degree", degreeInfoSimple.hasDegree());
		assertNotNull("LastModificationDate should be set for newly created DegreeInfo", degreeInfoSimple.getLastModificationDate());
	}
	
	public void testDelete() {
		
		setUpDelete();
		
		degreeInfoToDelete.delete();
		
		assertFalse("Failed to dereference Degree", degreeInfoToDelete.hasDegree());
	}
}
