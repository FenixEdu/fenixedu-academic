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

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

/**
 * Generic library operation over a thesis.
 * 
 * @author Pedro Santos (pmrsa)
 */
public abstract class ThesisLibraryOperation extends ThesisLibraryOperation_Base {

    public ThesisLibraryOperation() {
        super();
    }

    protected void init(Thesis thesis, Person performer) {
        if (thesis.hasLastLibraryOperation()) {
            setPrevious(thesis.getLastLibraryOperation());
        }
        super.setThesis(thesis);
        super.setPerformer(performer);
        super.setOperation(new DateTime());
    }

    public String getThesisId() {
        return getThesis().getExternalId();
    }

    public String getPerformerId() {
        return getPerformer().getExternalId();
    }

    public abstract ThesisLibraryState getState();

    public abstract String getLibraryReference();

    public abstract String getPendingComment();

    protected Bennu getRootDomainObject() {
        return getThesis().getRootDomainObject();
    }

    @Override
    public void setThesis(Thesis thesis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPerformer(Person performer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOperation(DateTime operation) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean hasThesis() {
        return getThesis() != null;
    }

    @Deprecated
    public boolean hasNext() {
        return getNext() != null;
    }

    @Deprecated
    public boolean hasOperation() {
        return getOperation() != null;
    }

    @Deprecated
    public boolean hasPrevious() {
        return getPrevious() != null;
    }

    @Deprecated
    public boolean hasPerformer() {
        return getPerformer() != null;
    }

}
