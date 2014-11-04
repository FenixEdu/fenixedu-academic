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
package org.fenixedu.academic.ui.struts.action.gep.a3es;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.gep.GepApplication.GepPortalApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

@StrutsFunctionality(app = GepPortalApp.class, path = "a3es", titleKey = "link.gep.a3es")
@Mapping(module = "gep", path = "/a3es")
@Forwards(@Forward(name = "listProcesses", path = "/gep/a3es/listProcesses.jsp"))
public class A3esDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("process", new A3ESDegreeProcess());
        request.setAttribute("selection", true);
        return mapping.findForward("listProcesses");
    }

    public ActionForward select(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        A3ESDegreeProcess process = getRenderedObject("process");
        process.initialize();
        request.setAttribute("process", process);
        return mapping.findForward("listProcesses");
    }

    public ActionForward uploadCompetenceCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        A3ESDegreeProcess process = getRenderedObject("process");
        request.setAttribute("process", process);
        request.setAttribute("output", process.uploadCompetenceCourses());
        return mapping.findForward("listProcesses");
    }

    public ActionForward uploadTeacherCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        A3ESDegreeProcess process = getRenderedObject("process");
        request.setAttribute("process", process);
        request.setAttribute("output", process.uploadTeacherCurriculum());
        return mapping.findForward("listProcesses");
    }

    public ActionForward exportTeacherCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        A3ESDegreeProcess process = getRenderedObject("process");
        SpreadsheetBuilder spreadsheetBuilder = process.exportTeacherCurriculum();
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=a3es.xls");
        spreadsheetBuilder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
        response.flushBuffer();
        return null;
    }
}