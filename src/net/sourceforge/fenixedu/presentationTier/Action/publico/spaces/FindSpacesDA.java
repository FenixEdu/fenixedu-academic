package net.sourceforge.fenixedu.presentationTier.Action.publico.spaces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceState;
import net.sourceforge.fenixedu.domain.space.Blueprint.BlueprintTextRectangles;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.spaceManager.ManageSpaceBlueprintsDA;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.spaceBlueprints.SpaceBlueprintsDWGProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
	RenderUtils.invalidateViewState("beanWithLabelToSearchID");
	return mapping.findForward("listFoundSpaces");
    }
    
    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	FindSpacesBean bean = (FindSpacesBean) getRenderedObject("beanWithLabelToSearchID");	
	if(bean != null) {	    
	    
	    String labelToSearch = bean.getLabelToSearch();
	    Campus campus = bean.getCampus();
	    Building building = bean.getBuilding();
	    
	    if(campus != null && building == null && (labelToSearch == null || StringUtils.isEmpty(labelToSearch.trim()))) {
		addActionMessage(request, "error.findSpaces.empty.building");
		request.setAttribute("bean", bean);
		return mapping.findForward("listFoundSpaces");
	    }
	    
	    if(campus == null && (labelToSearch == null || StringUtils.isEmpty(labelToSearch.trim()))) {
		 addActionMessage(request, "error.findSpaces.empty.labelToSearch");
		 request.setAttribute("bean", bean);
		 return mapping.findForward("listFoundSpaces");
	    }	    	    
	    
	    List<FindSpacesBean> result = new ArrayList<FindSpacesBean>();
	    Set<Space> resultSpaces = Space.findSpaces(labelToSearch, campus, building, bean.getSearchType());
	    for (Space space : resultSpaces) {
		result.add(new FindSpacesBean(space, bean.getSearchType(), ExecutionPeriod.readActualExecutionPeriod()));
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
	RenderUtils.invalidateViewState("beanWithLabelToSearchID");
	return mapping.findForward("listFoundSpaces");
    }
    
    public ActionForward searchWithoutExtraOptions(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	FindSpacesBean bean = (FindSpacesBean) getRenderedObject("beanWithLabelToSearchID");
	bean.setExtraOptions(false);
	bean.setCampus(null);
	bean.setBuilding(null);	
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("beanWithLabelToSearchID");
	return mapping.findForward("listFoundSpaces");
    }
       
    public ActionForward viewSpace(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	Space space = getSpaceFromParameter(request);	
	if(space != null) {
	    //setBlueprintTextRectangles(request, space);	
	    Set<Space> containedSpaces = space.getContainedSpacesByState(SpaceState.ACTIVE);
	    request.setAttribute("containedSpaces", containedSpaces);
	    request.setAttribute("selectedSpace", new FindSpacesBean(space, ExecutionPeriod.readActualExecutionPeriod()));	    
	}
	
	return mapping.findForward("viewSelectedSpace");
    }
    
    public ActionForward viewSpaceBlueprint(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	return new ManageSpaceBlueprintsDA().view(mapping, actionForm, request, response);	
    }
    
    // Private Methods
    
    private Space getSpaceFromParameter(final HttpServletRequest request) {
	final String spaceIDString = request.getParameter("spaceID");
	final Integer spaceID = Integer.valueOf(spaceIDString);
	return (Space) rootDomainObject.readResourceByOID(spaceID);
    }
    
    private void setBlueprintTextRectangles(HttpServletRequest request, Space space) throws IOException {

	Blueprint mostRecentBlueprint = space.getMostRecentBlueprint();
	Boolean suroundingSpaceBlueprint = mostRecentBlueprint == null;
	mostRecentBlueprint = (mostRecentBlueprint == null) ? space.getSuroundingSpaceMostRecentBlueprint() : mostRecentBlueprint;

	if (mostRecentBlueprint != null) {
	    	    
	    final BlueprintFile blueprintFile = mostRecentBlueprint.getBlueprintFile();
	    final byte[] blueprintBytes = blueprintFile.getContent().getBytes();
	    final InputStream inputStream = new ByteArrayInputStream(blueprintBytes);
	    BlueprintTextRectangles blueprintTextRectangles = SpaceBlueprintsDWGProcessor.getBlueprintTextRectangles(inputStream, mostRecentBlueprint.getSpace(), false, false, true, false, null);

	    request.setAttribute("mostRecentBlueprint", mostRecentBlueprint);
	    request.setAttribute("blueprintTextRectangles", blueprintTextRectangles);	    	   
	    request.setAttribute("suroundingSpaceBlueprint", suroundingSpaceBlueprint);
	}	
    }
}
