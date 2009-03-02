package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationsBean implements Serializable {

    private DomainReference<Registration> selected;
    private List<DomainReference<Registration>> registrations;

    public RegistrationsBean(){
	this.selected = new DomainReference<Registration>(null);
    }
    
    public List<Registration> getRegistrations() {
	List<Registration> result = new ArrayList<Registration>();
	for (DomainReference<Registration> registration : registrations){
            result.add(registration.getObject());
        }
	return result;
    }
    public void setRegistrations(List<Registration> registrations) {
        this.registrations = new ArrayList<DomainReference<Registration>>();
        
        for (Registration registration : registrations){
            this.registrations.add(new DomainReference<Registration>(registration));
        }
    }
    
    public Registration getSelected() {
        return selected.getObject();
    }
    public void setSelected(Registration selected) {
        this.selected = new DomainReference<Registration>(selected);
    }
    
    
}
