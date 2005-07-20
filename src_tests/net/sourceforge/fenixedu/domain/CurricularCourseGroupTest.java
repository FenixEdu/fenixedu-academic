package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;


public class CurricularCourseGroupTest extends DomainTestBase {
	
	private ICurricularCourseGroup curricularCourseGroupToDelete;
	private ICurricularCourseGroup curricularCourseGroupNotToDelete;
	
	private IAreaCurricularCourseGroup areaCurricularcourseGroupToEdit = null;
	private IOptionalCurricularCourseGroup optionalCurricularcourseGroupToEdit = null;
	private String editedAreaCCGName = null;
	private String editedOptionalCCGName = null;
	private IBranch editedBranch = null;
	private Integer editedMinimumValue = null;
	private Integer editedMaximumValue = null;
	private AreaType editedAreaType = null;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		setUpDeleteCase();
		
		setUpEditCase();
    }

	private void setUpEditCase() {
		areaCurricularcourseGroupToEdit = new AreaCurricularCourseGroup();
		optionalCurricularcourseGroupToEdit = new OptionalCurricularCourseGroup();
		
		editedAreaCCGName = "Area Curricular Course Group";
		editedOptionalCCGName = "Optional Curricular Course Group";
		
		editedBranch = new Branch();
		
		editedMinimumValue = 1;
		editedMaximumValue = 2;
		
		editedAreaType = AreaType.BASE;
	}

	private void setUpDeleteCase() {
		curricularCourseGroupToDelete = new AreaCurricularCourseGroup();
		curricularCourseGroupNotToDelete = new OptionalCurricularCourseGroup();
		
		ICurricularCourse cc1 = new CurricularCourse();
		ICurricularCourse cc2 = new CurricularCourse();
		
		IBranch br1 = new Branch();
		IScientificArea sa1 = new ScientificArea();
		IScientificArea sa2 = new ScientificArea();
		
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
		
		
		
		areaCurricularcourseGroupToEdit.edit(editedAreaCCGName,editedBranch,editedMinimumValue,editedMaximumValue,editedAreaType);
		
		assertEquals(areaCurricularcourseGroupToEdit.getName(),editedAreaCCGName);
		assertEquals(areaCurricularcourseGroupToEdit.getBranch(),editedBranch);
		assertEquals(areaCurricularcourseGroupToEdit.getMinimumCredits(),editedMinimumValue);
		assertEquals(areaCurricularcourseGroupToEdit.getMaximumCredits(),editedMaximumValue);
		assertEquals(areaCurricularcourseGroupToEdit.getAreaType(),editedAreaType);
		
		optionalCurricularcourseGroupToEdit.edit(editedOptionalCCGName,editedBranch,editedMinimumValue,editedMaximumValue,editedAreaType);

		assertEquals(optionalCurricularcourseGroupToEdit.getName(),editedOptionalCCGName);
		assertEquals(optionalCurricularcourseGroupToEdit.getBranch(),editedBranch);
		assertEquals(optionalCurricularcourseGroupToEdit.getMinimumNumberOfOptionalCourses(),editedMinimumValue);
		assertEquals(optionalCurricularcourseGroupToEdit.getMaximumNumberOfOptionalCourses(),editedMaximumValue);
		assertNull(optionalCurricularcourseGroupToEdit.getAreaType());
	}
}
