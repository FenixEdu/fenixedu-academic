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
package org.fenixedu.academic.ui.struts.action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.pedagogicalCouncil.delegates.DelegateBean;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.ui.struts.action.commons.delegates.DelegatesManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/delegatesManagement", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "createEditDelegates", path = "/coordinator/viewDelegates.jsp"))
public class DelegatesManagementDispatchActionForCoordinator extends DelegatesManagementDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionDegree executionDegree =
                (ExecutionDegree) FenixFramework.getDomainObject((String) getFromRequest(request, "executionDegreeId"));
        DelegateBean bean = new DelegateBean();
        bean.setDegreeType(executionDegree.getDegreeType());
        bean.setDegree(executionDegree.getDegree());
        request.setAttribute("delegateBean", bean);

        return prepareViewDelegates(mapping, actionForm, request, response);
    }
}