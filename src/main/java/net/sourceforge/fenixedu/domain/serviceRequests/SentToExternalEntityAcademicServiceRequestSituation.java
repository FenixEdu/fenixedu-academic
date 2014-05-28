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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SentToExternalEntityAcademicServiceRequestSituation extends SentToExternalEntityAcademicServiceRequestSituation_Base {

    private SentToExternalEntityAcademicServiceRequestSituation() {
        super();
    }

    SentToExternalEntityAcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        this();
        checkOwnParameters(academicServiceRequest, academicServiceRequestBean);
        super.init(academicServiceRequest, academicServiceRequestBean);
        super.setSentDate(academicServiceRequestBean.getFinalSituationDate());
    }

    @Override
    protected void checkParameters(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        super.checkParameters(academicServiceRequest, academicServiceRequestBean);
        if (!academicServiceRequestBean.hasJustification()) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequestSituation.justification.cannot.be.null.for.when.send");
        }
    }

    @Override
    public void setSentDate(DateTime sentDate) {
        throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.cannot.modify.situation.date");
    }

    private void checkOwnParameters(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        if (!academicServiceRequestBean.isToSendToExternalEntity()) {
            throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.invalid.situation.type");
        }

        if (academicServiceRequestBean.getFinalSituationDate().isBefore(
                academicServiceRequest.getActiveSituation().getSituationDate())) {
            throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.invalid.situation.date");
        }
    }

    @Override
    public DateTime getFinalSituationDate() {
        return getSentDate();
    }

    @Deprecated
    public boolean hasSentDate() {
        return getSentDate() != null;
    }

}
