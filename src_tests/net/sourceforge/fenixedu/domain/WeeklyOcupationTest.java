/*
 * Created on 2/Out/2005 - 17:37:28
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class WeeklyOcupationTest extends DomainTestBase {
	private Teacher teacher;
	
	private InfoWeeklyOcupation infoWeeklyOcupationToCreate;
	private InfoWeeklyOcupation infoWeeklyOcupationToEdit;
	
	private WeeklyOcupation weeklyOcupationToEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
//		teacher = new Teacher();
		infoWeeklyOcupationToCreate = new InfoWeeklyOcupation();
		infoWeeklyOcupationToCreate.setOther(1);
		infoWeeklyOcupationToCreate.setLecture(2);
		infoWeeklyOcupationToCreate.setManagement(3);
		infoWeeklyOcupationToCreate.setResearch(4);
		infoWeeklyOcupationToCreate.setSupport(5);

	}
	
	private void setUpEdit() {
		weeklyOcupationToEdit = new WeeklyOcupation(teacher, infoWeeklyOcupationToCreate);
		
		infoWeeklyOcupationToEdit = new InfoWeeklyOcupation();
		infoWeeklyOcupationToEdit.setOther(5);
		infoWeeklyOcupationToEdit.setLecture(5);
		infoWeeklyOcupationToEdit.setManagement(6);
		infoWeeklyOcupationToEdit.setResearch(7);
		infoWeeklyOcupationToEdit.setSupport(5);
	}
	
	public void testCreateWeeklyOcupation() {
		try {
			new WeeklyOcupation(null, infoWeeklyOcupationToCreate);
			fail("Should have thrown a DomainException");
		
		} catch (DomainException de) {
			
		}
		
		WeeklyOcupation newWeeklyOcupation = new WeeklyOcupation(teacher, infoWeeklyOcupationToCreate);
		assertTrue("Failed to reference teacher!", newWeeklyOcupation.hasTeacher());
		assertEquals("Different teacher!", newWeeklyOcupation.getTeacher(), teacher);
		verifyWeeklyOcupationAttributes(newWeeklyOcupation, infoWeeklyOcupationToCreate);
	}
	
	public void testEditWeeklyOcupation() {
		setUpEdit();
		weeklyOcupationToEdit.edit(infoWeeklyOcupationToEdit);
		verifyWeeklyOcupationAttributes(weeklyOcupationToEdit, infoWeeklyOcupationToEdit);
	}

	private void verifyWeeklyOcupationAttributes(WeeklyOcupation weeklyOcupation, InfoWeeklyOcupation infoWeeklyOcupation) {
		assertEquals("Different research!", weeklyOcupation.getResearch(), infoWeeklyOcupation.getResearch());
		assertEquals("Different other!", weeklyOcupation.getOther(), infoWeeklyOcupation.getOther());
		assertEquals("Different management!", weeklyOcupation.getManagement(), infoWeeklyOcupation.getManagement());
		assertEquals("Different lecture!", weeklyOcupation.getLecture(), infoWeeklyOcupation.getLecture());
		assertEquals("Different support!", weeklyOcupation.getSupport(), infoWeeklyOcupation.getSupport());
	}
}

