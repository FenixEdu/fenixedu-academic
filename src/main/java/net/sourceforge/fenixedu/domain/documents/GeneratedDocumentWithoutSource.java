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
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

/**
 * @author Pedro Santos (pmrsa)
 */
public class GeneratedDocumentWithoutSource extends GeneratedDocumentWithoutSource_Base {
    public GeneratedDocumentWithoutSource(GeneratedDocumentType type, Party addressee, Person operator, String filename,
            byte[] content) {
        super();
        init(type, addressee, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
        return RoleGroup.get(RoleType.MANAGER);
    }

    @Atomic
    public static void createDocument(GeneratedDocumentType type, Party addressee, Person operator, String filename,
            byte[] content) {
        new GeneratedDocumentWithoutSource(type, addressee, operator, filename, content);
    }
}
