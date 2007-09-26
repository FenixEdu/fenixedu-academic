package net.sourceforge.fenixedu.presentationTier.Action.publico.spaces;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public class FindSpacesDA extends FenixDispatchAction {
    
    public ActionForward prepareSearchSpaces(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	FindSpacesBean bean = new FindSpacesBean();
	request.setAttribute("bean", bean);	
	return mapping.findForward("listFoundSpaces");
    }
    
    public ActionForward prepareSearchSpacesPostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	FindSpacesBean bean = (FindSpacesBean) getRenderedObject("beanWithLabelToSearchID");	
	request.setAttribute("bean", bean);	
	return mapping.findForward("listFoundSpaces");
    }
    
    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	FindSpacesBean bean = (FindSpacesBean) getRenderedObject("beanWithLabelToSearchID");
	
	if(bean != null) {
	    
	    String labelToSearch = bean.getLabelToSearch();
	    Campus campus = bean.getCampus();
	    Building building = bean.getBuilding();
	    
	    Set<FindSpacesBean> result = new HashSet<FindSpacesBean>();
	    Set<Space> resultSpaces = Space.findSpaces(labelToSearch, campus, building);
	    for (Space space : resultSpaces) {
		result.add(new FindSpacesBean(space));
	    }
	   
	    request.setAttribute("foundSpaces", result);
	}
	
	request.setAttribute("bean", bean);
	return mapping.findForward("listFoundSpaces");
    }
    
    public ActionForward searchWithExtraOptions(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	FindSpacesBean bean = (FindSpacesBean) getRenderedObject("beanWithLabelToSearchID");
	bean.setExtraOptions(true);
	request.setAttribute("bean", bean);
	return mapping.findForward("listFoundSpaces");
    }
       
    public ActionForward viewSpace(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	Space space = getSpaceFromParameter(request);
	
	if(space != null) {
	    
	}
	
	return mapping.findForward("viewSelectedSpace");
    }
    
    // Private Methods
    
    private Space getSpaceFromParameter(final HttpServletRequest request) {
	final String spaceIDString = request.getParameter("spaceID");
	final Integer spaceID = Integer.valueOf(spaceIDString);
	return (Space) rootDomainObject.readResourceByOID(spaceID);
    }
}
