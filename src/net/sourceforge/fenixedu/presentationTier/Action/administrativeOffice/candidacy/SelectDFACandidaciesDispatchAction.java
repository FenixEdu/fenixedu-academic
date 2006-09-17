/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.SelectDFACandidacyBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectDFACandidaciesDispatchAction extends FenixDispatchAction {

    public ActionForward prepareListCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	DFACandidacyBean candidacyBean = new DFACandidacyBean();
	request.setAttribute("candidacyBean", candidacyBean);
	return mapping.findForward("listCandidacies");
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject()
		.getObject();
	candidacyBean.setDegreeCurricularPlan(null);
	candidacyBean.setExecutionDegree(null);
	RenderUtils.invalidateViewState();
	request.setAttribute("candidacyBean", candidacyBean);

	return mapping.getInputForward();
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject()
		.getObject();

	ExecutionDegree executionDegree = null;
	if (candidacyBean.getDegreeCurricularPlan() != null) {
	    executionDegree = Collections.min(candidacyBean.getDegreeCurricularPlan()
		    .getExecutionDegrees(), ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
	}

	candidacyBean.setExecutionDegree(executionDegree);
	RenderUtils.invalidateViewState();
	request.setAttribute("candidacyBean", candidacyBean);

	return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyBean", RenderUtils.getViewState().getMetaObject().getObject());
	return mapping.getInputForward();
    }

    public ActionForward listCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) RenderUtils.getViewState()
		.getMetaObject().getObject();

	List<SelectDFACandidacyBean> candidacies = new ArrayList<SelectDFACandidacyBean>();
	for (DFACandidacy candidacy : dfaCandidacyBean.getExecutionDegree().getDfaCandidacies()) {
	    CandidacySituationType candidacySituationType = candidacy.getActiveCandidacySituation()
		    .getCandidacySituationType();
	    if (candidacySituationType.equals(CandidacySituationType.STAND_BY_CONFIRMED_DATA)
		    || candidacySituationType.equals(CandidacySituationType.SUBSTITUTE)) {
		candidacies.add(new SelectDFACandidacyBean(candidacy));
	    }
	}

	request.setAttribute("candidacies", candidacies);
	request.setAttribute("candidacyBean", dfaCandidacyBean);
	return mapping.findForward("listCandidacies");
    }

    public ActionForward selectCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	List<SelectDFACandidacyBean> candidaciesListForSelection = (List<SelectDFACandidacyBean>) RenderUtils
		.getViewState().getMetaObject().getObject();

	List<SelectDFACandidacyBean> admittedCandidacies = new ArrayList<SelectDFACandidacyBean>();
	List<SelectDFACandidacyBean> notAdmittedCandidacies = new ArrayList<SelectDFACandidacyBean>();
	List<SelectDFACandidacyBean> substituteCandidacies = new ArrayList<SelectDFACandidacyBean>();

	for (SelectDFACandidacyBean candidacyBean : candidaciesListForSelection) {
	    if (candidacyBean.getSelectionSituation() != null) {
		switch (candidacyBean.getSelectionSituation()) {
		case ADMITTED:
		    admittedCandidacies.add(candidacyBean);
		    break;
		case SUBSTITUTE:
		    substituteCandidacies.add(candidacyBean);
		    break;
		case NOT_ADMITTED:
		    notAdmittedCandidacies.add(candidacyBean);
		    break;
		default:
		    break;
		}
	    }
	}

	if (admittedCandidacies.isEmpty() && substituteCandidacies.isEmpty()
		&& notAdmittedCandidacies.isEmpty()) {
	    return setError(request, mapping, "no.candidacy.situations.selected",
		    "confirmCandidaciesForSelection", null);
	}

	Collections.sort(substituteCandidacies, new BeanComparator("candidacy.number"));

	request.setAttribute("admittedCandidacies", admittedCandidacies);
	request.setAttribute("substituteCandidacies", substituteCandidacies);
	request.setAttribute("notAdmittedCandidacies", notAdmittedCandidacies);

	return mapping.findForward("confirmCandidaciesForSelection");
    }

    public ActionForward confirmCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	List<SelectDFACandidacyBean> admittedCandidacies = retrieveCandidaciesBeans("admittedCandidacies");
	List<SelectDFACandidacyBean> substituteCandidacies = retrieveCandidaciesBeans("substituteCandidacies");
	List<SelectDFACandidacyBean> notAdmittedCandidacies = retrieveCandidaciesBeans("notAdmittedCandidacies");

	if (substituteCandidacies != null) {
	    String substituteCandidaciesOrderTree = request.getParameter("substituteCandidaciesOrder");
	    String[] substitutesOrder = substituteCandidaciesOrderTree.replaceAll("-0", "").split(",");

	    for (int i = 0; i < substitutesOrder.length; i++) {
		substituteCandidacies.get(i).setOrder(Integer.valueOf(substitutesOrder[i]));
	    }
	}

	Object args[] = { admittedCandidacies, substituteCandidacies, notAdmittedCandidacies };
	ServiceUtils.executeService(SessionUtils.getUserView(request), "SelectCandidacies", args);

	request.setAttribute("admittedCandidacies", admittedCandidacies);
	request.setAttribute("substituteCandidacies", substituteCandidacies);
	request.setAttribute("notAdmittedCandidacies", notAdmittedCandidacies);

	return mapping.findForward("showCandidaciesForSelectionSuccess");
    }

    public ActionForward printAcceptanceDispatch(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	List<SelectDFACandidacyBean> admittedCandidacies = retrieveCandidaciesBeans("admittedCandidacies");
	List<SelectDFACandidacyBean> substituteCandidacies = retrieveCandidaciesBeans("substituteCandidacies");
	List<SelectDFACandidacyBean> notAdmittedCandidacies = retrieveCandidaciesBeans("notAdmittedCandidacies");
	
	if(substituteCandidacies != null){
	    Collections.sort(substituteCandidacies, new BeanComparator("order"));
	}

	ExecutionDegree executionDegree = retrieveExecutionDegree(admittedCandidacies,
		substituteCandidacies, notAdmittedCandidacies);

	request.setAttribute("degreeName", executionDegree.getDegreeCurricularPlan().getDegree()
		.getName());
	request.setAttribute("currentExecutionYear", executionDegree.getExecutionYear().getYear());
	request.setAttribute("admittedCandidacies", admittedCandidacies);
	request.setAttribute("substituteCandidacies", substituteCandidacies);
	request.setAttribute("notAdmittedCandidacies", notAdmittedCandidacies);

	return mapping.findForward("printAcceptanceDispatch");
    }

    private List<SelectDFACandidacyBean> retrieveCandidaciesBeans(String candidaciesSituation) {
	return (RenderUtils.getViewState(candidaciesSituation) != null) ? (List<SelectDFACandidacyBean>) RenderUtils
		.getViewState(candidaciesSituation).getMetaObject().getObject()
		: null;
    }

    private ExecutionDegree retrieveExecutionDegree(List<SelectDFACandidacyBean> admittedCandidacies,
	    List<SelectDFACandidacyBean> substituteCandidacies,
	    List<SelectDFACandidacyBean> notAdmittedCandidacies) {
	ExecutionDegree executionDegree = null;
	if (admittedCandidacies != null && !admittedCandidacies.isEmpty()) {
	    executionDegree = admittedCandidacies.get(0).getCandidacy().getExecutionDegree();
	} else if (substituteCandidacies != null && !substituteCandidacies.isEmpty()) {
	    executionDegree = substituteCandidacies.get(0).getCandidacy().getExecutionDegree();
	} else {
	    executionDegree = notAdmittedCandidacies.get(0).getCandidacy().getExecutionDegree();
	}
	return executionDegree;
    }

}
