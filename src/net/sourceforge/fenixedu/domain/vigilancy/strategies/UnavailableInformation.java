package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

public class UnavailableInformation implements Serializable {

    private DomainReference<VigilantWrapper> vigilant;
    private UnavailableTypes unavailableReason;

    UnavailableInformation(VigilantWrapper vigilant, UnavailableTypes unavailableReason) {
	this.vigilant = new DomainReference<VigilantWrapper>(vigilant);
	this.unavailableReason = unavailableReason;
    }

    public VigilantWrapper getVigilant() {
	return this.vigilant.getObject();
    }

    public UnavailableTypes getReason() {
	return this.unavailableReason;
    }
}
