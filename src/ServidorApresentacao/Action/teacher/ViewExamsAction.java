/*
 * Created on 13/Mai/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 */
public class ViewExamsAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession(false);
		
		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
	
		UserView userView =
						(UserView) session.getAttribute(SessionConstants.U_VIEW);
					
		Object args[] = { infoSite.getInfoExecutionCourse()};
		
		GestorServicos serviceManager = GestorServicos.manager();
					
					List infoExams =
						(List) serviceManager.executar(
							userView,
							"ReadExams",
							args);	
	
		request.setAttribute("exams",infoExams);
		
		return mapping.findForward("Sucess");
		}

}
