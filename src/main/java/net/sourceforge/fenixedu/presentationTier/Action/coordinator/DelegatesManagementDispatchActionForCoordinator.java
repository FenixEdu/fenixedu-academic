package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.DelegatesManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "coordinator", path = "/delegatesManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "createEditDelegates", path = "/coordinator/viewDelegates.jsp", tileProperties = @Tile(
        title = "private.coordinator.management.courses.students.delegates")) })
public class DelegatesManagementDispatchActionForCoordinator extends DelegatesManagementDispatchAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionDegree executionDegree =
                (ExecutionDegree) AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionDegreeId"));
        DelegateBean bean = new DelegateBean();
        bean.setDegreeType(executionDegree.getDegreeType());
        bean.setDegree(executionDegree.getDegree());
        request.setAttribute("delegateBean", bean);

        return prepareViewDelegates(mapping, actionForm, request, response);
    }
}