/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.serviceAgreementTemplates;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class AdministrativeOfficeServiceAgreementTemplate extends AdministrativeOfficeServiceAgreementTemplate_Base {

    private AdministrativeOfficeServiceAgreementTemplate() {
        super();
    }

    public AdministrativeOfficeServiceAgreementTemplate(AdministrativeOffice administrativeOffice) {
        this();
        init(administrativeOffice);
    }

    private void checkParameters(AdministrativeOffice administrativeOffice) {
        if (administrativeOffice == null) {
            throw new DomainException(
                    "error.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate.administrativeOffice.cannot.be.null");
        }

    }

    protected void init(AdministrativeOffice administrativeOffice) {
        checkParameters(administrativeOffice);
        addAdministrativeOffice(administrativeOffice);
    }

}
