package ServidorApresentacao.Action.publico;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class SelectSemesterAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		SessionUtils.validSessionVerification(request, mapping);			

		HttpSession session = request.getSession(false);
//	TODO: No futuro os periodos (semestres) devem ser lidos da base de dados.
		if (session != null) {
// TODO:			 No futuro, os tipos de salas devem ser lidos da BD 
			ArrayList types = new ArrayList();
			types.add(new LabelValueBean("Escolher", null));
			types.add(new LabelValueBean("1º semestre", "1"));
			types.add(new LabelValueBean("2º semestre", "2"));
			session.setAttribute("publico.semester", types);
			
			return mapping.findForward("Sucess");
		} else
			throw new Exception(); 
		}
}