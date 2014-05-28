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

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalEnrolmentWrapper extends ExternalEnrolmentWrapper_Base {

    private ExternalEnrolmentWrapper() {
        super();
    }

    protected ExternalEnrolmentWrapper(final Credits credits, final ExternalEnrolment externalEnrolment) {
        this();
        super.init(credits);
        init(externalEnrolment);
    }

    private void init(final ExternalEnrolment externalEnrolment) {
        String[] args = {};
        if (externalEnrolment == null) {
            throw new DomainException("error.EnrolmentWrapper.enrolment.cannot.be.null", args);
        }
        super.setEnrolment(externalEnrolment);
    }

    @Override
    public void setEnrolment(ExternalEnrolment enrolment) {
        throw new RuntimeException("error.EnrolmentWrapper.cannot.modify.enrolment");
    }

    @Override
    public IEnrolment getIEnrolment() {
        return getEnrolment();
    }

    @Override
    public void delete() {
        super.setEnrolment(null);
        super.delete();
    }

    @Deprecated
    public boolean hasEnrolment() {
        return getEnrolment() != null;
    }

}
