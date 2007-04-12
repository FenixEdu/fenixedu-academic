/*
 * Created on 26/Jul/2005 - 17:52:31
 * 
 */

package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class QualificationTest extends DomainTestBase {

	Date lastModificationDate1 = new Date(System.currentTimeMillis());
    String degreeRecognition1 = "degreeRecognition1";
    String equivalenceSchool1 = "equivalenceSchool1";
    String mark1 = "mark1";
    String title1 = "title1";
    String specializationArea1 = "specializationArea1";
    Date date1 = new Date(2000, 01, 01);
    String branch1 = "branch1";
    java.util.Date equivalenceDate1 = new Date(2005, 01, 01);;
    String degree1 = "degree1";
    String school1 = "school1";
    
	Date lastModificationDate2 = new Date(System.currentTimeMillis() + 80000);
    String degreeRecognition2 = "degreeRecognition2";
    String equivalenceSchool2 = "equivalenceSchool2";
    String mark2 = "mark2";
    String title2 = "title2";
    String specializationArea2 = "specializationArea2";
    Date date2 = new Date(2002, 01, 01);
    String branch2 = "branch2";
    java.util.Date equivalenceDate2 = new Date(2004, 01, 01);;
    String degree2 = "degree2";
    String school2 = "school2";

	Country country1;
    Person person1;
	
	Country country2;
	Person person2;
	
	InfoQualification infoQualificationToCreate;
	InfoQualification infoQualificationToEdit;
	
	Qualification qualificationToDelete;
	Qualification qualificationToEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		country1 = new Country("country1Name", "country1Nacionality", "country1Code");
		person1 = new Person();
		person1.setName("person1Name");
		person1.setUsername(RoleType.PERSON);
		
		
		country2 = new Country("nameCountry2", "nacionalityCountry2", "codeCountry2");
		person2 = new Person();
		person2.setName("namePerson2");
		person2.setUsername(RoleType.PERSON);
		
		infoQualificationToCreate = new InfoQualification();
		infoQualificationToCreate.setDegree(degree1);
		infoQualificationToCreate.setTitle(title1);
		infoQualificationToCreate.setSchool(school1);
		infoQualificationToCreate.setMark(mark1);

	}
		
	private void setUpDelete() {
		qualificationToDelete = new Qualification(person1, country1, infoQualificationToCreate);
	}
 
	private void setUpEdit() {
		qualificationToEdit = new Qualification(person1, country1, infoQualificationToCreate);

		infoQualificationToEdit = new InfoQualification();
		infoQualificationToEdit.setDegree(degree1);
		infoQualificationToEdit.setTitle(title1);
		infoQualificationToEdit.setSchool(school2);
		infoQualificationToEdit.setMark(mark2);
	}
		
	public void testCreateQualification() {
		
		try {
			new Qualification(null, country1, infoQualificationToCreate);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}

		
		Qualification newQualificationWithCountry = new Qualification(person1, country1, infoQualificationToCreate);
		
		assertTrue("Failed to reference person!", newQualificationWithCountry.hasPerson());
		assertTrue("Failed to reference country!", newQualificationWithCountry.hasCountry());
		verifyQualificationAttributes(newQualificationWithCountry, infoQualificationToCreate);

		Qualification newQualification = new Qualification(person1, null, infoQualificationToCreate);
		
		assertTrue("Failed to reference person!", newQualification.hasPerson());
		assertFalse("Shouldn't reference country!", newQualification.hasCountry());
		verifyQualificationAttributes(newQualification, infoQualificationToCreate);
	}
	
	public void testDeleteQualification() {
		setUpDelete();
		
		qualificationToDelete.delete();
		
		assertFalse("Failed to dereference person!", qualificationToDelete.hasPerson());
		assertFalse("Failed to dereference country!", qualificationToDelete.hasCountry());
				
		
	}
	
	public void testEditQualification() {
		setUpEdit();
				
		qualificationToEdit.edit(infoQualificationToEdit, null);

		assertNull("Different country!", qualificationToEdit.getCountry());
		
		verifyQualificationAttributes(qualificationToEdit, infoQualificationToEdit);
	}
	
	private void verifyQualificationAttributes(Qualification qualification, InfoQualification infoQualification) {
		assertEquals("Different degree!", qualification.getDegree(), infoQualification.getDegree());
		assertEquals("Different title!", qualification.getTitle(), infoQualification.getTitle());
		assertEquals("Different school!", qualification.getSchool(), infoQualification.getSchool());
		assertEquals("Different mark!", qualification.getMark(), infoQualification.getMark());
	}
}
