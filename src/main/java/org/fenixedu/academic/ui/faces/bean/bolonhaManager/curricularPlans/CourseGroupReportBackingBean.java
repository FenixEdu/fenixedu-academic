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
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularPlans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

import pt.ist.fenixframework.FenixFramework;

public class CourseGroupReportBackingBean extends FenixBackingBean {
    private InfoToExport infoToExport;
    private final boolean rootWasClicked;
    private String name = null;
    private String courseGroupID;
    private final Map<Context, String> contextPaths = new HashMap<Context, String>();

    private enum InfoToExport {
        CURRICULAR_STRUCTURE, STUDIES_PLAN;
    }

    public CourseGroupReportBackingBean() throws FenixServiceException {
        super();
        rootWasClicked = this.getDegreeCurricularPlan().getRoot().equals(this.getCourseGroup());
    }

    public Boolean getRootWasClicked() {
        return rootWasClicked;
    }

    public String getDegreeCurricularPlanID() {
        return getAndHoldStringParameter("degreeCurricularPlanID");
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixServiceException {
        return FenixFramework.getDomainObject(getDegreeCurricularPlanID());
    }

    public String getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldStringParameter("courseGroupID");
    }

    public void setCourseGroupID(String courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public CourseGroup getCourseGroup() {
        return (CourseGroup) FenixFramework.getDomainObject(getCourseGroupID());
    }

    public String getName() throws FenixServiceException {
        return (name == null && getCourseGroupID() != null) ? this.getCourseGroup().getName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void exportCourseGroupCurricularStructureToExcel() throws FenixServiceException {
        infoToExport = InfoToExport.CURRICULAR_STRUCTURE;
        exportToExcel();
    }

    public void exportCourseGroupStudiesPlanToExcel() throws FenixServiceException {
        infoToExport = InfoToExport.STUDIES_PLAN;
        exportToExcel();
    }

    public Map<Context, String> getContextPaths() {
        return contextPaths;
    }

    public void exportToExcel() throws FenixServiceException {
        String filename = this.getDegreeCurricularPlan().getName().replace(" ", "_") + "-";
        filename += (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) ? "Estrutura_Curricular" : "Plano_de_Estudos";
        if (!rootWasClicked) {
            filename += "-" + this.getCourseGroup().getName().replace(" ", "_");
        }
        filename += "-" + getFileName(Calendar.getInstance().getTime());

        try {
            exportToXls(filename);
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private List<Context> contextsWithCurricularCoursesToList(CourseGroup startingPoint) throws FenixServiceException {
        List<Context> result = new ArrayList<Context>();
        getContextPaths().clear();
        collectChildDegreeModules(result, startingPoint, startingPoint.getName());
        return result;
    }

    private void collectChildDegreeModules(final List<Context> result, CourseGroup courseGroup, String previousPath)
            throws FenixServiceException {
        for (final Context context : courseGroup.getChildContexts(CurricularCourse.class).stream().sorted()
                .collect(Collectors.toList())) {
            result.add(context);
            getContextPaths().put(context, previousPath);
        }
        for (final Context context : courseGroup.getChildContexts(CourseGroup.class).stream().sorted()
                .collect(Collectors.toList())) {
            collectChildDegreeModules(result, (CourseGroup) context.getChildDegreeModule(),
                    previousPath + " > " + context.getChildDegreeModule().getName());
        }
    }

    private String getFileName(Date date) throws FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "_" + month + "_" + year + "-" + hour + ":" + minutes);
    }

    private void exportToXls(String filename) throws IOException, FenixServiceException {
        this.getResponse().setContentType("application/vnd.ms-excel");
        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream outputStream = this.getResponse().getOutputStream();

        List<Object> headers = null;
        String spreadSheetName = null;
        Spreadsheet spreadsheet = null;
        if (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) {
            spreadSheetName = "Estrutura Curricular do Grupo";
            headers = getCurricularStructureHeader();
            spreadsheet = new Spreadsheet(spreadSheetName, headers);
            reportInfo(spreadsheet);
        } else {
            spreadSheetName = "Plano de Estudos do Grupo";
            headers = getStudiesPlanHeaders();
            spreadsheet = new Spreadsheet(spreadSheetName, headers);
            reportInfo(spreadsheet);
        }

        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        this.getResponse().flushBuffer();
    }

    private void reportInfo(Spreadsheet spreadsheet) throws FenixServiceException {
        List<Context> contextsWithCurricularCourses = null;

        contextsWithCurricularCourses = contextsWithCurricularCoursesToList(this.getCourseGroup());

        if (infoToExport.equals(InfoToExport.CURRICULAR_STRUCTURE)) {
            fillCurricularStructure(this.getCourseGroup().getName(), contextsWithCurricularCourses, spreadsheet);
        } else {
            fillStudiesPlan(contextsWithCurricularCourses, spreadsheet);
        }
    }

    private List<Object> getCurricularStructureHeader() {
        final List<Object> headers = new ArrayList<Object>();
        if (rootWasClicked && !this.getCourseGroup().getChildContexts(CourseGroup.class).isEmpty()) {
            headers.add("Grupo");
        }
        headers.add("Área Científica");
        headers.add("Sigla");
        headers.add("Créditos Obrigatórios");
        headers.add("Créditos Optativos");
        return headers;
    }

    private void fillCurricularStructure(String courseGroupBeingReported, List<Context> contextsWithCurricularCourses,
            final Spreadsheet spreadsheet) throws FenixServiceException {
        Set<Unit> scientificAreaUnits = new HashSet<Unit>();

        for (final Context contextWithCurricularCourse : contextsWithCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) contextWithCurricularCourse.getChildDegreeModule();

            if (!curricularCourse.isOptionalCurricularCourse()
                    && !scientificAreaUnits.contains(curricularCourse.getCompetenceCourse().getScientificAreaUnit())) {
                final Row row = spreadsheet.addRow();

                if (rootWasClicked && !this.getCourseGroup().getChildContexts(CourseGroup.class).isEmpty()) {
                    row.setCell(courseGroupBeingReported);
                }
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());
                row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit()
                        .getScientificAreaUnitEctsCredits(contextsWithCurricularCourses).toString());

                scientificAreaUnits.add(curricularCourse.getCompetenceCourse().getScientificAreaUnit());
            }
        }
    }

    private List<Object> getStudiesPlanHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Unidade Curricular");
        headers.add("Grupo");
        headers.add("Área Científica");
        headers.add("Sigla");
        headers.add("Tipo");
        headers.add("Ano");
        headers.add("Semestre");
        headers.add("Créditos");
        headers.add("T");
        headers.add("TP");
        headers.add("PL");
        headers.add("TC");
        headers.add("S");
        headers.add("E");
        headers.add("OT");
        headers.add("TA");
        headers.add("Observações");
        return headers;
    }

    private void fillStudiesPlan(List<Context> contextsWithCurricularCourses, final Spreadsheet spreadsheet)
            throws FenixServiceException {
        for (final Context contextWithCurricularCourse : contextsWithCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) contextWithCurricularCourse.getChildDegreeModule();
            CurricularPeriod curricularPeriod = contextWithCurricularCourse.getCurricularPeriod();
            String parentCourseGroupName = getContextPaths().get(contextWithCurricularCourse);

            fillCurricularCourse(spreadsheet, curricularCourse, curricularPeriod, parentCourseGroupName);
            if (curricularCourse.isAnual()) {
                fillCurricularCourse(spreadsheet, curricularCourse, curricularPeriod.getNext(), parentCourseGroupName);
            }
        }
    }

    private void fillCurricularCourse(final Spreadsheet spreadsheet, CurricularCourse curricularCourse,
            CurricularPeriod curricularPeriod, String parentCourseGroupName) {
        final Row row = spreadsheet.addRow();

        row.setCell(curricularCourse.getName());
        row.setCell(parentCourseGroupName);
        if (curricularCourse.isOptionalCurricularCourse()) {
            row.setCell(""); // scientific area unit name
            row.setCell(""); // scientific area unit acronym

            row.setCell(""); // regime
            row.setCell(curricularPeriod.getParent().getChildOrder() == null ? "" : curricularPeriod.getParent().getChildOrder()
                    .toString());
            row.setCell(curricularPeriod.getChildOrder().toString());

            row.setCell(""); // ects
            row.setCell(""); // t
            row.setCell(""); // tp
            row.setCell(""); // pl
            row.setCell(""); // tc
            row.setCell(""); // s
            row.setCell(""); // e
            row.setCell(""); // ot
            row.setCell(""); // ta
        } else {
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getName());
            row.setCell(curricularCourse.getCompetenceCourse().getScientificAreaUnit().getAcronym());

            row.setCell(BundleUtil.getString(Bundle.ENUMERATION,
                    curricularCourse.getCompetenceCourse().getAcademicPeriod().getName()));
            row.setCell(curricularPeriod.getParent().getChildOrder() == null ? "" : curricularPeriod.getParent().getChildOrder()
                    .toString());
            row.setCell(curricularPeriod.getChildOrder().toString());

//            row.setCell(curricularCourse.getEctsCredits(curricularPeriod).toString());
//            row.setCell(curricularCourse.getTheoreticalHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getProblemsHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getLaboratorialHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getFieldWorkHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getSeminaryHours().toString());
//            row.setCell(curricularCourse.getTrainingPeriodHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getTutorialOrientationHours(curricularPeriod).toString());
//            row.setCell(curricularCourse.getAutonomousWorkHours(curricularPeriod).toString());
        }
        row.setCell(""); // notes
    }

}
