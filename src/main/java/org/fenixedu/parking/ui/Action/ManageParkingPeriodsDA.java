package org.fenixedu.parking.ui.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.parking.domain.ParkingRequestPeriod;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ParkingManagerApp.class, path = "manage-request-periods", titleKey = "link.manageRequestsPeriods")
@Mapping(module = "parkingManager", path = "/manageParkingPeriods", input = "/exportParkingDB.do?method=prepareExportFile",
        formBean = "parkingRenewalForm")
@Forwards(@Forward(name = "manageRequestsPeriods", path = "/parkingManager/manageRequestsPeriods.jsp"))
public class ManageParkingPeriodsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareManageRequestsPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<ParkingRequestPeriod> parkingRequestPeriods =
                new ArrayList<ParkingRequestPeriod>(rootDomainObject.getParkingRequestPeriodsSet());
        Collections.sort(parkingRequestPeriods, new BeanComparator("beginDate"));
        request.setAttribute("parkingRequestPeriods", parkingRequestPeriods);
        return mapping.findForward("manageRequestsPeriods");
    }

    public ActionForward editRequestPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("parkingRequestPeriodToEdit", FenixFramework.getDomainObject(request.getParameter("externalId")));
        return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    public ActionForward deleteRequestPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        deleteParkingRequestPeriod(request.getParameter("externalId"));
        return prepareManageRequestsPeriods(mapping, actionForm, request, response);
    }

    @Atomic
    private void deleteParkingRequestPeriod(String id) {
        FenixFramework.<ParkingRequestPeriod> getDomainObject(id).delete();
    }
}