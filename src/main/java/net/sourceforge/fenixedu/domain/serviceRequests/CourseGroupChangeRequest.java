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

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.lang.StringUtils;

public class CourseGroupChangeRequest extends CourseGroupChangeRequest_Base {

    protected CourseGroupChangeRequest() {
        super();
    }

    public CourseGroupChangeRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setOldCourseGroup(bean.getCurriculumGroup().getDegreeModule());
        super.setNewCourseGroup(bean.getCourseGroup());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        final CurriculumGroup curriculumGroup = bean.getCurriculumGroup();
        final CourseGroup newCourseGroup = bean.getCourseGroup();
        final ExecutionYear executionYear = bean.getExecutionYear();
        final Registration registration = bean.getRegistration();

        if (curriculumGroup == null) {
            throw new DomainException("error.CourseGroupChangeRequest.curriculumGroup.cannot.be.null");
        }

        if (newCourseGroup == null) {
            throw new DomainException("error.CourseGroupChangeRequest.newCourseGroup.cannot.be.null");
        }

        if (executionYear == null) {
            throw new DomainException("error.CourseGroupChangeRequest.executionYear.cannot.be.null");
        }

        if (!registration.getLastStudentCurricularPlan().hasCurriculumModule(curriculumGroup)) {
            throw new DomainException("error.CourseGroupChangeRequest.invalid.curriculumGroup");
        }

        if (!registration.getLastDegreeCurricularPlan().hasDegreeModule(newCourseGroup)) {
            throw new DomainException("error.CourseGroupChangeRequest.invalid.newCourseGroup");
        }
    }

    @Override
    public void setOldCourseGroup(CourseGroup oldCourseGroup) {
        throw new DomainException("error.CourseGroupChangeRequest.cannot.modify.oldCourseGroup");
    }

    public String getOldCourseGroupOneFullName() {
        return hasOldCourseGroup() ? getOldCourseGroup().getOneFullName() : StringUtils.EMPTY;
    }

    @Override
    public void setNewCourseGroup(CourseGroup newCourseGroup) {
        throw new DomainException("error.CourseGroupChangeRequest.cannot.modify.newCourseGroup");
    }

    public String getNewCourseGroupOneFullName() {
        return hasNewCourseGroup() ? getNewCourseGroup().getOneFullName() : StringUtils.EMPTY;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.COURSE_GROUP_CHANGE_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    protected void disconnect() {
        super.setOldCourseGroup(null);
        super.setNewCourseGroup(null);
        super.disconnect();
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
        }
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Deprecated
    public boolean hasOldCourseGroup() {
        return getOldCourseGroup() != null;
    }

    @Deprecated
    public boolean hasNewCourseGroup() {
        return getNewCourseGroup() != null;
    }

}
