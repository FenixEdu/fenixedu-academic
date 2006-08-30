package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.OrderChange;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultParticipationManagementAction extends ResultsManagementAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final Result result = readResultFromRequest(request);
	if (result == null) {
	    return backToResultList(mapping, form, request, response);
	}

	setResParticipationRequestAttributes(request, result);
	return mapping.findForward("editParticipation");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final ResultParticipationCreationBean bean = getRenderedObject();
	
	if (checkBean(bean)) {
	    try {
		final Object[] args = { bean };
		executeService(request, "CreateResultParticipation", args);
		RenderUtils.invalidateViewState();
	    } catch (Exception e) {
		final ActionForward defaultForward = backToResultList(mapping, form, request, response);
		return processException(request, mapping, defaultForward, e);
	    }
	} else {
	    bean.setBeanExternal(true);
	    request.setAttribute("bean", bean);
	}
	RenderUtils.invalidateViewState();
	return prepareEdit(mapping, form, request, response);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final ResultParticipation participation = readResultParticipationFromRequest(request);
	if (participation == null) {
	    return backToResultList(mapping, form, request, response);
	}

	try {
	    final Object[] args = { participation };
	    executeService(request, "DeleteResultParticipation", args);
	} catch (Exception e) {
	    final ActionForward defaultForward = backToResultList(mapping, form, request, response);
	    return processException(request, mapping, defaultForward, e);
	}

	return prepareEdit(mapping, form, request, response);
    }

    public ActionForward saveOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final String treeStructure = (String) getFromRequest(request, "tree");
	final Result result = readResultFromRequest(request);

	if (treeStructure != null && treeStructure.length() != 0) {
	    final List<ResultParticipation> newParticipationsOrder = reOrderParticipations(
		    treeStructure, result);
	    try {
		final Object[] args = { result, newParticipationsOrder };
		executeService(request, "SaveResultParticipationsOrder", args);
	    } catch (Exception e) {
		final ActionForward defaultForward = backToResultList(mapping, form, request, response);
		return processException(request, mapping, defaultForward, e);
	    }
	}

	return prepareEdit(mapping, form, request, response);
    }

    public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return move(ResultParticipation.OrderChange.MoveUp, mapping, form, request, response);
    }

    public ActionForward moveDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return move(ResultParticipation.OrderChange.MoveDown, mapping, form, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return move(ResultParticipation.OrderChange.MoveTop, mapping, form, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return move(ResultParticipation.OrderChange.MoveBottom, mapping, actionForm, request, response);
    }

    private ActionForward move(OrderChange orderChange, ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final ResultParticipation participation = readResultParticipationFromRequest(request);
	if (participation == null) {
	    return backToResultList(mapping, form, request, response);
	}

	try {
	    final Object[] args = { participation, orderChange };
	    executeService(request, "ChangeResultParticipationsOrder", args);
	} catch (Exception e) {
	    final ActionForward defaultForward = backToResultList(mapping, form, request, response);
	    return processException(request, mapping, defaultForward, e);
	}

	return prepareEdit(mapping, form, request, response);
    }

    private void checkNeededSchemas(HttpServletRequest request, Result result,
	    ResultParticipationCreationBean bean) {
	String resultParticipationsSchema = "result.participations";
	String createResultParticipationSchema = "resultParticipation.creation";

	if (bean.isBeanExternal()) {
	    createResultParticipationSchema = "resultParticipation.fullCreation";
	}

	// Defining schemas with roles
	if (result instanceof ResultPublication) {
	    if (((ResultPublication) result).hasResultPublicationRole()) {
		resultParticipationsSchema = "result.participationsWithRole";
		if (!bean.isBeanExternal()) {
		    createResultParticipationSchema = "resultParticipation.creationWithRole";
		} else {
		    createResultParticipationSchema = "resultParticipation.fullCreationWithRole";
		}
	    }
	}
	request.setAttribute("listSchema", resultParticipationsSchema);
	request.setAttribute("createSchema", createResultParticipationSchema);
    }

    private void checkNeededWarnings(HttpServletRequest request, Result result)
	    throws FenixFilterException, FenixServiceException {
	if (!result.hasPersonParticipation(getLoggedPerson(request))) {
	    addActionMessage(request, "researcher.ResultParticipation.last.participation.warning");
	}
    }

    private List<ResultParticipation> reOrderParticipations(String treeStructure, Result result) {
	final List<ResultParticipation> newParticipationsOrder = new ArrayList<ResultParticipation>();
	final List<ResultParticipation> oldParticipationsOrder = result.getOrderedResultParticipations();
	final String[] nodes = treeStructure.split(",");

	for (int i = 0; i < nodes.length; i++) {
	    String[] parts = nodes[i].split("-");

	    Integer index = getId(parts[0]) - 1;
	    ResultParticipation participation = oldParticipationsOrder.get(index);
	    newParticipationsOrder.add(participation);
	}

	return newParticipationsOrder;
    }

    private ResultParticipationCreationBean getBeanFromRequest(HttpServletRequest request, Result result)
	    throws FenixFilterException, FenixServiceException {
	ResultParticipationCreationBean bean = (ResultParticipationCreationBean) getFromRequest(request, "bean");
	
	if (bean==null) {
	    bean = new ResultParticipationCreationBean(result);
	}

	return bean;
    }

    private boolean checkBean(ResultParticipationCreationBean bean) {
	// Person already exists in system.
	if (bean.getParticipator() != null) {
	    return true;
	}
	// External person creation
	if (bean.getParticipatorName() != null && !bean.getParticipatorName().equals("")) {
	    if (bean.getOrganization() != null
		    || (bean.getOrganizationName() != null && !bean.getOrganizationName().equals(""))) {
		return true;
	    }
	}
	return false;
    }

    private ResultParticipation readResultParticipationFromRequest(HttpServletRequest request) {
	final Integer oid = getRequestParameterAsInteger(request, "participationId");
	ResultParticipation participation = null;
	
	try {
	    participation = ResultParticipation.readByOid(oid);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}
	
	return participation;
    }

    private void setResParticipationRequestAttributes(HttpServletRequest request, Result result)
	    throws FenixFilterException, FenixServiceException {
	final ResultParticipationCreationBean bean = getBeanFromRequest(request, result);
	
	checkNeededSchemas(request, result, bean); // Define schemas to use
	checkNeededWarnings(request, result); // Action Warning Messages
	request.setAttribute("bean", bean);
	request.setAttribute("result", result);
    }

    protected Integer getId(String id) {
	if (id == null) {
	    return null;
	}

	try {
	    return new Integer(id);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public ResultParticipationCreationBean getRenderedObject() {
	return (ResultParticipationCreationBean) super.getRenderedObject();
    }
}