package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepararCriarTurnoFormAction extends FenixExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            /** to be sure that execution course list is in session */
            SessionUtils.getExecutionCourses(request);
            List tiposAula = new ArrayList();
            tiposAula.add(new LabelValueBean("escolher", ""));
            tiposAula.add(new LabelValueBean("Teorica", ShiftType.TEORICA.name()));
            tiposAula.add(new LabelValueBean("Pratica", ShiftType.PRATICA.name()));
            tiposAula.add(new LabelValueBean("Teorico-Pratica", ShiftType.TEORICO_PRATICA.name()));
            tiposAula.add(new LabelValueBean("Laboratorial", ShiftType.LABORATORIAL.name()));
            tiposAula.add(new LabelValueBean("Duvidas", ShiftType.DUVIDAS.name()));
            tiposAula.add(new LabelValueBean("Reserva", ShiftType.RESERVA.name()));
            request.setAttribute("tiposAula", tiposAula);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}