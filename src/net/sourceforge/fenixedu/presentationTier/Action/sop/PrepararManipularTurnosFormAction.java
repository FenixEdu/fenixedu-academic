package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.comparators.InfoShiftComparatorByLessonType;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */

public class PrepararManipularTurnosFormAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            // Ler Turnos de Disciplinas em Execucao
            InfoExecutionCourse iDE = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);
            Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };

            List infoTurnosDeDisciplinaExecucao = (List) ServiceManagerServiceFactory.executeService(
                    userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);

            Collections.sort(infoTurnosDeDisciplinaExecucao, new InfoShiftComparatorByLessonType());

            request.removeAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY);
            if (infoTurnosDeDisciplinaExecucao.size() > 0)
                request.setAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY,
                        infoTurnosDeDisciplinaExecucao);

            request.removeAttribute(SessionConstants.SHIFT);
            request.removeAttribute(SessionConstants.CLASS_VIEW);
            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}