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
/*
 * Created on Jun 26, 2006
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapping(path = "/payments", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showOperations", path = "/academicAdminOffice/payments/showOperations.jsp")
})
public class PaymentsManagementDispatchAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsManagementDispatchAction.class);


    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("person", getDomainObject(request, "personId"));
        return mapping.findForward("showOperations");
    }
}
