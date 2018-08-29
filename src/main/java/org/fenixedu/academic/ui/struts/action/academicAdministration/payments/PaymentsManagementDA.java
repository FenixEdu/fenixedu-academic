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
package org.fenixedu.academic.ui.struts.action.academicAdministration.payments;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.service.services.accounting.AnnulReceipt;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPaymentsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.stream.Collectors;

@StrutsFunctionality(app = AcademicAdminPaymentsApp.class, path = "manage-payments", titleKey = "label.payments.management",
        accessGroup = "academic(MANAGE_STUDENT_PAYMENTS_ADV)")
@Mapping(path = "/paymentsManagement", module = "academicAdministration")
@Forwards({
        @Forward(name = "searchPersons", path = "/academicAdministration/payments/events/searchPersons.jsp"),
        @Forward(name = "showEvents", path = "/academicAdministration/payments/events/showEvents.jsp"),
        @Forward(name = "showOperations", path = "/academicAdministration/payments/showOperations.jsp"),
        @Forward(name = "showReceipts", path = "/academicAdministration/payments/receipts/showReceipts.jsp"),
        @Forward(name = "showReceipt", path = "/academicAdministration/payments/receipts/showReceipt.jsp") })
public class PaymentsManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

        return mapping.findForward("searchPersons");
    }

    public ActionForward prepareSearchPersonInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", getObjectFromViewState("searchPersonBean"));

        return mapping.findForward("searchPersons");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final SimpleSearchPersonWithStudentBean searchPersonBean =
                (SimpleSearchPersonWithStudentBean) getObjectFromViewState("searchPersonBean");
        request.setAttribute("searchPersonBean", searchPersonBean);

        Collection<Person> persons = searchPersonBean.search();
        request.removeAttribute("sizeWarning");
        if (persons.size() == 1) {
            request.setAttribute("personId", persons.iterator().next().getExternalId());

            return showOperations(mapping, form, request, response);

        }
        if (persons.size() > 50) {
            persons = persons.stream().limit(50).collect(Collectors.toSet());
            request.setAttribute("sizeWarning", BundleUtil.getString(Bundle.ACADEMIC, "warning.need.to.filter.candidates"));
        }
        request.setAttribute("persons", persons);
        return mapping.findForward("searchPersons");
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showEvents");

    }

    public ActionForward backToShowEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showEvents");

    }

    protected Person getPerson(HttpServletRequest request) {
        return getDomainObject(request, "personId");
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("permission", getPermissionForAcademicAdministration(getPerson(request)));
        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showOperations");
    }

    private boolean getPermissionForAcademicAdministration(Person person) {
        return AcademicPredicates.MANAGE_STUDENT_PAYMENTS_ADV.evaluate(person);
    }

    public ActionForward showReceipts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showReceipts");
    }

    public ActionForward showReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("receipt", getReceipt(request));

        return mapping.findForward("showReceipt");
    }

    public ActionForward annulReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            AnnulReceipt.run(getLoggedPerson(request), getReceipt(request));
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        }

        return showReceipts(mapping, form, request, response);
    }

    private Receipt getReceipt(HttpServletRequest request) {
        return getDomainObject(request, "receiptId");
    }

}