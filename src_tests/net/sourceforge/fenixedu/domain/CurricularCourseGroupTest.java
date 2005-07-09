package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class CurricularCourseGroupTest extends DomainTestBase {

	private ICurricularCourseGroup curricularCourseGroupToDelete;
	private ICurricularCourseGroup curricularCourseGroupNotToDelete;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		curricularCourseGroupToDelete = new AreaCurricularCourseGroup();
		curricularCourseGroupToDelete.setIdInternal(1);
		curricularCourseGroupNotToDelete = new OptionalCurricularCourseGroup();
		curricularCourseGroupNotToDelete.setIdInternal(2);
		
		ICurricularCourse cc1 = new CurricularCourse();
		cc1.setIdInternal(1);
		ICurricularCourse cc2 = new CurricularCourse();
		cc2.setIdInternal(2);
		IBranch br1 = new Branch();
		br1.setIdInternal(1);
		IScientificArea sa1 = new ScientificArea();
		sa1.setIdInternal(1);
		IScientificArea sa2 = new ScientificArea();
		sa2.setIdInternal(2);

		curricularCourseGroupToDelete.setBranch(br1);
		curricularCourseGroupNotToDelete.setBranch(br1);
		
		curricularCourseGroupToDelete.addScientificAreas(sa1);
		curricularCourseGroupToDelete.addScientificAreas(sa2);
		
		curricularCourseGroupNotToDelete.addScientificAreas(sa1);
		curricularCourseGroupNotToDelete.addScientificAreas(sa2);
		
		curricularCourseGroupNotToDelete.addCurricularCourses(cc1);
		curricularCourseGroupNotToDelete.addCurricularCourses(cc2);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDeleteCurricularCourseGroup() {
		try {
			curricularCourseGroupNotToDelete.deleteCurricularCourseGroup();
			fail("Expected DomainException.");
		} catch (DomainException e) {

		}
		
		try {
			curricularCourseGroupToDelete.deleteCurricularCourseGroup();
		} catch (DomainException e) {
			fail("Unexpected DomainException.");
		}
		
		assertNull(curricularCourseGroupToDelete.getBranch());
		assertTrue(curricularCourseGroupToDelete.getScientificAreas().isEmpty());
		assertTrue(curricularCourseGroupToDelete.getCurricularCourses().isEmpty());
		
		assertNotNull(curricularCourseGroupNotToDelete.getBranch());
		assertFalse(curricularCourseGroupNotToDelete.getScientificAreas().isEmpty());
		assertFalse(curricularCourseGroupNotToDelete.getCurricularCourses().isEmpty());
	}
}
