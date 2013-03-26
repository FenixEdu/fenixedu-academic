package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriodBean;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgram", module = "academicAdministration")
@Forwards({
        @Forward(name = "listPhdProgramForPeriods",
                path = "/phd/academicAdminOffice/periods/phdProgram/listPhdProgramForPeriods.jsp"),
        @Forward(name = "viewPhdProgramPeriods", path = "/phd/academicAdminOffice/periods/phdProgram/viewPhdProgramPeriods.jsp"),
        @Forward(name = "addPhdProgramPeriod", path = "/phd/academicAdminOffice/periods/phdProgram/addPhdProgramPeriod.jsp") })
public class PhdProgramDA extends PhdDA {

    // Phd Program Management Periods

    public ActionForward listPhdProgramForPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdPrograms", AcademicAuthorizationGroup.getPhdProgramsForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_PHD_PROCESSES));
        return mapping.findForward("listPhdProgramForPeriods");
    }

    public ActionForward viewPhdProgramPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");

        request.setAttribute("phdProgram", phdProgram);
        return mapping.findForward("viewPhdProgramPeriods");
    }

    public ActionForward removePhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramContextPeriod period = getDomainObject(request, "phdProgramContextPeriodId");
        period.deletePeriod();

        return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward prepareAddPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");
        PhdProgramContextPeriodBean bean = new PhdProgramContextPeriodBean(phdProgram);

        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramContextPeriodBean", bean);

        return mapping.findForward("addPhdProgramPeriod");
    }

    public ActionForward addPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
            PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");
            phdProgram.create(bean);

        } catch (PhdDomainOperationException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return addPhdProgramPeriodInvalid(mapping, form, request, response);
        }

        return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward addPhdProgramPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");

        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramContextPeriodBean", bean);

        return mapping.findForward("addPhdProgramPeriod");
    }

    // End of Phd Program Management Periods

}
