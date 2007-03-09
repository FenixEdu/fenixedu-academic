package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ChangeThesisPerson extends Service {

    public static enum PersonTarget {
        orientator,
        coorientator,
        president,
        vowel
    };
    
    public static class PersonChange {
        public PersonTarget type;
        Person person;
        String personName;
        Unit unit;
        String unitName;
        Person target;
        
        public PersonChange(PersonTarget type, Person person, Person target) {
            super();
        
            this.type = type;
            this.person = person;
            this.target = target;
        }

        public PersonChange(PersonTarget type, String personName, Unit unit, Person target) {
            super();
            
            this.type = type;
            this.personName = personName;
            this.unit = unit;
            this.target = target;
        }

        public PersonChange(PersonTarget type, String personName, String unitName, Person target) {
            super();
        
            this.type = type;
            this.personName = personName;
            this.unitName = unitName;
            this.target = target;
        }
    }
    
    public void run(Integer degreeCurricularPlanId, Thesis thesis, PersonChange change) throws FenixServiceException {
        Person person = getPerson(change);
        
        switch (change.type) {
        case orientator:
            thesis.setOrientator(person);
            break;
        case coorientator:
            thesis.setCoorientator(person);
            break;
        case president:
            thesis.setPresident(person);
            break;
        case vowel:
            List<Person> vowels = thesis.getVowels();

            if (change.target != null) {
                if (person != null) {
                    int index = vowels.indexOf(change.target);
                    vowels.set(index, person);
                } else {
                    vowels.remove(change.target);
                }
            } else {
                if (person != null) {
                    vowels.add(person);
                }
            }

            break;
        }
    }

    private Person getPerson(PersonChange change) throws FenixServiceException {
        if (change.person != null) {
            return change.person;
        }
        else {
            if (change.personName == null) {
                return null;
            }
            else {
                if (change.unit != null) {
                    return new InsertExternalPerson().run(change.personName, change.unit).getPerson();
                }
                else {
                    return new InsertExternalPerson().run(change.personName, change.unitName).getPerson();
                }
            }
        }
    }
}
