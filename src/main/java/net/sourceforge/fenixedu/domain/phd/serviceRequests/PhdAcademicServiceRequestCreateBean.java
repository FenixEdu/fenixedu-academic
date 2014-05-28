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

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;

public class PhdAcademicServiceRequestCreateBean extends AcademicServiceRequestCreateBean implements IPhdAcademicServiceRequest {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AcademicServiceRequestType requestType;

    private PhdIndividualProgramProcess phdIndividualProgramProcess;

    public PhdAcademicServiceRequestCreateBean(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        super(null);
        this.phdIndividualProgramProcess = phdIndividualProgramProcess;
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return this.phdIndividualProgramProcess;
    }

    public void setPhdIndividualProgramProcess(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        this.phdIndividualProgramProcess = phdIndividualProgramProcess;
    }

    public AcademicServiceRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(final AcademicServiceRequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    protected void setRegistration(final Registration registration) {
        // DO NOTHING
    }

    @Override
    public Registration getRegistration() {
        throw new DomainException("error.PhdAcademicServiceRequestCreateBean.get.registration.invalid");
    }

    @Atomic
    public PhdAcademicServiceRequest createNewRequest() {
        switch (getRequestType()) {
        case PHD_STUDENT_REINGRESSION:
            return PhdStudentReingressionRequest.createRequest(this);
        default:
            throw new DomainException("error.PhdAcademicServiceRequest.create.request.type.unknown");
        }
    }
}
