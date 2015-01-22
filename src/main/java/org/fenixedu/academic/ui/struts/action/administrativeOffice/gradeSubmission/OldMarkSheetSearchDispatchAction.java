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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AcademicAdminMarksheetApp.class, path = "old-marksheets", titleKey = "link.old.markSheet.management")
@Mapping(path = "/oldMarkSheetManagement", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/oldMarkSheets/markSheetManagement.jsp")
@Forward(name = "searchMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/markSheetManagement.jsp")
@Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp")
@Forward(name = "removeMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/removeMarkSheet.jsp")
@Forward(name = "searchMarkSheetFilled",
        path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled")
@Forward(name = "confirmMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/confirmMarkSheet.jsp")
public class OldMarkSheetSearchDispatchAction extends MarkSheetSearchDispatchAction {

    @Override
    @EntryPoint
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
        request.setAttribute("edit", markSheetManagementSearchBean);

        return mapping.findForward("searchMarkSheet");
    }
}
