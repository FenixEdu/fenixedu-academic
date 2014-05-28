/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
