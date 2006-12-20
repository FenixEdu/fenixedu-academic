package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkAuthorizationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkAuthorizationSearchBean;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AssiduousnessExtraWorkDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateExtraWorkAuthorization(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory = (ExtraWorkAuthorizationFactory) getFactoryObject();
        if (extraWorkAuthorizationFactory != null) {
            DynaActionForm actionForm = (DynaActionForm) form;
            String addEmployee = actionForm.getString("addEmployee");
            if (addEmployee.equalsIgnoreCase("yes")) {
                extraWorkAuthorizationFactory.addEmployeeExtraWorkAuthorization();
                request.setAttribute("extraWorkAuthorizationFactory", extraWorkAuthorizationFactory);
                RenderUtils.invalidateViewState();
            } else {
                request.setAttribute("extraWorkAuthorizationFactory", extraWorkAuthorizationFactory);
            }
        } else {
            IUserView userView = SessionUtils.getUserView(request);
            request.setAttribute("extraWorkAuthorizationFactory", new ExtraWorkAuthorizationFactory(
                    userView.getPerson().getEmployee()));
        }
        return mapping.findForward("create-extra-work-authorization");
    }

    public ActionForward createExtraWorkAuthorization(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory = (ExtraWorkAuthorizationFactory) getFactoryObject();
        ExtraWorkAuthorization extraWorkAuthorization = (ExtraWorkAuthorization) ServiceUtils
                .executeService(SessionUtils.getUserView(request), "ExecuteFactoryMethod",
                        new Object[] { extraWorkAuthorizationFactory });
        request.setAttribute("authorizationID", extraWorkAuthorization.getIdInternal());

        return viewExtraWorkAuthorization(mapping, form, request, response);        
    }

    public ActionForward prepareExtraWorkAuthorizationsSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ExtraWorkAuthorizationSearchBean extraWorkAuthorizationSearchBean = (ExtraWorkAuthorizationSearchBean) getRenderedObject();
        if (extraWorkAuthorizationSearchBean != null) {
            request.setAttribute("extraWorkAuthorizationSearchBean", extraWorkAuthorizationSearchBean);
        } else {
            request.setAttribute("extraWorkAuthorizationSearchBean",
                    new ExtraWorkAuthorizationSearchBean());
        }
        return mapping.findForward("show-extra-work-authorizations");
    }

    public ActionForward showExtraWorkAuthorizations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ExtraWorkAuthorizationSearchBean extraWorkAuthorizationSearchBean = (ExtraWorkAuthorizationSearchBean) getRenderedObject();
        extraWorkAuthorizationSearchBean.doSearch();
        request.setAttribute("extraWorkAuthorizationSearchBean", extraWorkAuthorizationSearchBean);

        return mapping.findForward("show-extra-work-authorizations");
    }

    public ActionForward viewExtraWorkAuthorization(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Integer authorizationID = getIntegerFromRequest(request, "authorizationID");
        ExtraWorkAuthorization extraWorkAuthorization = rootDomainObject
                .readExtraWorkAuthorizationByOID(authorizationID);
        request.setAttribute("extraWorkAuthorization", extraWorkAuthorization);

        return mapping.findForward("view-extra-work-authorization");
    }

    public ActionForward prepareEditExtraWorkAuthorization(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory = (ExtraWorkAuthorizationFactory) getFactoryObject();
        if (extraWorkAuthorizationFactory != null) {
            DynaActionForm actionForm = (DynaActionForm) form;
            String addEmployee = actionForm.getString("addEmployee");
            if (addEmployee.equalsIgnoreCase("yes")) {
                extraWorkAuthorizationFactory.addEmployeeExtraWorkAuthorization();
                request.setAttribute("extraWorkAuthorizationFactory", extraWorkAuthorizationFactory);
                RenderUtils.invalidateViewState();
            } else {
                request.setAttribute("extraWorkAuthorizationFactory", extraWorkAuthorizationFactory);
            }
        } else {
            Integer authorizationID = getIntegerFromRequest(request, "authorizationID");
            ExtraWorkAuthorization extraWorkAuthorization = rootDomainObject
                    .readExtraWorkAuthorizationByOID(authorizationID);
            IUserView userView = SessionUtils.getUserView(request);
            request.setAttribute("extraWorkAuthorizationFactory", new ExtraWorkAuthorizationFactory(
                    userView.getPerson().getEmployee(), extraWorkAuthorization));
        }
        return mapping.findForward("edit-extra-work-authorization");
    }

    public ActionForward editExtraWorkAuthorization(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory = (ExtraWorkAuthorizationFactory) getFactoryObject();
        ServiceUtils.executeService(SessionUtils.getUserView(request), "ExecuteFactoryMethod",
                new Object[] { extraWorkAuthorizationFactory });
        request.setAttribute("authorizationID", extraWorkAuthorizationFactory
                .getExtraWorkAuthorization().getIdInternal());

        return viewExtraWorkAuthorization(mapping, form, request, response);
    }
}