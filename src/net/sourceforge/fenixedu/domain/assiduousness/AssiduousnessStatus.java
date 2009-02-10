package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;

import org.joda.time.DateTime;

public class AssiduousnessStatus extends AssiduousnessStatus_Base {

    public AssiduousnessStatus(String description, AssiduousnessState assiduousnessState, DateTime lastModifiedDate,
	    Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDescription(description);
	setState(assiduousnessState);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public AssiduousnessStatus(String description, AssiduousnessState assiduousnessState, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDescription(description);
	setState(assiduousnessState);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }

    public static AssiduousnessStatus getAssiduousnessStatusByDescription(String assiduousnessStatusDescription) {
	for (AssiduousnessStatus assiduousnessStatus : RootDomainObject.getInstance().getAssiduousnessStatus()) {
	    if (assiduousnessStatus.getDescription().equalsIgnoreCase(assiduousnessStatusDescription)) {
		return assiduousnessStatus;
	    }
	}
	return null;
    }

    public void editAssiduousnessStatus(String description, AssiduousnessState assiduousnessState, Employee modifiedBy) {
	setDescription(description);
	setState(assiduousnessState);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }

    public Boolean isContractedEmployee() {
	if (getDescription().equals("Contrato a termo certo")) {
	    return true;
	}
	return false;
    }

    public Boolean isADISTEmployee() {
	if (getDescription().equals("Contratado pela ADIST")) {
	    return true;
	}
	return false;
    }
}
