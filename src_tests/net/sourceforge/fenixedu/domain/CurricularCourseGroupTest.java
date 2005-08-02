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

	
	public void testDelete() {
		
		setUpDeleteCase();
		
		try {
			curricularCourseGroupNotToDelete.delete();
			fail("Expected DomainException: should not have been deleted.");
		} catch (DomainException e) {

		}
		
		assertTrue("Should not have dereferenced Branch", curricularCourseGroupNotToDelete.hasBranch());
		assertTrue("Should not have dereferenced ScientificAreas", curricularCourseGroupNotToDelete.hasAnyScientificAreas());
		assertTrue("Should not have dereferenced CurricularCourses", curricularCourseGroupNotToDelete.hasAnyCurricularCourses());
		
		try {
			curricularCourseGroupToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Should have dereferenced Branch", curricularCourseGroupToDelete.hasBranch());
		assertFalse("Should have dereferenced ScientificAreas", curricularCourseGroupToDelete.hasAnyScientificAreas());
		assertFalse("Should have dereferenced CurricularCourses", curricularCourseGroupToDelete.hasAnyCurricularCourses());
	}
	
	public void testEdit() {
		
		setUpEditCase();
		
		areaCurricularcourseGroupToEdit.edit(editedAreaCCGName,editedBranch,editedMinimumValue,editedMaximumValue,editedAreaType);
		
		assertEquals("Edited name does not match expected", areaCurricularcourseGroupToEdit.getName(),editedAreaCCGName);
		assertEquals("Edited Branch does not match expected", areaCurricularcourseGroupToEdit.getBranch(),editedBranch);
		assertEquals("Edited minimumCredits does not match expected", areaCurricularcourseGroupToEdit.getMinimumCredits(),editedMinimumValue);
		assertEquals("Edited maximumCredits does not match expected", areaCurricularcourseGroupToEdit.getMaximumCredits(),editedMaximumValue);
		assertEquals("Edited AreaType does not match expected", areaCurricularcourseGroupToEdit.getAreaType(),editedAreaType);
		
		optionalCurricularcourseGroupToEdit.edit(editedOptionalCCGName,editedBranch,editedMinimumValue,editedMaximumValue,editedAreaType);

		assertEquals("Edited name does not match expected", optionalCurricularcourseGroupToEdit.getName(),editedOptionalCCGName);
		assertEquals("Edited Branch does not match expected", optionalCurricularcourseGroupToEdit.getBranch(),editedBranch);
		assertEquals("Edited minimumNumberOfOptionalCourses does not match expected", optionalCurricularcourseGroupToEdit.getMinimumNumberOfOptionalCourses(),editedMinimumValue);
		assertEquals("Edited maximumNumberOfOptionalCourses does not match expected", optionalCurricularcourseGroupToEdit.getMaximumNumberOfOptionalCourses(),editedMaximumValue);
		assertNull("Edited AreaType does not match expected", optionalCurricularcourseGroupToEdit.getAreaType());
	}
}
