package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoRoom;
import ServidorApresentacao.Action.FenixAction;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm indexForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);
		if (session != null) {
			List infoRooms = (List) session.getAttribute("publico.infoRooms");
			InfoRoom infoRoom =	(InfoRoom) infoRooms.get(((Integer) indexForm.get("index")).intValue());
			session.removeAttribute("publico.infoRoom");
			session.setAttribute("publico.infoRoom", infoRoom);
			  
			return mapping.findForward("Sucess");
		} else
			throw new Exception(); 
		}
}