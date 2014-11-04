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
package org.fenixedu.academic.ui.struts.action.student.administrativeOfficeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "annual-irs-documents",
        titleKey = "label.documents.anualIRS")
@Mapping(path = "/generatedDocuments", module = "student")
@Forwards(@Forward(name = "showAnnualIRSDocuments",
        path = "/student/administrativeOfficeServices/generatedDocuments/showAnnualIRSDocuments.jsp"))
public class GeneratedDocumentsDA extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        request.setAttribute("person", AccessControl.getPerson());

        request.setAttribute("annualIRSDocuments", AccessControl.getPerson().getAnnualIRSDocuments());

        return mapping.findForward("showAnnualIRSDocuments");
    }

}
