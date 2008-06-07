package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.AccessGroupBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AccessGroupsManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form,	   	        
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	Role role = Role.getRoleByRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER);
	request.setAttribute("resourceAllocationRole", role);
	return mapping.findForward("prepareAccessGroupsManagement");	
    }

    public ActionForward addPersonToAccessGroup(ActionMapping mapping, ActionForm form,	   	        
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException, 
	    FenixFilterException, FenixServiceException {		

	AccessGroupBean bean = (AccessGroupBean) getRenderedObject("PersonToAccessGroupBeanID");
	ResourceAllocationAccessGroupType accessGroupType = bean != null ? bean.getAccessGroupType() : null;
	Person person = bean != null ? bean.getPerson() : null;
	Role role = Role.getRoleByRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER);
	
	if(person == null) {
	    addActionMessage(request, "error.ResourceAllocation.access.groups.empty.person");
	    request.setAttribute("resourceAllocationRole", role);
	    return mapping.findForward("prepareAccessGroupsManagement");	
	}
	
	try {
	    PersonGroup personGroup = new PersonGroup(person);	
	    executeService(request, "AddPersonToAccessGroup", new Object[] {accessGroupType, personGroup.getExpression(), true, role});

	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    request.setAttribute("resourceAllocationRole", role);
	    return mapping.findForward("prepareAccessGroupsManagement");
	}

	RenderUtils.invalidateViewState("PersonToAccessGroupBeanID");
	request.setAttribute("resourceAllocationRole", role);
	return mapping.findForward("prepareAccessGroupsManagement");	
    }

    public ActionForward removePersonFromAccessGroup(ActionMapping mapping, ActionForm form,	   	        
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException, 
	    FenixFilterException, FenixServiceException {		

	Role role = Role.getRoleByRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER);
	String groupExpression = getGroupExpressionFromRequest(request);
	ResourceAllocationAccessGroupType groupType = getAccessGroupTypeFromRequest(request);

	try {	    
	    executeService(request, "AddPersonToAccessGroup", new Object[] {groupType, groupExpression, false, role});

	} catch (DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    request.setAttribute("resourceAllocationRole", role);
	    return mapping.findForward("prepareAccessGroupsManagement");
	}

	request.setAttribute("resourceAllocationRole", role);
	return mapping.findForward("prepareAccessGroupsManagement");	
    }


    // Private Methods

    private String getGroupExpressionFromRequest(final HttpServletRequest request) {
	return request.getParameter("expression");	
    }

    private ResourceAllocationAccessGroupType getAccessGroupTypeFromRequest(final HttpServletRequest request) {
	String parameter = request.getParameter("accessGroupType");
	return parameter != null ? ResourceAllocationAccessGroupType.valueOf(parameter) : null;
    }
}
