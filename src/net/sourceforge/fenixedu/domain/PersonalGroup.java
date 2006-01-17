package net.sourceforge.fenixedu.domain;

public class PersonalGroup extends PersonalGroup_Base {
    
    public PersonalGroup() {
        super();
    }

    public void delete() {
        removePerson();

        super.deleteDomainObject();
    }
    
}
