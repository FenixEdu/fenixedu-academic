package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author tfc130
 */
public class EditarTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm editarTurnoForm = (DynaActionForm) form;
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //			List infoTurnos =
            //				(ArrayList) session.getAttribute(
            //					"infoTurnosDeDisciplinaExecucao");

            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

            //InfoShift infoTurnoAntigo =
            //	(InfoShift) request.getAttribute("infoTurno");

            Object args[] = { shiftOID };
            InfoShift infoTurnoAntigo = (InfoShift) ServiceManagerServiceFactory.executeService(
                    userView, "ReadShiftByOID", args);

            InfoShift infoTurnoNovo = new InfoShift((String) editarTurnoForm.get("nome"),
                    infoTurnoAntigo.getTipo(), (Integer) editarTurnoForm.get("lotacao"), infoTurnoAntigo
                            .getInfoDisciplinaExecucao());

            Object argsEditarTurno[] = { infoTurnoAntigo, infoTurnoNovo };

            ActionErrors actionErrors = null;
            try {
                ServiceManagerServiceFactory.executeService(userView, "EditarTurno", argsEditarTurno);
            } catch (ExistingServiceException e) {

                throw new ExistingActionException("O Turno", e);

            }

            if (actionErrors == null) {

                return (mapping.findForward("Guardar"));
            }
            return mapping.getInputForward();

        }
        throw new FenixActionException();

    }
}