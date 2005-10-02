/*
 * Created on 8/Ago/2005 - 11:19:46
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldPublicationTest extends DomainTestBase {

    String publication = "publication";
    OldPublicationType oldPublicationType = OldPublicationType.DIDACTIC;
	
	String publication2 = "publication2";
    OldPublicationType oldPublicationType2 = OldPublicationType.CIENTIFIC;
	
    ITeacher teacher;

	IOldPublication oldPublicationToDelete;
	IOldPublication oldPublicationToEdit;
	
	InfoOldPublication infoOldPublication;
	
	
	
	protected void setUp() throws Exception {
		super.setUp();
		teacher = new Teacher();
	}
	
	private void initializeOldPublication(IOldPublication oldPublication) {
		oldPublication.setPublication(publication);
		oldPublication.setOldPublicationType(oldPublicationType);
		
		oldPublication.setTeacher(teacher);
	}

	private void setUpDelete() {
		oldPublicationToDelete = new OldPublication();
		initializeOldPublication(oldPublicationToDelete);
	}
	
	private void setUpEdit() {
		oldPublicationToEdit = new OldPublication();
		initializeOldPublication(oldPublicationToEdit);
		
		infoOldPublication = new InfoOldPublication();
		
		infoOldPublication.setOldPublicationType(oldPublicationType2);
		infoOldPublication.setPublication(publication2);
	}
	
	public void testDeleteOldPublication() {
		setUpDelete();
		oldPublicationToDelete.delete();
		assertFalse("Failed to dereference Teacher", oldPublicationToDelete.hasTeacher());
	}
	
	public void testEditOldPublication() {
		setUpEdit();
		
		try {
			oldPublicationToEdit.edit(null, teacher);
			fail("Should have thrown a NullPointerException");
			
		} catch (NullPointerException npe) {
			verifyOldPublication(oldPublicationToEdit);
		}
		
		try {
			oldPublicationToEdit.edit(infoOldPublication, null);
			fail("Should have thrown a NullPointerException");
			
		} catch (NullPointerException npe) {
			verifyOldPublication(oldPublicationToEdit);
		}
		
		oldPublicationToEdit.edit(infoOldPublication, teacher);

		assertEquals("Different publication!", oldPublicationToEdit.getPublication(), infoOldPublication.getPublication());
		assertEquals("Different oldPublicationType!", oldPublicationToEdit.getOldPublicationType(), infoOldPublication.getOldPublicationType());
	
	}
	
	private void verifyOldPublication(IOldPublication oldPublication) {
		assertEquals("Different publication!", oldPublication.getPublication(), publication);
		assertEquals("Different oldPublicationType!", oldPublication.getOldPublicationType(), oldPublicationType);
	}

}
