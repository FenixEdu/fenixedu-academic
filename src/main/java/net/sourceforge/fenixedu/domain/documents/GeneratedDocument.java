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

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.fenixedu.bennu.core.groups.Group;

/**
 * {@link GeneratedDocument}s are output files resulting of some process of the
 * application, all generated files are stored in dspace.
 * 
 * Generated documents have 3 core relations:
 * <ul>
 * <li>Addressee: Someone or something that is the interested party of the document, like a person who receives some certificate
 * document.</li>
 * <li>Operator: An employee who processes the document.</li>
 * <li>Source: A domain entity who feeds the information presented in the document. This one may not make sense in all cases, it
 * is only used when needed.</li>
 * </ul>
 * 
 * @author Pedro Santos (pmrsa)
 */
public abstract class GeneratedDocument extends GeneratedDocument_Base {

    public GeneratedDocument() {
        super();
    }

    protected void init(GeneratedDocumentType type, Party addressee, Person operator, String filename, byte[] content) {
        setType(type);
        setAddressee(addressee);
        setOperator(operator);
        init(filename, filename, content, computePermittedGroup());
    }

    @Override
    public void delete() {
        setAddressee(null);
        setOperator(null);
        super.delete();
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
        if (person == null) {
            return false;
        }
        if (person.equals(getOperator())) {
            return true;
        }
        if (person.equals(getAddressee())) {
            return true;
        }
        return super.isPersonAllowedToAccess(person);
    }

    protected abstract Group computePermittedGroup();

    public static final Comparator<GeneratedDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<GeneratedDocument>() {

        @Override
        public int compare(GeneratedDocument o1, GeneratedDocument o2) {
            return o1.getUploadTime().compareTo(o2.getUploadTime());
        }

    };

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasOperator() {
        return getOperator() != null;
    }

    @Deprecated
    public boolean hasAddressee() {
        return getAddressee() != null;
    }

}
