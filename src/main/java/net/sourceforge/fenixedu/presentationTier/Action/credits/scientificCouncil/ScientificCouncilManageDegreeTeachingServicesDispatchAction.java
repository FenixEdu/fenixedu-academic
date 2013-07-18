package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/degreeTeachingServiceManagement",
        input = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails",
        attribute = "teacherExecutionCourseShiftProfessorshipForm", formBean = "teacherExecutionCourseShiftProfessorshipForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0"),
        @Forward(name = "sucessfull-edit", path = "/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "show-teaching-service-percentages",
                path = "/credits/degreeTeachingService/showTeachingServicePercentages.jsp") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class,
        key = "message.invalid.professorship.percentage", handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&page=0", scope = "request") })
public class ScientificCouncilManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer professorshipID = (Integer) dynaForm.get("professorshipID");
        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

        if (professorship == null) {
            return mapping.findForward("teacher-not-found");
        }

        teachingServiceDetailsProcess(professorship, request, dynaForm);
        return mapping.findForward("show-teaching-service-percentages");
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {
        return updateTeachingServices(mapping, form, request, RoleType.SCIENTIFIC_COUNCIL);
    }
}
