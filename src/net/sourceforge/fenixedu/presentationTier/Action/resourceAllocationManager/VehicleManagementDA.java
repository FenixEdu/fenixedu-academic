package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.math.BigDecimal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.VehicleAllocationBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.VehicleAllocationHistoryBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class VehicleManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws InvalidArgumentException {

	Set<VehicleAllocation> activeVehicleAllocations = VehicleAllocation.getActiveVehicleAllocations();
	Set<VehicleAllocation> futureVehicleAllocations = VehicleAllocation.getFutureVehicleAllocations();

	request.setAttribute("activeAllocations", activeVehicleAllocations);
	request.setAttribute("futureAllocations", futureVehicleAllocations);

	return mapping.findForward("prepareVehicleManagement");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	VehicleAllocationBean vehicleAllocationBean = new VehicleAllocationBean();
	request.setAttribute("allocationBean", vehicleAllocationBean);
	return mapping.findForward("prepareCreate");
    }

    public ActionForward prepareConfirmCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	VehicleAllocationBean bean = (VehicleAllocationBean) getRenderedObject();
	BigDecimal allocationCost = VehicleAllocation.getAllocationCost(bean.getVehicle(), bean.getDistance(), bean
		.getBeginDateTime(), bean.getEndDateTime());
	bean.setAmountCharged(allocationCost);
	request.setAttribute("allocationBean", bean);
	RenderUtils.invalidateViewState();
	return mapping.findForward("prepareCreate");
    }

    public ActionForward createAllocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {

	VehicleAllocationBean bean = (VehicleAllocationBean) getRenderedObject();

	try {
	    executeService("CreateVehicleAllocation", new Object[] { bean });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("allocationBean", bean);
	    return mapping.findForward("prepareCreate");
	}

	return prepare(mapping, form, request, response);
    }

    public ActionForward prepareEditAllocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	VehicleAllocation allocation = getAllocationFromParameter(request);
	request.setAttribute("vehicleAllocation", allocation);
	return mapping.findForward("prepareCreate");
    }

    public ActionForward deleteAllocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {

	VehicleAllocation allocation = getAllocationFromParameter(request);

	try {
	    executeService("DeleteVehicleAllocation", new Object[] { allocation });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	return prepare(mapping, form, request, response);
    }

    public ActionForward seeVehicleAllocationHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	VehicleAllocationHistoryBean bean = (VehicleAllocationHistoryBean) getRenderedObject();
	if (bean == null) {
	    bean = new VehicleAllocationHistoryBean();
	}

	request.setAttribute("vehicleAllocationHistoryBean", bean);

	if (bean.getYear() != null && bean.getMonth() != null) {
	    DateTime firstDayOfMonth = new DateTime(bean.getYear().get(DateTimeFieldType.year()), bean.getMonth().get(
		    DateTimeFieldType.monthOfYear()), 1, 0, 0, 0, 0);
	    DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
	    Set<VehicleAllocation> pastVehicleAllocations = VehicleAllocation.getPastVehicleAllocations(firstDayOfMonth,
		    lastDayOfMonth, bean.getVehicle());
	    request.setAttribute("pastVehicleAllocations", pastVehicleAllocations);
	}

	return mapping.findForward("seeVehicleAllocationHistory");
    }

    public ActionForward seeVehicleAllocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidArgumentException {

	VehicleAllocation allocation = getAllocationFromParameter(request);
	request.setAttribute("vehicleAllocation", allocation);
	return mapping.findForward("seeVehicleAllocation");
    }

    private VehicleAllocation getAllocationFromParameter(final HttpServletRequest request) {
	final String allocationIDString = request.getParameter("allocationID");
	final Integer allocationID = allocationIDString != null ? Integer.valueOf(allocationIDString) : null;
	return (VehicleAllocation) rootDomainObject.readResourceAllocationByOID(allocationID);
    }
}
