package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageBuildingsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final Object args1[] = { OldBuilding.class };
        final List buildings = new ArrayList((Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args1));
        Collections.sort(buildings, new BeanComparator("name"));
        request.setAttribute("buildings", buildings);

        final Object args2[] = { Campus.class };
        final List campuss = new ArrayList((Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args2));
        Collections.sort(campuss, new BeanComparator("name"));
        request.setAttribute("campuss", campuss);

        return mapping.findForward("viewBuildings");
    }

    public ActionForward createBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String buildingName = (String) dynaActionForm.get("name");
        final String campusID = (String) dynaActionForm.get("campusID");

        final Object args[] = { buildingName, Integer.valueOf(campusID) };
        ServiceUtils.executeService(userView, "CreateBuilding", args);

        return prepare(mapping, form, request, response);
    }

    public ActionForward editBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = SessionUtils.getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String buildingID = (String) dynaActionForm.get("buildingID");
        final String campusID = (String) dynaActionForm.get("campusID");

        final Object args[] = { Integer.valueOf(buildingID), Integer.valueOf(campusID) };
        ServiceUtils.executeService(userView, "EditOldBuilding", args);

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