/*
 * Created on 8/Ago/2005 - 16:26:34
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class CareerTest extends DomainTestBase {
	
	//Career attributes
    Integer beginYear = 2004;
    Integer endYear = 2005;
    Teacher teacher;

	//Teaching career attributes
    String courseOrPosition = "courseOrPosition";
	Category category;
	String categoryCode = "code";
	String categoryLongName = "longName";
	String categoryShortName = "shortName";
	Boolean categoryCanBeExecutionCourseResponsible = Boolean.TRUE;
	Category category2;
	String categoryCode2 = "code2";
	String categoryLongName2 = "longName2";
	String categoryShortName2 = "shortName2";
	Boolean categoryCanBeExecutionCourseResponsible2 = Boolean.TRUE;
	
	//Professional career attributes
    String entity = "entity";
    String function = "function";
	
	
	TeachingCareer teachingCareerToDelete;
	ProfessionalCareer professionalCareerToDelete;
	
	TeachingCareer teachingCareerToEdit;
	ProfessionalCareer professionalCareerToEdit;
	
	InfoTeachingCareer infoTeachingCareerToCreate;
	InfoProfessionalCareer infoProfessionalCareerToCreate;
	InfoTeachingCareer infoTeachingCareerToEdit;
	InfoProfessionalCareer infoProfessionalCareerToEdit;
	

	
	protected void setUp() throws Exception {
		super.setUp();
//		teacher = new Teacher();

		infoTeachingCareerToCreate = new InfoTeachingCareer();
		infoTeachingCareerToCreate.setBeginYear(2003);
		infoTeachingCareerToCreate.setEndYear(2006);
		infoTeachingCareerToCreate.setCourseOrPosition("new course or position");
		
		infoProfessionalCareerToCreate = new InfoProfessionalCareer();
		infoProfessionalCareerToCreate.setBeginYear(2003);
		infoProfessionalCareerToCreate.setEndYear(2006);
		infoProfessionalCareerToCreate.setEntity("new entity");
		infoProfessionalCareerToCreate.setFunction("new function");

		category = new Category(); 
		category.setCode(categoryCode);
		category.setLongName(categoryLongName);
		category.setShortName(categoryShortName);
		category.setCanBeExecutionCourseResponsible(categoryCanBeExecutionCourseResponsible);

		category2 = new Category();
		category2.setCode(categoryCode2);
		category2.setLongName(categoryLongName2);
		category2.setShortName(categoryShortName2);
		category2.setCanBeExecutionCourseResponsible(categoryCanBeExecutionCourseResponsible2);

		
		
	}

//	private void initializeCareer(TeachingCareer teachingCareer) {
//		teachingCareer.setBeginYear(beginYear);
//		teachingCareer.setEndYear(endYear);
//		teachingCareer.setCourseOrPosition(courseOrPosition);
//			
//		teachingCareer.setCategory(category);
//	}
//	
//	
//	private void initializeCareer(ProfessionalCareer professionalCareer) {
//		professionalCareer.setBeginYear(beginYear);
//		professionalCareer.setEndYear(endYear);
//		professionalCareer.setEntity(entity);
//		professionalCareer.setFunction(function);
//	}

	private void setUpDelete() {
		teachingCareerToDelete = new TeachingCareer(teacher, category, infoTeachingCareerToCreate);
		
		professionalCareerToDelete = new ProfessionalCareer(teacher, infoProfessionalCareerToCreate);
	}

	private void setUpEdit() {
		teachingCareerToEdit = new TeachingCareer(teacher, category, infoTeachingCareerToCreate);
		
		professionalCareerToEdit = new ProfessionalCareer(teacher, infoProfessionalCareerToCreate);
		
		infoTeachingCareerToEdit = new InfoTeachingCareer();
		infoTeachingCareerToEdit.setBeginYear(2002);
		infoTeachingCareerToEdit.setEndYear(2006);
		infoTeachingCareerToEdit.setCourseOrPosition("Edited course or position");
		
		infoProfessionalCareerToEdit = new InfoProfessionalCareer();
		infoProfessionalCareerToEdit.setBeginYear(2003);
		infoProfessionalCareerToEdit.setEndYear(2005);
		infoProfessionalCareerToEdit.setEntity("edited entity");
		infoProfessionalCareerToEdit.setFunction("edited function");
	}
	
	public void testCreateCareer() {

		try {
			new ProfessionalCareer(null, infoProfessionalCareerToCreate);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}

		try {
			new TeachingCareer(null, category, infoTeachingCareerToCreate);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		
		try {
			new TeachingCareer(teacher, null, infoTeachingCareerToCreate);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		

		ProfessionalCareer newProfessionalCareer = new ProfessionalCareer(teacher, infoProfessionalCareerToCreate);
		assertTrue("Failed to reference teacher!", newProfessionalCareer.hasTeacher());
		assertEquals("Different teacher!", newProfessionalCareer.getTeacher(), teacher);
		verifyCareerAttributes(newProfessionalCareer, infoProfessionalCareerToCreate);
		
		TeachingCareer newTeachingCareer = new TeachingCareer(teacher, category, infoTeachingCareerToCreate);
		assertTrue("Failed to reference teacher!", newTeachingCareer.hasTeacher());
		assertEquals("Different teacher!", newTeachingCareer.getTeacher(), teacher);
		assertEquals("Different category!", newTeachingCareer.getCategory(), category);
		verifyCareerAttributes(newTeachingCareer, infoTeachingCareerToCreate);
	}
	
	public void testDeleteCareer() {
		setUpDelete();
		
		teachingCareerToDelete.delete();
		assertFalse("Failed to dereference teacher!", teachingCareerToDelete.hasTeacher());
		assertFalse("Failed to dereference category!", teachingCareerToDelete.hasCategory());
		
		professionalCareerToDelete.delete();
		assertFalse("Failed to dereference teacher!", professionalCareerToDelete.hasTeacher());
	}
	
	public void testEditCareer() {
		setUpEdit();
		
		try {
			teachingCareerToEdit.edit(infoTeachingCareerToEdit, null);
			fail("Should have thrown a DomainException!");
			
		} catch (DomainException npe) {
			verifyCareerAttributes(teachingCareerToEdit, infoTeachingCareerToCreate);
		}

		teachingCareerToEdit.edit(infoTeachingCareerToEdit, category2);
		verifyCareerAttributes(teachingCareerToEdit, infoTeachingCareerToEdit);
		assertEquals("Different category!", teachingCareerToEdit.getCategory(), category2);



		professionalCareerToEdit.edit(infoProfessionalCareerToEdit);
		verifyCareerAttributes(professionalCareerToEdit, infoProfessionalCareerToEdit);

	}

	private void verifyCareerAttributes(TeachingCareer teachingCareer, InfoTeachingCareer infoTeachingCareer) {
		assertEquals("Different objConcreteType!",
				teachingCareer.getOjbConcreteClass(), TeachingCareer.class.getName());
		assertEquals("Different beginYear!", teachingCareer.getBeginYear(), infoTeachingCareer.getBeginYear());
		assertEquals("Different endYear!", teachingCareer.getEndYear(), infoTeachingCareer.getEndYear());
		assertEquals("Different courseOrPosition!", teachingCareer.getCourseOrPosition(), infoTeachingCareer.getCourseOrPosition());
	}

	private void verifyCareerAttributes(ProfessionalCareer professionalCareer, InfoProfessionalCareer infoProfessionalCareer) {
		assertEquals("Different objConcreteType!",
				professionalCareer.getOjbConcreteClass(), ProfessionalCareer.class.getName());
		assertEquals("Different beginYear!", professionalCareer.getBeginYear(), infoProfessionalCareer.getBeginYear());
		assertEquals("Different endYear!", professionalCareer.getEndYear(), infoProfessionalCareer.getEndYear());
		assertEquals("Different courseOrPosition!", professionalCareer.getEntity(), infoProfessionalCareer.getEntity());
		assertEquals("Different function!", professionalCareer.getFunction(), infoProfessionalCareer.getFunction());
	}


}
