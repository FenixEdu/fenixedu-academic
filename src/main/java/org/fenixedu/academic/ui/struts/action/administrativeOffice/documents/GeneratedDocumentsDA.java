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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.documents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.documents.AnnualIRSDeclarationDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.report.IRSCustomDeclaration;
import org.fenixedu.academic.report.IRSCustomDeclaration.IRSDeclarationDTO;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminDocumentsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

@StrutsFunctionality(app = AcademicAdminDocumentsApp.class, path = "generated-documents", titleKey = "label.documents",
        accessGroup = "academic(MANAGE_DOCUMENTS)")
@Mapping(path = "/generatedDocuments", module = "academicAdministration", formBeanClass = FenixActionForm.class)
@Forwards({
        @Forward(name = "searchPerson", path = "/academicAdminOffice/generatedDocuments/searchPerson.jsp"),
        @Forward(name = "showAnnualIRSDocuments", path = "/academicAdminOffice/generatedDocuments/showAnnualIRSDocuments.jsp"),
        @Forward(name = "payments.manageIRSDocuments",
                path = "/academicAdminOffice/generatedDocuments/payments/manageIRSDocuments.jsp") })
public class GeneratedDocumentsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

        return mapping.findForward("searchPerson");
    }

    public ActionForward prepareSearchPersonInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", getObjectFromViewState("searchPersonBean"));

        return mapping.findForward("searchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final SimpleSearchPersonWithStudentBean searchPersonBean =
                (SimpleSearchPersonWithStudentBean) getObjectFromViewState("searchPersonBean");
        request.setAttribute("searchPersonBean", searchPersonBean);

        request.setAttribute("persons", searchPersonBean.search());

        return mapping.findForward("searchPerson");
    }

    public ActionForward showAnnualIRSDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));
        request.setAttribute("annualIRSDocuments", getPerson(request).getAnnualIRSDocuments());

        return mapping.findForward("showAnnualIRSDocuments");
    }

    private Person getPerson(HttpServletRequest request) {
        return getDomainObject(request, "personId");
    }

    public ActionForward showAnnualIRSDocumentsInPayments(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        showAnnualIRSDocuments(mapping, actionForm, request, response);
        return mapping.findForward("payments.manageIRSDocuments");
    }

    public ActionForward prepareGenerateNewIRSDeclaration(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("declarationDTO", new IRSDeclarationDTO(null, getPerson(request)));
        return showAnnualIRSDocumentsInPayments(mapping, actionForm, request, response);
    }

    public ActionForward generateNewIRSDeclarationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("declarationDTO", getRenderedObject("declarationDTO"));
        return showAnnualIRSDocumentsInPayments(mapping, actionForm, request, response);
    }

    public ActionForward generateNewIRSDeclaration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getPerson(request);
        final IRSDeclarationDTO declarationDTO = getRenderedObject("declarationDTO");

        try {

            if (declarationDTO.getCivilYear().intValue() >= new LocalDate().getYear()) {
                addActionMessage("error", request, "error.annual.irs.declaration.year.must.be.previous.to.current");

            } else {
                byte[] declaration = buildIRSCustomDeclaration(declarationDTO, person);
                AnnualIRSDeclarationDocument.create(person, getLoggedPerson(request), declaration, declarationDTO.getCivilYear());
                addActionMessage("success", request, "message.new.irs.annual.document.generated.with.success");
            }

        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());

        } catch (final FenixActionException e) {
            addActionMessage("error", request, e.getMessage());
        }

        return showAnnualIRSDocumentsInPayments(mapping, actionForm, request, response);
    }

    public ActionForward generateAgainAnnualIRSDeclarationDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            final AnnualIRSDeclarationDocument document = getDomainObject(request, "annualIRSDocumentOid");
            request.setAttribute("personId", document.getAddressee().getExternalId());

            final IRSDeclarationDTO declarationDTO =
                    new IRSDeclarationDTO(document.getYear().intValue(), document.getAddressee());
            byte[] declaration = buildIRSCustomDeclaration(declarationDTO, document.getAddressee());

            document.generateAnotherDeclaration(AccessControl.getPerson(), declaration);
            addActionMessage("success", request, "message.new.irs.annual.document.generated.with.success");

        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());

        } catch (final FenixActionException e) {
            addActionMessage("error", request, e.getMessage());
        }

        return showAnnualIRSDocumentsInPayments(mapping, actionForm, request, response);
    }

    private byte[] buildIRSCustomDeclaration(final IRSDeclarationDTO declarationDTO, final Person person)
            throws FenixActionException {

        addPayedAmount(person, declarationDTO.getCivilYear(), declarationDTO);
        final IRSCustomDeclaration customDeclaration = new IRSCustomDeclaration(declarationDTO);

        return ReportsUtils.generateReport(customDeclaration.getReportTemplateKey(), customDeclaration.getParameters(),
                customDeclaration.getDataSource()).getData();
    }

    private void addPayedAmount(Person person, int civilYear, final IRSDeclarationDTO declarationDTO) throws FenixActionException {
        for (final Event event : person.getEventsSet()) {
            if (event.hasPaymentsByPersonForCivilYear(civilYear)
                    && event.getMaxDeductableAmountForLegalTaxes(civilYear).isPositive()) {
                declarationDTO.addAmount(event, civilYear);
            }
        }

        if (!declarationDTO.getTotalAmount().isPositive()) {
            throw new FenixActionException("error.annual.irs.declaration.no.payments.for.civil.year", civilYear);
        }
    }
}
