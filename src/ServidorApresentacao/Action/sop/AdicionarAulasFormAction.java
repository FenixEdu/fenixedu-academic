package ServidorApresentacao.Action.sop;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import DataBeans.InfoShiftServiceResult;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class AdicionarAulasFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    public static String THEORETICAL_HOURS_LIMIT_EXCEEDED = "errors.shift.theoretical.hours.limit.exceeded";

    public static String PRATICAL_HOURS_LIMIT_EXCEEDED = "errors.shift.pratical.hours.limit.exceeded";

    public static String THEO_PRAT_HOURS_LIMIT_EXCEEDED = "errors.shift.theo.pratical.hours.limit.exceeded";

    public static String LAB_HOURS_LIMIT_EXCEEDED = "errors.shift.lab.hours.limit.exceeded";

    public static String THEORETICAL_HOURS_LIMIT_REACHED = "errors.shift.theoretical.hours.limit.reached";

    public static String PRATICAL_HOURS_LIMIT_REACHED = "errors.shift.pratical.hours.limit.reached";

    public static String THEO_PRAT_HOURS_LIMIT_REACHED = "errors.shift.theo.pratical.hours.limit.reached";

    public static String LAB_HOURS_LIMIT_REACHED = "errors.shift.lab.hours.limit.reached";

    public static String UNKNOWN_ERROR = "errors.unknown";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm adicionarAulasForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            //DynaActionForm manipularTurnosForm =
            //	(DynaActionForm) request.getAttribute("manipularTurnosForm");
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
            //Integer indexTurno =
            //	(Integer) manipularTurnosForm.get("indexTurno");
            //List infoTurnos =
            //	(ArrayList) request.getAttribute(
            //		"infoTurnosDeDisciplinaExecucao");
            InfoShift infoTurno = (InfoShift) request.getAttribute(SessionConstants.SHIFT);
            //	infoTurnos.get(indexTurno.intValue());

            String[] classesList = (String[]) adicionarAulasForm.get("lessonsList");

            Object argsAdicionarAula[] = { infoTurno, classesList };
            List serviceResult = null;
            try {
                serviceResult = (List) ServiceUtils.executeService(userView, "AdicionarAula",
                        argsAdicionarAula);
            } catch (ExistingServiceException ex) {
                throw new ExistingActionException("Essa relação Turno-Aula", ex);
            }
            ActionErrors actionErrors = getActionErrors(serviceResult, infoTurno);
            if (actionErrors.isEmpty()) {

                return mapping.findForward("Sucesso");
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        throw new Exception();

    }

    private ActionErrors getActionErrors(List serviceResult, InfoShift infoShift) {
        ActionErrors actionErrors = new ActionErrors();
        Iterator iter = serviceResult.iterator();
        while (iter.hasNext()) {
            InfoShiftServiceResult result = (InfoShiftServiceResult) iter.next();

            switch (result.getMessageType()) {
            case InfoShiftServiceResult.SUCESS:
                break;
            case InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED:
                actionErrors.add(THEORETICAL_HOURS_LIMIT_EXCEEDED, new ActionError(
                        THEORETICAL_HOURS_LIMIT_EXCEEDED, ""
                                + infoShift.getInfoDisciplinaExecucao().getTheoreticalHours()));
                break;
            case InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED:
                actionErrors.add(THEORETICAL_HOURS_LIMIT_REACHED, new ActionError(
                        THEORETICAL_HOURS_LIMIT_REACHED, ""
                                + infoShift.getInfoDisciplinaExecucao().getTheoreticalHours()));
                break;
            case InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED:
                actionErrors.add(PRATICAL_HOURS_LIMIT_EXCEEDED, new ActionError(
                        PRATICAL_HOURS_LIMIT_EXCEEDED, ""
                                + infoShift.getInfoDisciplinaExecucao().getPraticalHours()));
                break;
            case InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED:
                actionErrors.add(PRATICAL_HOURS_LIMIT_REACHED, new ActionError(
                        PRATICAL_HOURS_LIMIT_REACHED, ""
                                + infoShift.getInfoDisciplinaExecucao().getPraticalHours()));
                break;
            case InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED:
                actionErrors.add(THEO_PRAT_HOURS_LIMIT_EXCEEDED, new ActionError(
                        THEO_PRAT_HOURS_LIMIT_EXCEEDED, ""
                                + infoShift.getInfoDisciplinaExecucao().getTheoPratHours()));
                break;
            case InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED:
                actionErrors.add(THEO_PRAT_HOURS_LIMIT_REACHED, new ActionError(
                        THEO_PRAT_HOURS_LIMIT_REACHED, ""
                                + infoShift.getInfoDisciplinaExecucao().getTheoPratHours()));
                break;
            case InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED:
                actionErrors.add(LAB_HOURS_LIMIT_EXCEEDED, new ActionError(LAB_HOURS_LIMIT_EXCEEDED, ""
                        + infoShift.getInfoDisciplinaExecucao().getLabHours()));
                break;
            case InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED:
                actionErrors.add(LAB_HOURS_LIMIT_REACHED, new ActionError(LAB_HOURS_LIMIT_REACHED, ""
                        + infoShift.getInfoDisciplinaExecucao().getLabHours()));
                break;
            default:
                actionErrors.add(UNKNOWN_ERROR, new ActionError(UNKNOWN_ERROR));
                break;
            }

        }

        return actionErrors;
    }

}