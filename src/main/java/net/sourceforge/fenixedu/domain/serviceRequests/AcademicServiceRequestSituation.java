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

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class AcademicServiceRequestSituation extends AcademicServiceRequestSituation_Base {

    public static Comparator<AcademicServiceRequestSituation> COMPARATOR_BY_MOST_RECENT_SITUATION_DATE_AND_ID =
            new Comparator<AcademicServiceRequestSituation>() {
                @Override
                public int compare(AcademicServiceRequestSituation leftAcademicServiceRequestSituation,
                        AcademicServiceRequestSituation rightAcademicServiceRequestSituation) {
                    DateTime leftDate = leftAcademicServiceRequestSituation.getSituationDate();
                    DateTime rightDate = rightAcademicServiceRequestSituation.getSituationDate();

                    if (leftDate == null) {
                        leftDate = leftAcademicServiceRequestSituation.getCreationDate();
                    }
                    if (rightDate == null) {
                        rightDate = rightAcademicServiceRequestSituation.getCreationDate();
                    }

                    int comparationResult = leftDate.compareTo(rightDate);
                    return -((comparationResult == 0) ? DomainObjectUtil.COMPARATOR_BY_ID.compare(
                            leftAcademicServiceRequestSituation, rightAcademicServiceRequestSituation) : comparationResult);
                }
            };

    protected AcademicServiceRequestSituation() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setCreationDate(new DateTime());
    }

    private AcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        this();
        init(academicServiceRequest, academicServiceRequestBean);
    }

    protected void init(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        checkParameters(academicServiceRequest, academicServiceRequestBean);

        super.setAcademicServiceRequest(academicServiceRequest);
        super.setAcademicServiceRequestSituationType(academicServiceRequestBean.getAcademicServiceRequestSituationType());
        super.setCreator(academicServiceRequestBean.getResponsible());
        super.setJustification(academicServiceRequestBean.hasJustification() ? academicServiceRequestBean.getJustification() : null);
        super.setSituationDate(academicServiceRequestBean.getFinalSituationDate());
    }

    protected void checkParameters(final AcademicServiceRequest academicServiceRequest, final AcademicServiceRequestBean bean) {
        if (academicServiceRequest == null) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequest.cannot.be.null");
        }

        if (!bean.hasAcademicServiceRequestSituationType()) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequestSituationType.cannot.be.null");
        }

        final AcademicServiceRequestSituation activeSituation = academicServiceRequest.getActiveSituation();
        if (activeSituation != null) {
            final DateTime activeSituationDate = activeSituation.getSituationDate();
            final DateTime finalSituationDate = bean.getFinalSituationDate();
            if (finalSituationDate.toLocalDate().isBefore(activeSituationDate.toLocalDate())) {
                throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.situation.date.is.before");
            }

            if (finalSituationDate.toLocalDate().isEqual(activeSituationDate.toLocalDate())
                    && finalSituationDate.isBefore(activeSituationDate)) {
                bean.setFinalSituationDate(activeSituationDate.plusMinutes(1));
            }
        }

        if (bean.getFinalSituationDate().isAfterNow()) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.situation.date.is.after");
        }

        if (bean.isToCancelOrReject()) {
            if (!bean.hasJustification()) {
                throw new DomainException(
                        "error.serviceRequests.AcademicServiceRequestSituation.justification.cannot.be.null.for.cancelled.and.rejected.situations");
            }
        }
    }

    @Override
    public void setAcademicServiceRequest(AcademicServiceRequest academicServiceRequest) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequest");
    }

    @Override
    public void setCreator(Person reponsible) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.creator");
    }

    @Override
    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        throw new DomainException(
                "error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequestSituationType");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.creationDate");
    }

    @Override
    public void setJustification(String justification) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.justification");
    }

    public boolean isDelivered() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED;
    }

    public boolean isNew() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW;
    }

    public boolean isProcessing() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING;
    }

    public boolean isCancelled() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CANCELLED;
    }

    public boolean isSentToExternalEntity() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY;
    }

    void edit(final AcademicServiceRequestBean academicServiceRequestBean) {
        super.setCreator(academicServiceRequestBean.getResponsible());
        super.setJustification(academicServiceRequestBean.getJustification());
    }

    public void delete() {
        delete(true);
    }

    public void delete(boolean checkRules) {
        if (checkRules) {
            checkRulesToDelete();
        }

        super.setRootDomainObject(null);
        super.setCreator(null);
        super.setAcademicServiceRequest(null);

        super.deleteDomainObject();
    }

    protected void checkRulesToDelete() {
        if (isDelivered()) {
            throw new DomainException("AcademicServiceRequestSituation.already.delivered");
        }
    }

    public DateTime getFinalSituationDate() {
        return getSituationDate();
    }

    static AcademicServiceRequestSituation create(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {

        switch (academicServiceRequestBean.getAcademicServiceRequestSituationType()) {
        case SENT_TO_EXTERNAL_ENTITY:
            return new SentToExternalEntityAcademicServiceRequestSituation(academicServiceRequest, academicServiceRequestBean);
        case RECEIVED_FROM_EXTERNAL_ENTITY:
            return new ReceivedFromExternalEntityAcademicServiceRequestSituation(academicServiceRequest,
                    academicServiceRequestBean);
        default:
            return new AcademicServiceRequestSituation(academicServiceRequest, academicServiceRequestBean);
        }
    }

    @Deprecated
    public boolean hasAcademicServiceRequestSituationType() {
        return getAcademicServiceRequestSituationType() != null;
    }

    @Deprecated
    public boolean hasJustification() {
        return getJustification() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasSituationDate() {
        return getSituationDate() != null;
    }

    @Deprecated
    public boolean hasAcademicServiceRequest() {
        return getAcademicServiceRequest() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

}
