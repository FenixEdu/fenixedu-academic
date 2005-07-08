package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class ApagarAulaFormAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            DynaActionForm manipularAulasForm = (DynaActionForm) request
                    .getAttribute("manipularAulasForm");

            IUserView userView = (IUserView) sessao.getAttribute("UserView");
            Integer indexAula = (Integer) manipularAulasForm.get("indexAula");

            InfoExecutionCourse iDE = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);

            Object argsLerAulas[] = new Object[1];
            argsLerAulas[0] = iDE;
            List infoAulas = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAulasDeDisciplinaExecucao", argsLerAulas);

            //			List infoAulas =
            //				(ArrayList) request.getAttribute("listaAulas");
            InfoLesson infoAula = (InfoLesson) infoAulas.get(indexAula.intValue());

            manipularAulasForm.set("indexAula", null);
            //sessao.removeAttribute("indexAula");

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);

            Object argsApagarAula[] = { infoAula, infoExecutionPeriod };
            Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "ApagarAula", argsApagarAula);

            if (result != null && result.booleanValue()) {
                infoAulas.remove(indexAula.intValue());

                if (!infoAulas.isEmpty())
                    request.setAttribute("listaAulas", infoAulas);
            }

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}