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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

abstract public class EnrolmentWrapper extends EnrolmentWrapper_Base {

    protected EnrolmentWrapper() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final Credits credits) {
        String[] args = {};
        if (credits == null) {
            throw new DomainException("error.EnrolmentWrapper.credits.cannot.be.null", args);
        }
        super.setCredits(credits);
    }

    @Override
    public void setCredits(Credits credits) {
        throw new DomainException("error.EnrolmentWrapper.cannot.modify.credits");
    }

    public void delete() {
        super.setCredits(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    abstract public IEnrolment getIEnrolment();

    static public EnrolmentWrapper create(final Credits credits, final IEnrolment enrolment) {

        if (enrolment.isExternalEnrolment()) {
            return new ExternalEnrolmentWrapper(credits, (ExternalEnrolment) enrolment);

        } else if (enrolment.isEnrolment()) {
            return new InternalEnrolmentWrapper(credits, (Enrolment) enrolment);

        } else {
            throw new DomainException("error.EnrolmentWrapper.unknown.enrolment");
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

}
