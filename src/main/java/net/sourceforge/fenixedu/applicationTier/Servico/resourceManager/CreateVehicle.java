package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.resource.Vehicle;

import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateVehicle {

    @Atomic
    public static void run(String numberPlate, String make, String model, YearMonthDay acquisition, YearMonthDay cease,
            BigDecimal allocationCostMultiplier) {
        check(RolePredicates.RESOURCE_MANAGER_PREDICATE);

        new Vehicle(numberPlate, make, model, acquisition, cease, allocationCostMultiplier);
    }
}