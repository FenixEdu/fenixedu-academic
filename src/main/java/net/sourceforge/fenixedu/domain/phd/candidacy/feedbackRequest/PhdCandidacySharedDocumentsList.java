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
package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdElementsList;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;

import org.apache.commons.lang.StringUtils;

public class PhdCandidacySharedDocumentsList extends PhdElementsList<PhdIndividualProgramDocumentType> {

    static private final long serialVersionUID = 1L;

    protected PhdCandidacySharedDocumentsList() {
        super();
    }

    protected PhdCandidacySharedDocumentsList(final String types) {
        super(types);
    }

    public PhdCandidacySharedDocumentsList(Collection<PhdIndividualProgramDocumentType> types) {
        super(types);
    }

    @Override
    protected PhdIndividualProgramDocumentType convertElementToSet(String valueToParse) {
        return PhdIndividualProgramDocumentType.valueOf(valueToParse);
    }

    @Override
    protected String convertElementToString(PhdIndividualProgramDocumentType element) {
        return element.name();
    }

    @Override
    protected PhdCandidacySharedDocumentsList createNewInstance() {
        return new PhdCandidacySharedDocumentsList();
    }

    @Override
    public PhdCandidacySharedDocumentsList addAccessTypes(PhdIndividualProgramDocumentType... types) {
        return (PhdCandidacySharedDocumentsList) super.addAccessTypes(types);
    }

    static public PhdCandidacySharedDocumentsList importFromString(String value) {
        return StringUtils.isEmpty(value) ? EMPTY : new PhdCandidacySharedDocumentsList(value);
    }

    final static private PhdCandidacySharedDocumentsList EMPTY = new PhdCandidacySharedDocumentsList() {

        static private final long serialVersionUID = 1L;

        @Override
        public Set<PhdIndividualProgramDocumentType> getTypes() {
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "";
        }
    };
}
