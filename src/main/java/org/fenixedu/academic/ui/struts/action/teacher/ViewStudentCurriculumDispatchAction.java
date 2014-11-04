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
package org.fenixedu.academic.ui.struts.action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

/**
 * @author Fernanda Quitério 05/Fev/2004
 * 
 */
public class ViewStudentCurriculumDispatchAction extends FenixDispatchAction {

    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getExecutionDegreeId(request);

        if (request.getParameter("degreeCurricularPlanID") != null) {
            String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        return mapping.findForward("prepareViewStudentCurriculumChooseStudent");
    }

    private void getExecutionDegreeId(HttpServletRequest request) {
        String executionDegreeId = request.getParameter("executionDegreeId");
        if (executionDegreeId == null) {
            executionDegreeId = (String) request.getAttribute("executionDegreeId");
        }
        request.setAttribute("executionDegreeId", executionDegreeId);
    }
}