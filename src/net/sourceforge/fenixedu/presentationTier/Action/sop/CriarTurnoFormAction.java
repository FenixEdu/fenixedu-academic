package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class CriarTurnoFormAction extends FenixExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm criarTurnoForm = (DynaActionForm) form;
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute("UserView");
            InfoExecutionCourse courseView = RequestUtils.getExecutionCourseBySigla(request,
                    (String) criarTurnoForm.get("courseInitials"));

            final InfoShiftEditor infoShiftEditor = new InfoShiftEditor();
            infoShiftEditor.setNome((String) criarTurnoForm.get("nome"));
            infoShiftEditor.setTipo(ShiftType.valueOf((String) criarTurnoForm.get("tipoAula")));
            infoShiftEditor.setLotacao((Integer) criarTurnoForm.get("lotacao"));
            infoShiftEditor.setInfoDisciplinaExecucao(courseView);
            Object argsCriarTurno[] = { infoShiftEditor };
            try {
                ServiceUtils.executeService(userView, "CriarTurno", argsCriarTurno);
            } catch (ExistingServiceException ex) {
                throw new ExistingActionException("O Shift", ex);
            }

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}