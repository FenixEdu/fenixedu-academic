/*
 * Created on 2/Out/2005 - 17:37:28
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.util.ProviderRegimeType;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ServiceProviderRegimeTest extends DomainTestBase {
	private Teacher teacher;
	
	private InfoServiceProviderRegime infoServiceProviderRegimeToCreate;
	private InfoServiceProviderRegime infoServiceProviderRegimeToEdit;
	
	private ServiceProviderRegime serviceProviderRegimeToEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
//		teacher = new Teacher();
		infoServiceProviderRegimeToCreate = new InfoServiceProviderRegime();
		infoServiceProviderRegimeToCreate.setProviderRegimeType(ProviderRegimeType.COMPLEMENT);

	}
	
	private void setUpEdit() {
		serviceProviderRegimeToEdit = new ServiceProviderRegime(teacher, infoServiceProviderRegimeToCreate);
		
		infoServiceProviderRegimeToEdit = new InfoServiceProviderRegime();
		infoServiceProviderRegimeToCreate.setProviderRegimeType(ProviderRegimeType.CUMULATIVE);
	}
	
	public void testCreateServiceProviderRegime() {
		try {
			new ServiceProviderRegime(null, infoServiceProviderRegimeToCreate);
			fail("Should have thrown a DomainException");
		
		} catch (DomainException de) {
			
		}
		
		ServiceProviderRegime newServiceProviderRegime = new ServiceProviderRegime(teacher, infoServiceProviderRegimeToCreate);
		assertTrue("Failed to reference teacher!", newServiceProviderRegime.hasTeacher());
		assertEquals("Different teacher!", newServiceProviderRegime.getTeacher(), teacher);
		verifyServiceProviderRegimeAttributes(newServiceProviderRegime, infoServiceProviderRegimeToCreate);
	}
	
	public void testEditServiceProviderRegime() {
		setUpEdit();
		serviceProviderRegimeToEdit.edit(infoServiceProviderRegimeToEdit);
		verifyServiceProviderRegimeAttributes(serviceProviderRegimeToEdit, infoServiceProviderRegimeToEdit);
	}

	private void verifyServiceProviderRegimeAttributes(ServiceProviderRegime serviceProviderRegime, InfoServiceProviderRegime infoServiceProviderRegime) {
		assertEquals("Different research!", serviceProviderRegime.getProviderRegimeType(), infoServiceProviderRegime.getProviderRegimeType());
	}
}

