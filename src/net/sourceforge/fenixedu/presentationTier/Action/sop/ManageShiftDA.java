package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.EditarTurno.InvalidFinalAvailabilityException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.EditarTurno.InvalidNewShiftCapacity;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.EditarTurno.InvalidNewShiftExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.EditarTurno.InvalidNewShiftType;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageShiftDA extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepareEditShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        InfoShift infoShiftToEdit = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        /* Fill out form to be edited with shifts original values */
        DynaActionForm editShiftForm = (DynaActionForm) form;
        editShiftForm.set("courseInitials", infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
        editShiftForm.set("nome", infoShiftToEdit.getNome());
        editShiftForm.set("tipoAula", infoShiftToEdit.getTipo().toString());
        editShiftForm.set("lotacao", infoShiftToEdit.getLotacao());

        /* Place list of execution courses in request */
        SessionUtils.getExecutionCourses(request);

        /* Place label list of types of shifts/lessons in request */
        RequestUtils.setLessonTypes(request);

        return mapping.findForward("EditShift");
    }

    public ActionForward editShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editShiftForm = (DynaActionForm) form;

        InfoShift infoShiftOld = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        InfoShiftEditor infoShiftNew = new InfoShiftEditor();
        infoShiftNew.setIdInternal(infoShiftOld.getIdInternal());
        infoShiftNew.setAvailabilityFinal(infoShiftOld.getAvailabilityFinal());
        InfoExecutionCourse infoExecutionCourseNew = RequestUtils.getExecutionCourseBySigla(request,
                (String) editShiftForm.get("courseInitials"));
        infoShiftNew.setInfoDisciplinaExecucao(infoExecutionCourseNew);
        infoShiftNew.setInfoLessons(infoShiftOld.getInfoLessons());
        infoShiftNew.setLotacao((Integer) editShiftForm.get("lotacao"));
        infoShiftNew.setNome((String) editShiftForm.get("nome"));

        infoShiftNew.setTipo(ShiftType.valueOf((String) editShiftForm.get("tipoAula")));

        Object argsCriarTurno[] = { infoShiftOld, infoShiftNew };
        try {
            ServiceUtils.executeService(userView, "EditarTurno", argsCriarTurno);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("O Shift", ex);
        } catch (InvalidFinalAvailabilityException e0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.exception.invalid.finalAvailability", new ActionError(
                    "errors.exception.invalid.finalAvailability"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (InvalidNewShiftType e1) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.exception.invalid.newShiftType", new ActionError(
                    "errors.exception.invalid.newShiftType"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (InvalidNewShiftExecutionCourse e2) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.exception.invalid.newExecutionCourse", new ActionError(
                    "errors.exception.invalid.newExecutionCourse"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (InvalidNewShiftCapacity e3) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.exception.invalid.newCapacity", new ActionError(
                    "errors.exception.invalid.newCapacity"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourseNew);

        //request.setAttribute(SessionConstants.SHIFT, infoShiftNew);
        ContextUtils.setShiftContext(request);

        return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setClassContext(request);

        IUserView userView = SessionUtils.getUserView(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        Object argsRemove[] = { infoShift, infoClass };
        ServiceUtils.executeService(userView, "RemoverTurno", argsRemove);

        ContextUtils.setShiftContext(request);

        request.removeAttribute(SessionConstants.CLASS_VIEW);

        return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClasses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm removeClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) removeClassesForm.get("selectedItems");

        if (selectedClasses.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors
                    .add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        List classOIDs = new ArrayList();
        for (int i = 0; i < selectedClasses.length; i++) {
            classOIDs.add(new Integer(selectedClasses[i]));
        }

        InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        Object args[] = { infoShift, classOIDs };
        ServiceUtils.executeService(SessionUtils.getUserView(request), "RemoveClasses", args);

        return mapping.findForward("EditShift");

    }

    public ActionForward deleteLessons(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm deleteLessonsForm = (DynaActionForm) form;
        String[] selectedLessons = (String[]) deleteLessonsForm.get("selectedItems");

        if (selectedLessons.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors
                    .add("errors.lessons.notSelected", new ActionError("errors.lessons.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }

        final List<Integer> lessonOIDs = new ArrayList<Integer>();
        for (int i = 0; i < selectedLessons.length; i++) {
            lessonOIDs.add(Integer.valueOf(selectedLessons[i]));
        }

        final Object args[] = { lessonOIDs };

        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteLessons", args);
        } catch (FenixServiceMultipleException e) {
            final ActionErrors actionErrors = new ActionErrors();
            
            for (final DomainException domainException: e.getExceptionList()) {
        	actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage(), domainException.getArgs()));
            }
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("EditShift");
    }

    public ActionForward viewStudentsEnroled(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        ShiftKey shiftKey = new ShiftKey(infoShift.getNome(), infoShift.getInfoDisciplinaExecucao());

        Object args[] = { shiftKey };
        List students = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                "LerAlunosDeTurno", args);

        Collections.sort(students, new BeanComparator("number"));

        if (students != null && !students.isEmpty()) {
            request.setAttribute(SessionConstants.STUDENT_LIST, students);
        }

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Object args2[] = { infoExecutionCourse };
        List shifts = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                "LerTurnosDeDisciplinaExecucao", args2);

        if (shifts != null && !shifts.isEmpty()) {
            request.setAttribute(SessionConstants.SHIFTS, shifts);
        }

        return mapping.findForward("ViewStudentsEnroled");
    }

    public ActionForward changeStudentsShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        Integer oldShiftId = new Integer((String) dynaActionForm.get("oldShiftId"));
        Integer newShiftId = dynaActionForm.get("newShiftId") == null ? null : new Integer((String) dynaActionForm.get("newShiftId"));

        final String[] studentIDs = (String[]) dynaActionForm.get("studentIDs");
        final Set<Registration> registrations = new HashSet<Registration>();
        if (studentIDs != null) {
            for (final String studentID : studentIDs) {
                final Integer id = Integer.valueOf(studentID);
                final Registration registration = rootDomainObject.readRegistrationByOID(id);
                registrations.add(registration);
            }
        }

        Object args[] = { userView, oldShiftId, newShiftId, registrations };
        ServiceUtils.executeService(userView, "ChangeStudentsShift", args);

        return mapping.findForward("Continue");
    }

}