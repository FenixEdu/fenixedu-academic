/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action.teacher;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac2
 */

public class ViewSectionAction extends FenixAction {
	/*
	 * Created on 7/Abr/2003
	 *
	 * To change this generated comment go to 
	 * Window>Preferences>Java>Code Generation>Code Template
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		

		HttpSession session = request.getSession(false);

		String indexString = (String) request.getParameter("index");

		if (indexString != null) {

			Integer index = new Integer(indexString);

			List sectionsList =
				(List) session.getAttribute(SessionConstants.SECTIONS);

			InfoSection infoSection =
				(InfoSection) sectionsList.get(index.intValue());

			session.setAttribute(SessionConstants.INFO_SECTION, infoSection);

			GestorServicos gestor = GestorServicos.manager();

			Object argsViewSection[] = { infoSection };

			List infoItems =
				(List) gestor.executar(null, "ReadItems", argsViewSection);

			session.setAttribute(
				SessionConstants.INFO_SECTION_ITEMS_LIST,
				infoItems);
				
			Iterator iter = infoItems.iterator();
			while(iter.hasNext()){
				InfoItem item = (InfoItem) iter.next();
				System.out.println(item);	
			}

		}
		return mapping.findForward("Sucess");

	}
}
