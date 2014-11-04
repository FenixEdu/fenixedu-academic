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
package org.fenixedu.academic.domain.studentCurriculum;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class InternalEnrolmentWrapper extends InternalEnrolmentWrapper_Base {

    protected InternalEnrolmentWrapper() {
        super();
    }

    protected InternalEnrolmentWrapper(final Credits credits, final Enrolment enrolment) {
        this();
        super.init(credits);
        init(enrolment);
    }

    private void init(final Enrolment enrolment) {
        String[] args = {};
        if (enrolment == null) {
            throw new DomainException("error.EnrolmentWrapper.enrolment.cannot.be.null", args);
        }
        super.setEnrolment(enrolment);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
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

}
