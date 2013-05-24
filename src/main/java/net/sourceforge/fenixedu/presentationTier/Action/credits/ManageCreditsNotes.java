package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.EditTeacherServiceNotes;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ManageCreditsNotes extends FenixDispatchAction {

    protected void getNote(ActionForm actionForm, Teacher teacher, ExecutionSemester executionSemester, String noteType) {

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (teacherService != null && teacherService.getTeacherServiceNotes() != null) {
            dynaActionForm.set("managementFunctionNote", teacherService.getTeacherServiceNotes().getManagementFunctionNotes());
            dynaActionForm.set("serviceExemptionNote", teacherService.getTeacherServiceNotes().getServiceExemptionNotes());
            dynaActionForm
                    .set("masterDegreeTeachingNote", teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes());
            dynaActionForm.set("otherNote", teacherService.getTeacherServiceNotes().getOthersNotes());
            dynaActionForm.set("functionsAccumulationNote", teacherService.getTeacherServiceNotes().getFunctionsAccumulation());
            dynaActionForm.set("thesisNote", teacherService.getTeacherServiceNotes().getThesisNote());
        }

        dynaActionForm.set("noteType", noteType);
        dynaActionForm.set("teacherId", teacher.getExternalId());
        dynaActionForm.set("executionPeriodId", executionSemester.getIdInternal());
    }

    protected ActionForward editNote(HttpServletRequest request, DynaActionForm dynaActionForm, Teacher teacher,
            Integer executionPeriodId, RoleType roleType, ActionMapping mapping, String noteType) throws FenixServiceException,
            FenixFilterException {

        ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
        String managementFunctionNote, serviceExemptionNote, otherNote, masterDegreeTeachingNote, functionsAccumulation, thesisNote;

        managementFunctionNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("managementFunctionNote"))) ? dynaActionForm
                        .getString("managementFunctionNote") : (noteType.equals("managementFunctionNote")) ? "" : null;

        serviceExemptionNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("serviceExemptionNote"))) ? dynaActionForm
                        .getString("serviceExemptionNote") : (noteType.equals("serviceExemptionNote")) ? "" : null;

        otherNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("otherNote"))) ? dynaActionForm.getString("otherNote") : (noteType
                        .equals("otherNote")) ? "" : null;

        masterDegreeTeachingNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("masterDegreeTeachingNote"))) ? dynaActionForm
                        .getString("masterDegreeTeachingNote") : (noteType.equals("masterDegreeTeachingNote")) ? "" : null;

        functionsAccumulation =
                (!StringUtils.isEmpty(dynaActionForm.getString("functionsAccumulationNote"))) ? dynaActionForm
                        .getString("functionsAccumulationNote") : (noteType.equals("functionsAccumulationNote")) ? "" : null;

        thesisNote =
                (!StringUtils.isEmpty(dynaActionForm.getString("thesisNote"))) ? dynaActionForm.getString("thesisNote") : (noteType
                        .equals("thesisNote")) ? "" : null;

        try {
            EditTeacherServiceNotes.runEditTeacherServiceNotes(teacher, executionPeriodId, managementFunctionNote,
                    serviceExemptionNote, otherNote, masterDegreeTeachingNote, functionsAccumulation, thesisNote, roleType);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException.getArgs()));
            saveMessages(request, actionMessages);
            getNote(dynaActionForm, teacher, executionSemester, noteType);
            return mapping.findForward("show-note");
        }

        request.setAttribute("teacherId", teacher.getExternalId());
        request.setAttribute("executionPeriodId", executionPeriodId);

        return mapping.findForward("edit-note");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        request.setAttribute("teacherId", dynaActionForm.get("teacherId"));
        request.setAttribute("executionPeriodId", dynaActionForm.get("executionPeriodId"));
        return mapping.findForward("edit-note");
    }
}
