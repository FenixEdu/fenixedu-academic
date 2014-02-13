package pt.ist.fenix.presentationTier.Action.parkingManager.parkingManager;

import pt.ist.fenix.presentationTier.Action.parkingManager.ParkingManagerDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "parkingManager", path = "/parking", input = "/parking.do?method=prepareEditParkingParty&page=0",
        attribute = "parkingForm", formBean = "parkingForm", scope = "request", parameter = "method")
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ParkingManagerDispatchActionForParkingManager extends ParkingManagerDispatchAction {
}