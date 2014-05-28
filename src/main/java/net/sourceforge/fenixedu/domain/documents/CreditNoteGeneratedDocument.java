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
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CreditNoteGeneratedDocument extends CreditNoteGeneratedDocument_Base {
    protected CreditNoteGeneratedDocument(CreditNote source, Party addressee, Person operator, String filename, byte[] content) {
        super();
        setSource(source);
        init(GeneratedDocumentType.CREDIT_NOTE, addressee, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
        return AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_STUDENT_PAYMENTS);
    }

    @Override
    public void delete() {
        setSource(null);
        super.delete();
    }

    @Atomic
    public static void store(CreditNote source, String filename, byte[] content) {
        new CreditNoteGeneratedDocument(source, source.getReceipt().getPerson(), AccessControl.getPerson(), filename, content);
    }

    @Deprecated
    public boolean hasSource() {
        return getSource() != null;
    }

}
