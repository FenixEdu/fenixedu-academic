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
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.SelectExportExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author joaosa & rmalo
 */
@Mapping(path = "/prepareSelectExecutionCourseAction", module = "teacher", functionality = ManageExecutionCourseDA.class)
public class PrepareSelectExecutionCourseAction extends ExecutionCourseBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        propageContextIds(request);

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        InfoSiteShiftsAndGroups shiftsAndGroupsView =
                StudentGroupManagementDA.getInfoSiteShiftsAndGroups(groupPropertiesCodeString);
        request.setAttribute("shiftsAndGroupsView", shiftsAndGroupsView);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        InfoExecutionDegree infoExecutionDegree =
                RequestUtils.getExecutionDegreeFromRequest(request, infoExecutionPeriod.getInfoExecutionYear());

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        List infoExecutionCourses =
                (List) SelectExportExecutionCourse.run(infoExecutionDegree, infoExecutionPeriod, curricularYear);
        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute("exeCourseList", infoExecutionCourses);
        return forward(request, "/teacher/prepareSelectExecutionCourse_bd.jsp");
    }

}