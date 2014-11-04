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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import com.google.common.collect.Ordering;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
@StrutsFunctionality(app = RAMSchedulesApp.class, path = "view-all-class-schedules", titleKey = "link.schedules.listAllByClass")
@Mapping(path = "/viewAllClassesSchedulesDA", module = "resourceAllocationManager")
@Forwards({ @Forward(name = "choose", path = "/resourceAllocationManager/chooseDegreesToViewClassesSchedules.jsp"),
        @Forward(name = "list", path = "/resourceAllocationManager/viewAllClassesSchedules.jsp") })
public class ViewAllClassesSchedulesDA extends FenixDispatchAction {

    public static class ChooseExecutionDegreeBean implements Serializable {

        private static final long serialVersionUID = -663198492313971329L;

        private AcademicInterval academicInterval;
        private List<ExecutionDegree> selectedDegrees;

        public ChooseExecutionDegreeBean() {
            this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
        }

        public AcademicInterval getAcademicInterval() {
            return academicInterval;
        }

        public void setAcademicInterval(AcademicInterval academicInterval) {
            this.academicInterval = academicInterval;
        }

        public List<ExecutionDegree> getDegrees() {
            return selectedDegrees;
        }

        public void setDegrees(List<ExecutionDegree> degrees) {
            this.selectedDegrees = degrees;
        }

        public List<ExecutionDegree> getAvailableDegrees() {
            List<ExecutionDegree> degrees = new ArrayList<>(ExecutionDegree.filterByAcademicInterval(academicInterval));
            Collections.sort(degrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
            return degrees;
        }

        public List<AcademicInterval> getAvailableIntervals() {
            return Ordering.from(AcademicInterval.COMPARATOR_BY_BEGIN_DATE).reverse()
                    .sortedCopy(AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER));
        }
    }

    @EntryPoint
    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseExecutionDegreeBean bean = getRenderedObject();
        if (bean == null) {
            bean = new ChooseExecutionDegreeBean();
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("choose");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseExecutionDegreeBean bean = getRenderedObject();
        request.setAttribute("academicInterval", bean.getAcademicInterval());
        request.setAttribute("degrees", bean.getDegrees());
        return mapping.findForward("list");
    }
}