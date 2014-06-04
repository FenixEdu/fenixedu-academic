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
 * Jan 30, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.EditTeacherMasterDegreeCredits;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.GenericTrio;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;
import net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * @author Ricardo Rodrigues Modified by Manuel Pinto
 */

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "master-degree-credits", titleKey = "link.credits.masterDegree")
@Mapping(module = "scientificCouncil", path = "/masterDegreeCreditsManagement",
        input = "/masterDegreeCreditsManagement.do?method=prepareEdit", formBean = "masterDegreeCreditsForm")
@Forwards({ @Forward(name = "showCreditsReport", path = "/scientificCouncil/credits/showMasterDegreeCreditsReport.jsp"),
        @Forward(name = "chooseMasterDegreeExecution", path = "/scientificCouncil/credits/chooseMasterDegreeExecution.jsp"),
        @Forward(name = "editMasterDegreeCredits", path = "/scientificCouncil/credits/editMasterDegreeCredits.jsp"),
        @Forward(name = "successfulEdit", path = "/masterDegreeCreditsManagement.do?method=viewMasterDegreeCredits") })
@Exceptions({
        @ExceptionHandling(type = NumberFormatException.class, key = "error.credits.invalidNumber",
                handler = ExceptionHandler.class, path = "/masterDegreeCreditsManagement.do?method=prepareEdit",
                scope = "request"),
        @ExceptionHandling(type = DomainException.class, handler = FenixDomainExceptionHandler.class, scope = "request") })
public class MasterDegreeCreditsManagementDispatchAction extends FenixDispatchAction {

    private final static String LINE_BRAKE = "\n";

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        final List<ExecutionYear> notClosedExecutionYears = ExecutionYear.readNotClosedExecutionYears();

        Iterator<ExecutionYear> orderedExecutionYearsIter =
                new OrderedIterator<ExecutionYear>(notClosedExecutionYears.iterator(), new BeanComparator("beginDate"));
        request.setAttribute("executionYears", orderedExecutionYearsIter);
        DynaActionForm dynaForm = (DynaActionForm) form;
        final String executionYearID = (String) dynaForm.get("executionYearID");
        ExecutionYear executionYear = null;
        if (StringUtils.isEmpty(executionYearID)) {
            for (final ExecutionYear tempExecutionYear : notClosedExecutionYears) {
                if (tempExecutionYear.isCurrent()) {
                    executionYear = tempExecutionYear;
                    break;
                }
            }
            dynaForm.set("executionYearID", executionYear.getExternalId());

        } else {
            for (ExecutionYear tempExecutionYear : notClosedExecutionYears) {
                if (tempExecutionYear.getExternalId().equals(executionYearID)) {
                    executionYear = tempExecutionYear;
                    break;
                }
            }
        }

        List<ExecutionDegree> executionDegrees =
                (List<ExecutionDegree>) executionYear.getExecutionDegreesByType(DegreeType.MASTER_DEGREE);
        executionDegrees.addAll(executionYear.getExecutionDegreesByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
        executionDegrees.addAll(executionYear.getExecutionDegreesByType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE));
        executionDegrees.addAll(executionYear.getExecutionDegreesByType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA));

        Iterator<ExecutionDegree> orderedExecutionDegreesIter =
                new OrderedIterator<ExecutionDegree>(executionDegrees.iterator(), new BeanComparator("degreeCurricularPlan.name"));
        request.setAttribute("masterDegreeExecutions", orderedExecutionDegreesIter);
        return mapping.findForward("chooseMasterDegreeExecution");
    }

    public ActionForward viewMasterDegreeCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String executionDegreeID = (String) dynaForm.get("executionDegreeID");

        if (!StringUtils.isEmpty(executionDegreeID)) {
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
            request.setAttribute("executionDegree", executionDegree);

            List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = getListing(executionDegree);
            if (!masterDegreeCoursesDTOs.isEmpty()) {
                Iterator<MasterDegreeCreditsDTO> orderedCoursesIter =
                        new OrderedIterator<MasterDegreeCreditsDTO>(masterDegreeCoursesDTOs.iterator(), new BeanComparator(
                                "curricularCourse.name"));
                request.setAttribute("masterDegreeCoursesDTOs", orderedCoursesIter);
            }

            return mapping.findForward("showCreditsReport");

        } else {
            return prepare(mapping, form, request, response);
        }
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        CurricularCourse curricularCourse = getDomainObject(dynaForm, "curricularCourseID");

        String executionDegreeID = (String) dynaForm.get("executionDegreeID");
        ExecutionDegree executionDegree = null;

        if (!StringUtils.isEmpty(executionDegreeID)) {
            executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        } else {
            String executionCourseID = request.getParameter("executionCourseId");
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
            executionDegree =
                    curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(
                            executionCourse.getExecutionPeriod().getExecutionYear());
        }

        request.setAttribute("executionDegree", executionDegree);

        MasterDegreeCreditsDTO masterDegreeCreditsDTO =
                new MasterDegreeCreditsDTO(curricularCourse, executionDegree.getExecutionYear());
        request.setAttribute("masterDegreeCreditsDTO", masterDegreeCreditsDTO);

        dynaForm.set("executionDegreeID", executionDegree.getExternalId());
        dynaForm.set("curricularCourseID", curricularCourse.getExternalId());
        return mapping.findForward("editMasterDegreeCredits");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;
        Map creditsMap = (Map) dynaForm.get("creditsMap");
        Map hoursMap = (Map) dynaForm.get("hoursMap");

        EditTeacherMasterDegreeCredits.run(hoursMap, creditsMap);
        return mapping.findForward("successfulEdit");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ExecutionDegree executionDegree = getExecutionDegreeFromParameter(request);
        List<MasterDegreeCreditsDTO> listing = getListing(executionDegree);
        if (!listing.isEmpty()) {
            Collections.sort(listing, new BeanComparator("curricularCourse.name"));
        }

        try {
            String filename = getFileName(executionDegree);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(executionDegree, listing, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    public class MasterDegreeCreditsDTO {

        CurricularCourse curricularCourse;

        ExecutionYear executionYear;

        StringBuilder semesters = new StringBuilder();

        Map<ExecutionCourse, String> dcpNames = new HashMap<ExecutionCourse, String>();

        int totalRowSpan = 0;

        Map<ExecutionSemester, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> executionCoursesMap =
                new TreeMap<ExecutionSemester, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>>();

        public MasterDegreeCreditsDTO(CurricularCourse curricularCourse, ExecutionYear executionYear) {
            setExecutionYear(executionYear);
            setCurricularCourse(curricularCourse);

            List<DegreeModuleScope> ccsList = curricularCourse.getDegreeModuleScopes();
            Iterator<DegreeModuleScope> cssListIter = ccsList.iterator();
            List<Integer> semesters = new ArrayList();

            if (!ccsList.isEmpty()) {

                while (cssListIter.hasNext()) {
                    DegreeModuleScope ccs = cssListIter.next();
                    if (!semesters.contains(ccs.getCurricularSemester())) {
                        semesters.add(ccs.getCurricularSemester());
                    }
                }

                int semestersSize = semesters.size();
                Collections.sort(semesters);
                for (Integer semesterNumber : semesters) {
                    this.semesters.append(semesterNumber);
                    if (semestersSize == 2) {
                        this.semesters.append(" e ");
                        semestersSize = 1;
                    }
                }

            } else {
                this.semesters.append("0");
            }

            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {

                List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);
                for (ExecutionCourse executionCourse : executionCourses) {

                    dcpNames.put(executionCourse, getExecutionCourseDCPNames(executionCourse).toString());

                    List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>> executionPeriodMap =
                            executionCoursesMap.get(executionSemester);
                    executionPeriodMap =
                            executionPeriodMap == null ? new ArrayList<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>() : executionPeriodMap;

                    GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer> executionCourseMap =
                            new GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>(new Pair<ExecutionCourse, Boolean>(
                                    executionCourse, executionCourse.isMasterDegreeDFAOrDEAOnly()), curricularCourse
                                    .getEnrolmentsByExecutionPeriod(executionCourse.getExecutionPeriod()).size(),
                                    Integer.valueOf(curricularCourse.getNumberOfStudentsWithFirstEnrolmentIn(executionCourse
                                            .getExecutionPeriod())));

                    executionPeriodMap.add(executionCourseMap);

                    executionCoursesMap.put(executionSemester, executionPeriodMap);

                    int profCounter = executionCourse.getProfessorshipsSet().size();
                    if (profCounter == 0) {
                        profCounter = 1;
                    }

                    totalRowSpan += profCounter;
                }
            }
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourse;
        }

        public void setCurricularCourse(CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        public String getCurricularCourseName() {
            return curricularCourse.getName(getExecutionYear().getFirstExecutionPeriod());
        }

        public int getTotalRowSpan() {
            return totalRowSpan;
        }

        public void setTotalRowSpan(int totalRowSpan) {
            this.totalRowSpan = totalRowSpan;
        }

        public Map<ExecutionSemester, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> getExecutionCoursesMap() {
            return executionCoursesMap;
        }

        public StringBuilder getSemesters() {
            return semesters;
        }

        public void setSemesters(StringBuilder semesters) {
            this.semesters = semesters;
        }

        public Map<ExecutionCourse, String> getDcpNames() {
            return dcpNames;
        }

        public void setDcpNames(Map<ExecutionCourse, String> dcpNames) {
            this.dcpNames = dcpNames;
        }

        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }
    }

    // Private Methods

    private List<MasterDegreeCreditsDTO> getListing(ExecutionDegree executionDegree) {
        List<CurricularCourse> curricularCourses =
                executionDegree.getDegreeCurricularPlan().getCurricularCoursesWithExecutionIn(executionDegree.getExecutionYear());
        List<MasterDegreeCreditsDTO> masterDegreeCoursesDTOs = new ArrayList<MasterDegreeCreditsDTO>();
        for (CurricularCourse curricularCourse : curricularCourses) {
            MasterDegreeCreditsDTO masterDegreeCreditsDTO =
                    new MasterDegreeCreditsDTO(curricularCourse, executionDegree.getExecutionYear());
            masterDegreeCoursesDTOs.add(masterDegreeCreditsDTO);
        }
        return masterDegreeCoursesDTOs;
    }

    private void exportToXls(ExecutionDegree executionDegree, List<MasterDegreeCreditsDTO> listing, ServletOutputStream writer)
            throws IOException {
        final List<Object> headers = getHeaders();
        final Spreadsheet spreadsheet =
                new Spreadsheet(executionDegree.getDegreeCurricularPlan().getName().replace('/', '_'), headers);
        fillSpreadSheet(executionDegree, listing, spreadsheet);
        spreadsheet.exportToXLSSheet(writer);
    }

    private void fillSpreadSheet(ExecutionDegree executionDegree, List<MasterDegreeCreditsDTO> listing, Spreadsheet spreadsheet) {
        for (MasterDegreeCreditsDTO masterDegreeCreditsDTO : listing) {
            Map<ExecutionSemester, List<GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer>>> executionCoursesMap =
                    masterDegreeCreditsDTO.getExecutionCoursesMap();
            for (ExecutionSemester executionSemester : executionCoursesMap.keySet()) {
                for (GenericTrio<Pair<ExecutionCourse, Boolean>, Integer, Integer> executionCourseMap : executionCoursesMap
                        .get(executionSemester)) {
                    ExecutionCourse executionCourse = executionCourseMap.getFirst().getKey();
                    final Row row = spreadsheet.addRow();
                    row.setCell(masterDegreeCreditsDTO.getCurricularCourse().getName());
                    row.setCell(BundleUtil.getString(Bundle.ENUMERATION, masterDegreeCreditsDTO.getCurricularCourse().getType().name()));
                    row.setCell(masterDegreeCreditsDTO.getCurricularCourse().getCredits().toString());
                    row.setCell(String.valueOf(executionCourseMap.getSecond()));
                    row.setCell(String.valueOf(executionCourseMap.getThird()));
                    row.setCell(executionCourse.getExecutionPeriod().getSemester().toString());
                    row.setCell(executionCourse.getSigla() + LINE_BRAKE);
                    row.setCell(getTeachersNumbers(executionCourse).toString());
                    row.setCell(getTeachersNames(executionCourse).toString());
                    row.setCell(getTeachersDepartaments(executionCourse).toString());
                    row.setCell(getTeachersHours(executionCourse).toString());
                    row.setCell(getTeachersCredits(executionCourse).toString());
                    row.setCell(getExecutionCourseDCPNames(executionCourse).toString());
                }
            }
        }
    }

    private StringBuilder getExecutionCourseDCPNames(ExecutionCourse executionCourse) {
        StringBuilder builder = new StringBuilder();
        for (CurricularCourse tempCurricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            builder.append(tempCurricularCourse.getDegreeCurricularPlan().getName()).append(";").append(LINE_BRAKE);
        }
        return builder;
    }

    private StringBuilder getTeachersCredits(ExecutionCourse executionCourse) {
        StringBuilder teachers = new StringBuilder();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            TeacherService teacherService =
                    professorship.getTeacher().getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
            TeacherMasterDegreeService masterDegreeService = null;
            if (teacherService != null) {
                masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);
            }
            teachers.append(masterDegreeService != null ? masterDegreeService.getCredits() : "").append(LINE_BRAKE);
        }
        return teachers;
    }

    private StringBuilder getTeachersHours(ExecutionCourse executionCourse) {
        StringBuilder teachers = new StringBuilder();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            TeacherService teacherService =
                    professorship.getTeacher().getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
            TeacherMasterDegreeService masterDegreeService = null;
            if (teacherService != null) {
                masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);
            }
            teachers.append(masterDegreeService != null ? masterDegreeService.getHours() : "").append(LINE_BRAKE);
        }
        return teachers;
    }

    private StringBuilder getTeachersDepartaments(ExecutionCourse executionCourse) {
        StringBuilder teachers = new StringBuilder();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            Department department =
                    professorship.getTeacher().getLastWorkingDepartment(executionSemester.getBeginDateYearMonthDay(),
                            executionSemester.getEndDateYearMonthDay());
            teachers.append(department != null ? department.getRealName() : "").append(LINE_BRAKE);
        }
        return teachers;
    }

    private StringBuilder getTeachersNames(ExecutionCourse executionCourse) {
        StringBuilder teachers = new StringBuilder();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            teachers.append(professorship.getPerson().getName()).append(LINE_BRAKE);
        }
        return teachers;
    }

    private StringBuilder getTeachersNumbers(ExecutionCourse executionCourse) {
        StringBuilder teachers = new StringBuilder();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            teachers.append(professorship.getTeacher().getPerson().getIstUsername()).append(LINE_BRAKE);
        }
        return teachers;
    }

    private String getFileName(ExecutionDegree executionDegree) {
        return executionDegree.getDegreeCurricularPlan().getName() + "_" + new DateTime().toString("dd_MM_yyyy_HH_mm");
    }

    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Disciplina");
        headers.add("Tipo");
        headers.add("Créditos");
        headers.add("Nº Alunos Inscritos (Total)");
        headers.add("Nº Alunos Inscritos (1º vez)");
        headers.add("Semestre");
        headers.add("Código da Execução");
        headers.add("Número");
        headers.add("Docente");
        headers.add("Departamento");
        headers.add("Horas Leccionadas");
        headers.add("Créditos Lectivos");
        headers.add("Cursos");
        return headers;
    }

    private ExecutionDegree getExecutionDegreeFromParameter(final HttpServletRequest request) {
        final String executionDegreeIDString = request.getParameter("executionDegreeID");
        return FenixFramework.getDomainObject(executionDegreeIDString);
    }
}