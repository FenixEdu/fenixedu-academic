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
package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

abstract public class RegistrationAcademicServiceRequest extends RegistrationAcademicServiceRequest_Base {

    public static Comparator<RegistrationAcademicServiceRequest> COMPARATOR_BY_SERVICE_REQUEST_NUMBER_AND_ID =
            new Comparator<RegistrationAcademicServiceRequest>() {
                @Override
                public int compare(RegistrationAcademicServiceRequest o1, RegistrationAcademicServiceRequest o2) {
                    if (o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber()) != 0) {
                        return o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber());
                    }
                    return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
                }
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
        checkRegistrationIsNotTransited(bean);
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

    protected void checkRegistrationIsNotTransited(RegistrationAcademicServiceRequestCreateBean bean) {
        if (!isAvailableForTransitedRegistrations() && bean.getRegistration().isTransited()) {
            throw new DomainException("RegistrationAcademicServiceRequest.registration.cannot.be.transited");
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
                hasExecutionYear() ? getExecutionYear() : ExecutionYear.readByDateTime(getRequestDate());
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

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

}
