package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class PrepareConsultRoomsFormAction extends FenixContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		super.execute(mapping, form, request, response);		

		HttpSession session = request.getSession(true);
		if (session != null) {
			//TODO: No futuro, os edificios devem ser lidos da BD 
			List buildings = Util.readExistingBuldings("*", null);
			request.setAttribute("publico.buildings", buildings);

			//TODO: No futuro, os tipos de salas devem ser lidos da BD 
			ArrayList types = new ArrayList();
			types.add(new LabelValueBean("*", null));
			types.add(
				new LabelValueBean(
					"Anfiteatro",
					(new Integer(TipoSala.ANFITEATRO)).toString()));
			types.add(
				new LabelValueBean(
					"Laboratório",
					(new Integer(TipoSala.LABORATORIO)).toString()));
			types.add(
				new LabelValueBean(
					"Plana",
					(new Integer(TipoSala.PLANA)).toString()));
			request.setAttribute("publico.types", types);

			return mapping.findForward("Sucess");
		} 
			throw new Exception();
	}
}
