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
package org.fenixedu.academic.report.academicAdministrativeOffice;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.serviceRequests.Under23TransportsDeclarationRequest;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;

public class Under23TransportsDeclarationDocument extends AdministrativeOfficeDocument {

    static private final long serialVersionUID = 1L;

    protected Under23TransportsDeclarationDocument(final Under23TransportsDeclarationRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public Under23TransportsDeclarationRequest getDocumentRequest() {
        return (Under23TransportsDeclarationRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

        final Person person = getDocumentRequest().getPerson();

        addParameter("personName", person.getName());
        addParameter("documentIdNumber", person.getDocumentIdNumber());
        addParameter("emissionDate", person.getEmissionDateOfDocumentIdYearMonthDay() == null ? "" : person
                .getEmissionDateOfDocumentIdYearMonthDay().toString("dd/MM/yyyy"));
        addParameter("birthDate", person.getDateOfBirthYearMonthDay().toString("dd/MM/yyyy"));

        addParameter("executionYear", getExecutionYear().getQualifiedName());
        addParameter("institutionName", UnitUtils.readInstitutionUnit().getName());

        Unit institutionUnit = Bennu.getInstance().getInstitutionUnit();

        addAddressInformation("person", getDocumentRequest().getPerson());
        addAddressInformation("institution", institutionUnit);
        addParameter("institutionPhone", institutionUnit.getDefaultPhone().getNumber());

        addParameter("reportDate", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", I18N.getLocale()));
    }

    private void addAddressInformation(final String prefix, final Party party) {
        final PhysicalAddress address = party.getDefaultPhysicalAddress();
        addParameter(prefix + "Address", address.getAddress());
        addParameter(prefix + "Parish", address.getParishOfResidence());
        addParameter(prefix + "Municipality", address.getDistrictSubdivisionOfResidence());
        addParameter(prefix + "AreaCode", address.getAreaCode());
        addParameter(prefix + "AreaOfAreaCode", address.getAreaOfAreaCode());
    }

}
