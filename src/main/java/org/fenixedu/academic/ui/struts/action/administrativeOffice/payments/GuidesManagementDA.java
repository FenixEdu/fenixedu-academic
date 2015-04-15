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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.dto.accounting.PaymentsManagementDTO;
import org.fenixedu.academic.report.accounting.GuideDocument;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/guides", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showGuide", path = "/academicAdminOffice/payments/guides/showGuide.jsp"),
        @Forward(name = "showEvents", path = "/academicAdministration/payments.do?method=showEvents") })
public class GuidesManagementDA extends PaymentsManagementDispatchAction {

    @Override
    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentsManagementDTO managementDTO =
                (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO").getMetaObject().getObject();
        request.setAttribute("paymentsManagementDTO", managementDTO);

        if (managementDTO.getSelectedEntries().isEmpty()) {
            addActionMessage("context", request, "error.payments.guide.entries.selection.is.required");
            request.setAttribute("personId", managementDTO.getPerson().getExternalId());
            return mapping.findForward("showEvents");
        } else {
            return mapping.findForward("showGuide");
        }
    }

    public ActionForward printGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        PaymentsManagementDTO managementDTO =
                (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO").getMetaObject().getObject();

        final GuideDocument document = new GuideDocument(managementDTO);
        final byte[] data = ReportsUtils.generateReport(document).getData();

        response.setContentLength(data.length);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

        response.getOutputStream().write(data);

        return null;

    }

}
