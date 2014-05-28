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
 */
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "view-quc-results", titleKey = "link.inquiry.results.version2",
        bundle = "InquiriesResources")
@Mapping(path = "/viewQucResults", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "chooseDepartment", path = "/pedagogicalCouncil/inquiries/chooseDepartment.jsp"),
        @Forward(name = "viewResumeResults", path = "/departmentMember/quc/viewResumeResults.jsp"),
        @Forward(name = "viewCompetenceResults", path = "/departmentMember/quc/viewCompetenceResults.jsp"),
        @Forward(name = "viewTeachersResults", path = "/departmentMember/quc/viewTeachersResults.jsp"),
        @Forward(name = "departmentUCView", path = "/departmentMember/quc/departmentUCView.jsp"),
        @Forward(name = "departmentTeacherView", path = "/departmentMember/quc/departmentTeacherView.jsp") })
public class ViewQUCResultsPedagogicalCouncilDA extends ViewQUCResultsDA {

    @EntryPoint
    public ActionForward chooseDepartment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<DepartmentUnit> departmentUnits = new ArrayList<DepartmentUnit>();
        for (DepartmentUnit departmentUnit : DepartmentUnit.readAllDepartmentUnits()) {
            if (departmentUnit.getDepartment() != null) {
                departmentUnits.add(departmentUnit);
            }
        }
        Collections.sort(departmentUnits, new BeanComparator("name"));
        request.setAttribute("departments", departmentUnits);
        return mapping.findForward("chooseDepartment");
    }

    @Override
    public ActionForward showTeacherResultsAndComments(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("showAllComments", "true");
        return super.showTeacherResultsAndComments(actionMapping, actionForm, request, response);
    }

    @Override
    protected DepartmentUnit getDepartmentUnit(HttpServletRequest request) {
        String departmentUnitOID = request.getParameter("departmentUnitOID");
        if (StringUtils.isEmpty(departmentUnitOID)) {
            DepartmentExecutionSemester departmentExecutionSemester = getRenderedObject("executionSemesterBean");
            departmentUnitOID = departmentExecutionSemester.getDepartmentUnitOID();
        }
        DepartmentUnit departmentUnit = FenixFramework.getDomainObject(departmentUnitOID);
        request.setAttribute("fromPedagogicalCouncil", "true");
        request.setAttribute("departmentName", departmentUnit.getName());
        return departmentUnit;
    }

    @Override
    public boolean getShowAllComments() {
        return true;
    }
}
