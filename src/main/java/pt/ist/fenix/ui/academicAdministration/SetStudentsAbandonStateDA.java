package pt.ist.fenix.ui.academicAdministration;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = FenixISTAcademicApp.class, path = "set-abandon-state",
        titleKey = "label.academicAdministration.setAbandonState")
@Mapping(module = "academicAdministration", path = "/setAbandonState")
@Forwards(value = { @Forward(name = "setAbandonState", path = "/academicAdministration/setAbandonState.jsp") })
public class SetStudentsAbandonStateDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("updateAbandonStateBean", new UpdateAbandonStateBean());
        return mapping.findForward("setAbandonState");
    }

    public ActionForward confirmUpdate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("updateAbandonStateBean", getRenderedObject("executionSemester"));
        request.setAttribute("confirm", "true");
        return mapping.findForward("setAbandonState");
    }

    public ActionForward updateState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        UpdateAbandonStateBean abandonStateBean = getRenderedObject("updateAbandonState");
        abandonStateBean.updateStates();

        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        response.setHeader("Content-disposition",
                "attachment; filename=studentsAbandonedState_" + new DateTime().toString("ddMMyyyy_HHmm") + ".csv");
        final ServletOutputStream writer = response.getOutputStream();
        writer.print(abandonStateBean.getLog().toString());
        writer.flush();
        response.flushBuffer();
        return null;
    }

}