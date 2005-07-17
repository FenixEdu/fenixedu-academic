package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class CurricularCourseGroupTest extends DomainTestBase {

	private IAreaCurricularCourseGroup areaCurricularCourseGroupToDelete;
	private ICurricularCourseGroup curricularCourseGroupToDelete;
	private ICurricularCourseGroup curricularCourseGroupNotToDelete;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		areaCurricularCourseGroupToDelete = new AreaCurricularCourseGroup();
		areaCurricularCourseGroupToDelete.setIdInternal(0);
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
		
		IScientificArea sa3 = new ScientificArea();
		sa3.setIdInternal(3);

		areaCurricularCourseGroupToDelete.setBranch(br1);
		
		curricularCourseGroupToDelete.setBranch(br1);
		curricularCourseGroupNotToDelete.setBranch(br1);
		
		areaCurricularCourseGroupToDelete.addScientificAreas(sa1);
		areaCurricularCourseGroupToDelete.addScientificAreasForThis(sa3);
		
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
	
	public void testDelete() {
		try {
			curricularCourseGroupNotToDelete.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {

		}
		
		assertTrue(curricularCourseGroupNotToDelete.hasBranch());
		assertTrue(curricularCourseGroupNotToDelete.hasAnyScientificAreas());
		assertTrue(curricularCourseGroupNotToDelete.hasAnyCurricularCourses());
		
		try {
			curricularCourseGroupToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(curricularCourseGroupToDelete.hasBranch());
		assertFalse(curricularCourseGroupToDelete.hasAnyScientificAreas());
		assertFalse(curricularCourseGroupToDelete.hasAnyCurricularCourses());
		
		try {
			areaCurricularCourseGroupToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(areaCurricularCourseGroupToDelete.hasBranch());
		assertFalse(areaCurricularCourseGroupToDelete.hasAnyScientificAreas());
		assertFalse(areaCurricularCourseGroupToDelete.hasAnyScientificAreasForThis());
		assertFalse(areaCurricularCourseGroupToDelete.hasAnyCurricularCourses());
	}
}
