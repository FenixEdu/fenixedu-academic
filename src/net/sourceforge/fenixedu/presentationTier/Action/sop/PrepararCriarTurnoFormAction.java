package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.TipoAula;

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
            tiposAula.add(new LabelValueBean("Teorica", (new Integer(TipoAula.TEORICA)).toString()));
            tiposAula.add(new LabelValueBean("Pratica", (new Integer(TipoAula.PRATICA)).toString()));
            tiposAula.add(new LabelValueBean("Teorico-Pratica", (new Integer(TipoAula.TEORICO_PRATICA))
                    .toString()));
            tiposAula.add(new LabelValueBean("Laboratorial", (new Integer(TipoAula.LABORATORIAL))
                    .toString()));
            tiposAula.add(new LabelValueBean("Duvidas", (new Integer(TipoAula.DUVIDAS)).toString()));
            tiposAula.add(new LabelValueBean("Reserva", (new Integer(TipoAula.RESERVA)).toString()));
            request.setAttribute("tiposAula", tiposAula);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}