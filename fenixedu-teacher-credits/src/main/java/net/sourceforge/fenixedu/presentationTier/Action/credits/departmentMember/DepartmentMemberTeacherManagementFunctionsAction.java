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
package org.fenixedu.academic.ui.struts.action.credits.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberTeacherApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = DepartmentMemberTeacherApp.class, path = "management-functions",
        titleKey = "label.teacher.details.functionsInformation")
@Mapping(path = "/teacherManagementFunctions", module = "departmentMember")
@Forwards(@Forward(name = "showTeacherCreditsManagementFunctions", path = "/credits/showTeacherCreditsManagementFunctions.jsp"))
public class DepartmentMemberTeacherManagementFunctionsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final User userView = Authenticate.getUser();
        if (userView.getPerson().getTeacher() != null) {
            List<PersonFunction> personFunctions =
                    new ArrayList<PersonFunction>(PersonFunction.getPersonFunctions(userView.getPerson(),
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION));
            Collections.sort(personFunctions, new ReverseComparator(new BeanComparator("beginDate")));
            request.setAttribute("personFunctions", personFunctions);
        }
        return mapping.findForward("showTeacherCreditsManagementFunctions");
    }

}
