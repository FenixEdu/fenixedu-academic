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

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdAcademicServiceRequestBean implements Serializable, IPhdAcademicServiceRequest {

    private static final long serialVersionUID = 1L;

    private PhdAcademicServiceRequest academicServiceRequest;
    private AcademicServiceRequestSituationType situationType;
    private DateTime whenNewSituationOccured = new DateTime();
    private String justification;

    public PhdAcademicServiceRequestBean(final PhdAcademicServiceRequest request) {
        this.academicServiceRequest = request;
    }

    public PhdAcademicServiceRequest getAcademicServiceRequest() {
        return academicServiceRequest;
    }

    public void setAcademicServiceRequest(PhdAcademicServiceRequest academicServiceRequest) {
        this.academicServiceRequest = academicServiceRequest;
    }

    public AcademicServiceRequestSituationType getSituationType() {
        return situationType;
    }

    public void setSituationType(AcademicServiceRequestSituationType situationType) {
        this.situationType = situationType;
    }

    public DateTime getWhenNewSituationOccured() {
        return whenNewSituationOccured;
    }

    public void setWhenNewSituationOccured(DateTime whenNewSituationOccured) {
        this.whenNewSituationOccured = whenNewSituationOccured;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(final String justification) {
        this.justification = justification;
    }

    @Atomic
    public void handleNewSituation() {

        switch (getSituationType()) {
        case PROCESSING:
            getAcademicServiceRequest().process(getWhenNewSituationOccured().toYearMonthDay());
            break;
        case CANCELLED:
            getAcademicServiceRequest().cancel(getJustification());
            break;
        case REJECTED:
            getAcademicServiceRequest().reject(getJustification());
            break;
        case RECEIVED_FROM_EXTERNAL_ENTITY:
            getAcademicServiceRequest().receivedFromExternalEntity(getWhenNewSituationOccured().toYearMonthDay(),
                    getJustification());
            break;
        case CONCLUDED:
            getAcademicServiceRequest().conclude(getWhenNewSituationOccured().toYearMonthDay(), getJustification());
            break;
        case SENT_TO_EXTERNAL_ENTITY:
            getAcademicServiceRequest().sendToExternalEntity(getWhenNewSituationOccured().toYearMonthDay(), getJustification());
            break;
        case DELIVERED:
            getAcademicServiceRequest().delivered(AccessControl.getPerson(), getWhenNewSituationOccured().toYearMonthDay());
            break;
        default:
            throw new DomainException("error.PhdAcademicServiceRequestBean.unknown.situation.type");
        }
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return academicServiceRequest.getPhdIndividualProgramProcess();
    }
}
