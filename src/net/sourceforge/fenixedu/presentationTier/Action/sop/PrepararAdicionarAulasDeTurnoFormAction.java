package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseKeyAndLessonType;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class PrepararAdicionarAulasDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute("UserView");
            List infoAulasDeTurno = (ArrayList) request.getAttribute("infoAulasDeTurno");

            //InfoShift infoTurno = (InfoShift)
            // request.getAttribute("infoTurno");
            InfoShift infoTurno = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

            // Ler Aulas de Disciplina em Execucao e Tipo
            InfoExecutionCourse infoDisciplina = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);
            ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = new ExecutionCourseKeyAndLessonType(
                    infoTurno.getTipo(), infoDisciplina.getSigla());

            Object argsLerAulasDeDisciplinaETipo[] = { tipoAulaAndKeyDisciplinaExecucao, infoDisciplina };
            List infoAulasDeDisciplina = (ArrayList) ServiceManagerServiceFactory.executeService(
                    userView, "LerAulasDeDisciplinaExecucaoETipo", argsLerAulasDeDisciplinaETipo);

            if (infoAulasDeTurno != null)
                infoAulasDeDisciplina.removeAll(infoAulasDeTurno);
            request.removeAttribute("infoAulasDeDisciplinaExecucao");
            if (!infoAulasDeDisciplina.isEmpty())
                request.setAttribute("infoAulasDeDisciplinaExecucao", infoAulasDeDisciplina);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}