package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.InfoShiftComparatorByLessonType;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author tfc130
 *  
 */

public class PrepararEditarTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm editarTurnoForm = (DynaActionForm) form;

        InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        if (infoShift == null) {

            HttpSession sessao = request.getSession(false);

            if (sessao != null) {

                DynaActionForm manipularTurnosForm = (DynaActionForm) request
                        .getAttribute("manipularTurnosForm");
                Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");

                IUserView userView = (IUserView) sessao.getAttribute("UserView");

                //List infoTurnos = (ArrayList)
                // request.getAttribute("infoTurnosDeDisciplinaExecucao");
                InfoExecutionCourse iDE = (InfoExecutionCourse) request
                        .getAttribute(SessionConstants.EXECUTION_COURSE);
                Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
                List infoTurnos = (List) ServiceManagerServiceFactory.executeService(userView,
                        "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
                Collections.sort(infoTurnos, new InfoShiftComparatorByLessonType());

                //InfoShift infoTurno =
                infoShift = (InfoShift) infoTurnos.get(indexTurno.intValue());

            } else
                throw new Exception();
            // nao ocorre... pedido passa pelo filtro Autorizacao
        }

        request.setAttribute("infoTurno", infoShift);
        request.setAttribute(SessionConstants.SHIFT, infoShift);
        editarTurnoForm.set("nome", infoShift.getNome());
        editarTurnoForm.set("lotacao", infoShift.getLotacao());

        return mapping.findForward("Sucesso");
    }
}