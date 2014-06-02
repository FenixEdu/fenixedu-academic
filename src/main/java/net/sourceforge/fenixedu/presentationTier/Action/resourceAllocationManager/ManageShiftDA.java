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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ChangeStudentsShift;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ChangeStudentsShift.UnableToTransferStudentsException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DeleteLessons;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditarTurno;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerAlunosDeTurno;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerTurnosDeDisciplinaExecucao;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.RemoveClasses;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.RemoverTurno;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/manageShift", module = "resourceAllocationManager", input = "/manageShift.do?method=prepareEditShift",
        formBean = "createShiftForm", functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "EditShift", path = "/resourceAllocationManager/manageShift_bd.jsp"),
        @Forward(name = "ViewStudentsEnroled", path = "/resourceAllocationManager/viewStudentsEnroledInShift_bd.jsp"),
        @Forward(name = "Continue", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift") })
@Exceptions({
        @ExceptionHandling(handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException", scope = "request"),
        @ExceptionHandling(handler = FenixErrorExceptionHandler.class, type = UnableToTransferStudentsException.class,
                key = "message.unable.to.transfer.students", scope = "request") })
public class ManageShiftDA extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    @Mapping(path = "/manageShiftMultipleItems", module = "resourceAllocationManager",
            input = "/manageShift.do?method=prepareEditShift&page=0", formBean = "selectMultipleItemsForm",
            functionality = ExecutionPeriodDA.class)
    @Forwards(@Forward(name = "EditShift", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift&page=0"))
    public static class ManageShiftMultipleItemsDA extends ManageShiftDA {
    }

    public ActionForward prepareEditShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoShift infoShiftToEdit = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        DynaActionForm editShiftForm = (DynaActionForm) form;
        editShiftForm.set("courseInitials", infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
        editShiftForm.set("nome", infoShiftToEdit.getNome());
        editShiftForm.set("lotacao", infoShiftToEdit.getLotacao());
        editShiftForm.set("comment", infoShiftToEdit.getComment());

        List<ShiftType> shiftTypes = infoShiftToEdit.getShift().getTypes();
        String[] selectedshiftTypesArray = new String[shiftTypes.size()];

        for (int i = 0; i < shiftTypes.size(); i++) {
            ShiftType shiftType = shiftTypes.get(i);
            selectedshiftTypesArray[i] = shiftType.getName();
        }

        editShiftForm.set("shiftTiposAula", selectedshiftTypesArray);

        getExecutionCourses(request);

        readAndSetShiftTypes(request, infoShiftToEdit.getInfoDisciplinaExecucao());

        return mapping.findForward("EditShift");
    }

    public ActionForward listExecutionCourseCourseLoads(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editShiftForm = (DynaActionForm) form;
        InfoShift infoShiftToEdit = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);
        InfoExecutionCourse infoExecutionCourse =
                RequestUtils.getExecutionCourseBySigla(request, (String) editShiftForm.get("courseInitials"));

        if (infoShiftToEdit.getInfoDisciplinaExecucao().getExecutionCourse().equals(infoExecutionCourse.getExecutionCourse())) {
            editShiftForm.set("courseInitials", infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
            editShiftForm.set("nome", infoShiftToEdit.getNome());

            List<ShiftType> shiftTypes = infoShiftToEdit.getShift().getTypes();
            String[] selectedshiftTypesArray = new String[shiftTypes.size()];
            for (int i = 0; i < shiftTypes.size(); i++) {
                ShiftType shiftType = shiftTypes.get(i);
                selectedshiftTypesArray[i] = shiftType.getName();
            }
            editShiftForm.set("shiftTiposAula", selectedshiftTypesArray);
            editShiftForm.set("lotacao", infoShiftToEdit.getLotacao());
            //editShiftForm.set("comment", infoShiftToEdit.getComment());

        } else {
            editShiftForm.set("shiftTiposAula", new String[] {});
            editShiftForm.set("lotacao", Integer.valueOf(0));
            editShiftForm.set("nome", "");
            //editShiftForm.set("comment", "");
        }

        getExecutionCourses(request);

        readAndSetShiftTypes(request, infoExecutionCourse);

        return mapping.findForward("EditShift");
    }

    public ActionForward editShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editShiftForm = (DynaActionForm) form;

        InfoShift infoShiftOld = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        InfoExecutionCourse infoExecutionCourseNew =
                RequestUtils.getExecutionCourseBySigla(request, (String) editShiftForm.get("courseInitials"));
        InfoShiftEditor infoShiftNew = new InfoShiftEditor();
        infoShiftNew.setExternalId(infoShiftOld.getExternalId());
        infoShiftNew.setInfoDisciplinaExecucao(infoExecutionCourseNew);
        infoShiftNew.setInfoLessons(infoShiftOld.getInfoLessons());
        infoShiftNew.setLotacao((Integer) editShiftForm.get("lotacao"));
        infoShiftNew.setNome((String) editShiftForm.get("nome"));
        infoShiftNew.setComment((String) editShiftForm.get("comment"));

        String[] selectedShiftTypes = (String[]) editShiftForm.get("shiftTiposAula");
        if (selectedShiftTypes.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.shift.types.notSelected", new ActionError("errors.shift.types.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        final List<ShiftType> shiftTypes = new ArrayList<ShiftType>();
        for (String selectedShiftType : selectedShiftTypes) {
            shiftTypes.add(ShiftType.valueOf(selectedShiftType.toString()));
        }

        infoShiftNew.setTipos(shiftTypes);

        try {
            EditarTurno.run(infoShiftOld, infoShiftNew);

        } catch (DomainException ex) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(ex.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourseNew);
        ContextUtils.setShiftContext(request);

        return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setClassContext(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);
        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        RemoverTurno.run(infoShift, infoClass);

        ContextUtils.setShiftContext(request);
        request.removeAttribute(PresentationConstants.CLASS_VIEW);

        return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm removeClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) removeClassesForm.get("selectedItems");

        if (selectedClasses.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        List<String> classOIDs = new ArrayList<String>();
        for (String selectedClasse : selectedClasses) {
            classOIDs.add(selectedClasse);
        }

        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        RemoveClasses.run(infoShift, classOIDs);

        return mapping.findForward("EditShift");

    }

    public ActionForward deleteLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm deleteLessonsForm = (DynaActionForm) form;
        String[] selectedLessons = (String[]) deleteLessonsForm.get("selectedItems");

        if (selectedLessons.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.lessons.notSelected", new ActionError("errors.lessons.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }

        final List<String> lessonOIDs = new ArrayList<String>();
        for (String selectedLesson : selectedLessons) {
            lessonOIDs.add(selectedLesson);
        }

        try {
            DeleteLessons.run(lessonOIDs);

        } catch (FenixServiceMultipleException e) {
            final ActionErrors actionErrors = new ActionErrors();
            for (final DomainException domainException : e.getExceptionList()) {
                actionErrors.add(domainException.getMessage(),
                        new ActionError(domainException.getMessage(), domainException.getArgs()));
            }
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("EditShift");
    }

    public ActionForward viewStudentsEnroled(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoShift infoShift = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

        ShiftKey shiftKey = new ShiftKey(infoShift.getNome(), infoShift.getInfoDisciplinaExecucao());

        List<InfoStudent> students = LerAlunosDeTurno.run(shiftKey);

        Collections.sort(students, new BeanComparator("number"));

        if (students != null && !students.isEmpty()) {
            request.setAttribute(PresentationConstants.STUDENT_LIST, students);
        }

        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

        List<InfoShift> shifts = LerTurnosDeDisciplinaExecucao.run(infoExecutionCourse);

        if (shifts != null && !shifts.isEmpty()) {
            request.setAttribute(PresentationConstants.SHIFTS, shifts);
        }

        return mapping.findForward("ViewStudentsEnroled");
    }

    public ActionForward changeStudentsShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String oldShiftId = (String) dynaActionForm.get("oldShiftId");
        final String newShiftIdString = (String) dynaActionForm.get("newShiftId");
        final String[] studentIDs = (String[]) dynaActionForm.get("studentIDs");
        final Set<Registration> registrations = new HashSet<Registration>();
        if (studentIDs != null) {
            for (final String studentID : studentIDs) {
                final Registration registration = FenixFramework.getDomainObject(studentID);
                registrations.add(registration);
            }
        }

        ChangeStudentsShift.run(userView, oldShiftId, newShiftIdString, registrations);

        return mapping.findForward("Continue");
    }

    private void readAndSetShiftTypes(HttpServletRequest request, InfoExecutionCourse infoExecutionCourse) {
        final List<LabelValueBean> tiposAula = new ArrayList<LabelValueBean>();
        final ResourceBundle bundle = ResourceBundle.getBundle(BundleUtil.ENUMERATION_BUNDLE, I18N.getLocale());
        for (final ShiftType shiftType : infoExecutionCourse.getExecutionCourse().getShiftTypes()) {
            tiposAula.add(new LabelValueBean(bundle.getString(shiftType.getName()), shiftType.name()));
        }
        request.setAttribute("tiposAula", tiposAula);
    }

    static List getExecutionCourses(HttpServletRequest request) throws Exception {

        List infoCourseList = new ArrayList();

        // Ler Disciplinas em Execucao
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));

        infoCourseList =
                LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular.run(infoExecutionDegree, academicInterval,
                        infoCurricularYear.getYear());

        request.setAttribute(PresentationConstants.EXECUTION_COURSE_LIST_KEY, infoCourseList);

        return infoCourseList;

    }
}