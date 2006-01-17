package net.sourceforge.fenixedu.applicationTier.Servico.groups;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreatePersonalGroup implements IService {

    public PersonalGroup run(Person person, String name, String description, Group group) throws FenixServiceException {
        PersonalGroup personalGroup = new PersonalGroup();
        
        if (person == null) {
            throw new FenixServiceException("Person can not be null");
        }
        
        if (group == null) {
            throw new FenixServiceException("Group can not be null");
        }
        
        personalGroup.setName(name);
        personalGroup.setDescription(description);
        personalGroup.setGroup(group);
        personalGroup.setPerson(person);
        
        return personalGroup;
    }
    
}
