/*
 * Created on Jun, 2013
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.dissertation.Dissertation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.ExecutionDegreeCoordinatorsBean;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/dissertationManagement", module = "teacher", scope = "request", parameter = "method")
@Forwards({
    	@Forward(name="chooseExecutionYear",
        	path="/teacher/listDissertations.jsp", tileProperties=@Tile(navLocal="/teacher/commons/navigationBarIndex.jsp",
        		title = "private.teacher.dissertations", bundle = "TITLES_RESOURCES")),
        @Forward(name = "listDissertations",
        	path = "/teacher/listDissertations.jsp",
        	tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp",
        		title = "private.teacher.dissertations", bundle = "TITLES_RESOURCES")) })

public class DissertationManagementAction extends FenixDispatchAction {

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException,
            IllegalAccessException, InstantiationException {
    	final IUserView userView = UserView.getUser();
        Boolean hasYearSelected = false;
        ExecutionDegreeCoordinatorsBean sessionBean = new ExecutionDegreeCoordinatorsBean();
        final String executionYearId = String.valueOf(request.getParameter("executionYearId"));
        ExecutionYear executionYear = null;
       	for (ExecutionYear testExecutionYear : rootDomainObject.getExecutionYears()) {
       		if(testExecutionYear.getExternalId().equals(executionYearId)) {
       			executionYear = testExecutionYear;
       		}
       	}
        request.setAttribute("hasYearSelected", hasYearSelected);               
        sessionBean.setExecutionYear(executionYear);
        request.setAttribute("sessionBean", sessionBean);
        return mapping.findForward("listDissertations");
    }

	public ActionForward listDissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException,
            IllegalAccessException, InstantiationException {
        final IUserView userView = UserView.getUser();
        ExecutionDegreeCoordinatorsBean sessionBean = getRenderedObject("sessionBean");
        ExecutionYear chosenExecutionYear = sessionBean.getExecutionYear();
        Boolean hasYearSelected = null;
        if (chosenExecutionYear == null) {
        	hasYearSelected = false;
        } else {
        	hasYearSelected = true;
        	
        }
        request.setAttribute("chosenExecutionYear", chosenExecutionYear);
        request.setAttribute("hasYearSelected", hasYearSelected);
//        if (sessionBean == null) {
//            sessionBean = new ExecutionDegreeCoordinatorsBean();
//            final String executionYearId = String.valueOf(request.getParameter("executionYearId"));
//            ExecutionYear executionYear = null;
//            if (!executionYearId.equals(null)) {
//            	for (ExecutionYear testExecutionYear : rootDomainObject.getExecutionYears()) {
//            		if(testExecutionYear.getExternalId().equals(executionYearId)) {
//            			executionYear = testExecutionYear;
//            		}
//            	}
//                hasYearSelected = true;
                
//                sessionBean.setExecutionYear(executionYear);
//                request.setAttribute("sessionBean", sessionBean);
//                return mapping.findForward("listDissertations");
//            } else {
//                request.setAttribute("sessionBean", sessionBean);
//                return mapping.findForward("listDissertations");
//            }
//        }
//    	List<ExecutionDegree> inputExecutionDegrees = sessionBean.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_MASTER_DEGREE);
//        final DynaActionForm finalWorkForm = (DynaActionForm) form;
		
        /*List<ExecutionYear> inputExecutionYears = rootDomainObject.getExecutionYears();
        ExecutionYear idInternalOfInputExecutionYear = ExecutionYear.readCurrentExecutionYear();//(String) finalWorkForm.get("executionYear");
        ExecutionYear outputExecutionYear = null;
        for (ExecutionYear executionYear : inputExecutionYears) {
        	if (executionYear.getIdInternal().toString().equals(idInternalOfInputExecutionYear)) {
        		outputExecutionYear = executionYear;
        	}
        }*/
        
        List<Dissertation> inputDissertations = rootDomainObject.getDissertations();
        List<Dissertation> orientatorProposals = new LinkedList<Dissertation>();
        List<Dissertation> coorientatorProposals = new LinkedList<Dissertation>();

        List<Dissertation> orientatorDissertations = new LinkedList<Dissertation>();
        List<Dissertation> coorientatorDissertations = new LinkedList<Dissertation>();
        
        for (Dissertation dissertation : inputDissertations) {
        	dissertation.setOrientator(Person.readPersonByIstUsername("ist14683"));
//        	if (dissertation.getOrientator() != null) {
        		if (dissertation.getOrientator().equals(userView.getPerson()) && dissertation.getSchedulings().get(0).getExecutionYear().equals(chosenExecutionYear)) {
        				
        			/*&& (dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        						|| dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED))*///) {
           			orientatorProposals.add(dissertation);
            	}
        		/*if (dissertation.getOrientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED)) {
        			orientatorDissertations.add(dissertation);
            	}
        	}
        	if (dissertation.getCoorientator() != null) {
        		if (dissertation.getCoorientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& (dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        						|| dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED))) {
           			coorientatorProposals.add(dissertation);
            	}
        		if (dissertation.getOrientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED)) {
        			coorientatorDissertations.add(dissertation);
            	}*/
//        	}
        	//orientatorProposals.add(dissertation);
        }
        request.setAttribute("orientatorProposals", orientatorProposals);
        request.setAttribute("coorientatorProposals", coorientatorProposals);
        request.setAttribute("orientatorDissertations", orientatorDissertations);
        request.setAttribute("coorientatorDissertations", coorientatorDissertations);
        //request.setAttribute("executionYear", outputExecutionYear);
        //request.setAttribute("executionYearId", outputExecutionYear.getExternalId());
        
        return mapping.findForward("listDissertations");
    }


    public ActionForward viewDissertationProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null && StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
            IUserView userView = UserView.getUser();

            Object args[] = { Integer.valueOf(finalDegreeWorkProposalOIDString) };
            try {
                InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService("ReadFinalDegreeWorkProposal", args);

                if (infoProposal != null) {
                    request.setAttribute("finalDissertationProposal", infoProposal);
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
        }

        return mapping.findForward("viewDissertationProposal");
    }
}