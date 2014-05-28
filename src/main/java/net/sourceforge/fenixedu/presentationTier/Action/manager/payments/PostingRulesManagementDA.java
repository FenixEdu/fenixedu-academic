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
package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.PostingRulesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity.paymentPlan.GratuityPaymentPlanManager;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.StandaloneInstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateSpecializationDegreeGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateStandaloneEnrolmentGratuityPRBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentService;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.DFAGratuityByAmountPerEctsPREditor;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR.DFAGratuityByNumberOfEnrolmentsPREditor;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.SpecializationDegreeGratuityByAmountPerEctsPREditor;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionPR;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPaymentsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerPaymentsApp.class, path = "posting-rules", titleKey = "label.payments.postingRules.management")
@Mapping(path = "/postingRules", module = "manager", formBeanClass = PostingRulesManagementDA.PostingRulesManagementForm.class)
@Forwards({
        @Forward(name = "chooseCategory", path = "/manager/payments/postingRules/management/chooseCategory.jsp"),
        @Forward(name = "choosePostGraduationDegreeCurricularPlans",
                path = "/manager/payments/postingRules/management/choosePostGraduationDegreeCurricularPlans.jsp"),
        @Forward(name = "showPostGraduationDegreeCurricularPlanPostingRules",
                path = "/manager/payments/postingRules/management/showPostGraduationDegreeCurricularPlanPostingRules.jsp"),
        @Forward(name = "viewPostingRuleDetails", path = "/manager/payments/postingRules/management/viewPostingRuleDetails.jsp"),
        @Forward(name = "createDFAGratuityPR", path = "/manager/payments/postingRules/management/createDFAGratuityPR.jsp"),
        @Forward(name = "editDFAGratuityPR", path = "/manager/payments/postingRules/management/editDFAGratuityPR.jsp"),
        @Forward(name = "editSpecializationDegreeGratuityPR",
                path = "/manager/payments/postingRules/management/specializationDegree/editSpecializationDegreeGratuityPR.jsp"),
        @Forward(name = "editDegreeCurricularPlanPostingRule",
                path = "/manager/payments/postingRules/management/editDegreeCurricularPlanPostingRule.jsp"),
        @Forward(name = "showInsurancePostingRules",
                path = "/manager/payments/postingRules/management/showInsurancePostingRules.jsp"),
        @Forward(name = "editInsurancePR", path = "/manager/payments/postingRules/management/editInsurancePR.jsp"),
        @Forward(name = "showGraduationDegreeCurricularPlans",
                path = "/manager/payments/postingRules/management/graduation/showGraduationDegreeCurricularPlans.jsp"),
        @Forward(name = "showPaymentPlans", path = "/manager/payments/postingRules/management/graduation/showPaymentPlans.jsp"),
        @Forward(name = "createPaymentPlan", path = "/manager/payments/postingRules/management/graduation/createPaymentPlan.jsp"),
        @Forward(name = "createGraduationGratuityPR",
                path = "/manager/payments/postingRules/management/graduation/createGraduationGratuityPR.jsp"),
        @Forward(name = "showGraduationDegreeCurricularPlanPostingRules",
                path = "/manager/payments/postingRules/management/graduation/showGraduationDegreeCurricularPlanPostingRules.jsp"),
        @Forward(name = "createGraduationStandaloneEnrolmentGratuityPR",
                path = "/manager/payments/postingRules/management/graduation/createGraduationStandaloneEnrolmentGratuityPR.jsp"),
        @Forward(name = "createSpecializationDegreeGratuityPR",
                path = "/manager/payments/postingRules/management/specializationDegree/createSpecializationDegreeGratuityPR.jsp"),
        @Forward(name = "createDEAGratuityPR", path = "/manager/payments/postingRules/management/dea/createDEAGratuityPR.jsp"),
        @Forward(name = "createDEAStandaloneEnrolmentGratuityPR",
                path = "/manager/payments/postingRules/management/dea/createDEAStandaloneEnrolmentGratuityPR.jsp"),

        @Forward(name = "prepareEditFCTScolarshipPostingRule",
                path = "/manager/payments/postingRules/management/prepareEditFCTScolarshipPostingRule.jsp"),

        @Forward(name = "showFCTScolarshipPostingRules",
                path = "/manager/payments/postingRules/management/showFCTScolarshipPostingRules.jsp"),
        @Forward(name = "prepareAddFCTPostingRule",
                path = "/manager/payments/postingRules/management/prepareAddFCTPostingRule.jsp"),
        @Forward(name = "editInstallment", path = "/manager/payments/postingRules/management/graduation/editInstallment.jsp") })
public class PostingRulesManagementDA extends FenixDispatchAction {

    public static class PostingRulesManagementForm extends ActionForm {

        static private final long serialVersionUID = 1L;

        private String executionYearId;

        public String getExecutionYearId() {
            return executionYearId;
        }

        public void setExecutionYearId(String executionYearId) {
            this.executionYearId = executionYearId;
        }

    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("chooseCategory");
    }

    public ActionForward managePostGraduationRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Set<DegreeType> degreeTypes = new HashSet<DegreeType>(3);
        degreeTypes.add(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        degreeTypes.add(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA);
        degreeTypes.add(DegreeType.BOLONHA_SPECIALIZATION_DEGREE);

        request.setAttribute("degreeCurricularPlans",
                DegreeCurricularPlan.readByDegreeTypesAndState(degreeTypes, DegreeCurricularPlanState.ACTIVE));

        request.setAttribute("phdPrograms", Bennu.getInstance().getPhdProgramsSet());

        return mapping.findForward("choosePostGraduationDegreeCurricularPlans");
    }

    public ActionForward viewPostingRuleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("postingRule", getPostingRule(request));

        return mapping.findForward("viewPostingRuleDetails");
    }

    protected PostingRule getPostingRule(HttpServletRequest request) {
        return getDomainObject(request, "postingRuleId");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
        return getDomainObject(request, "degreeCurricularPlanId");
    }

    public ActionForward prepareEditDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final PostingRule postingRule = getPostingRule(request);

        if (postingRule instanceof DFAGratuityPR) {
            return prepareEditDFAGratuityPR(mapping, form, request, response);
        } else if (postingRule instanceof SpecializationDegreeGratuityPR) {
            return prepareEditSpecializationDegreeGratuityPR(mapping, form, request, response);
        }

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("postingRule", postingRule);

        return mapping.findForward("editDegreeCurricularPlanPostingRule");
    }

    public ActionForward prepareEditDegreeCurricularPlanPostingRuleInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("postingRule", getRenderedObject("postingRule"));

        return mapping.findForward("editDegreeCurricularPlanPostingRule");

    }

    public ActionForward prepareEditDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

        final PostingRule rule = getPostingRule(request);
        if (rule instanceof DFAGratuityByAmountPerEctsPR) {
            request.setAttribute("postingRuleEditor",
                    DFAGratuityByAmountPerEctsPREditor.buildFrom((DFAGratuityByAmountPerEctsPR) rule));
        } else {
            request.setAttribute("postingRuleEditor",
                    DFAGratuityByNumberOfEnrolmentsPREditor.buildFrom((DFAGratuityByNumberOfEnrolmentsPR) rule));
        }

        return mapping.findForward("editDFAGratuityPR");
    }

    public ActionForward prepareEditDFAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("postingRuleEditor", getRenderedObject("postingRuleEditor"));

        return mapping.findForward("editDFAGratuityPR");
    }

    public ActionForward editDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {

            executeFactoryMethod((FactoryExecutor) getRenderedObject("postingRuleEditor"));
            request.setAttribute("degreeCurricularPlanId", getDegreeCurricularPlan(request).getExternalId());

            return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);

        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
            request.setAttribute("postingRuleEditor", getRenderedObject());
            return mapping.findForward("editDFAGratuityPR");
        }
    }

    public ActionForward prepareEditSpecializationDegreeGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

        final PostingRule rule = getPostingRule(request);
        request.setAttribute("postingRuleEditor", SpecializationDegreeGratuityByAmountPerEctsPREditor
                .buildFrom((SpecializationDegreeGratuityByAmountPerEctsPR) rule));

        return mapping.findForward("editSpecializationDegreeGratuityPR");
    }

    public ActionForward prepareEditSpecializationDegreeGratuityPRInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("postingRuleEditor", getRenderedObject("postingRuleEditor"));

        return mapping.findForward("editSpecializationDegreeGratuityPR");
    }

    public ActionForward editSpecializationDegreeGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {

            executeFactoryMethod((FactoryExecutor) getRenderedObject("postingRuleEditor"));
            request.setAttribute("degreeCurricularPlanId", getDegreeCurricularPlan(request).getExternalId());

            return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);

        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("postingRuleEditor", getRenderedObject());
            return mapping.findForward("editDegreeCurricularPlanPostingRule");
        }
    }

    public ActionForward deleteDEAPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            PostingRulesManager.deleteDEAPostingRule(getPostingRule(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    public ActionForward deleteDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            PostingRulesManager.deletePostingRule(getPostingRule(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);

    }

    public ActionForward showInsurancePostingRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("postingRules", getInsurancePostingRules());

        return mapping.findForward("showInsurancePostingRules");
    }

    private Set<PostingRule> getInsurancePostingRules() {
        return Bennu.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate()
                .getAllPostingRulesFor(EventType.INSURANCE);
    }

    public ActionForward prepareEditInsurancePR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("postingRule", getPostingRule(request));

        return mapping.findForward("editInsurancePR");
    }

    public ActionForward prepareEditInsurancePRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("postingRule", getRenderedObject("postingRuleEditor"));

        return mapping.findForward("editInsurancePR");
    }

    public ActionForward manageGraduationRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final List<DegreeCurricularPlan> degreeCurricularPlans =
                DegreeCurricularPlan.readByDegreeTypesAndState(DegreeType.getDegreeTypesFor(AdministrativeOfficeType.DEGREE),
                        null);
        DegreeCurricularPlan empty = DegreeCurricularPlan.readEmptyDegreeCurricularPlan();

        if (empty != null) {
            degreeCurricularPlans.add(DegreeCurricularPlan.readEmptyDegreeCurricularPlan());
        }

        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);

        return mapping.findForward("showGraduationDegreeCurricularPlans");
    }

    public ActionForward showPaymentPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PostingRulesManagementForm postingRulesManagementForm = (PostingRulesManagementForm) form;

        if (postingRulesManagementForm.getExecutionYearId() == null) {
            postingRulesManagementForm.setExecutionYearId(ExecutionYear.readCurrentExecutionYear().getExternalId());
        }

        setRequestAttributesToShowPaymentPlans(request, postingRulesManagementForm);

        return mapping.findForward("showPaymentPlans");
    }

    public ActionForward changeExecutionYearForPaymentPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        setRequestAttributesToShowPaymentPlans(request, (PostingRulesManagementForm) form);

        return mapping.findForward("showPaymentPlans");
    }

    private void setRequestAttributesToShowPaymentPlans(HttpServletRequest request, final PostingRulesManagementForm form) {

        final ExecutionYear executionYear = FenixFramework.getDomainObject(form.getExecutionYearId());

        request.setAttribute("executionYears", new ArrayList<ExecutionYear>(rootDomainObject.getExecutionYearsSet()));
        request.setAttribute("paymentPlans", getDegreeCurricularPlan(request).getServiceAgreementTemplate()
                .getGratuityPaymentPlansFor(executionYear));
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
    }

    public ActionForward prepareCreatePaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentPlanBean paymentPlanBean = new PaymentPlanBean(ExecutionYear.readCurrentExecutionYear());
        request.setAttribute("paymentPlanEditor", paymentPlanBean);
        request.setAttribute("installmentEditor", new InstallmentBean(paymentPlanBean));

        return mapping.findForward("createPaymentPlan");
    }

    public ActionForward prepareCreatePaymentPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());

        return mapping.findForward("createPaymentPlan");
    }

    public ActionForward changeExecutionYearForPaymentPlanCreate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());

        invalidatePaymentPlanViewStates();

        return mapping.findForward("createPaymentPlan");
    }

    public ActionForward addInstallmentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());

        return mapping.findForward("createPaymentPlan");
    }

    public ActionForward addInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!getInstallment().hasRequiredInformation()) {
            addActionMessage("installment", request,
                    "label.payments.postingRules.paymentPlan.information.to.create.installment.is.all.required");

            return addInstallmentInvalid(mapping, form, request, response);

        }

        getPaymentPlanBean().addInstallment(getInstallment());
        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", new InstallmentBean(getPaymentPlanBean()));
        invalidatePaymentPlanViewStates();

        return mapping.findForward("createPaymentPlan");
    }

    private void invalidatePaymentPlanViewStates() {
        RenderUtils.invalidateViewState("paymentPlanEditor");
        RenderUtils.invalidateViewState("installmentEditor");
        RenderUtils.invalidateViewState("installmentsEditor");
    }

    private InstallmentBean getInstallment() {
        return getRenderedObject("installmentEditor");
    }

    public ActionForward removeInstallments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        getPaymentPlanBean().removeSelectedInstallments();

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", new InstallmentBean(getPaymentPlanBean()));

        invalidatePaymentPlanViewStates();

        return mapping.findForward("createPaymentPlan");
    }

    public ActionForward createPaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            GratuityPaymentPlanManager.create(getPaymentPlanBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
            request.setAttribute("installmentEditor", getInstallment());

            return mapping.findForward("createPaymentPlan");
        }

        return manageGraduationRules(mapping, form, request, response);
    }

    private PaymentPlanBean getPaymentPlanBean() {
        return getRenderedObject("paymentPlanEditor");
    }

    private PaymentPlan getPaymentPlan(final HttpServletRequest request) {
        return getDomainObject(request, "paymentPlanId");
    }

    public ActionForward deletePaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            GratuityPaymentPlanManager.delete(getPaymentPlan(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        setRequestAttributesToShowPaymentPlans(request, (PostingRulesManagementForm) form);

        return mapping.findForward("showPaymentPlans");

    }

    public ActionForward prepareCreateGraduationGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createPostingRuleBean", new CreateGratuityPostingRuleBean());

        return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward prepareCreateGraduationGratuityPRInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createPostingRuleBean", getRenderedObject("createPostingRuleBean"));

        return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward prepareCreateGraduationGratuityPRPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final Object object = getRenderedObject("createPostingRuleBean");
        RenderUtils.invalidateViewState();

        request.setAttribute("createPostingRuleBean", object);
        return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward createGraduationGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateGratuityPostingRuleBean bean = getRenderedObject("createPostingRuleBean");

        try {
            PostingRulesManager.createGraduationGratuityPostingRule(bean);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("createPostingRuleBean", bean);

            return mapping.findForward("createGraduationGratuityPR");
        }

        return manageGraduationRules(mapping, form, request, response);

    }

    public ActionForward showPostGraduationDegreeCurricularPlanPostingRules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("allowCreateGratuityPR", allowCreateGratuityPR(degreeCurricularPlan));
        request.setAttribute("allowCreateStandaloneGratuityPR", allowCreateStandaloneGratuityPR(degreeCurricularPlan));
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("showPostGraduationDegreeCurricularPlanPostingRules");
    }

    private static final List<DegreeType> CREATE_GRATUITIES_DEGREE_TYPES = Arrays.asList(new DegreeType[] {
            DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA, DegreeType.BOLONHA_SPECIALIZATION_DEGREE,
            DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA });

    private boolean allowCreateGratuityPR(final DegreeCurricularPlan degreeCurricularPlan) {
        if (!CREATE_GRATUITIES_DEGREE_TYPES.contains(degreeCurricularPlan.getDegreeType())) {
            return false;
        }

        return !degreeCurricularPlan.getServiceAgreementTemplate().hasActivePostingRuleFor(EventType.GRATUITY);
    }

    private boolean allowCreateStandaloneGratuityPR(final DegreeCurricularPlan degreeCurricularPlan) {
        if (!CREATE_GRATUITIES_DEGREE_TYPES.contains(degreeCurricularPlan.getDegreeType())) {
            return false;
        }

        boolean activePRStandalone =
                degreeCurricularPlan.getServiceAgreementTemplate().hasActivePostingRuleFor(
                        EventType.STANDALONE_ENROLMENT_GRATUITY);
        boolean activePRGratuity = degreeCurricularPlan.getServiceAgreementTemplate().hasActivePostingRuleFor(EventType.GRATUITY);
        return !activePRStandalone && activePRGratuity;
    }

    public ActionForward showGraduationDegreeCurricularPlanPostingRules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

        return mapping.findForward("showGraduationDegreeCurricularPlanPostingRules");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final CreateGratuityPostingRuleBean bean = new CreateStandaloneEnrolmentGratuityPRBean();
        bean.setRule(StandaloneEnrolmentGratuityPR.class);

        request.setAttribute("createPostingRuleBean", bean);

        return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPRInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createPostingRuleBean", getRenderedObject("createPostingRuleBean"));
        return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPRPostback(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        final Object object = getRenderedObject("createPostingRuleBean");
        RenderUtils.invalidateViewState();
        request.setAttribute("createPostingRuleBean", object);

        return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward createGraduationStandaloneEnrolmentGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final CreateStandaloneEnrolmentGratuityPRBean bean = getRenderedObject("createPostingRuleBean");

        try {
            PostingRulesManager.createStandaloneGraduationGratuityPostingRule(bean);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("createPostingRuleBean", bean);

            return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");

        }

        return manageGraduationRules(mapping, form, request, response);

    }

    public ActionForward deleteGraduationDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            PostingRulesManager.deletePostingRule(getPostingRule(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

        }

        request.setAttribute("degreeCurricularPlanId", getDegreeCurricularPlan(request).getExternalId());

        return showGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    /* DFA Gratuity Posting Rule */

    public ActionForward prepareCreateDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("createDFAGratuityPostingRuleBean",
                new CreateDFAGratuityPostingRuleBean(degreeCurricularPlan.getServiceAgreementTemplate()));

        return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward prepareCreateDFAGratuityPRTypeChosen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createDFAGratuityPostingRuleBean",
                getObjectFromViewState("createDFAGratuityPostingRuleBean.chooseType"));

        RenderUtils.invalidateViewState();

        return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward prepareCreateDFAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

        return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward createDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            PostingRulesManager.createDFAGratuityPostingRule(getCreateDFAGratuityPostingRuleBeanFromRequest());
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

            return mapping.findForward("createDFAGratuityPR");
        }

        request.setAttribute("degreeCurricularPlanId", getCreateDFAGratuityPostingRuleBeanFromRequest().getDegreeCurricularPlan()
                .getExternalId());

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    private CreateDFAGratuityPostingRuleBean getCreateDFAGratuityPostingRuleBeanFromRequest() {
        return (CreateDFAGratuityPostingRuleBean) getObjectFromViewState("createDFAGratuityPostingRuleBean");
    }

    /* Specialization Degree Gratuity Posting Rule */

    public ActionForward prepareCreateSpecializationDegreeGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("createSpecializationDegreeGratuityPostingRuleBean",
                new CreateSpecializationDegreeGratuityPostingRuleBean(degreeCurricularPlan.getServiceAgreementTemplate()));

        return mapping.findForward("createSpecializationDegreeGratuityPR");
    }

    public ActionForward prepareCreateSpecializationDegreeGratuityPRTypeChosen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createSpecializationDegreeGratuityPostingRuleBean",
                getObjectFromViewState("createSpecializationDegreeGratuityPostingRuleBean.chooseType"));

        RenderUtils.invalidateViewState();

        return mapping.findForward("createSpecializationDegreeGratuityPR");
    }

    public ActionForward prepareCreateSpecializationDegreeGratuityPRInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("createSpecializationDegreeGratuityPostingRuleBean",
                getCreateSpecializationDegreeGratuityPostingRuleBeanFromRequest());
        return mapping.findForward("createSpecializationDegreeGratuityPR");
    }

    public ActionForward createSpecializationDegreeGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            PostingRulesManager
                    .createSpecializationDegreeGratuityPostingRule(getCreateSpecializationDegreeGratuityPostingRuleBeanFromRequest());
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            request.setAttribute("createSpecializationDegreeGratuityPostingRuleBean",
                    getCreateSpecializationDegreeGratuityPostingRuleBeanFromRequest());

            return mapping.findForward("createSpecializationDegreeGratuityPR");
        }

        request.setAttribute("degreeCurricularPlanId", getCreateSpecializationDegreeGratuityPostingRuleBeanFromRequest()
                .getDegreeCurricularPlan().getExternalId());

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    /* Gratuities for DEAs */
    public ActionForward prepareCreateDEAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final PaymentPlanBean paymentPlanBean = new PaymentPlanBean(ExecutionYear.readCurrentExecutionYear());

        paymentPlanBean.setMain(true);
        paymentPlanBean.setForFirstTimeInstitutionStudents(false);
        paymentPlanBean.setForPartialRegime(false);
        paymentPlanBean.setForStudentEnroledOnSecondSemesterOnly(false);
        paymentPlanBean.setDegreeCurricularPlans(Collections.singletonList(degreeCurricularPlan));

        request.setAttribute("paymentPlanEditor", paymentPlanBean);
        InstallmentBean installmentBean = new InstallmentBean(paymentPlanBean);

        installmentBean.setPenaltyAppliable(false);
        request.setAttribute("installmentEditor", installmentBean);

        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("createDEAGratuityPR");
    }

    /* Gratuities for DEAs */
    public ActionForward prepareCreateDEAStandaloneEnrolmentGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final PaymentPlanBean paymentPlanBean = new PaymentPlanBean(ExecutionYear.readCurrentExecutionYear());

        paymentPlanBean.setMain(true);
        paymentPlanBean.setForFirstTimeInstitutionStudents(false);
        paymentPlanBean.setForPartialRegime(false);
        paymentPlanBean.setForStudentEnroledOnSecondSemesterOnly(false);
        paymentPlanBean.setDegreeCurricularPlans(Collections.singletonList(degreeCurricularPlan));

        request.setAttribute("paymentPlanEditor", paymentPlanBean);
        StandaloneInstallmentBean installmentBean = new StandaloneInstallmentBean(paymentPlanBean);

        installmentBean.setPenaltyAppliable(false);
        request.setAttribute("installmentEditor", installmentBean);

        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("createDEAStandaloneEnrolmentGratuityPR");
    }

    public ActionForward createDEAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        InstallmentBean installment = getInstallment();
        if (!installment.hasRequiredInformation()) {
            addActionMessage("installment", request,
                    "label.payments.postingRules.paymentPlan.information.to.create.installment.is.all.required");

            return createDEAGratuityPRInvalid(mapping, form, request, response);
        }

        PaymentPlanBean paymentPlanBean = getPaymentPlanBean();
        paymentPlanBean.addInstallment(installment);

        try {
            PostingRulesManager.createDEAGratuityPostingRule(getPaymentPlanBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            return createDEAGratuityPRInvalid(mapping, form, request, response);
        }

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    public ActionForward createDEAStandaloneGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        InstallmentBean installment = getInstallment();

        StandaloneInstallmentBean standaloneInstallment = (StandaloneInstallmentBean) installment;

        if (!installment.hasRequiredInformation()) {
            addActionMessage("installment", request,
                    "label.payments.postingRules.paymentPlan.information.to.create.installment.is.all.required");

            return createDEAGratuityPRInvalid(mapping, form, request, response);
        }

        try {
            PostingRulesManager.createDEAStandaloneGratuityPostingRule(standaloneInstallment, degreeCurricularPlan);
        } catch (DomainException e) {
            PaymentPlanBean paymentPlanEditor = new PaymentPlanBean(ExecutionYear.readCurrentExecutionYear());
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
            request.setAttribute("paymentPlanEditor", paymentPlanEditor);
            request.setAttribute("installmentEditor", installment);

            addActionMessage(request, e.getKey(), e.getArgs());

            //return createDEAGratuityPRInvalid(mapping, form, request, response);
            return mapping.findForward("createDEAGratuityPR");
        }

        return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    public ActionForward changeExecutionYearForDEAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        RenderUtils.invalidateViewState();
        return mapping.findForward("createDEAGratuityPR");
    }

    public ActionForward changeExecutionYearForDEAStandaloneGratuityPR(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        RenderUtils.invalidateViewState();
        return mapping.findForward("createDEAStandaloneEnrolmentGratuityPR");
    }

    public ActionForward createDEAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
        request.setAttribute("installmentEditor", getInstallment());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("createDEAGratuityPR");
    }

    private CreateSpecializationDegreeGratuityPostingRuleBean getCreateSpecializationDegreeGratuityPostingRuleBeanFromRequest() {
        return (CreateSpecializationDegreeGratuityPostingRuleBean) getObjectFromViewState("createSpecializationDegreeGratuityPostingRuleBean");
    }

    public ActionForward showFCTScolarshipPostingRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        List<PostingRule> list = new ArrayList<PostingRule>();
        for (PostingRule postingRule : Bennu.getInstance().getPostingRulesSet()) {
            if (postingRule instanceof ExternalScholarshipPhdGratuityContribuitionPR) {
                list.add(postingRule);
            }
        }
        request.setAttribute("list", list);
        return mapping.findForward("showFCTScolarshipPostingRules");
    }

    public static class FctScolarshipPostingRuleBean implements Serializable {
        DateTime startDate = new DateTime();
        DateTime endDate;
        String externalId;

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public DateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(DateTime startDate) {
            this.startDate = startDate;
        }

        public DateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(DateTime endDate) {
            this.endDate = endDate;
        }
    }

    public ActionForward prepareAddFCTPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FctScolarshipPostingRuleBean bean = new FctScolarshipPostingRuleBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareAddFCTPostingRule");
    }

    @Atomic
    public void createFCTScolarshipPostingRule(FctScolarshipPostingRuleBean bean) {
        ExternalScholarshipPhdGratuityContribuitionPR postingRule =
                new ExternalScholarshipPhdGratuityContribuitionPR(bean.getStartDate(), bean.getEndDate(), AdministrativeOffice
                        .readMasterDegreeAdministrativeOffice().getServiceAgreementTemplate());
        postingRule.setRootDomainObject(Bennu.getInstance());
    }

    public ActionForward addFCTScolarshipPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FctScolarshipPostingRuleBean bean = getRenderedObject("bean");
        createFCTScolarshipPostingRule(bean);
        return showFCTScolarshipPostingRules(mapping, form, request, response);
    }

    public ActionForward prepareEditFCTScolarshipPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExternalScholarshipPhdGratuityContribuitionPR postingRule =
                (ExternalScholarshipPhdGratuityContribuitionPR) FenixFramework.getDomainObject(request
                        .getParameter("postingRule"));
        FctScolarshipPostingRuleBean bean = new FctScolarshipPostingRuleBean();

        bean.setStartDate(postingRule.getStartDate());
        bean.setEndDate(postingRule.getEndDate());
        bean.setExternalId(postingRule.getExternalId());
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareEditFCTScolarshipPostingRule");
    }

    @Atomic
    public ActionForward editFCTScolarshipPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FctScolarshipPostingRuleBean bean = getRenderedObject("bean");
        ExternalScholarshipPhdGratuityContribuitionPR postingRule =
                (ExternalScholarshipPhdGratuityContribuitionPR) FenixFramework.getDomainObject(bean.getExternalId());

        postingRule.setStartDate(bean.getStartDate());
        postingRule.setEndDate(bean.getEndDate());

        return showFCTScolarshipPostingRules(mapping, form, request, response);
    }

    @Atomic
    public ActionForward deleteFCTScolarshipPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExternalScholarshipPhdGratuityContribuitionPR postingRule =
                (ExternalScholarshipPhdGratuityContribuitionPR) FenixFramework.getDomainObject(request
                        .getParameter("postingRule"));
        postingRule.delete();

        return showFCTScolarshipPostingRules(mapping, form, request, response);
    }

    public ActionForward prepareEditInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PaymentPlan paymentPlan = getDomainObject(request, "paymentPlanId");
        Installment installment = getDomainObject(request, "installmentId");

        request.setAttribute("paymentPlan", paymentPlan);
        request.setAttribute("installment", installment);
        request.setAttribute("installmentBean", new InstallmentBean(installment));

        return mapping.findForward("editInstallment");
    }

    public ActionForward editInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Installment installment = getDomainObject(request, "installmentId");
            InstallmentBean bean = getRenderedObject("installmentBean");

            InstallmentService.edit(installment, bean);
        } catch (final DomainException e) {
            addErrorMessage(request, "error", e.getKey(), new String[] {});

            return editInstallmentInvalid(mapping, form, request, response);
        }

        return showPaymentPlans(mapping, form, request, response);
    }

    public ActionForward editInstallmentInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        PaymentPlan paymentPlan = getDomainObject(request, "paymentPlanId");
        Installment installment = getDomainObject(request, "installmentId");
        InstallmentBean bean = getRenderedObject("installmentBean");

        request.setAttribute("paymentPlan", paymentPlan);
        request.setAttribute("installment", installment);
        request.setAttribute("installmentBean", bean);

        return mapping.findForward("editInstallment");
    }
}