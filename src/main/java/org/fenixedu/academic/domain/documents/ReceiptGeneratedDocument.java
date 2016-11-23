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
package org.fenixedu.academic.domain.documents;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.predicate.AccessControl;

import pt.ist.fenixframework.Atomic;

/**
 * @author Pedro Santos (pmrsa)
 */
public class ReceiptGeneratedDocument extends ReceiptGeneratedDocument_Base {
    protected ReceiptGeneratedDocument(Receipt source, Party addressee, Person operator, String filename, byte[] content) {
        super();
        setSource(source);
        init(GeneratedDocumentType.RECEIPT, addressee, operator, filename, content);
    }

    @Override
    public void delete() {
        setSource(null);
        super.delete();
    }

    @Atomic
    public static void store(Receipt source, String filename, byte[] content) {
        new ReceiptGeneratedDocument(source, source.getPerson(), AccessControl.getPerson(), filename, content);
    }

}
