/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PublicationManagementAction extends FenixAction{
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
	    
		IUserView userView = SessionUtils.getUserView(request);
		
		Object[] args = { userView.getUtilizador()};
		
		SiteView siteView =
			(SiteView) ServiceUtils.executeService(userView, "ReadAuthorPublications", args);
		InfoSitePublications infoSitePublications = (InfoSitePublications)siteView.getComponent();
		
		List listaOthers = (List) CollectionUtils.select(infoSitePublications.getInfoPublications(), new Predicate() {
		    public boolean evaluate(Object obj) {
		        InfoPublication infoPublication = (InfoPublication)obj;
		        if (Integer.parseInt(infoPublication.getYear()) == 0) return true;
		        else return false;
		    }
		}) ;
		List listaPub = infoSitePublications.getInfoPublications();
		
		/** this class is a mere comparator to sort publications */
		class PublicationComparator implements Comparator {
		    public int compare(Object o1, Object o2) {
		        InfoPublication infoPublication1 = (InfoPublication) o1;
		        InfoPublication infoPublication2 = (InfoPublication) o2;
		        int year1,year2;
		        
		        //date sorting rules for publications
//		        try {
		            year1 = Integer.parseInt(infoPublication1.getYear());
		            year2 = Integer.parseInt(infoPublication2.getYear());
		            
		            if (year1 < year2)
		                return 1;
		            if (year1 == year2)
		                return 0;
		            else
		                return -1;
/*		        } catch (NumberFormatException nfe) {
		            throw nfe;
		        }*/
		    }
		}
		
		Collections.sort(listaPub,new PublicationComparator());
		infoSitePublications.setInfoPublications(listaPub);
		request.setAttribute("infoSitePublications",infoSitePublications);
	    
		String msg = (String) request.getAttribute("msg");
		if (msg != null){
		    ActionMessages messages = new ActionMessages();
			messages.add("message1", new ActionMessage(msg));
			saveMessages(request,messages);
		}
		

//		ActionErrors errors = new ActionErrors();
//		errors.add("error1", new ActionError("message.publications.managementDeleted"));
//		saveErrors(request,errors);
		
		return mapping.findForward("show-publications");
	}

	
}
