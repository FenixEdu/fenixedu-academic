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
package org.fenixedu.academic.domain.phd.serviceRequests.documentRequests;

import java.util.List;
import java.util.Optional;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PhdDocumentRequest extends PhdDocumentRequest_Base implements IDocumentRequest {

    private static final Logger logger = LoggerFactory.getLogger(PhdDocumentRequest.class);

    protected PhdDocumentRequest() {
        super();
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
        throw new DomainException("invoke init(PhdAcademicServiceRequestCreateBean)");
    }

    protected void init(PhdDocumentRequestCreateBean bean) {
        super.init(bean);
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

    @Override
    final public boolean isDiploma() {
        return getDocumentRequestType().isDiploma();
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
    public boolean isCertificate() {
        return false;
    }

    @Override
    public boolean isToShowCredits() {
        return false;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            if (!getFreeProcessed()) {
                assertPayedEvents();
            }
        }
    }

    protected void assertPayedEvents() {
        if (getPhdIndividualProgramProcess().hasInsuranceDebtsCurrently()) {
            throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.insurance.fees");
        }

        if (getPhdIndividualProgramProcess().hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(getAdministrativeOffice())) {
            throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.administrative.office.fees");
        }
    }

    @Override
    public boolean isDownloadPossible() {
        return getLastGeneratedDocument() != null;
    }

    public boolean hasNumberOfPages() {
        return getNumberOfPages() != null && getNumberOfPages().intValue() != 0;
    }

    @Override
    public byte[] generateDocument() {
        final List<AdministrativeOfficeDocument> documents =
                AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this);
        byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(documents);
        DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
        return data;
    }

    @Override
    public String getReportFileName() {
        return AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this).iterator().next()
                .getReportFileName();
    }

    public ProgramConclusion getProgramConclusion() {

        if (getPhdIndividualProgramProcess().getRegistration() != null) {
            return ProgramConclusion.conclusionsFor(getPhdIndividualProgramProcess().getRegistration()).findAny()
                    .orElseThrow(() -> new DomainException("error.program.conclusion.empty"));
        }

        /**
         * TODO: phd-refactor
         * The following code should be removed since after phd-refactor all individual processes must have a registration
         */

        DegreeCurricularPlan lastActiveDegreeCurricularPlan =
                Optional.ofNullable(getPhdIndividualProgramProcess().getPhdProgram().getDegree())
                        .map(Degree::getLastActiveDegreeCurricularPlan)
                        .orElseThrow(() -> new DomainException("error.program.conclusion.empty"));

        return ProgramConclusion.conclusionsFor(lastActiveDegreeCurricularPlan).findAny()
                .orElseThrow(() -> new DomainException("error.program.conclusion.empty"));
    }

}
