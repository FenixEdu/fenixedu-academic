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
package org.fenixedu.academic.ui.struts.action.vigilancy;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.WrittenEvaluationVigilancyView;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.vigilancy.VigilantGroup;
import org.fenixedu.academic.domain.vigilancy.VigilantWrapper;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(module = "examCoordination", path = "/vigilancy/exportReport", functionality = VigilantGroupManagement.class)
public class VigilantGroupExportReport extends VigilantGroupManagement {

    public ActionForward exportGroupReportAsXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String vigilantGroupId = request.getParameter("oid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(vigilantGroupId);

        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=\"" + group.getName() + ".xls\"");

        List<WrittenEvaluationVigilancyView> beans = getStatsViewForVigilantGroup(group);
        Boolean withNames = Boolean.valueOf(request.getParameter("showVigilants"));
        final Spreadsheet spreadsheet = getSpreadsheet(withNames);

        for (final WrittenEvaluationVigilancyView view : getStatsViewForVigilantGroup(group)) {

            WrittenEvaluation evaluation = view.getWrittenEvaluation();

            final Row row = spreadsheet.addRow();

            row.setCell(evaluation.getDayDateYearMonthDay() + " " + evaluation.getBeginningDateHourMinuteSecond() + " "
                    + evaluation.getName());
            row.setCell(String.valueOf(view.getTotalVigilancies()));
            row.setCell(String.valueOf(view.getVigilanciesFromTeachers()));
            if (withNames) {
                row.setCell(getVigilantListAsString(view.getTeachersVigilants()));
            }
            row.setCell(String.valueOf(view.getVigilanciesFromOthers()));
            if (withNames) {
                row.setCell(getVigilantListAsString(view.getOtherVigilants()));
            }
            row.setCell(String.valueOf(view.getNumberOfCancelledConvokes()));
            if (withNames) {
                row.setCell(getVigilantListAsString(view.getCancelledConvokes()));
            }
            row.setCell(String.valueOf(view.getNumberOfConfirmedConvokes()));
            if (withNames) {
                row.setCell(getVigilantListAsString(view.getConfirmedConvokes()));
            }
            row.setCell(String.valueOf(view.getNumberOfAttendedConvokes()));
            if (withNames) {
                row.setCell(getVigilantListAsString(view.getAttendedConvokes()));
            }
        }

        final OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();
        return null;
    }

    private String getVigilantListAsString(List<VigilantWrapper> vigilants) {
        String vigilantList = "";
        for (VigilantWrapper vigilant : vigilants) {
            vigilantList += vigilant.getPerson().getFirstAndLastName() + " (" + vigilant.getPerson().getUsername() + ")\n";
        }
        return vigilantList;
    }

    private Spreadsheet getSpreadsheet(Boolean withNames) {
        final Spreadsheet spreadsheet = new Spreadsheet("Report");
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.vigilancy.writtenEvaluation.header"));
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.totalVigilancies"));
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.vigilanciesFromTeachers"));
        if (withNames) {
            spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.teachersVigilants"));
        }
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.vigilanciesFromOthers"));
        if (withNames) {
            spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.otherVigilants"));
        }
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.numberOfCancelledConvokes"));
        if (withNames) {
            spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.cancelledConvokes"));
        }
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.numberOfConfirmedConvokes"));
        if (withNames) {
            spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.confirmedConvokes"));
        }
        spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.numberOfAttendedConvokes"));
        if (withNames) {
            spreadsheet.setHeader(BundleUtil.getString("resources.VigilancyResources", "label.attendedConvokes"));
        }

        return spreadsheet;
    }
}
