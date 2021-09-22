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
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Comparator;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;

abstract public class RegistrationAcademicServiceRequest extends RegistrationAcademicServiceRequest_Base {

    public static Comparator<RegistrationAcademicServiceRequest> COMPARATOR_BY_SERVICE_REQUEST_NUMBER_AND_ID = (o1, o2) -> {
        if (o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber()) != 0) {
            return o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber());
        }
        return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
    };

    protected RegistrationAcademicServiceRequest() {
        super();
    }

    public void init(final RegistrationAcademicServiceRequestCreateBean bean) {
        checkParameters(bean);
        super.setRegistration(bean.getRegistration());
        super.init(bean, getDegree().getAdministrativeOffice());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        checkRegistration(bean);
        checkRegistrationStartDate(bean);
        checkRegistrationExecutionYear(bean);
    }

    protected void checkRegistrationExecutionYear(RegistrationAcademicServiceRequestCreateBean bean) {
        if (bean.getExecutionYear() != null && bean.getExecutionYear().isBefore(bean.getRegistration().getStartExecutionYear())) {
            throw new DomainException("error.RegistrationAcademicServiceRequest.executionYear.before.registrationStartDate");
        }
    }

    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {
        if (ExecutionYear.readByDateTime(bean.getRequestDate()).isBefore(bean.getRegistration().getStartExecutionYear())) {
            throw new DomainException("error.RegistrationAcademicServiceRequest.requestDate.before.registrationStartDate");
        }
    }

    protected void checkRegistration(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (bean.getRegistration() == null) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.registration.cannot.be.null");
        }
    }

    public Degree getDegree() {
        return getRegistration().getDegree();
    }

    @Override
    public AcademicProgram getAcademicProgram() {
        return getDegree();
    }

    @Override
    public void setRegistration(Registration registration) {
        throw new DomainException("error.serviceRequests.RegistrationAcademicServiceRequest.cannot.modify.registration");
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        final ExecutionYear executionYear =
                getExecutionYear() != null ? getExecutionYear() : ExecutionYear.readByDateTime(getRequestDate());
        return getRegistration().getStudentCurricularPlan(executionYear);
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    public boolean isBolonha() {
        return getDegree().isBolonhaDegree();
    }

    @Override
    public boolean isRequestForRegistration() {
        return true;
    }

    @Override
    protected void disconnect() {
        super.setRegistration(null);
        super.disconnect();
    }

    @Override
    public Person getPerson() {
        return getRegistration().getPerson();
    }

    public Student getStudent() {
        return getRegistration().getStudent();
    }

    abstract public boolean isAvailableForTransitedRegistrations();

    public boolean hasRegistration() {
        return getRegistration() != null;
    }
}
