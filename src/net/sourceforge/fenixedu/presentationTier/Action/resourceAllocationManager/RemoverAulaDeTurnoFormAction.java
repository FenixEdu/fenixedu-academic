package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz & SaraRibeiro
 *  
 */
public class RemoverAulaDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

            DynaActionForm editarAulasDeTurnoForm = (DynaActionForm) request
                    .getAttribute("editarAulasDeTurnoForm");

            IUserView userView = UserView.getUser();

            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));
            Object args[] = { shiftOID };
            InfoShift infoTurno = (InfoShift) ServiceManagerServiceFactory.executeService(
                    "ReadShiftByOID", args);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);

            Integer indexAula = (Integer) editarAulasDeTurnoForm.get("indexAula");

            Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoExecutionCourse) };
            List infoAulas = (ArrayList) ServiceManagerServiceFactory.executeService(
                    "LerAulasDeTurno", argsLerAulasDeTurno);

            InfoLesson infoLesson = (InfoLesson) infoAulas.get(indexAula.intValue());

            Object argsRemoverAula[] = { infoLesson, infoTurno };

            ServiceManagerServiceFactory.executeService( "RemoverAula", argsRemoverAula);

            return mapping.findForward("Sucesso");
    }
}