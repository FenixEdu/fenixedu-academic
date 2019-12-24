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
/*
 * Created on 2003/07/16
 *  
 */
package org.fenixedu.academic.ui.struts.action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.manager.AlterExecutionPeriodState;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerExecutionsApp;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Crus &amp; Sara Ribeiro
 */
@StrutsFunctionality(app = ManagerExecutionsApp.class, path = "manage-periods", titleKey = "title.execution.periods")
@Mapping(module = "manager", path = "/manageExecutionPeriods")
@Forwards({ @Forward(name = "Manage", path = "/manager/manageExecutionPeriods_bd.jsp"),
        @Forward(name = "EditExecutionPeriod", path = "/manager/editExecutionPeriodDates.jsp") })
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        List<ExecutionInterval> periods = new ArrayList<>(Bennu.getInstance().getExecutionPeriodsSet());
        Collections.sort(periods);
        request.setAttribute("periods", periods);
        return mapping.findForward("Manage");
    }

    public ActionForward alterExecutionPeriodState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String year = request.getParameter("year");
        final Integer semester = new Integer(request.getParameter("semester"));
        final String periodStateToSet = request.getParameter("periodState");
        final PeriodState periodState = PeriodState.valueOf(periodStateToSet);

        try {
            AlterExecutionPeriodState.run(year, semester, periodState);
        } catch (InvalidArgumentsServiceException ex) {
            throw new FenixActionException("errors.nonExisting.executionPeriod", ex);
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final String externalId = request.getParameter("executionPeriodID");
        try {
            ExecutionInterval executionSemester = (ExecutionInterval) FenixFramework.getDomainObject(externalId);
            request.setAttribute("executionPeriod", executionSemester);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        return mapping.findForward("EditExecutionPeriod");
    }
}