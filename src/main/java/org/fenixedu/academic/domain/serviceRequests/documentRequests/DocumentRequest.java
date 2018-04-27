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
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.joda.time.LocalDate;

public abstract class DocumentRequest extends DocumentRequest_Base implements IDocumentRequest {
    public static Comparator<AcademicServiceRequest> COMPARATOR_BY_REGISTRY_NUMBER = new Comparator<AcademicServiceRequest>() {
        @Override
        public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
            int codeCompare = RegistryCode.COMPARATOR_BY_CODE.compare(o1.getRegistryCode(), o2.getRegistryCode());
            if (codeCompare != 0) {
                return codeCompare;
            } else {
                return o1.getExternalId().compareTo(o2.getExternalId());
            }
        }
    };

    protected DocumentRequest() {
        super();
    }

    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getChosenDocumentPurposeType().getDocumentPurposeType() == DocumentPurposeType.OTHER
                && bean.getOtherPurpose() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DocumentRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
        }
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName());
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DOCUMENT;
    }

    @Override
    abstract public DocumentRequestType getDocumentRequestType();

    @Override
    abstract public String getDocumentTemplateKey();

    abstract public boolean isPagedDocument();

    @Override
    final public boolean isCertificate() {
        return getDocumentRequestType().isCertificate();
    }

    final public boolean isDeclaration() {
        return getDocumentRequestType().isDeclaration();
    }

    @Override
    final public boolean isDiploma() {
        return getDocumentRequestType().isDiploma();
    }

    @Override
    final public boolean isPastDiploma() {
        return getDocumentRequestType().isPastDiploma();
    }

    @Override
    public boolean isRegistryDiploma() {
        return getDocumentRequestType().isRegistryDiploma();
    }

    @Override
    final public boolean isDiplomaSupplement() {
        return getDocumentRequestType().isDiplomaSupplement();
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
    }

    protected void assertPayedEvents(final ExecutionYear executionYear) {
        if (executionYear != null) {
            if (TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(getPerson(), new LocalDate())) {
                throw new DomainException("DocumentRequest.student.has.not.payed.debts");
            }
        }
    }

    @Override
    public boolean isDownloadPossible() {
        return getLastGeneratedDocument() != null && !isCancelled() && !isRejected();
    }

    @Override
    final public boolean isToShowCredits() {
        return !getDegreeType().isPreBolonhaDegree();
    }

    public boolean hasNumberOfPages() {
        return getNumberOfPages() != null && getNumberOfPages().intValue() != 0;
    }

    public Locale getLocale() {
        return null;
    }

    @Override
    protected void checkRulesToDelete() {
        super.checkRulesToDelete();
        if (hasRegistryCode()) {
            throw new DomainException("error.AcademicServiceRequest.cannot.be.deleted");
        }
    }

    @Override
    public byte[] generateDocument() {
        List<AdministrativeOfficeDocument> documents =
                AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this);
        final AdministrativeOfficeDocument[] array = {};
        byte[] data = ReportsUtils.generateReport(documents.toArray(array)).getData();
        DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
        return data;
    }

    @Override
    public String getReportFileName() {
        return AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this).iterator().next()
                .getReportFileName();
    }

    public Collection<ICurriculumEntry> getApprovedCurriculumEntries() {
        return null;
    }

}
