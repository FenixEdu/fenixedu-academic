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
 * Library operation of marking a thesis pending with an optional code
 * explaining why.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisLibraryPendingOperation extends ThesisLibraryPendingOperation_Base {
    public ThesisLibraryPendingOperation(Thesis thesis, Person performer, String comment) {
        super();
        if (thesis.getState() != ThesisState.EVALUATED || !thesis.isFinalAndApprovedThesis()) {
            throw new DomainException("thesis.makepending.notEvaluatedNorArchive");
        }
        init(thesis, performer);
        setPendingComment(comment);
    }

    public ThesisLibraryPendingOperation(String thesisId, String performerId, String comment) {
        this(FenixFramework.<Thesis> getDomainObject(thesisId), FenixFramework.<Person> getDomainObject(performerId), comment);
    }

    @Override
    public ThesisLibraryState getState() {
        return ThesisLibraryState.PENDING_ARCHIVE;
    }

    @Override
    public String getLibraryReference() {
        return null;
    }

    @Deprecated
    public boolean hasPendingComment() {
        return getPendingComment() != null;
    }

}
