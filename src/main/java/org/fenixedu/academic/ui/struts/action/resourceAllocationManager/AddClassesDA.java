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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.AddSchoolClassesToShift;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadAvailableClassesForShift;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(module = "resourceAllocationManager", path = "/addClasses", input = "/manageShift.do?method=prepareEditShift",
        formBean = "selectMultipleItemsForm", functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "ListClasses", path = "/resourceAllocationManager/addClasses_bd.jsp"),
        @Forward(name = "BackToEditShift", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift") })
public class AddClassesDA extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        List classes = ReadAvailableClassesForShift.run(infoShift.getExternalId());

        if (classes != null && !classes.isEmpty()) {
            Collections.sort(classes, new BeanComparator("nome"));
            request.setAttribute(PresentationConstants.CLASSES, classes);
        }

        return mapping.findForward("ListClasses");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        DynaActionForm addClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) addClassesForm.get("selectedItems");

        List classOIDs = new ArrayList();
        for (String selectedClasse : selectedClasses) {
            classOIDs.add(selectedClasse);
        }

        try {
            AddSchoolClassesToShift.run(infoShift, classOIDs);
        } catch (FenixServiceException ex) {
            // No probem, the user refreshed the page after adding classes
            request.setAttribute("selectMultipleItemsForm", null);
            return mapping.getInputForward();
        }

        request.setAttribute("selectMultipleItemsForm", null);

        return mapping.findForward("BackToEditShift");
    }

}