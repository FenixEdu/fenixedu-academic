package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ManageCreditsNotes extends FenixDispatchAction {

    protected void getNote(ActionForm actionForm, Teacher teacher, ExecutionPeriod executionPeriod, String noteType) {

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (teacherService != null) {
            dynaActionForm.set("managementFunctionNote", teacherService.getManagementFunctionNotes());
            dynaActionForm.set("serviceExemptionNote", teacherService.getServiceExemptionNotes());
            dynaActionForm.set("masterDegreeTeachingNote", teacherService.getMasterDegreeTeachingNotes());
            dynaActionForm.set("otherNote", teacherService.getOthersNotes());
        }

        dynaActionForm.set("noteType", noteType);
        dynaActionForm.set("teacherId", teacher.getIdInternal());
        dynaActionForm.set("executionPeriodId", executionPeriod.getIdInternal());
    }

    protected ActionForward editNote(HttpServletRequest request, DynaActionForm dynaActionForm,
            Integer teacherId, Integer executionPeriodId, RoleType roleType, ActionMapping mapping, String noteType) throws FenixServiceException,
            FenixFilterException {

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        String managementFunctionNote, serviceExemptionNote, otherNote, masterDegreeTeachingNote;
                     
        managementFunctionNote = (!StringUtils.isEmpty(dynaActionForm.getString("managementFunctionNote"))) ? 
                dynaActionForm.getString("managementFunctionNote") : (noteType.equals("managementFunctionNote")) ? "" : null;                 
       
        serviceExemptionNote = (!StringUtils.isEmpty(dynaActionForm.getString("serviceExemptionNote"))) ? 
                dynaActionForm.getString("serviceExemptionNote") : (noteType.equals("serviceExemptionNote")) ? "" : null;                
        
        otherNote = (!StringUtils.isEmpty(dynaActionForm.getString("otherNote"))) ? 
                dynaActionForm.getString("otherNote") : (noteType.equals("otherNote")) ? "" : null;                 
        
        masterDegreeTeachingNote = (!StringUtils.isEmpty(dynaActionForm.getString("masterDegreeTeachingNote"))) ? 
                dynaActionForm.getString("masterDegreeTeachingNote") : (noteType.equals("masterDegreeTeachingNote")) ? "" : null;                     
        
        Object[] args = { teacherId, executionPeriodId, managementFunctionNote, serviceExemptionNote, otherNote,
                masterDegreeTeachingNote, roleType};

        try {
            ServiceUtils.executeService(getUserView(request), "EditTeacherServiceNotes", args);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException
                    .getArgs()));
            saveMessages(request, actionMessages);            
            getNote(dynaActionForm, teacher, executionPeriod, noteType);
            return mapping.findForward("show-note");      
        }
        
        request.setAttribute("teacherId", teacherId);
        request.setAttribute("executionPeriodId", executionPeriodId);
        
        return mapping.findForward("edit-note");   
    }
    
    public ActionForward cancel (ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Integer teacherId = (Integer) dynaActionForm.get("teacherId");
        Integer executionPeriodId = (Integer) dynaActionForm.get("executionPeriodId");                       
        request.setAttribute("teacherId", teacherId);
        request.setAttribute("executionPeriodId", executionPeriodId);
        
        return mapping.findForward("edit-note");
    }
}
