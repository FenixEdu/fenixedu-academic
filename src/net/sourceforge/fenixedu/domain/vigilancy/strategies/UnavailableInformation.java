package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;



public class UnavailableInformation implements Serializable {

	private DomainReference<Vigilant> vigilant;
	private UnavailableTypes unavailableReason;
	
	UnavailableInformation(Vigilant vigilant, UnavailableTypes unavailableReason) {
		this.vigilant = new DomainReference<Vigilant>(vigilant);
		this.unavailableReason = unavailableReason;
	}
	
	public Vigilant getVigilant() {
		return this.vigilant.getObject();
	}
	
	public UnavailableTypes getReason() {
		return this.unavailableReason;
	}
}
