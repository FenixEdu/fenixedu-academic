/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ClassKey;
import DataBeans.InfoClass;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 */
public class ViewClassTimeTableAction extends Action {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		String className = request.getParameter("className");

		if (className == null)
			return mapping.getInputForward();

		Object[] args = { new ClassKey(className)};
		List lessons =
			(List) ServiceUtils.executeService(null, "LerAulasDeTurma", args);
		HttpSession session = request.getSession();
		InfoClass classView = new InfoClass();
		classView.setNome(className);
		session.setAttribute(SessionConstants.CLASS_VIEW,classView);
		session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);

		return mapping.findForward("Sucess");
	}
}
