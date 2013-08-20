package net.sourceforge.fenixedu.presentationTier.Action.resourceManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceManager.CreateVehicle;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceManager.DeleteVehicle;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.VehicleBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Vehicle;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "resourceManager", path = "/vehicleManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "prepareVehicleManage", path = "/resourceManager/vehicleManagement/vehicleManagement.jsp"),
        @Forward(name = "editVehicle", path = "/resourceManager/vehicleManagement/editVehicle.jsp") })
public class VehicleManagementDA extends FenixDispatchAction {

    public ActionForward prepareVehicleManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        VehicleBean bean = new VehicleBean();
        request.setAttribute("vehicleBean", bean);
        return mapping.findForward("prepareVehicleManage");
    }

    public ActionForward listVehicles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        VehicleBean bean;
        List<Vehicle> result;

        Vehicle vehicle = getRenderedObject("vehicleEditID");
        if (vehicle != null) {
            bean = new VehicleBean();
            bean.setNumberPlate(vehicle.getNumberPlate());
        } else {
            bean = getRenderedObject("vehicleBeanID");
            bean = bean != null ? bean : (VehicleBean) getRenderedObject("createVehicleBeanID");
            bean = bean != null ? bean : new VehicleBean();
        }

        String numberPlate = bean != null ? bean.getNumberPlate() : null;
        if (StringUtils.isEmpty(numberPlate)) {
            result = Vehicle.getAllVehicles();
        } else {
            Vehicle vehicleByNumberPlate = Vehicle.getVehicleByNumberPlate(numberPlate);
            result = new ArrayList<Vehicle>();
            if (vehicleByNumberPlate != null) {
                result.add(vehicleByNumberPlate);
            }
        }

        CollectionPager<Vehicle> collectionPager =
                new CollectionPager<Vehicle>(result != null ? result : new ArrayList<Vehicle>(), 50);
        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));
        request.setAttribute("vehicles", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("vehicleBean", bean);

        return mapping.findForward("prepareVehicleManage");
    }

    public ActionForward prepareCreateVehicle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        VehicleBean bean = new VehicleBean();
        request.setAttribute("vehicleBean", bean);
        return mapping.findForward("editVehicle");
    }

    public ActionForward prepareEditVehicle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        Vehicle vehicle = getVehicleFromParameter(request);
        request.setAttribute("vehicle", vehicle);
        return mapping.findForward("editVehicle");
    }

    public ActionForward createVehicle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException, FenixServiceException {

        VehicleBean bean = getRenderedObject("createVehicleBeanID");

        try {
            CreateVehicle.run(bean.getNumberPlate(), bean.getMake(), bean.getModel(), bean.getAcquisition(), bean.getCease(),
                    bean.getAllocationCostMultiplier());

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("vehicleBean", bean);
            return mapping.findForward("editVehicle");
        }

        return listVehicles(mapping, form, request, response);
    }

    public ActionForward deleteVehicle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException, FenixServiceException {

        Vehicle vehicle = getVehicleFromParameter(request);

        try {
            DeleteVehicle.run(vehicle);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        return prepareVehicleManage(mapping, form, request, response);
    }

    // ///

    private Vehicle getVehicleFromParameter(HttpServletRequest request) {
        final String vehicleIDString = request.getParameter("vehicleID");
        return (Vehicle) AbstractDomainObject.fromExternalId(vehicleIDString);
    }

}