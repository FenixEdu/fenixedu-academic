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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.documents;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.SimpleSearchPersonWithStudentBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.documents.AnnualIRSDeclarationDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminDocumentsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.docs.IRSCustomDeclaration;
import net.sourceforge.fenixedu.presentationTier.docs.IRSCustomDeclaration.IRSDeclarationDTO;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

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

        request.setAttribute("persons", searchPerson(request, searchPersonBean));

        return mapping.findForward("searchPerson");
    }

    @SuppressWarnings("unchecked")
    private Collection<Person> searchPerson(HttpServletRequest request, SimpleSearchPersonWithStudentBean searchPersonBean)
            throws FenixServiceException {
        final SearchParameters searchParameters =
                new SearchPerson.SearchParameters(searchPersonBean.getName(), null, searchPersonBean.getUsername(),
                        searchPersonBean.getDocumentIdNumber(), searchPersonBean.getIdDocumentType() != null ? searchPersonBean
                                .getIdDocumentType().toString() : null, null, null, null, null, null,
                        searchPersonBean.getStudentNumber(), Boolean.FALSE, (String) null);

        final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        final CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        return result.getCollection();
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
            HttpServletResponse response) throws JRException {

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
            HttpServletRequest request, HttpServletResponse response) throws JRException {

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

    private byte[] buildIRSCustomDeclaration(final IRSDeclarationDTO declarationDTO, final Person person) throws JRException,
            FenixActionException {

        addPayedAmount(person, declarationDTO.getCivilYear(), declarationDTO);
        final IRSCustomDeclaration customDeclaration = new IRSCustomDeclaration(declarationDTO);

        return ReportsUtils.exportToPdfFileAsByteArray(customDeclaration.getReportTemplateKey(),
                customDeclaration.getParameters(), customDeclaration.getDataSource());
    }

    private void addPayedAmount(Person person, int civilYear, final IRSDeclarationDTO declarationDTO) throws FenixActionException {
        for (final Event event : person.getEvents()) {
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
