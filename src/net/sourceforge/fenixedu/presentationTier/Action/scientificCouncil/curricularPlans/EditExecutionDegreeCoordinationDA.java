package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/curricularPlans/editExecutionDegreeCoordination", module = "scientificCouncil")
@Forwards( { @Forward(name = "presentCoordination", path = "/scientificCouncil/curricularPlans/presentCoordination.jsp"),
	@Forward(name = "editCoordination", path = "/scientificCouncil/curricularPlans/editCoordination.jsp"),
	@Forward(name = "selectYearAndDegree", path = "/scientificCouncil/curricularPlans/selectYearAndDegree.jsp") })
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
	
	String backTo = String.valueOf(request.getParameter("from"));
	String backPath;
	if(backTo.equals("byYears")){
	    backPath = "/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears&executionYearId=" +  executionDegree.getExecutionYear().getExternalId().toString();
	} else {
	    backPath = "/curricularPlans/editExecutionDegreeCoordination.do?method=prepareEditCoordination&degreeCurricularPlanId=" +  executionDegree.getDegreeCurricularPlan().getIdInternal().toString() ;
	}

	ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
	
	coordsBean.setBackPath(backPath);

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
	    HttpServletResponse response) {

	final Integer coordinatorId = Integer.valueOf(request.getParameter("coordinatorId"));
	Coordinator coordinator = (Coordinator) rootDomainObject.readCoordinatorByOID(coordinatorId);

	final Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));
	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

	if (coordinator.isResponsible()) {
	    coordinator.setAsNotResponsible();
	} else {
	    coordinator.setAsResponsible();
	}

	ExecutionDegreeCoordinatorsBean coordsBean = new ExecutionDegreeCoordinatorsBean(executionDegree);
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");

	return mapping.findForward("editCoordination");
    }

    public ActionForward deleteCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

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
	    HttpServletResponse response) {

	ExecutionDegreeCoordinatorsBean coordsBean = (ExecutionDegreeCoordinatorsBean) getRenderedObject("coordsBean");
	request.setAttribute("coordsBean", coordsBean);
	RenderUtils.invalidateViewState("coordsBean");
	request.setAttribute("startVisible", true);

	return mapping.findForward("editCoordination");
    }
    
    public ActionForward editByYears(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
	ExecutionDegreeCoordinatorsBean sessionBean = (ExecutionDegreeCoordinatorsBean) getRenderedObject("sessionBean");
	if(sessionBean == null) {
	    sessionBean = new ExecutionDegreeCoordinatorsBean();
	    final String executionYearId = String.valueOf(request.getParameter("executionYearId"));
	    if(!executionYearId.equals("null")) {
		ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearId);
		sessionBean.setExecutionYear(executionYear);
	    } else {
		request.setAttribute("sessionBean", sessionBean);
		RenderUtils.invalidateViewState("sessionBean");
		
		return mapping.findForward("selectYearAndDegree");
	    }
	}
	
	List<ExecutionDegree> bachelors = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_DEGREE);
	request.setAttribute("bachelors", bachelors);
	    
	List<ExecutionDegree> masters = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_MASTER_DEGREE);
	request.setAttribute("masters", masters);
	    
	List<ExecutionDegree> integratedMasters = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	request.setAttribute("integratedMasters", integratedMasters);
	    
	List<ExecutionDegree> otherDegrees = new ArrayList<ExecutionDegree>(sessionBean.getExecutionYear().getExecutionDegrees());
	otherDegrees.removeAll(bachelors);
	otherDegrees.removeAll(masters);
	otherDegrees.removeAll(integratedMasters);
	request.setAttribute("otherDegrees", otherDegrees);
	    
	boolean hasYearSelected = true;
	request.setAttribute("hasYearSelected", hasYearSelected);

	request.setAttribute("sessionBean", sessionBean);
	RenderUtils.invalidateViewState("sessionBean");
	
	return mapping.findForward("selectYearAndDegree");
    }
}
