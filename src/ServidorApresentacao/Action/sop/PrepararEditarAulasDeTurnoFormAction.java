package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * author tfc130
 *  
 */
public class PrepararEditarAulasDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            //DynaActionForm manipularTurnosForm = (DynaActionForm)
            // request.getAttribute("manipularTurnosForm");
            IUserView userView = (IUserView) sessao.getAttribute("UserView");

            //Integer indexTurno = (Integer)
            // manipularTurnosForm.get("indexTurno");
            //List infoTurnos = (ArrayList)
            // request.getAttribute("infoTurnosDeDisciplinaExecucao");
            //InfoShift infoTurno = (InfoShift)
            // infoTurnos.get(indexTurno.intValue());
            request.removeAttribute("infoTurno");
            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

            //InfoShift infoTurnoAntigo =
            //	(InfoShift) request.getAttribute("infoTurno");

            Object args[] = { shiftOID };
            InfoShift infoTurno = (InfoShift) ServiceManagerServiceFactory.executeService(userView,
                    "ReadShiftByOID", args);

            request.setAttribute("infoTurno", infoTurno);
            request.setAttribute(SessionConstants.SHIFT, infoTurno);
            Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoTurno
                    .getInfoDisciplinaExecucao()) };
            List infoAulasDeTurno = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAulasDeTurno", argsLerAulasDeTurno);
            request.removeAttribute("infoAulasDeTurno");
            if (!infoAulasDeTurno.isEmpty())
                request.setAttribute("infoAulasDeTurno", infoAulasDeTurno);
            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}