package net.sourceforge.fenixedu.domain.dissertation;

import java.util.List;

import net.sourceforge.fenixedu.domain.student.Registration;

public class DissertationCandidacy extends DissertationCandidacy_Base {
    
    public  DissertationCandidacy() {
        super();
    }
    
    private Scheduling scheduling = null;
    private List<Registration> registrations = null;

	public Scheduling getScheduling() {
		return scheduling;
	}

	public void setScheduling(Scheduling scheduling) {
		this.scheduling = scheduling;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}
    
}
