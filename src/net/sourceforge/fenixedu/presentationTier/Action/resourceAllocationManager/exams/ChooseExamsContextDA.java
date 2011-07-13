package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Ana & Ricardo
 * 
 */
@Mapping(module = "resourceAllocationManager", path = "/chooseExamsContext", input = "/chooseExamsContext.do?method=prepare&page=0", attribute = "chooseExamsContextForm", formBean = "chooseExamsContextForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "ManageExams", path = "/showExamsManagement.do?method=view") })
public class ChooseExamsContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

	return mapping.findForward("ManageExams");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm chooseExamsContextForm = (DynaActionForm) form;

	IUserView userView = UserView.getUser();

	/* Determine Selected Curricular Year */
	Integer anoCurricular = new Integer((String) chooseExamsContextForm.get("curricularYear"));

	InfoCurricularYear infoCurricularYear = (InfoCurricularYear) ReadCurricularYearByOID.run(anoCurricular);

	request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);

	/* Determine Selected Execution Degree */
	Integer executionDegreeOID = new Integer((String) chooseExamsContextForm.get("executionDegreeOID"));

	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ReadExecutionDegreeByOID.run(executionDegreeOID);

	if (infoExecutionDegree == null) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("errors.invalid.execution.degree", new ActionError("errors.invalid.execution.degree"));
	    saveErrors(request, actionErrors);

	    return mapping.getInputForward();
	}
	request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);

	return mapping.findForward("ManageExams");

    }

}