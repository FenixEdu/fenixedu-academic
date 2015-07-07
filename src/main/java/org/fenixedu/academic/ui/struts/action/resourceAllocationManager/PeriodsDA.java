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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.resourceAllocationManager.PeriodsManagementBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMPeriodsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author Luis Cruz
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = RAMPeriodsApp.class, path = "manage", titleKey = "label.occupation.period.management")
@Mapping(path = "/periods", module = "resourceAllocationManager")
@Forwards(@Forward(name = "managePeriods", path = "/resourceAllocationManager/periods/managePeriods.jsp"))
public class PeriodsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward managePeriods(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        PeriodsManagementBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (bean == null) {
            bean = new PeriodsManagementBean();
        } else {
            try {
                updateBean(request, bean);
            } catch (DomainException e) {
                addActionMessage("error", request, e.getKey());
            }
        }

        request.setAttribute("managementBean", bean);

        return mapping.findForward("managePeriods");
    }

    public ActionForward addNewPeriod(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        PeriodsManagementBean bean = getRenderedObject();

        bean.addNewBean();

        request.setAttribute("managementBean", bean);

        return mapping.findForward("managePeriods");
    }

    public ActionForward duplicatePeriod(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        PeriodsManagementBean bean = getRenderedObject();

        try {
            bean.duplicatePeriod(request.getParameter("toDuplicateId"));
            addActionMessage("success", request, "label.occupation.period.duplicate.success");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey());
        }

        request.setAttribute("managementBean", bean);

        return mapping.findForward("managePeriods");
    }

    /*
     * Utility method, used to perform the various functions
     */

    private void updateBean(HttpServletRequest request, PeriodsManagementBean bean) {

        if (request.getParameter("editPeriod") != null) {
            bean.getBeanById(request.getParameter("periodId")).updateDates(request.getParameter("intervals"));
        } else if (request.getParameter("removePeriod") != null) {
            bean.removePeriod(request.getParameter("removePeriod"));
        } else if (request.getParameter("editPeriodCourses") != null) {
            bean.getBeanById(request.getParameter("periodId")).updateCourses(request.getParameter("courses"));
        } else if (request.getParameter("createPeriod") != null) {
            bean.getBeanById(request.getParameter("periodId")).create(request.getParameter("intervals"),
                    request.getParameter("courses"));
        } else if (request.getParameter("removeNewPeriod") != null) {
            bean.removeNewBean();
        }
    }

}
