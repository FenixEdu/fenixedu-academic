/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSite;
import DataBeans.gesdis.InfoTeacher;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadSite extends FenixAction {

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
			InfoTeacher infoTeacher = (InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);	
			List infoSites = (List) session.getAttribute(SessionConstants.INFO_SITES_LIST);
			
			String index = (String) request.getParameter("index");
			System.out.println("index= "+index);
			InfoSite site = (InfoSite) infoSites.get((new Integer(index)).intValue());
			session.setAttribute(SessionConstants.INFO_SITE,site);	
			//TODO: Read Sections
			//TODO: Read last Anouncement	
				
			
			return mapping.findForward("viewSite");}


}
