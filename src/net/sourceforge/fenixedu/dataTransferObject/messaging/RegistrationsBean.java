package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationsBean implements Serializable {

	private Registration selected;
	private List<Registration> registrations;

	public RegistrationsBean() {
		this.selected = null;
	}

	public List<Registration> getRegistrations() {
		List<Registration> result = new ArrayList<Registration>();
		for (Registration registration : registrations) {
			result.add(registration);
		}
		return result;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = new ArrayList<Registration>();

		for (Registration registration : registrations) {
			this.registrations.add(registration);
		}
	}

	public Registration getSelected() {
		return selected;
	}

	public void setSelected(Registration selected) {
		this.selected = selected;
	}

}
