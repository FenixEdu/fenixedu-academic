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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AcademicEvent;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.events.AcademicEventExemption;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import org.fenixedu.academic.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.phd.debts.PhdEvent;
import org.fenixedu.academic.domain.phd.debts.PhdEventExemption;
import org.fenixedu.academic.domain.phd.debts.PhdEventExemptionJustificationType;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityEvent;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityExternalScholarshipExemption;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityFineExemption;
import org.fenixedu.academic.domain.phd.debts.PhdRegistrationFee;
import org.fenixedu.academic.dto.accounting.AcademicEventExemptionBean;
import org.fenixedu.academic.dto.accounting.AdministrativeOfficeFeeAndInsuranceExemptionBean;
import org.fenixedu.academic.dto.accounting.InsuranceExemptionBean;
import org.fenixedu.academic.dto.accounting.SecondCycleIndividualCandidacyExemptionBean;
import org.fenixedu.academic.dto.accounting.gratuityExemption.CreateGratuityExemptionBean;
import org.fenixedu.academic.dto.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import org.fenixedu.academic.dto.accounting.penaltyExemption.CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean;
import org.fenixedu.academic.dto.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import org.fenixedu.academic.dto.accounting.penaltyExemption.CreatePhdRegistrationFeePenaltyExemptionBean;
import org.fenixedu.academic.service.services.accounting.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption;
import org.fenixedu.academic.service.services.accounting.CreateImprovementOfApprovedEnrolmentPenaltyExemption;
import org.fenixedu.academic.service.services.accounting.CreatePhdRegistrationFeePenaltyExemption;
import org.fenixedu.academic.service.services.accounting.CreateSecondCycleIndividualCandidacyExemption;
import org.fenixedu.academic.service.services.accounting.DeleteExemption;
import org.fenixedu.academic.service.services.accounting.ExemptionsManagement;
import org.fenixedu.academic.service.services.accounting.gratuity.CreateGratuityExemption;
import org.fenixedu.academic.service.services.accounting.gratuity.CreateInstallmentPenaltyExemption;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.phd.PhdEventExemptionBean;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/exemptionsManagement", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showEventsToApplyExemption",
                path = "/academicAdminOffice/payments/exemptions/showEventsToApplyExemption.jsp"),
        @Forward(name = "showForGratuityEvent", path = "/academicAdminOffice/payments/exemptions/showForGratuityEvent.jsp"),
        @Forward(name = "showForImprovementOfApprovedEnrolmentEvent",
                path = "/academicAdminOffice/payments/exemptions/showForImprovementOfApprovedEnrolmentEvent.jsp"),
        @Forward(name = "showForAdministrativeOfficeFeeAndInsuranceEvent",
                path = "/academicAdminOffice/payments/exemptions/showForAdministrativeOfficeFeeAndInsuranceEvent.jsp"),
        @Forward(name = "showForInsuranceEvent", path = "/academicAdminOffice/payments/exemptions/showForInsuranceEvent.jsp"),
        @Forward(name = "showForSecondCycleIndividualCandidacyEvent",
                path = "/academicAdminOffice/payments/exemptions/showForSecondCycleIndividualCandidacyEvent.jsp"),
        @Forward(name = "showForPhdRegistrationFee",
                path = "/phd/academicAdminOffice/payments/exemptions/showForPhdRegistrationFee.jsp"),
        @Forward(name = "showForAcademicEvent", path = "/academicAdminOffice/payments/exemptions/showForAcademicEvent.jsp"),
        @Forward(name = "showPhdGratuity", path = "/phd/academicAdminOffice/payments/exemptions/showPhdGratuity.jsp"),
        @Forward(name = "showForPhdEvent", path = "/phd/academicAdminOffice/payments/exemptions/showForPhdEvent.jsp"),
        @Forward(name = "createGratuityExemption", path = "/academicAdminOffice/payments/exemptions/payment/gratuity/create.jsp"),
        @Forward(name = "createInstallmentPenaltyExemption",
                path = "/academicAdminOffice/payments/exemptions/penalty/createInstallmentExemption.jsp"),
        @Forward(name = "createImprovementOfApprovedEnrolmentPenaltyExemption",
                path = "/academicAdminOffice/payments/exemptions/penalty/createImprovementOfApprovedEnrolmentExemption.jsp"),
        @Forward(name = "createAdministrativeOfficeFeeAndInsurancePenaltyExemption",
                path = "/academicAdminOffice/payments/exemptions/penalty/createAdministrativeOfficeFeeAndInsuranceExemption.jsp"),
        @Forward(name = "createAdministrativeOfficeFeeAndInsuranceExemption",
                path = "/academicAdminOffice/payments/exemptions/payment/administrativeOfficeFeeAndInsurance/create.jsp"),
        @Forward(name = "createInsuranceExemption",
                path = "/academicAdminOffice/payments/exemptions/payment/insurance/createInsuranceExemption.jsp"),
        @Forward(name = "createSecondCycleIndividualCandidacyExemption",
                path = "/academicAdminOffice/payments/exemptions/createSecondCycleIndividualCandidacyExemption.jsp"),
        @Forward(name = "createPhdRegistrationFeePenaltyExemption",
                path = "/phd/academicAdminOffice/payments/exemptions/createPhdRegistrationFeePenaltyExemption.jsp"),
        @Forward(name = "createAcademicEventExemption",
                path = "/academicAdminOffice/payments/exemptions/createAcademicEventExemption.jsp"),
        @Forward(name = "createPhdEventExemption",
                path = "/phd/academicAdminOffice/payments/exemptions/createPhdEventExemption.jsp"),
        @Forward(name = "createFCTExemption", path = "/phd/academicAdminOffice/payments/exemptions/createFCTExemption.jsp") })
public class ExemptionsManagementDispatchAction extends PaymentsManagementDispatchAction {

    public ActionForward showEventsToApplyExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getPerson(request);
        request.setAttribute("person", person);
        request.setAttribute("eventsToApplyExemption", person.getEventsWithExemptionAppliable());

        return mapping.findForward("showEventsToApplyExemption");
    }

    public ActionForward prepareCreateGratuityExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createGratuityExemptionBean", new CreateGratuityExemptionBean(getGratuityEvent(request)));

        return mapping.findForward("createGratuityExemption");
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String propertyName,
            String messageKey) {
        return invalidCreateGratuityExemption(mapping, request, propertyName, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String messageKey,
            String[] args) {
        return invalidCreateGratuityExemption(mapping, request, null, messageKey, args);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String messageKey) {
        return invalidCreateGratuityExemption(mapping, request, messageKey, new String[0]);
    }

    private ActionForward invalidCreateGratuityExemption(ActionMapping mapping, HttpServletRequest request, String propertyName,
            String messageKey, String[] args) {

        if (propertyName == null) {
            addActionMessage(request, messageKey, args);
        } else {
            addActionMessage(propertyName, request, messageKey, args);
        }

        request.setAttribute("createGratuityExemptionBean", getRenderedObject("createGratuityExemptionBean"));

        return mapping.findForward("createGratuityExemption");
    }

    public ActionForward createGratuityExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateGratuityExemptionBean createGratuityExemptionBean = getRenderedObject("createGratuityExemptionBean");

        if (!createGratuityExemptionBean.isPercentageExemption() && createGratuityExemptionBean.getAmount() == null) {
            return invalidCreateGratuityExemption(mapping, request, "context",
                    "error.payments.gratuityExemption.amount.or.percentage.are.required");
        }

        if (createGratuityExemptionBean.isPercentageExemption() && createGratuityExemptionBean.getAmount() != null) {
            return invalidCreateGratuityExemption(mapping, request, "context",
                    "error.payments.gratuityExemption.cannot.select.both.amount.and.percentage");
        }

        try {
            CreateGratuityExemption.run(getUserView(request).getPerson(), createGratuityExemptionBean);
        } catch (DomainExceptionWithLabelFormatter ex) {
            return invalidCreateGratuityExemption(mapping, request, ex.getKey(),
                    solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            return invalidCreateGratuityExemption(mapping, request, ex.getKey());

        }

        request.setAttribute("eventId", createGratuityExemptionBean.getGratuityEvent().getExternalId());

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward deleteExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Exemption exemption = getExemption(request);
        request.setAttribute("eventId", exemption.getEvent().getExternalId());

        try {
            DeleteExemption.run(exemption);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        }

        return showExemptions(mapping, form, request, response);

    }

    public ActionForward prepareCreateInstallmentPenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createInstallmentPenaltyExemptionBean", new CreateInstallmentPenaltyExemptionBean(
                (GratuityEventWithPaymentPlan) getGratuityEvent(request)));

        // Logic to decide the view to show based on gratuity dueDate should
        // be placed here
        return mapping.findForward("createInstallmentPenaltyExemption");

    }

    public ActionForward prepareCreateInstallmentPenaltyExemptionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createInstallmentPenaltyExemptionBean",
                getRenderedObject("create-installment-penalty-exemption-bean"));

        return mapping.findForward("createInstallmentPenaltyExemption");
    }

    public ActionForward createInstallmentPenaltyExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateInstallmentPenaltyExemptionBean createInstallmentPenaltyExemptionBean =
                (CreateInstallmentPenaltyExemptionBean) RenderUtils.getViewState("create-installment-penalty-exemption-bean")
                        .getMetaObject().getObject();
        request.setAttribute("eventId", createInstallmentPenaltyExemptionBean.getGratuityEventWithPaymentPlan().getExternalId());

        try {
            CreateInstallmentPenaltyExemption.run(getLoggedPerson(request), createInstallmentPenaltyExemptionBean);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));

            return prepareCreateInstallmentPenaltyExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            return prepareCreateInstallmentPenaltyExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    private GratuityEvent getGratuityEvent(HttpServletRequest request) {
        return (GratuityEvent) getEvent(request);
    }

    public ActionForward showExemptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Event event = getEvent(request);
        request.setAttribute("person", event.getPerson());
        request.setAttribute("event", event);

        if (event instanceof GratuityEvent) {
            if (event instanceof GratuityEventWithPaymentPlan) {
                request.setAttribute("hasPaymentPlan", true);
            }
            return mapping.findForward("showForGratuityEvent");
        } else if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent) {
            return mapping.findForward("showForAdministrativeOfficeFeeAndInsuranceEvent");
        } else if (event instanceof InsuranceEvent) {
            return mapping.findForward("showForInsuranceEvent");
        } else if (event instanceof ImprovementOfApprovedEnrolmentEvent) {
            return mapping.findForward("showForImprovementOfApprovedEnrolmentEvent");
        } else if (event instanceof SecondCycleIndividualCandidacyEvent) {
            return mapping.findForward("showForSecondCycleIndividualCandidacyEvent");
        } else if (event instanceof PhdRegistrationFee) {
            return mapping.findForward("showForPhdRegistrationFee");
        } else if (event instanceof PhdGratuityEvent) {
            return mapping.findForward("showPhdGratuity");
        } else if (event instanceof PhdEvent) {
            return mapping.findForward("showForPhdEvent");
        } else if (event instanceof AcademicEvent) {
            return mapping.findForward("showForAcademicEvent");
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Exemption getExemption(final HttpServletRequest request) {
        return getDomainObject(request, "exemptionId");
    }

    public ActionForward prepareCreateImprovementOfApprovedEnrolmentPenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createPenaltyExemptionBean", new CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean(
                (ImprovementOfApprovedEnrolmentEvent) getEvent(request)));

        return mapping.findForward("createImprovementOfApprovedEnrolmentPenaltyExemption");

    }

    public ActionForward prepareCreateImprovementOfApprovedEnrolmentPenaltyExemptionInvalid(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));
        return mapping.findForward("createImprovementOfApprovedEnrolmentPenaltyExemption");
    }

    public ActionForward createImprovementOfApprovedEnrolmentPenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean penaltyExemptionBean =
                (CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean) RenderUtils
                        .getViewState("create-penalty-exemption-bean").getMetaObject().getObject();
        request.setAttribute("eventId", penaltyExemptionBean.getEvent().getExternalId());

        try {
            CreateImprovementOfApprovedEnrolmentPenaltyExemption.run(getLoggedPerson(request), penaltyExemptionBean);
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            return prepareCreateImprovementOfApprovedEnrolmentPenaltyExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createPenaltyExemptionBean", new CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean(
                (AdministrativeOfficeFeeAndInsuranceEvent) getEvent(request)));

        return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");

    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));

        return mapping.findForward("createAdministrativeOfficeFeeAndInsurancePenaltyExemption");
    }

    public ActionForward createAdministrativeOfficeFeeAndInsurancePenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean =
                (CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean) RenderUtils
                        .getViewState("create-penalty-exemption-bean").getMetaObject().getObject();
        request.setAttribute("eventId", penaltyExemptionBean.getEvent().getExternalId());

        try {
            CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption.run(getLoggedPerson(request), penaltyExemptionBean);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));

            return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            return prepareCreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateSecondCycleIndividualCandidacyExemption(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", new SecondCycleIndividualCandidacyExemptionBean(
                (SecondCycleIndividualCandidacyEvent) getEvent(request)));
        return mapping.findForward("createSecondCycleIndividualCandidacyExemption");
    }

    public ActionForward prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));
        return mapping.findForward("createSecondCycleIndividualCandidacyExemption");
    }

    public ActionForward createSecondCycleIndividualCandidacyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            CreateSecondCycleIndividualCandidacyExemption.run(getLoggedPerson(request),
                    (SecondCycleIndividualCandidacyExemptionBean) getRenderedObject("create-penalty-exemption-bean"));

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return prepareCreateSecondCycleIndividualCandidacyExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateAcademicEventExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("exemptionBean", new AcademicEventExemptionBean((AcademicEvent) getEvent(request)));
        return mapping.findForward("createAcademicEventExemption");
    }

    public ActionForward prepareCreateAcademicEventExemptionInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("exemptionBean", getRenderedObject("exemptionBean"));
        return mapping.findForward("createAcademicEventExemption");
    }

    public ActionForward createAcademicEventExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final AcademicEventExemptionBean bean = getRenderedObject("exemptionBean");
            AcademicEventExemption.create(getLoggedPerson(request), bean.getEvent(), bean.getValue(),
                    bean.getJustificationType(), bean.getDispatchDate(), bean.getReason());

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return prepareCreateAcademicEventExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return prepareCreateAcademicEventExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreatePhdEventExemption(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PhdEventExemptionBean phdEventExemptionBean = new PhdEventExemptionBean((PhdEvent) getEvent(request));
        phdEventExemptionBean.setJustificationType(PhdEventExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION);
        request.setAttribute("exemptionBean", phdEventExemptionBean);
        return mapping.findForward("createPhdEventExemption");
    }

    @Deprecated
    public ActionForward prepareCreatePhdGratuityExemptionForGratuity(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        PhdEventExemptionBean phdEventExemptionBean = new PhdEventExemptionBean((PhdEvent) getEvent(request));
        phdEventExemptionBean.setJustificationType(PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION);
        request.setAttribute("exemptionBean", phdEventExemptionBean);
        return mapping.findForward("createFCTExemption");
    }

    public ActionForward prepareCreatePhdEventExemptionInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("exemptionBean", getRenderedObject("exemptionBean"));
        return mapping.findForward("createPhdEventExemption");
    }

    @Deprecated
    public ActionForward createFCTExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdEventExemptionBean bean = getRenderedObject("exemptionBean");
            PhdGratuityExternalScholarshipExemption.createPhdGratuityExternalScholarshipExemption(getLoggedPerson(request),
                    bean.getValue(), bean.getProvider(), ((PhdGratuityEvent) bean.getEvent()));
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return showExemptions(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return showExemptions(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward createPhdEventExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdEventExemptionBean bean = getRenderedObject("exemptionBean");
            if (bean.getJustificationType() == PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION) {
                PhdGratuityExternalScholarshipExemption.createPhdGratuityExternalScholarshipExemption(getLoggedPerson(request),
                        bean.getValue(), bean.getProvider(), (PhdGratuityEvent) bean.getEvent());
            } else if (bean.getJustificationType() == PhdEventExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION) {
                PhdEventExemption.create(getLoggedPerson(request), bean.getEvent(), bean.getValue(), bean.getJustificationType(),
                        bean.getDispatchDate(), bean.getReason());
            } else if (bean.getJustificationType() == PhdEventExemptionJustificationType.FINE_EXEMPTION) {
                PhdGratuityFineExemption.createPhdGratuityFineExemption(getLoggedPerson(request),
                        (PhdGratuityEvent) bean.getEvent(), bean.getReason());
            } else if (bean.getJustificationType().equals(PhdEventExemptionJustificationType.THIRD_PARTY_CONTRIBUTION)) {
                PhdGratuityEvent event = ((PhdGratuityEvent) bean.getEvent());
                PhdGratuityExternalScholarshipExemption.createPhdGratuityExternalScholarshipExemption(getLoggedPerson(request),
                        event.getAmountToPay(), bean.getProvider(), event, bean.getReason(),bean
                                .getFileName(), bean.getFile());
            }
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return prepareCreatePhdEventExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return prepareCreatePhdEventExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsuranceExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(
                "createAdministrativeOfficeFeeAndInsuranceExemptionBean",
                new AdministrativeOfficeFeeAndInsuranceExemptionBean((AdministrativeOfficeFeeAndInsuranceEvent) getEvent(request)));

        return mapping.findForward("createAdministrativeOfficeFeeAndInsuranceExemption");
    }

    public ActionForward prepareCreateAdministrativeOfficeFeeAndInsuranceExemptionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createAdministrativeOfficeFeeAndInsuranceExemptionBean",
                getRenderedObject("createAdministrativeOfficeFeeAndInsuranceExemptionBean"));

        return mapping.findForward("createAdministrativeOfficeFeeAndInsuranceExemption");
    }

    public ActionForward createAdministrativeOfficeFeeAndInsuranceExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            ExemptionsManagement
                    .createAdministrativeOfficeFeeAndInsuranceExemption(
                            getLoggedPerson(request),
                            (AdministrativeOfficeFeeAndInsuranceExemptionBean) getRenderedObject("createAdministrativeOfficeFeeAndInsuranceExemptionBean"));

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return prepareCreateAdministrativeOfficeFeeAndInsuranceExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return prepareCreateAdministrativeOfficeFeeAndInsuranceExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward prepareCreatePhdRegistrationFeePenaltyExemption(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", new CreatePhdRegistrationFeePenaltyExemptionBean(
                (PhdRegistrationFee) getEvent(request)));
        return mapping.findForward("createPhdRegistrationFeePenaltyExemption");
    }

    public ActionForward createPhdRegistrationFeePenaltyExemptionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createPenaltyExemptionBean", getRenderedObject("create-penalty-exemption-bean"));
        return mapping.findForward("createPhdRegistrationFeePenaltyExemption");
    }

    public ActionForward createPhdRegistrationFeePenaltyExemption(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final CreatePhdRegistrationFeePenaltyExemptionBean penaltyExemptionBean =
                getRenderedObject("create-penalty-exemption-bean");
        request.setAttribute("eventId", penaltyExemptionBean.getEvent().getExternalId());

        try {
            CreatePhdRegistrationFeePenaltyExemption.run(getLoggedPerson(request), penaltyExemptionBean);
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return createPhdRegistrationFeePenaltyExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    /* Insurance Exemption */
    public ActionForward prepareCreateInsuranceExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createInsuranceExemptionBean", new InsuranceExemptionBean((InsuranceEvent) getEvent(request)));

        return mapping.findForward("createInsuranceExemption");
    }

    public ActionForward prepareCreateInsuranceExemptionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createInsuranceExemptionBean", getRenderedObject("createInsuranceExemptionBean"));

        return mapping.findForward("createInsuranceExemption");
    }

    public ActionForward createInsuranceExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExemptionsManagement.createInsuranceExemption(getLoggedPerson(request),
                    (InsuranceExemptionBean) getRenderedObject("createInsuranceExemptionBean"));

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return prepareCreateAdministrativeOfficeFeeAndInsuranceExemptionInvalid(mapping, form, request, response);

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return prepareCreateAdministrativeOfficeFeeAndInsuranceExemptionInvalid(mapping, form, request, response);
        }

        return showExemptions(mapping, form, request, response);
    }

    public ActionForward changeForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PhdEventExemptionBean bean = getRenderedObject("exemptionBean");
        RenderUtils.invalidateViewState();
        request.setAttribute("exemptionBean", bean);
        return mapping.findForward("createPhdEventExemption");
    }
}
