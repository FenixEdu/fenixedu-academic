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

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import pt.ist.fenixframework.Atomic;
import java.util.Locale;

public class SpecialSeasonRequest extends SpecialSeasonRequest_Base {

    public SpecialSeasonRequest() {
        super();
    }

    public SpecialSeasonRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        super.setBeginExecutionPeriod(bean.getExecutionYear().getFirstExecutionPeriod());
        super.setEndExecutionPeriod(bean.getExecutionYear().getLastExecutionPeriod());
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isNew()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.PROCESSING, academicServiceRequestBean.getResponsible()));
        }

        if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
        }

    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {

            Student student = getRegistration().getStudent();

            if (getDeferred() != null && getDeferred() == true) {
                new StudentStatute(student, StudentStatuteType.SPECIAL_SEASON_GRANTED_BY_REQUEST, getBeginExecutionPeriod(),
                        getEndExecutionPeriod());
            }
        }

    }

    public String getDefermentDescription() {
        if (getDeferred() == null) {
            return "-";
        }
        if (getDeferred() == true) {
            return ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale()).getString("request.granted");
        } else {
            return ResourceBundle.getBundle("resources.AcademicAdminOffice", I18N.getLocale()).getString("request.declined");
        }
    }

    @Atomic
    public void setDeferment(Boolean deferred) {
        this.setDeferred(deferred);
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.SPECIAL_SEASON_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Deprecated
    public boolean hasBeginExecutionPeriod() {
        return getBeginExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasEndExecutionPeriod() {
        return getEndExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasDeferred() {
        return getDeferred() != null;
    }

}
