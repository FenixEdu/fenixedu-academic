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
package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.FenixFramework;

/**
 * Library operation for canceling an archive or pending operation.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryCancelOperation extends ThesisLibraryCancelOperation_Base {
    public ThesisLibraryCancelOperation(Thesis thesis, Person performer) {
        super();
        if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis()) {
            throw new DomainException("thesis.makepending.notEvaluatedNorPendingArchive");
        }
        init(thesis, performer);
    }

    public ThesisLibraryCancelOperation(String thesisId, String performerId) {
        this(FenixFramework.<Thesis> getDomainObject(thesisId), FenixFramework.<Person> getDomainObject(performerId));
    }

    @Override
    public ThesisLibraryState getState() {
        return ThesisLibraryState.ARCHIVE_CANCELED;
    }

    @Override
    public String getPendingComment() {
        return null;
    }

    @Override
    public String getLibraryReference() {
        return null;
    }
}
