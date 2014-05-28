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
package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class AnnualIRSDeclarationDocument extends AnnualIRSDeclarationDocument_Base {

    public AnnualIRSDeclarationDocument(Person addressee, Person operator, String filename, byte[] content, Integer year) {
        super();
        checkParameters(year);
        checkRulesToCreate(addressee, year);
        setYear(year);
        super.init(GeneratedDocumentType.ANNUAL_IRS_DECLARATION, addressee, operator, filename, content);
    }

    @Override
    public Person getAddressee() {
        return (Person) super.getAddressee();
    }

    @Override
    protected Group computePermittedGroup() {
        return RoleGroup.get(RoleType.MANAGER);
    }

    private void checkRulesToCreate(Person addressee, Integer year) {
        if (addressee.hasAnnualIRSDocumentFor(year)) {
            throw new DomainException("error.documents.AnnualIRSDeclarationDocument.annual.irs.document.alread.exists.for.year");
        }
    }

    private void checkParameters(Integer year) {
        if (year == null) {
            throw new DomainException("error.documents.AnnualIRSDeclarationDocument.year.cannot.be.null");
        }
    }

    @Atomic
    public AnnualIRSDeclarationDocument generateAnotherDeclaration(Person operator, byte[] content) {

        final Person addressee = getAddressee();
        final Integer year = getYear();

        delete();

        return new AnnualIRSDeclarationDocument(addressee, operator, buildFilename(addressee, year), content, year);
    }

    static private String buildFilename(Person person, Integer year) {
        return String.format("IRS-%s-%s-%s.pdf", year, person.getDocumentIdNumber(), new LocalDate().toString("yyyyMMdd"));
    }

    @Atomic
    static public AnnualIRSDeclarationDocument create(Person addressee, Person operator, byte[] content, Integer year) {
        return new AnnualIRSDeclarationDocument(addressee, operator, buildFilename(addressee, year), content, year);
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

}
