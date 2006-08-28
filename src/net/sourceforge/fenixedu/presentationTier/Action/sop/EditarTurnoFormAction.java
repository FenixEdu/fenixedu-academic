package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
            IUserView userView = getUserView(request);

            //			List infoTurnos =
            //				(ArrayList) session.getAttribute(
            //					"infoTurnosDeDisciplinaExecucao");

            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

            //InfoShift infoTurnoAntigo =
            //	(InfoShift) request.getAttribute("infoTurno");

            Object args[] = { shiftOID };
            InfoShift infoTurnoAntigo = (InfoShift) ServiceManagerServiceFactory.executeService(
                    userView, "ReadShiftByOID", args);

            final InfoShiftEditor infoShiftEditor = new InfoShiftEditor();
            infoShiftEditor.setNome((String) editarTurnoForm.get("nome"));
            infoShiftEditor.setTipo(infoTurnoAntigo.getTipo());
            infoShiftEditor.setLotacao((Integer) editarTurnoForm.get("lotacao"));
            infoShiftEditor.setInfoDisciplinaExecucao(infoTurnoAntigo.getInfoDisciplinaExecucao());

            Object argsEditarTurno[] = { infoTurnoAntigo, infoShiftEditor };

            ActionErrors actionErrors = null;
            try {
                ServiceManagerServiceFactory.executeService(userView, "EditarTurno", argsEditarTurno);
            } catch (ExistingServiceException e) {

                throw new ExistingActionException("O Shift", e);

            }

            if (actionErrors == null) {

                return (mapping.findForward("Guardar"));
            }
            return mapping.getInputForward();

        }
        throw new FenixActionException();

    }
}