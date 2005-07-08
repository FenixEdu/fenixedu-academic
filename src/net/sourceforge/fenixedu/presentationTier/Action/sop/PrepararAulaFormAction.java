package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author tfc130d
 */
public class PrepararAulaFormAction extends FenixExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            List diasSemana = Util.getDaysOfWeek();
            request.setAttribute("diasSemana", diasSemana);

            RequestUtils.setLessonTypes(request);

            List horas = Util.getHours();
            request.setAttribute("horas", horas);

            List minutos = Util.getMinutes();
            request.setAttribute("minutos", minutos);

            SessionUtils.getExecutionCourses(request);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}