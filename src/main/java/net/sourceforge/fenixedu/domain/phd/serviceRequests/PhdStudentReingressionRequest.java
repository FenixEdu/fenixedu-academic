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
package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class PhdStudentReingressionRequest extends PhdStudentReingressionRequest_Base {

    private PhdStudentReingressionRequest() {
        super();
    }

    private PhdStudentReingressionRequest(final PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean) {
        super();

        init(academicServiceRequestCreateBean);
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
        if (!bean.getRequestType().equals(getAcademicServiceRequestType())) {
            throw new DomainException("error.PhdStudentReingressionRequest.type.not.supported");
        }

        if (!isValidRequest(bean)) {
            throw new PhdDomainOperationException(
                    "error.PhdStudentReingressionRequest.phd.individual.program.process.must.be.flunked.or.suspended");
        }

        super.init(bean);
    }

    private static boolean isValidRequest(final PhdAcademicServiceRequestCreateBean bean) {
        return bean.getPhdIndividualProgramProcess().isFlunked() || bean.getPhdIndividualProgramProcess().isSuspended();
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.PHD_STUDENT_REINGRESSION;
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

    @Override
    public Boolean getFreeProcessed() {
        return true;
    }

    public static PhdAcademicServiceRequest createRequest(
            final PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean) {
        return new PhdStudentReingressionRequest(academicServiceRequestCreateBean);
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {
            PhdIndividualProgramProcess process = getPhdIndividualProgramProcess();
            PhdProgramProcessState lastActiveState = process.getLastActiveState();
            String remarks =
                    String.format(
                            BundleUtil.getString(Bundle.PHD, "message.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdStudentReingressionRequest.conclusion.remark"),
                            getServiceRequestNumberYear());

            process.createState(lastActiveState.getType(), AccessControl.getPerson(), remarks);

            if (process.hasRegistration() && !process.getRegistration().isActive()) {
                RegistrationState registrationLastActiveState = process.getRegistration().getLastActiveState();

                RegistrationStateCreator.createState(process.getRegistration(), AccessControl.getPerson(), new DateTime(),
                        registrationLastActiveState.getStateType());
            }

        }
    }

    @Deprecated
    public boolean hasPhdStudentReingressionRequestDocument() {
        return getPhdStudentReingressionRequestDocument() != null;
    }

}
