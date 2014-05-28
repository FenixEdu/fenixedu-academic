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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPR;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPaymentPeriod;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPriceQuirk;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/phdPostingRules", module = "manager",
        formBeanClass = PostingRulesManagementDA.PostingRulesManagementForm.class, functionality = PostingRulesManagementDA.class)
@Forwards({
        @Forward(name = "showPhdProgramPostingRules",
                path = "/manager/payments/postingRules/management/phd/showPhdProgramPostingRules.jsp"),
        @Forward(name = "viewPostingRuleDetails",
                path = "/manager/payments/postingRules/management/phd/viewPostingRuleDetails.jsp"),
        @Forward(name = "editPhdProgramPostingRule",
                path = "/manager/payments/postingRules/management/phd/editPhdProgramPostingRule.jsp"),
        @Forward(name = "addPhdProgramPostingRule",
                path = "/manager/payments/postingRules/management/phd/addPhdProgramPostingRule.jsp") })
public class PhdPostingRulesManagementDA extends PostingRulesManagementDA {

    public static final Integer STANDARD_GRATUITY = 3000;
    public static final Double STANDARD_FINE_RATE = 0.01;

    public static class CreateGratuityPhdPRQuickBean implements Serializable {
        Integer year;
        Integer gratuity;

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getGratuity() {
            return gratuity;
        }

        public void setGratuity(Integer gratuity) {
            this.gratuity = gratuity;
        }
    }

    public static class CreateGratuityPhdPRPeriodBean implements Serializable {
        Partial periodStartDate;
        Partial periodEndDate;
        Partial limitePaymentDay;

        public Partial getPeriodStartDate() {
            return periodStartDate;
        }

        public void setPeriodStartDate(Partial periodStartDate) {
            this.periodStartDate = periodStartDate;
        }

        public Partial getPeriodEndDate() {
            return periodEndDate;
        }

        public void setPeriodEndDate(Partial periodEndDate) {
            this.periodEndDate = periodEndDate;
        }

        public Partial getLimitePaymentDay() {
            return limitePaymentDay;
        }

        public void setLimitePaymentDay(Partial limitePaymentDay) {
            this.limitePaymentDay = limitePaymentDay;
        }

        public String getPeriodStartString() {
            return periodStartDate.toString("d/M");
        }

        public String getPeriodEndString() {
            return periodEndDate.toString("d/M");
        }

        public String getLimitePaymentDayString() {
            return limitePaymentDay.toString("d/M");
        }

        public DateTime getStartAsDateTime() {

            return new DateTime(new DateTime().getYear(), getPeriodStartDate().get(DateTimeFieldType.monthOfYear()),
                    getPeriodStartDate().get(DateTimeFieldType.dayOfMonth()), 0, 0, 0, 0);
        }

        public DateTime getEndAsDateTime() {
            return new DateTime(new DateTime().getYear(), getPeriodEndDate().get(DateTimeFieldType.monthOfYear()),
                    getPeriodEndDate().get(DateTimeFieldType.dayOfMonth()), 0, 0, 0, 0);
        }

        private Interval toInterval() {
            DateTime start = getStartAsDateTime();
            DateTime end = getEndAsDateTime();
            return new Interval(start, end);
        }
    }

    public static class CreateGratuityPhdBean implements Serializable {
        DateTime startDate = new DateTime();
        DateTime endDate;
        Integer gratuity = STANDARD_GRATUITY;
        Double fineRate = STANDARD_FINE_RATE;
        private List<CreateGratuityPhdPRPeriodBean> periods = new ArrayList<CreateGratuityPhdPRPeriodBean>();
        private List<CreateGratuityPhdPRQuickBean> exceptions = new ArrayList<CreateGratuityPhdPRQuickBean>();

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

        public Integer getGratuity() {
            return gratuity;
        }

        public void setGratuity(Integer gratuity) {
            this.gratuity = gratuity;
        }

        public Double getFineRate() {
            return fineRate;
        }

        public void setFineRate(Double fineRate) {
            this.fineRate = fineRate;
        }

        public List<CreateGratuityPhdPRPeriodBean> getPeriods() {
            return periods;
        }

        public void setPeriods(List<CreateGratuityPhdPRPeriodBean> periods) {
            this.periods = periods;
        }

        public List<CreateGratuityPhdPRQuickBean> getExceptions() {
            return exceptions;
        }

        public void setExceptions(List<CreateGratuityPhdPRQuickBean> exceptions) {
            this.exceptions = exceptions;
        }
    }

    /* Phd Programs */

    public ActionForward showPhdProgramPostingRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        request.setAttribute("phdProgram", phdProgram);

        return mapping.findForward("showPhdProgramPostingRules");
    }

    @Override
    public ActionForward viewPostingRuleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
        request.setAttribute("postingRule", getPostingRule(request));

        return mapping.findForward("viewPostingRuleDetails");
    }

    public ActionForward prepareAddGratuityPhdPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        final CreateGratuityPhdPRPeriodBean period = getRenderedObject("period");
        final CreateGratuityPhdPRQuickBean quirks = getRenderedObject("quirks");
        final CreateGratuityPhdBean bean = getRenderedObject("bean");
        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("bean", (bean == null) ? new CreateGratuityPhdBean() : bean);
        request.setAttribute("period", (period == null) ? new CreateGratuityPhdPRPeriodBean() : period);
        request.setAttribute("quirks", (quirks == null) ? new CreateGratuityPhdPRQuickBean() : quirks);
        return mapping.findForward("addPhdProgramPostingRule");
    }

    public ActionForward addPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final CreateGratuityPhdPRPeriodBean period = getRenderedObject("period");
        final CreateGratuityPhdBean bean = getRenderedObject("bean");
        final CreateGratuityPhdPRQuickBean quirks = getRenderedObject("quirks");
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");

        if (period.periodEndDate == null || period.periodStartDate == null || period.limitePaymentDay == null) {
            addErrorMessage(request, "bean", "error.missing.field.in.period.form");
            return prepareAddGratuityPhdPostingRule(mapping, form, request, response);
        }

        Interval created = period.toInterval();
        if (period.getStartAsDateTime().isAfter(period.getEndAsDateTime())) {
            addErrorMessage(request, "bean", "error.end.before.start");
            return backToAddPeriod(mapping, request, period, bean, phdProgram, quirks);
        }

        for (CreateGratuityPhdPRPeriodBean previousPeriod : bean.getPeriods()) {
            if (previousPeriod.toInterval().overlap(created) != null) {
                addErrorMessage(request, "bean", "error.intervals.overlap");
                return backToAddPeriod(mapping, request, period, bean, phdProgram, quirks);
            }
        }

        bean.getPeriods().add(period);

        RenderUtils.invalidateViewState("phdProgram");
        request.setAttribute("phdProgram", phdProgram);
        RenderUtils.invalidateViewState("bean");
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("period");
        request.setAttribute("period", new CreateGratuityPhdPRPeriodBean());
        return mapping.findForward("addPhdProgramPostingRule");
    }

    public ActionForward removePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final CreateGratuityPhdBean bean = getRenderedObject("bean");
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        final int index = Integer.parseInt(request.getParameter("periodToRemove"));

        bean.periods.remove(index);

        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("bean", bean);
        request.setAttribute("period", new CreateGratuityPhdPRPeriodBean());
        return mapping.findForward("addPhdProgramPostingRule");
    }

    public ActionForward addQuirk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        final CreateGratuityPhdPRPeriodBean period = getRenderedObject("period");
        final CreateGratuityPhdPRQuickBean quirks = getRenderedObject("quirks");
        final CreateGratuityPhdBean bean = getRenderedObject("bean");

        bean.getExceptions().add(quirks);

        return backToAddPeriod(mapping, request, period, bean, phdProgram, new CreateGratuityPhdPRQuickBean());
    }

    public ActionForward removeQuirk(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        final CreateGratuityPhdPRPeriodBean period = getRenderedObject("period");
        final CreateGratuityPhdPRQuickBean quirks = getRenderedObject("quirks");
        final CreateGratuityPhdBean bean = getRenderedObject("bean");
        final int index = Integer.parseInt(request.getParameter("quirkToRemove"));
        bean.getExceptions().remove(index);
        return backToAddPeriod(mapping, request, period, bean, phdProgram, quirks);
    }

    private ActionForward backToAddPeriod(ActionMapping mapping, HttpServletRequest request,
            final CreateGratuityPhdPRPeriodBean period, final CreateGratuityPhdBean bean, final PhdProgram phdProgram,
            Object quirks) {
        RenderUtils.invalidateViewState("phdProgram");
        request.setAttribute("phdProgram", phdProgram);
        RenderUtils.invalidateViewState("bean");
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("period");
        request.setAttribute("period", period);
        RenderUtils.invalidateViewState("period");
        request.setAttribute("quirks", quirks);
        return mapping.findForward("addPhdProgramPostingRule");
    }

    @Atomic
    private void makeGratuityPostingRule(CreateGratuityPhdBean bean, PhdProgram phdProgram) {
        if (bean.getPeriods().size() == 0) {
            throw new RuntimeException("error.empty.periods");
        }
        PhdGratuityPR postingRule =
                new PhdGratuityPR(bean.getStartDate(), bean.getEndDate(), phdProgram.getServiceAgreementTemplate(), new Money(
                        bean.getGratuity()), bean.getFineRate());

        for (CreateGratuityPhdPRPeriodBean periodBean : bean.getPeriods()) {
            PhdGratuityPaymentPeriod period =
                    new PhdGratuityPaymentPeriod(periodBean.getPeriodStartDate(), periodBean.getPeriodEndDate(),
                            periodBean.getLimitePaymentDay());
            postingRule.addPhdGratuityPaymentPeriods(period);
            period.setRootDomainObject(Bennu.getInstance());
        }

        for (CreateGratuityPhdPRQuickBean quirkBean : bean.getExceptions()) {
            PhdGratuityPriceQuirk quirk = new PhdGratuityPriceQuirk(quirkBean.getYear(), new Money(quirkBean.getGratuity()));
            postingRule.addPhdGratuityPriceQuirks(quirk);
            quirk.setRootDomainObject(Bennu.getInstance());
        }
        postingRule.setRootDomainObject(Bennu.getInstance());

    }

    public ActionForward addGratuityPhdPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        CreateGratuityPhdBean bean = (CreateGratuityPhdBean) getRenderedObject("bean");
        try {
            makeGratuityPostingRule(bean, phdProgram);
        } catch (DomainException e) {
            addErrorMessage(request, "bean", e.getMessage());
            return prepareAddGratuityPhdPostingRule(mapping, form, request, response);
        } catch (RuntimeException e) {
            addErrorMessage(request, "bean", e.getMessage());
            return prepareAddGratuityPhdPostingRule(mapping, form, request, response);
        }
        return showPhdProgramPostingRules(mapping, form, request, response);
    }

    @Override
    protected PostingRule getPostingRule(HttpServletRequest request) {
        return getDomainObject(request, "postingRuleId");
    }

    public ActionForward prepareEditPhdProgramPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PostingRule postingRule = getPostingRule(request);

        request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
        request.setAttribute("postingRule", postingRule);

        return mapping.findForward("editPhdProgramPostingRule");
    }

    public ActionForward editPhdProgramPostingRuleInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
        request.setAttribute("postingRule", getRenderedObject("postingRule"));

        return mapping.findForward("editPhdProgramPostingRule");
    }

}
