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
package org.fenixedu.academic.ui.struts.action.teacher.evaluation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.accessControl.DepartmentPresidentStrategy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.evaluation.FacultyEvaluationProcess;
import org.fenixedu.academic.domain.teacher.evaluation.FacultyEvaluationProcessBean;
import org.fenixedu.academic.domain.teacher.evaluation.FacultyEvaluationProcessServices;
import org.fenixedu.academic.domain.teacher.evaluation.FileUploadBean;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluation;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationFile;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationFileBean;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationFileType;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationProcess;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTeachingApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "teacher-evaluation", titleKey = "label.teacher.evaluation.title",
        bundle = "ResearcherResources")
@Mapping(module = "teacher", path = "/teacherEvaluation")
@Forwards({ @Forward(name = "entryPoint", path = "/teacher/evaluation/entryPoint.jsp"),
        @Forward(name = "viewAutoEvaluation", path = "/teacher/evaluation/viewAutoEvaluation.jsp"),
        @Forward(name = "changeEvaluationType", path = "/teacher/evaluation/changeEvaluationType.jsp"),
        @Forward(name = "insertEvaluationMark", path = "/teacher/evaluation/insertEvaluationMark.jsp"),
        @Forward(name = "viewEvaluees", path = "/teacher/evaluation/viewEvaluees.jsp"),
        @Forward(name = "viewEvaluation", path = "/teacher/evaluation/viewEvaluation.jsp"),
        @Forward(name = "viewEvaluationByCCAD", path = "/teacher/evaluation/viewEvaluationByCCAD.jsp"),
        @Forward(name = "uploadEvaluationFile", path = "/teacher/evaluation/uploadEvaluationFile.jsp"),
        @Forward(name = "viewManagementInterface", path = "/teacher/evaluation/viewManagementInterface.jsp") })
public class TeacherEvaluationDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(TeacherEvaluationDA.class);

    @EntryPoint
    public ActionForward entryPoint(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("entryPoint");
    }

    public ActionForward viewAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SortedSet<TeacherEvaluationProcess> processes =
                new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
        processes.addAll(getLoggedPerson(request).getTeacherEvaluationProcessFromEvalueeSet());
        request.setAttribute("processes", processes);
        return mapping.findForward("viewAutoEvaluation");
    }

    public ActionForward changeEvaluationType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        request.setAttribute("typeSelection", new TeacherEvaluationTypeSelection(process));
        return mapping.findForward("changeEvaluationType");
    }

    public ActionForward selectEvaluationType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationTypeSelection selection = getRenderedObject("process-selection");
        selection.createEvaluation();
        if ((AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(
                RoleType.MANAGER))
                && selection.getProcess().getEvaluee() != AccessControl.getPerson()) {
            request.setAttribute("process", selection.getProcess());
            return mapping.findForward("viewEvaluationByCCAD");
        } else {
            return viewAutoEvaluation(mapping, form, request, response);
        }
    }

    @Deprecated
    public ActionForward insertAutoEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        request.setAttribute("action", "viewAutoEvaluation");
        request.setAttribute("process", process);
        request.setAttribute("slot", "autoEvaluationMark");
        return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward insertEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        if (process.getEvaluator() != AccessControl.getPerson()
                && AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember()) {
            request.setAttribute("action", "viewEvaluationByCCAD&processId=" + process.getExternalId());
        } else {
            request.setAttribute("action", "viewEvaluation&evalueeOID=" + process.getEvaluee().getExternalId());
        }
        request.setAttribute("process", process);
        request.setAttribute("slot", "evaluationMark");
        return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward insertApprovedEvaluationMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        request.setAttribute("action", "viewEvaluationByCCAD&processId=" + process.getExternalId());
        request.setAttribute("process", process);
        request.setAttribute("slot", "approvedEvaluationMark");
        return mapping.findForward("insertEvaluationMark");
    }

    public ActionForward lockAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        process.getCurrentTeacherEvaluation().lickAutoEvaluationStamp();
        if ((AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(
                RoleType.MANAGER))
                && process.getEvaluee() != AccessControl.getPerson()) {
            request.setAttribute("process", process);
            return mapping.findForward("viewEvaluationByCCAD");
        } else {
            return viewAutoEvaluation(mapping, form, request, response);
        }
    }

    public ActionForward lockEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        process.getCurrentTeacherEvaluation().lickEvaluationStamp();
        if ((AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(
                RoleType.MANAGER))
                && process.getEvaluee() != AccessControl.getPerson()) {
            request.setAttribute("process", process);
            return mapping.findForward("viewEvaluationByCCAD");
        } else {
            return viewEvaluation(mapping, request, process.getEvaluee());
        }
    }

    public ActionForward unlockAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        process.getCurrentTeacherEvaluation().rubAutoEvaluationStamp();
        request.setAttribute("process", process);
        return mapping.findForward("viewEvaluationByCCAD");
    }

    public ActionForward unlockEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherEvaluationProcess process = getDomainObject(request, "process");
        process.getCurrentTeacherEvaluation().rubEvaluationStamp();
        request.setAttribute("process", process);
        return mapping.findForward("viewEvaluationByCCAD");
    }

    public class EvalueesMap implements Serializable {
        private final Person evaluee;

        private final SortedMap<FacultyEvaluationProcess, TeacherEvaluationProcess> processes =
                new TreeMap<FacultyEvaluationProcess, TeacherEvaluationProcess>();

        public EvalueesMap(Person evaluee) {
            this.evaluee = evaluee;
            final Set<FacultyEvaluationProcess> facultyEvaluationProcessSet = rootDomainObject.getFacultyEvaluationProcessSet();
            for (FacultyEvaluationProcess process : facultyEvaluationProcessSet) {
                processes.put(process, null);
            }
        }

        public Person getEvaluee() {
            return evaluee;
        }

        public List<TeacherEvaluationProcess> getProcesses() {
            List<TeacherEvaluationProcess> list = new ArrayList<TeacherEvaluationProcess>();
            for (TeacherEvaluationProcess process : processes.values()) {
                list.add(process);
            }
            return list;
        }
    }

    public ActionForward viewEvaluees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<Person, EvalueesMap> evaluees = new HashMap<Person, EvalueesMap>();
        final Person loggedPerson = getLoggedPerson(request);
        for (TeacherEvaluationProcess process : loggedPerson.getTeacherEvaluationProcessFromEvaluatorSet()) {
            if (!evaluees.containsKey(process.getEvaluee())) {
                evaluees.put(process.getEvaluee(), new EvalueesMap(process.getEvaluee()));
            }
            evaluees.get(process.getEvaluee()).processes.put(process.getFacultyEvaluationProcess(), process);
        }
        request.setAttribute("processes", rootDomainObject.getFacultyEvaluationProcessSet());
        request.setAttribute("evaluees", evaluees.values());
        return mapping.findForward("viewEvaluees");
    }

    public ActionForward viewEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person evaluee = getDomainObject(request, "evalueeOID");
        return viewEvaluation(mapping, request, evaluee);
    }

    public ActionForward viewEvaluation(final ActionMapping mapping, final HttpServletRequest request, final Person evaluee)
            throws Exception {
        final Person loggedPerson = getLoggedPerson(request);
        SortedSet<TeacherEvaluationProcess> openProcesses =
                new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
        for (TeacherEvaluationProcess teacherEvaluationProcess : evaluee.getTeacherEvaluationProcessFromEvalueeSet()) {
            if (teacherEvaluationProcess.getEvaluator().equals(loggedPerson)) {
                openProcesses.add(teacherEvaluationProcess);
            }
        }
        request.setAttribute("openProcesses", openProcesses);
        return mapping.findForward("viewEvaluation");
    }

    public ActionForward prepareUploadEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareUploadFile(request);
        final String param = request.getParameter("backAction");
        if (param == null || param.isEmpty()) {
            request.setAttribute("backAction", "viewEvaluation");
        } else {
            request.setAttribute("backAction", param);
        }
        return mapping.findForward("uploadEvaluationFile");
    }

    public ActionForward prepareUploadAutoEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareUploadFile(request);
        request.setAttribute("backAction", "viewAutoEvaluation");
        return mapping.findForward("uploadEvaluationFile");
    }

    private void prepareUploadFile(HttpServletRequest request) {
        final TeacherEvaluationProcess teacherEvaluationProcess = getDomainObject(request, "OID");

        TeacherEvaluationFileType teacherEvaluationFileType =
                TeacherEvaluationFileType.valueOf((String) getFromRequest(request, "type"));
        FileUploadBean fileUploadBean = new FileUploadBean(teacherEvaluationProcess, teacherEvaluationFileType);
        request.setAttribute("fileUploadBean", fileUploadBean);
    }

    public ActionForward uploadEvaluationFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FileUploadBean fileUploadBean = getRenderedObject("fileUploadBean");
        fileUploadBean.consumeInputStream();
        TeacherEvaluationFile.create(fileUploadBean, getLoggedPerson(request));
        String backAction = request.getParameter("backAction");
        if (backAction.equals("viewEvaluation")) {
            request.setAttribute("evalueeOID", fileUploadBean.getTeacherEvaluationProcess().getEvaluee().getExternalId());
            return viewEvaluation(mapping, form, request, response);
        } else {
            if ((AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(
                    RoleType.MANAGER))
                    && fileUploadBean.getTeacherEvaluationProcess().getEvaluee() != AccessControl.getPerson()) {
                request.setAttribute("process", fileUploadBean.getTeacherEvaluationProcess());
                return mapping.findForward("viewEvaluationByCCAD");
            } else {
                return viewAutoEvaluation(mapping, form, request, response);
            }
        }
    }

    public ActionForward viewManagementInterface(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Set<FacultyEvaluationProcess> facultyEvaluationProcessSet = rootDomainObject.getFacultyEvaluationProcessSet();
        request.setAttribute("facultyEvaluationProcessSet", facultyEvaluationProcessSet);
        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward prepareCreateFacultyEvaluationProcess(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcessBean facultyEvaluationProcessBean = new FacultyEvaluationProcessBean();
        request.setAttribute("facultyEvaluationProcessCreationBean", facultyEvaluationProcessBean);
        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward createFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcessBean facultyEvaluationProcessBean = getRenderedObject();
        final FacultyEvaluationProcess facultyEvaluationProcess = facultyEvaluationProcessBean.create();
        return viewFacultyEvaluationProcess(mapping, request, facultyEvaluationProcess);
    }

    public ActionForward prepareEditFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        final FacultyEvaluationProcessBean facultyEvaluationProcessBean =
                new FacultyEvaluationProcessBean(facultyEvaluationProcess);
        request.setAttribute("facultyEvaluationProcessEditnBean", facultyEvaluationProcessBean);
        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward editFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcessBean facultyEvaluationProcessBean = getRenderedObject();
        facultyEvaluationProcessBean.edit();
        return viewFacultyEvaluationProcess(mapping, request, facultyEvaluationProcessBean.getFacultyEvaluationProcess());
    }

    public ActionForward viewFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        return viewFacultyEvaluationProcess(mapping, request, facultyEvaluationProcess);
    }

    private ActionForward viewFacultyEvaluationProcess(ActionMapping mapping, HttpServletRequest request,
            final FacultyEvaluationProcess facultyEvaluationProcess) {
        final SortedSet<TeacherEvaluationProcess> teacherEvaluationProcesses;
        int autoEvaluatedCount = 0;
        int evaluatedCount = 0;
        int approvedEvaluatedCount = 0;
        final Person person = AccessControl.getPerson();
        if (person.hasRole(RoleType.MANAGER) || person.isTeacherEvaluationCoordinatorCouncilMember()) {
            teacherEvaluationProcesses = facultyEvaluationProcess.getSortedTeacherEvaluationProcess();
            autoEvaluatedCount = facultyEvaluationProcess.getAutoEvaluatedCount();
            evaluatedCount = facultyEvaluationProcess.getEvaluatedCount();
            approvedEvaluatedCount = facultyEvaluationProcess.getApprovedEvaluatedCount();
        } else {
            teacherEvaluationProcesses = new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_EVALUEE);
            final Teacher teacher = person.getTeacher();
            if (teacher != null) {
                final Department department = teacher.getDepartment();
                if (DepartmentPresidentStrategy.getCurrentDepartmentPresident(department) == person) {
                    for (final TeacherEvaluationProcess teacherEvaluation : facultyEvaluationProcess
                            .getTeacherEvaluationProcessSet()) {
                        if (teacherEvaluation.getEvaluee().getTeacher().getDepartment() == department) {
                            teacherEvaluationProcesses.add(teacherEvaluation);
                            final TeacherEvaluation currentTeacherEvaluation = teacherEvaluation.getCurrentTeacherEvaluation();
                            if (currentTeacherEvaluation != null && currentTeacherEvaluation.getAutoEvaluationLock() != null) {
                                autoEvaluatedCount++;
                            }
                            if (currentTeacherEvaluation != null && currentTeacherEvaluation.getEvaluationLock() != null) {
                                evaluatedCount++;
                            }
                            if (!teacherEvaluation.getApprovedTeacherEvaluationProcessMarkSet().isEmpty()) {
                                approvedEvaluatedCount++;
                            }
                        }
                    }
                }
            }
        }
        request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
        request.setAttribute("teacherEvaluationProcesses", teacherEvaluationProcesses);
        request.setAttribute("autoEvaluatedCount", autoEvaluatedCount);
        request.setAttribute("evaluatedCount", evaluatedCount);
        request.setAttribute("approvedEvaluatedCount", approvedEvaluatedCount);

        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward deleteFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        if (!facultyEvaluationProcess.getTeacherEvaluationProcessSet().isEmpty()) {
            addActionMessage(request, "error.teacher.evaluation.facultyEvaluationProcess.undeletable");
        } else {
            facultyEvaluationProcess.delete();
        }
        return viewManagementInterface(mapping, form, request, response);
    }

    public ActionForward prepareUploadEvaluators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        final FileUploadBean fileUploadBean = new FileUploadBean(facultyEvaluationProcess);
        request.setAttribute("fileUploadBean", fileUploadBean);
        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward uploadEvaluators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FileUploadBean fileUploadBean = getRenderedObject();
        final FacultyEvaluationProcess facultyEvaluationProcess = fileUploadBean.getFacultyEvaluationProcess();
        fileUploadBean.consumeInputStream();
        try {
            fileUploadBean.uploadEvaluators();
        } catch (final DomainException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
            RenderUtils.invalidateViewState();
        }
        return viewFacultyEvaluationProcess(mapping, request, facultyEvaluationProcess);
    }

    public ActionForward viewEvaluationByCCAD(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        TeacherEvaluationProcess process = getDomainObject(request, "processId");
        request.setAttribute("process", process);
        return mapping.findForward("viewEvaluationByCCAD");
    }

    public ActionForward exportAutoEvaluationFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bout);
        final String fileSeparator = "/";
        final String fileNameSeparator = "_";
        final String withoutDepartment = "Sem departamento";
        try {
            for (FacultyEvaluationProcess facultyEvaluationProcess : rootDomainObject.getFacultyEvaluationProcessSet()) {
                String evaluationName =
                        (facultyEvaluationProcess.getSuffix() == null ? facultyEvaluationProcess.getTitle().getContent() : facultyEvaluationProcess
                                .getSuffix());
                for (TeacherEvaluationProcess teacherEvaluationProcess : facultyEvaluationProcess
                        .getTeacherEvaluationProcessSet()) {
                    TeacherEvaluation teacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
                    if (teacherEvaluation != null) {
                        String department =
                                teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastDepartment() == null ? withoutDepartment : teacherEvaluation
                                        .getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastDepartment().getName();
                        for (TeacherEvaluationFileType teacherEvaluationFileType : teacherEvaluation.getAutoEvaluationFileSet()) {
                            TeacherEvaluationFileBean teacherEvaluationFileBean =
                                    new TeacherEvaluationFileBean(teacherEvaluation, teacherEvaluationFileType);
                            if (teacherEvaluationFileBean.getTeacherEvaluationFile() != null) {
                                zip.putNextEntry(new ZipEntry(department + fileSeparator + evaluationName + fileSeparator
                                        + teacherEvaluationFileBean.getTeacherEvaluationFileType() + fileNameSeparator
                                        + teacherEvaluationFileBean.getTeacherEvaluationFile().getFilename()));
                                zip.write(teacherEvaluationFileBean.getTeacherEvaluationFile().getContents());
                                zip.closeEntry();
                            }
                        }
                    }
                }
            }
            zip.close();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=autoAvaliação.zip");
            ServletOutputStream writer = response.getOutputStream();
            writer.write(bout.toByteArray());
            writer.flush();
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public ActionForward exportEvaluationFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bout);
        final String fileSeparator = "/";
        final String fileNameSeparator = "_";
        final String withoutDepartment = "Sem departamento";
        try {
            for (FacultyEvaluationProcess facultyEvaluationProcess : rootDomainObject.getFacultyEvaluationProcessSet()) {
                String evaluationName =
                        (facultyEvaluationProcess.getSuffix() == null ? facultyEvaluationProcess.getTitle().getContent() : facultyEvaluationProcess
                                .getSuffix());
                for (TeacherEvaluationProcess teacherEvaluationProcess : facultyEvaluationProcess
                        .getTeacherEvaluationProcessSet()) {
                    TeacherEvaluation teacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
                    if (teacherEvaluation != null) {
                        String department =
                                teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastDepartment() == null ? withoutDepartment : teacherEvaluation
                                        .getTeacherEvaluationProcess().getEvaluee().getTeacher().getLastDepartment().getName();
                        for (TeacherEvaluationFileType teacherEvaluationFileType : teacherEvaluation.getEvaluationFileSet()) {
                            TeacherEvaluationFileBean teacherEvaluationFileBean =
                                    new TeacherEvaluationFileBean(teacherEvaluation, teacherEvaluationFileType);
                            if (teacherEvaluationFileBean.getTeacherEvaluationFile() != null) {
                                zip.putNextEntry(new ZipEntry(department + fileSeparator + evaluationName + fileSeparator
                                        + teacherEvaluationFileBean.getTeacherEvaluationFileType() + fileNameSeparator
                                        + teacherEvaluationFileBean.getTeacherEvaluationFile().getFilename()));
                                zip.write(teacherEvaluationFileBean.getTeacherEvaluationFile().getContents());
                                zip.closeEntry();
                            }
                        }
                    }
                }
            }
            zip.close();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=avaliação.zip");
            ServletOutputStream writer = response.getOutputStream();
            writer.write(bout.toByteArray());
            writer.flush();
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public ActionForward prepareUploadApprovedMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        final FileUploadBean fileUploadBean = new FileUploadBean(facultyEvaluationProcess);
        request.setAttribute("fileUploadBeanForApprovedMarks", fileUploadBean);
        return mapping.findForward("viewManagementInterface");
    }

    public ActionForward uploadApprovedEvaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FileUploadBean fileUploadBean = getRenderedObject();
        final FacultyEvaluationProcess facultyEvaluationProcess = fileUploadBean.getFacultyEvaluationProcess();
        fileUploadBean.consumeInputStream();
        try {
            fileUploadBean.uploadApprovedEvaluations();
        } catch (final DomainException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
            RenderUtils.invalidateViewState();
        }
        return viewFacultyEvaluationProcess(mapping, request, facultyEvaluationProcess);
    }

    public ActionForward publish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        FacultyEvaluationProcessServices.publish(facultyEvaluationProcess);
        return viewFacultyEvaluationProcess(mapping, form, request, response);
    }

    public ActionForward unPublish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
        FacultyEvaluationProcessServices.unPublish(facultyEvaluationProcess);
        return viewFacultyEvaluationProcess(mapping, form, request, response);
    }

}
