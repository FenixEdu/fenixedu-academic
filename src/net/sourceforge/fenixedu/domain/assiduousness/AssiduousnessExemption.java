package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AssiduousnessExemption extends AssiduousnessExemption_Base {

    public AssiduousnessExemption(Integer year, String description) {
	super();
	setYear(year);
	setDescription(description);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static int getAssiduousnessExemptionDaysQuantity(Integer year) {
	int numberOfDays = 0;
	for (AssiduousnessExemption assiduousnessExemption : RootDomainObject.getInstance().getAssiduousnessExemptions()) {
	    if (assiduousnessExemption.getYear().equals(year)) {
		numberOfDays += assiduousnessExemption.getNumberOfDays();
	    }
	}
	return numberOfDays;
    }

    private int getNumberOfDays() {
	AssiduousnessExemptionShift assiduousnessExemptionShift = getAssiduousnessExemptionShifts().get(0);
	return assiduousnessExemptionShift.getPartialShift().getPartials().size();
    }

    public void delete() {
	deleteShifts();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void edit(Integer year, String description) {
	setYear(year);
	setDescription(description);
    }

    public void deleteShifts() {
	List<AssiduousnessExemptionShift> list = new ArrayList<AssiduousnessExemptionShift>(getAssiduousnessExemptionShifts());
	for (AssiduousnessExemptionShift assiduousnessExemptionShift : list) {
	    assiduousnessExemptionShift.delete();
	}
    }

}
