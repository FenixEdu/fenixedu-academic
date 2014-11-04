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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.AddShiftsToSchoolClass;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "resourceAllocationManager", path = "/addShifts", input = "/manageClass.do?method=prepare&page=0",
        formBean = "selectMultipleItemsForm", functionality = ExecutionPeriodDA.class)
@Forwards(@Forward(name = "EditClass", path = "/resourceAllocationManager/manageClass.do?method=prepare&page=0"))
public class AddShiftsDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        final DynaActionForm addShiftsForm = (DynaActionForm) form;
        List<String> selectedShifts = Arrays.asList((String[]) addShiftsForm.get("selectedItems"));

        try {
            AddShiftsToSchoolClass.run(infoClass, selectedShifts);
        } catch (ExistingServiceException ex) {
            // No problem, the user refreshed the page after adding classes
            request.setAttribute("selectMultipleItemsForm", null);
            return mapping.getInputForward();
        }

        return mapping.findForward("EditClass");
    }

}