package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class ManageBuildingsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final Object args[] = {};
        final List infoBuildings = (List) ServiceUtils.executeService(userView, "ReadBuildings", args);

        Collections.sort(infoBuildings, new BeanComparator("name"));

        request.setAttribute("infoBuildings", infoBuildings);

        return mapping.findForward("viewBuildings");
    }

    public ActionForward createBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String buildingName = (String) dynaActionForm.get("name");

        final Object args[] = { buildingName };
        ServiceUtils.executeService(userView, "CreateBuilding", args);

        return prepare(mapping, form, request, response);
    }

    public ActionForward deleteBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final Integer buildingId = new Integer(request.getParameter("buildingId"));

        final Object args[] = { buildingId };
        ServiceUtils.executeService(userView, "DeleteBuilding", args);

        return prepare(mapping, form, request, response);
    }

}