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
/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 24/Fev/2003
 */
package org.fenixedu.academic.ui.struts.action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import org.fenixedu.academic.ui.struts.action.base.FenixContextAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author tfc130
 * 
 */
@Mapping(module = "publico", path = "/viewClassTimeTableWithClassNameAndDegreeInitialsAction",
        formBean = "classTimeTableWithClassNameAndDegreeInitialsForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "/publico/viewClassTimeTable.do") })
public class ViewClassTimeTableWithClassNameAndDegreeInitialsAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.execute(mapping, form, request, response);

        String degreeInitials = request.getParameter("degreeInitials");
        String classIdString = request.getParameter("classId");
        if (degreeInitials == null && classIdString == null) {
            return mapping.getInputForward();
        }

        final SchoolClass schoolClass = FenixFramework.getDomainObject(classIdString);
        final InfoExecutionDegree infoExecutionDegree =
                ReadExecutionDegreesByExecutionYearAndDegreeInitials.getInfoExecutionDegree(schoolClass.getExecutionDegree());
        request.setAttribute("exeDegree", infoExecutionDegree);
        return mapping.findForward("Sucess");
    }
}