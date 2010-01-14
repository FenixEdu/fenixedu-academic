package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

@Mapping(path = "/curricularPlans/editExecutionDegreeCoordination", module = "scientificCouncil")
@Forwards( { @Forward(name = "presentCoordination", path = "/scientificCouncil/curricularPlans/presentCoordination.jsp"),
	@Forward(name = "editCoordination", path = "/scientificCouncil/curricularPlans/editCoordination.jsp") })
public class EditExecutionDegreeCoordinationDA extends FenixDispatchAction {

    public ActionForward prepareEditCoordination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Integer degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanId"));
	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

	final Set<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegreesSet();

	request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
	request.setAttribute("executionDegreesSet", executionDegrees);

	return mapping.findForward("presentCoordination");

    }

    public ActionForward editCoordination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));
	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

	ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);

	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");

	return mapping.findForward("editCoordination");
    }

    public ActionForward addCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeCoordinatorsBean coordsBean = (ExecutionDegreeCoordinatorsBean) getRenderedObject("coordsBean");
	Coordinator.createCoordinator(coordsBean.getExecutionDegree(), coordsBean.getNewCoordinator(), Boolean.valueOf(false));

	coordsBean.setNewCoordinator(null);
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");
	request.setAttribute("startVisible", true);

	return mapping.findForward("editCoordination");
    }
    
    public ActionForward switchResponsability(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response){
	
	final Integer coordinatorId = Integer.valueOf(request.getParameter("coordinatorId"));
	Coordinator coordinator = (Coordinator) rootDomainObject.readCoordinatorByOID(coordinatorId);

	final Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));
	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

	
	if(coordinator.isResponsible()){
	    coordinator.setAsNotResponsible();
	}else{
	    coordinator.setAsResponsible();
	}
	
	ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");

	return mapping.findForward("editCoordination");
    }
    
    public ActionForward deleteCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response){
	
	final Integer coordinatorId = Integer.valueOf(request.getParameter("coordinatorId"));
	Coordinator coordinator = (Coordinator) rootDomainObject.readCoordinatorByOID(coordinatorId);
	
	final Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));
	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
	
	
	coordinator.removeCoordinator();
	
	ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");

	return mapping.findForward("editCoordination");
    }
    
    public ActionForward invalidAddCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response){
	
	ExecutionDegreeCoordinatorsBean coordsBean = (ExecutionDegreeCoordinatorsBean)getRenderedObject("coordsBean");
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");
	request.setAttribute("startVisible", true);
	
	return mapping.findForward("editCoordination");
    }
}
