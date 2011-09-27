package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadAllMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings.ReadCPlanFromChosenMasterDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DegreeType degreeType = DegreeType.MASTER_DEGREE;

	List result = null;
	try {
	    result = (List) ReadAllMasterDegrees.run(degreeType);
	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("O Degree de Mestrado", e);
	}

	request.setAttribute(PresentationConstants.MASTER_DEGREE_LIST, result);

	return mapping.findForward("DisplayMasterDegreeList");
    }

    public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// Get the Chosen Master Degree
	Integer masterDegreeID = new Integer(request.getParameter("degreeID"));
	if (masterDegreeID == null) {
	    masterDegreeID = (Integer) request.getAttribute("degreeID");
	}

	List result = null;

	try {

	    result = (List) ReadCPlanFromChosenMasterDegree.run(masterDegreeID);

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("O plano curricular ", e);
	}

	request.setAttribute(PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);

	return mapping.findForward("MasterDegreeReady");
    }

    public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// Get the Chosen Master Degree
	Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
	if (curricularPlanID == null) {
	    curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
	}
	List executionYearList = null;
	Object args[] = { curricularPlanID };
	try {
	    executionYearList = (ArrayList) ServiceManagerServiceFactory.executeService(
		    "ReadExecutionDegreesByDegreeCurricularPlanID", args);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException(e);
	}
	List executionYearsLabels = transformIntoLabels(executionYearList);
	request.setAttribute(PresentationConstants.EXECUTION_YEAR_LIST, executionYearsLabels);
	request.setAttribute(PresentationConstants.EXECUTION_DEGREE, curricularPlanID);

	return mapping.findForward("PrepareSuccess");
    }

    private List transformIntoLabels(List executionYearList) {
	List executionYearsLabels = new ArrayList();
	CollectionUtils.collect(executionYearList, new Transformer() {
	    public Object transform(Object input) {
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
		LabelValueBean labelValueBean = new LabelValueBean(infoExecutionDegree.getInfoExecutionYear().getYear(),
			infoExecutionDegree.getIdInternal().toString());
		return labelValueBean;
	    }
	}, executionYearsLabels);
	Collections.sort(executionYearsLabels, new BeanComparator("label"));
	Collections.reverse(executionYearsLabels);

	return executionYearsLabels;
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer curricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
	Integer executionDegreeID = new Integer(request.getParameter("executionDegreeID"));

	if (curricularPlanID == null) {
	    curricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanID");

	}

	request.setAttribute(PresentationConstants.EXECUTION_DEGREE, request.getParameter("executionDegreeID"));
	request.setAttribute("degreeCurricularPlanID", curricularPlanID);
	
	if(executionDegreeID != null) {
	    ExecutionDegree executionDegree = (ExecutionDegree) RootDomainObject.readDomainObjectByOID(ExecutionDegree.class,
		    executionDegreeID);
	    ExecutionYear executionYear = executionDegree.getExecutionYear();
	    request.setAttribute(PresentationConstants.EXECUTION_YEAR, executionYear.getName());
	}

	return mapping.findForward("ChooseSuccess");
    }

}