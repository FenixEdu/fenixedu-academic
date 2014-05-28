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
package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.Money;

public class RegistrationAcademicServiceRequestCreateBean extends AcademicServiceRequestCreateBean {

    private AcademicServiceRequestType academicServiceRequestType;
    private CurriculumGroup curriculumGroup;
    private CourseGroup courseGroup;
    private Enrolment enrolment;
    private EquivalencePlanRequest equivalencePlanRequest;
    private String subject;
    private String purpose;
    private Integer numberOfEquivalences = null;
    private String description;
    private Money amountToPay;

    public RegistrationAcademicServiceRequestCreateBean(final Registration registration) {
        super(registration);
    }

    final public AcademicServiceRequestType getAcademicServiceRequestType() {
        return academicServiceRequestType;
    }

    final public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
        this.academicServiceRequestType = academicServiceRequestType;
    }

    final public CurriculumGroup getCurriculumGroup() {
        return curriculumGroup;
    }

    final public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    final public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    final public void setCourseGroup(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    final public Enrolment getEnrolment() {
        return enrolment;
    }

    final public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    final public EquivalencePlanRequest getEquivalencePlanRequest() {
        return equivalencePlanRequest;
    }

    final public void setEquivalencePlanRequest(EquivalencePlanRequest equivalencePlanRequest) {
        this.equivalencePlanRequest = equivalencePlanRequest;
    }

    final public String getSubject() {
        return subject;
    }

    final public void setSubject(String subject) {
        this.subject = subject;
    }

    final public String getPurpose() {
        return purpose;
    }

    final public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getNumberOfEquivalences() {
        return numberOfEquivalences;
    }

    public void setNumberOfEquivalences(Integer numberOfEquivalences) {
        this.numberOfEquivalences = numberOfEquivalences;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Money amountToPay) {
        this.amountToPay = amountToPay;
    }

}
