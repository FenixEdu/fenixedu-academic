package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz & SaraRibeiro
 *  
 */
public class RemoverAulaDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            DynaActionForm editarAulasDeTurnoForm = (DynaActionForm) request
                    .getAttribute("editarAulasDeTurnoForm");

            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));
            Object args[] = { shiftOID };
            InfoShift infoTurno = (InfoShift) ServiceManagerServiceFactory.executeService(userView,
                    "ReadShiftByOID", args);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);

            Integer indexAula = (Integer) editarAulasDeTurnoForm.get("indexAula");

            Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoExecutionCourse) };
            List infoAulas = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAulasDeTurno", argsLerAulasDeTurno);

            InfoLesson infoLesson = (InfoLesson) infoAulas.get(indexAula.intValue());

            Object argsRemoverAula[] = { infoLesson, infoTurno };

            ServiceManagerServiceFactory.executeService(userView, "RemoverAula", argsRemoverAula);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}