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
/*
 * Created on 2003/07/16
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.AlterExecutionPeriodState;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerExecutionsApp;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Crus & Sara Ribeiro
 */
@StrutsFunctionality(app = ManagerExecutionsApp.class, path = "manage-periods", titleKey = "title.execution.periods")
@Mapping(module = "manager", path = "/manageExecutionPeriods")
@Forwards({ @Forward(name = "Manage", path = "/manager/manageExecutionPeriods_bd.jsp"),
        @Forward(name = "EditExecutionPeriod", path = "/manager/editExecutionPeriodDates.jsp") })
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        List<ExecutionSemester> periods = new ArrayList<>(Bennu.getInstance().getExecutionPeriodsSet());
        Collections.sort(periods);
        request.setAttribute("periods", periods);
        return mapping.findForward("Manage");
    }

    public ActionForward alterExecutionPeriodState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String year = request.getParameter("year");
        final Integer semester = new Integer(request.getParameter("semester"));
        final String periodStateToSet = request.getParameter("periodState");
        final PeriodState periodState = new PeriodState(periodStateToSet);

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
            ExecutionSemester executionSemester = (ExecutionSemester) FenixFramework.getDomainObject(externalId);
            request.setAttribute("executionPeriod", executionSemester);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        return mapping.findForward("EditExecutionPeriod");
    }
}