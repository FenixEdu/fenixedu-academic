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
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import static pt.utl.ist.fenix.tools.util.DateFormatUtil.DEFAULT_DATE_FORMAT;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.serviceRequests.Under23TransportsDeclarationRequest;

import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;

public class Under23TransportsDeclarationDocument extends AdministrativeOfficeDocument {

    static private final long serialVersionUID = 1L;

    protected Under23TransportsDeclarationDocument(final Under23TransportsDeclarationRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected Under23TransportsDeclarationRequest getDocumentRequest() {
        return (Under23TransportsDeclarationRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

        final Person person = getDocumentRequest().getPerson();

        addParameter("personName", person.getName());
        addParameter("documentIdNumber", person.getDocumentIdNumber());
        addParameter("emissionDate", person.getEmissionDateOfDocumentIdYearMonthDay() == null ? "" : person
                .getEmissionDateOfDocumentIdYearMonthDay().toString(DEFAULT_DATE_FORMAT));
        addParameter("birthDate", person.getDateOfBirthYearMonthDay().toString(DEFAULT_DATE_FORMAT));

        addParameter("executionYear", getExecutionYear().getQualifiedName());
        addParameter("institutionName", UnitUtils.readInstitutionUnit().getName());

        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();

        addAddressInformation("person", getDocumentRequest().getPerson());
        addAddressInformation("institution", adminOfficeUnit);
        addParameter("institutionPhone", adminOfficeUnit.getDefaultPhone().getNumber());

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
