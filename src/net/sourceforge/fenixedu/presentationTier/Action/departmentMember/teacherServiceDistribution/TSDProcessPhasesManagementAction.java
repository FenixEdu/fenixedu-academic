package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class TSDProcessPhasesManagementAction extends FenixDispatchAction {
    private static final Integer COPY_LAST_YEAR_REAL_DATA = 0;
    private static final Integer COPY_PREVIOUS_PHASE_DATA = 1;
    private static final Integer SUCCESSFUL_VALUATION = 2;

    private static final Integer NOT_SELECTED_EXECUTION_PERIOD = -1;
    private static final Integer NOT_SELECTED_EXECUTION_YEAR = -1;
    private static final Integer NOT_SELECTED_DEPARTMENT = -1;
    private static final Integer NOT_SELECTED_DISTRIBUTION = -1;

    public ActionForward prepareForTSDProcessPhasesManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);

	initializeVariables(dynaForm);

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward createTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

	String name = (String) dynaForm.get("name");

	Object[] parameters = new Object[] { tsdProcess.getIdInternal(), name };

	ServiceUtils.executeService("CreateTSDProcessPhase", parameters);

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward setCurrentTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	ServiceUtils.executeService("SetCurrentTSDProcessPhase", new Object[] { selectedTSDProcessPhase.getIdInternal() });

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward closeTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	ServiceUtils.executeService("CloseTSDProcessPhase", new Object[] { selectedTSDProcessPhase.getIdInternal() });

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward openTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	ServiceUtils.executeService("OpenTSDProcessPhase", new Object[] { selectedTSDProcessPhase.getIdInternal() });

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward deleteTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	ServiceUtils.executeService("DeleteTSDProcessPhase", new Object[] { selectedTSDProcessPhase.getIdInternal() });

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward setPublishedStateOnTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);
	Boolean isPublished = (Boolean) dynaForm.get("isPublished");

	Object[] arguments = new Object[] { selectedTSDProcessPhase.getIdInternal(), isPublished };

	ServiceUtils.executeService("SetPublishedStateOnTSDProcessPhase", arguments);

	return loadTSDProcessPhases(mapping, form, request, response);
    }

    public ActionForward loadTSDProcessPhases(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

	request.setAttribute("tsdProcess", tsdProcess);
	request.setAttribute("tsdProcessPhaseList", tsdProcess.getOrderedTSDProcessPhases());

	return mapping.findForward("showTSDProcessPhasesManagementForm");
    }

    public ActionForward prepareForCurrentTSDProcessPhaseDataManagement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	Integer tsdProcessId = getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);

	dynaForm.set("dataManagementOption", COPY_LAST_YEAR_REAL_DATA);

	return showTSDProcessPhaseDataManagementOptions(mapping, form, request, response);
    }

    public ActionForward showTSDProcessPhaseDataManagementOptions(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;
	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

	request.setAttribute("tsdProcess", tsdProcess);

	return mapping.findForward("tsdProcessPhaseDataManagementOptions");
    }

    public ActionForward manageCurrentTSDProcessPhaseData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;
	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

	Integer dataManagementOption = (Integer) dynaForm.get("dataManagementOption");

	if (dataManagementOption.equals(COPY_LAST_YEAR_REAL_DATA)) {
	    ServiceUtils.executeService("CopyLastYearRealDataToTSDProcessPhase", new Object[] { tsdProcess
		    .getCurrentTSDProcessPhase().getIdInternal() });
	} else if (dataManagementOption.equals(COPY_PREVIOUS_PHASE_DATA)) {
	    Integer tsdProcessPhaseId = (Integer) dynaForm.get("tsdProcessPhase");
	    ServiceUtils.executeService("CopyTSDProcessPhaseDataToTSDProcessPhase", new Object[] { tsdProcessPhaseId,
		    tsdProcess.getCurrentTSDProcessPhase().getIdInternal() });
	}

	dynaForm.set("dataManagementOption", SUCCESSFUL_VALUATION);
	return showTSDProcessPhaseDataManagementOptions(mapping, form, request, response);
    }

    public ActionForward prepareForOmissionValuesValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	Integer tsdProcessId = getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

	request.setAttribute("tsdProcess", tsdProcess);
	request.setAttribute("currentTSDProcessPhase", tsdProcess.getCurrentTSDProcessPhase());

	return mapping.findForward("showOmissionValuesValuationForm");
    }

    public ActionForward resetSelectedTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("selectedTSDProcess", null);

	return prepareToChooseTSDProcessPhase(mapping, form, request, response);
    }

    public ActionForward prepareToChooseTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();

	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
	List<TSDProcessPhase> tsdProcessPhases = new ArrayList<TSDProcessPhase>();

	tsdProcessPhases.addAll(tsdProcess.getCurrentTSDProcessPhase().getPreviousTSDProcessPhases());

	request.setAttribute("tsdProcessPhaseList", tsdProcessPhases);

	if (!tsdProcessPhases.isEmpty()) {
	    dynaForm.set("tsdProcessPhase", tsdProcessPhases.get(0).getIdInternal());
	}

	return showTSDProcessPhaseDataManagementOptions(mapping, form, request, response);
    }

    private void initializeVariables(DynaActionForm dynaForm) {
	dynaForm.set("tsdProcessPhase", null);
    }

    private Integer getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	dynaForm.set("tsdProcess", tsdProcessId);
	return tsdProcessId;
    }

    private TSDProcess getTSDProcess(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException,
	    FenixFilterException {
	Integer tsdProcessId = (Integer) dynaForm.get("tsdProcess");
	TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

	return tsdProcess;
    }

    private TSDProcessPhase getSelectedTSDProcessPhase(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException,
	    FenixServiceException {
	Integer selectedTSDProcessPhaseId = (Integer) dynaForm.get("tsdProcessPhase");
	TSDProcessPhase selectedTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(selectedTSDProcessPhaseId);

	return selectedTSDProcessPhase;
    }
}
