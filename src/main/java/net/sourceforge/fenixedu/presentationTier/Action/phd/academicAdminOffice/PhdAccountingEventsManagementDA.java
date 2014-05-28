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
package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.events.AccountingEventCreateBean;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithInvocationResult;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;
import net.sourceforge.fenixedu.util.InvocationResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdAccountingEventsManagement", module = "academicAdministration",
        functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "chooseEventType", path = "/phd/academicAdminOffice/payments/chooseEventType.jsp"),
        @Forward(name = "chooseYear", path = "/phd/academicAdminOffice/payments/chooseYear.jsp"),
        @Forward(name = "chooseYear2", path = "/phd/academicAdminOffice/payments/chooseYear2.jsp"),
        @Forward(name = "createInsuranceEvent", path = "/phd/academicAdminOffice/payments/createInsuranceEvent.jsp") })
public class PhdAccountingEventsManagementDA extends PhdProcessDA {

    public static class PhdGratuityCreationInformation implements Serializable {
        private int year = new DateTime().getYear();

        public void setYear(int year) {
            this.year = year;
        }

        public int getYear() {
            return year;
        }
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
        return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("chooseEventType");
    }

    public ActionForward createPhdRegistrationFee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            PhdRegistrationFee.create(getProcess(request));
        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }

        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateInsuranceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("eventBean", new AccountingEventCreateBean());
        return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward updatePrepareCreateGratuityEvent(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("yearBean", request.getAttribute("yearBean"));
        return mapping.findForward("chooseYear");
    }

    public ActionForward prepareCreateGratuityEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("yearBean", new PhdGratuityCreationInformation());
        return mapping.findForward("chooseYear");
    }

    public ActionForward createGratuityEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            PhdGratuityCreationInformation renderedObject = (PhdGratuityCreationInformation) getRenderedObject("yearBean");
            for (PhdGratuityEvent event : getProcess(request).getPhdGratuityEvents()) {
                if (event.getYear().intValue() == ((PhdGratuityCreationInformation) getRenderedObject("yearBean")).getYear()
                        && event.isOpen()) {
                    throw new DomainException("already.has.phd.gratuity.for.that.year");
                }
            }

            PhdIndividualProgramProcess process = getProcess(request);

            TreeSet<PhdProgramProcessState> orderdStates =
                    new TreeSet<PhdProgramProcessState>(new Comparator<PhdProgramProcessState>() {

                        @Override
                        public int compare(PhdProgramProcessState o1, PhdProgramProcessState o2) {
                            return o1.getStateDate().compareTo(o2.getStateDate());
                        }
                    });

            orderdStates.addAll(process.getStates());

            int lastOpenYear = new DateTime().getYear();

            int year = renderedObject.getYear();
            boolean yearWithinWorkingDevelopmentPeriod = false;
            for (PhdProgramProcessState state : process.getStates()) {
                if (state.getType().equals(PhdIndividualProgramProcessState.WORK_DEVELOPMENT)) {
                    if (state.getStateDate().getYear() <= year && year <= lastOpenYear) {
                        yearWithinWorkingDevelopmentPeriod = true;
                        break;
                    }
                }
                lastOpenYear = state.getStateDate().getYear();

            }
            if (!yearWithinWorkingDevelopmentPeriod) {
                throw new FenixActionException("error.chosen.year.not.within.working.period");
            }

            PhdGratuityEvent.create(getProcess(request), year, process.getWhenFormalizedRegistration().toDateTimeAtMidnight());

            return prepare(mapping, actionForm, request, response);

        } catch (DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        } catch (FenixActionException e) {
            addErrorMessage(request, e.getMessage());
        }

        return prepareCreateGratuityEvent(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateInsuranceEventInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("eventBean", getRenderedObject("eventBean"));
        return mapping.findForward("createInsuranceEvent");
    }

    public ActionForward createPhdThesisRequestFee(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {

        try {
            getProcess(request).getThesisProcess().createRequestFee();
            addActionMessage("success", request, "message.phd.accounting.events.create.thesis.request.fee.created.with.success");
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward createInsuranceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            final AccountingEventCreateBean bean = getRenderedObject("eventBean");
            final PhdIndividualProgramProcess process = getProcess(request);

            if (process.getExecutionYear().isAfter(bean.getExecutionYear())) {
                addActionMessage("error", request, "error.phd.accounting.events.insurance.invalid.execution.period", process
                        .getExecutionYear().getQualifiedName());
                return prepareCreateInsuranceEvent(mapping, actionForm, request, response);
            }

            final InvocationResult result =
                    new AccountingEventsManager().createInsuranceEvent(process.getPerson(), bean.getExecutionYear());

            if (result.isSuccess()) {
                addActionMessage("success", request, "message.phd.accounting.events.insurance.created.with.success");
                return prepare(mapping, actionForm, request, response);
            } else {
                addActionMessages("error", request, result.getMessages());
            }

        } catch (DomainExceptionWithInvocationResult e) {
            addActionMessages("error", request, e.getInvocationResult().getMessages());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        return prepareCreateInsuranceEvent(mapping, actionForm, request, response);
    }

}
