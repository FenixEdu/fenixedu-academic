package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateVehicle extends FenixService {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Service
    public static void run(String numberPlate, String make, String model, YearMonthDay acquisition, YearMonthDay cease,
            BigDecimal allocationCostMultiplier) {

        new Vehicle(numberPlate, make, model, acquisition, cease, allocationCostMultiplier);
    }
}