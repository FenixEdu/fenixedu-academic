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
package org.fenixedu.academic.ui.struts.action.commons.documentRequestExcel;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;

public class DocumentRequestExcelUtils {

    HttpServletRequest request;
    HttpServletResponse response;

    public DocumentRequestExcelUtils(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void generateSortedExcel(Set<AcademicServiceRequest> documents, String prefix) {

        SortedSet<AcademicServiceRequest> sorted =
                new TreeSet<AcademicServiceRequest>(DocumentRequest.COMPARATOR_BY_REGISTRY_NUMBER);
        sorted.addAll(documents);
        generate(sorted, getCodes(sorted), prefix);

    }

    public void generateExcel(Set<AcademicServiceRequest> documents, String prefix) {

        generate(documents, getCodes(documents), prefix);

    }

    private Set<RegistryCode> getCodes(Set<AcademicServiceRequest> documents) {
        Set<RegistryCode> codes = new HashSet<RegistryCode>();
        for (AcademicServiceRequest document : documents) {
            codes.add(document.getRegistryCode());
        }
        return codes;
    }

    private void generate(Set<AcademicServiceRequest> documents, Set<RegistryCode> codes, String prefix) {

        SheetData<AcademicServiceRequest> data = new SheetData<AcademicServiceRequest>(documents) {
            @Override
            protected void makeLine(AcademicServiceRequest request) {
                IDocumentRequest document = (IDocumentRequest) request;
                addCell("Código", document.getRegistryCode().getCode());
                addCell("Tipo de Documento", BundleUtil.getString(Bundle.ENUMERATION, document.getDocumentRequestType().name()));
                ProgramConclusion programConclusion = null;
                switch (document.getDocumentRequestType()) {
                case REGISTRY_DIPLOMA_REQUEST:
                    programConclusion = ((IRegistryDiplomaRequest) document).getProgramConclusion();
                    break;
                case DIPLOMA_REQUEST:
                    programConclusion = ((IDiplomaRequest) document).getProgramConclusion();
                    break;
                case DIPLOMA_SUPPLEMENT_REQUEST:
                    programConclusion = ((IDiplomaSupplementRequest) document).getProgramConclusion();
                    break;
                default:
                    addCell(BundleUtil.getString(Bundle.APPLICATION, "label.programConclusion"));
                }
                addCell(BundleUtil.getString(Bundle.APPLICATION, "label.programConclusion"),
                        programConclusion != null ? programConclusion.getName().getContent() : null);

                if (document.isRequestForRegistration()) {
                    addCell("Tipo de Curso", ((RegistrationAcademicServiceRequest) document).getDegreeType().getName()
                            .getContent());
                } else if (document.isRequestForPhd()) {
                    addCell("Tipo de Estudos", "Programa doutoral");
                }
                addCell("Nº de Aluno", document.getStudent().getNumber());
                addCell("Nome", document.getPerson().getName());
                if (!(document.isDiploma())) {
                    addCell("Ficheiro", request.getLastGeneratedDocument().getFilename());
                }
            }
        };

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + prefix + "-" + "(" + codes.size() + ")" + ".xls");
            final ServletOutputStream writer = response.getOutputStream();
            new SpreadsheetBuilder().addSheet("lote", data).build(WorkbookExportFormat.EXCEL, writer);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
        }
    }
}
