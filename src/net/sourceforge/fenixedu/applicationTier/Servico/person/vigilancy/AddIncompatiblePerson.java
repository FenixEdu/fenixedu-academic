package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddIncompatiblePerson extends Service {

    public void run(Vigilant vigilant, Person person) throws ExcepcaoPersistencia {

    	if(vigilant.hasIncompatiblePerson()) {
    		vigilant.removeIncompatiblePerson();
    	}
    	if(person.getCurrentVigilant().hasIncompatiblePerson()) {
    		person.getCurrentVigilant().removeIncompatiblePerson();
    	}
        vigilant.setIncompatiblePerson(person);
    }

}