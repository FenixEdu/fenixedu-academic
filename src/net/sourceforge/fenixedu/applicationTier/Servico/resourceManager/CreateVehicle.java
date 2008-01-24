package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

import org.joda.time.YearMonthDay;

public class CreateVehicle extends Service {

    public void run(String numberPlate, String make, String model, YearMonthDay acquisition, YearMonthDay cease,
	    BigDecimal allocationCostMultiplier) {
	
	new Vehicle(numberPlate, make, model, acquisition, cease, allocationCostMultiplier);
    }
}
