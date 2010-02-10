package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/viewDegree", module = "externalSupervision")
@Forwards( {
    	@Forward(name = "selectDegree", path = "/externalSupervision/consult/selectDegree.jsp")})
public class ExternalSupervisorViewDegreeDA extends FenixDispatchAction{
    
    public ActionForward beginTaskFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	final IUserView userView = UserView.getUser();
	final Person supervisor = userView.getPerson();
	
	RegistrationProtocol protocol = supervisor.getOnlyRegistrationProtocol();
	ExternalSupervisorViewsBean bean;
	
	if(protocol == null){
	    bean = new ExternalSupervisorViewsBean(ExecutionYear.readCurrentExecutionYear());
	    boolean selectProtocol = true;
	    request.setAttribute("selectProtocol", selectProtocol);
	    
	} else {
	    bean  = new ExternalSupervisorViewsBean(ExecutionYear.readCurrentExecutionYear(), protocol);
	}
	
	request.setAttribute("sessionBean", bean);
	return mapping.findForward("selectDegree");
    }
    
    public ActionForward degreePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	final IUserView userView = UserView.getUser();
	final Person supervisor = userView.getPerson();
	
	if(supervisor.getRegistrationProtocolsCount() > 1){
	    boolean selectProtocol = true;
	    request.setAttribute("selectProtocol", selectProtocol);
	}
	
	ExternalSupervisorViewsBean bean = (ExternalSupervisorViewsBean) getRenderedObject("sessionBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("sessionBean", bean);
	
	return mapping.findForward("selectDegree");
    }
    
    public ActionForward showStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	ExternalSupervisorViewsBean bean = (ExternalSupervisorViewsBean) getRenderedObject("sessionBean");
	
	if(bean == null){
	    final IUserView userView = UserView.getUser();
	    final Person supervisor = userView.getPerson();
	    RegistrationProtocol protocol = supervisor.getRegistrationProtocols().get(0);
	    
	    final String executionYearId = request.getParameter("executionYearId");
	    ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearId);
	    final String executionDegreeId = request.getParameter("executionDegreeId");
	    ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);
	    
	    bean = new ExternalSupervisorViewsBean(executionYear, protocol);
	    bean.setExecutionDegree(executionDegree);
	}
	
	bean.generateStudentsFromDegree();
	Boolean degreeSelected = true;
	
	request.setAttribute("sessionBean", bean);
	request.setAttribute("hasChosenDegree", degreeSelected);
	
	return mapping.findForward("selectDegree");
    }

}
