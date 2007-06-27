package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;

public class RemovePersistentGroupMember extends Service {
    
    public void run(Person person, PersistentGroupMembers persistentGroupMembers) {
	if(person != null && persistentGroupMembers != null) {
	    persistentGroupMembers.removePersons(person);
	}
    }
}
