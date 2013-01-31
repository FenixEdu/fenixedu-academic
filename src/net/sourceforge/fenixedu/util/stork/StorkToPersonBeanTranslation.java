package net.sourceforge.fenixedu.util.stork;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class StorkToPersonBeanTranslation {
	public static void copyStorkAttributesToPersonBean(final PersonBean personBean, AttributesManagement attrManagement) {
		personBean.setIdDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);
		personBean.setDocumentIdNumber(attrManagement.getIdentificationNumber());
		personBean.setEmail(attrManagement.getEmail());
		personBean.setName(attrManagement.getStorkFullname());
		personBean.setDateOfBirth(attrManagement.getBirthDate());
		personBean.setNationality(attrManagement.getStorkNationality());
		personBean.setCountryOfBirth(attrManagement.getStorkCountryOfBirth());
		personBean.setGender(attrManagement.getGender());

		setAddressFields(personBean, attrManagement);
	}

	private static void setAddressFields(PersonBean personBean, AttributesManagement attrManagement) {
		if (attrManagement.hasCanonicalAddress()) {
			// Set from the canonical address
			personBean.setAddress(attrManagement.getAddressCompound());
			personBean.setArea(attrManagement.getCityCompound());
			personBean.setAreaCode(attrManagement.getZipCodeCompound());
			personBean.setCountryOfResidence(attrManagement.getResidenceCountryCompound());
		} else {
			personBean.setAddress(attrManagement.getTextAddress());
		}
	}
}
