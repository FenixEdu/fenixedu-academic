/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

/**
 * @author lmac1
 *
 */
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;



public class DeleteSectionAction extends FenixAction{
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
	    UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		InfoSection infoSuperiorSection = infoSection.getSuperiorInfoSection();
		
		
		try {
			Object deleteSectionArguments[] = { infoSection };
			ServiceManagerServiceFactory.executeService(userView, "DeleteSection", deleteSectionArguments);

			session.removeAttribute(SessionConstants.INFO_SECTION);
			session.removeAttribute(SessionConstants.SECTIONS);
			
			InfoSite infoSite = infoSection.getInfoSite();
			Object readSectionsArguments[] = { infoSite };
			List allInfoSections = (List) ServiceManagerServiceFactory.executeService(null, "ReadSections", readSectionsArguments);
			
			Collections.sort(allInfoSections);
			session.setAttribute(SessionConstants.SECTIONS, allInfoSections);	
		
		    
        	if(infoSuperiorSection == null) { 
    
					return mapping.findForward("AccessSiteManagement");		
		   	}
		    
		    		session.setAttribute(SessionConstants.INFO_SECTION,infoSuperiorSection);
			        return mapping.findForward("AccessSectionManagement");
		          
		   }
			catch (FenixServiceException fenixServiceException){
					   throw new FenixActionException(fenixServiceException.getMessage());
				   }
			
		}			
			
}
