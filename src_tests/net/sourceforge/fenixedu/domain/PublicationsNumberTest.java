/*
 * Created on 18/Set/2005 - 19:35:33
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.util.PublicationType;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class PublicationsNumberTest extends DomainTestBase {
	
	private Teacher teacher;
	
	private InfoPublicationsNumber infoPublicationsNumberToCreate;
	private InfoPublicationsNumber infoPublicationsNumberEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
//		teacher = new Teacher();
		
		infoPublicationsNumberToCreate = new InfoPublicationsNumber();
		infoPublicationsNumberToCreate.setNational(1);
		infoPublicationsNumberToCreate.setInternational(3);
		infoPublicationsNumberToCreate.setPublicationType(PublicationType.AUTHOR_BOOK);
		
	}
	
	private void setUpEdit() {
		infoPublicationsNumberEdit = new InfoPublicationsNumber();
		infoPublicationsNumberEdit.setNational(5);
		infoPublicationsNumberEdit.setInternational(3);
	}
	
	public void testCreatePublicationsNumber() {
		try {
			new PublicationsNumber(null, infoPublicationsNumberToCreate);
			fail("Should have thrown a DomainException");
		
		} catch (DomainException de) {
			
		}
		
		PublicationsNumber newPublicationsNumber = new PublicationsNumber(teacher, infoPublicationsNumberToCreate);
		
		assertTrue("Failed to reference teacher!", newPublicationsNumber.hasTeacher());
		assertEquals("Different teacher!", newPublicationsNumber.getTeacher(), teacher);
		verifyPublicationsNumberAttributes(newPublicationsNumber, infoPublicationsNumberToCreate);
	}
	
	public void testEditPublicationsNumber() {
		setUpEdit();
		PublicationsNumber publicationsNumberToEdit = new PublicationsNumber(teacher, infoPublicationsNumberToCreate);
		
		publicationsNumberToEdit.edit(infoPublicationsNumberEdit);
		verifyPublicationsNumberAttributes(publicationsNumberToEdit, infoPublicationsNumberEdit);
		
	}


	private void verifyPublicationsNumberAttributes(PublicationsNumber publicationsNumber, InfoPublicationsNumber infoPublicationsNumber) {
		assertEquals("Different national!", publicationsNumber.getNational(), infoPublicationsNumber.getNational());
		assertEquals("Different international!", publicationsNumber.getInternational(), infoPublicationsNumber.getInternational());
		assertEquals("Different publicationType!", publicationsNumber.getPublicationType(), infoPublicationsNumber.getPublicationType());
	}
	

	
}
