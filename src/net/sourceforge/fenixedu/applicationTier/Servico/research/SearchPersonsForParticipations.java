package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.SearchObjects;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;

public class SearchPersonsForParticipations extends SearchObjects {

    @Override
    public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {
	final List<DomainObject> objects = super.run(type, value, limit, arguments);
	List personsWrapped = new ArrayList();
	
	for (Object object : objects) {
	    final Person person = (Person)object;
	    if(person!=null)
		personsWrapped.add(new PersonWrapper(person));
	}
	
        return personsWrapped;
    }
    
    public static class PersonWrapper {
        private Integer idInternal;
        private String text;

        public PersonWrapper(Person person) {
            super();
            this.idInternal = person.getIdInternal();
            this.text = buildText(person);
        }

	public Integer getIdInternal() {
            return idInternal;
        }
        
        public String getText() {
            return text;
        }
        
        private String buildText(Person person) {
            StringBuilder text = new StringBuilder();
            text.append(person.getName());
            text.append(" - ");
            
	    if (person.getExternalPerson() == null) {
		text.append((person.getUsername() != null) ? person.getUsername() : "Interno");
	    }
	    else {
		text.append((person.getExternalPerson().getInstitutionUnit()!= null) ?
			person.getExternalPerson().getInstitutionUnit().getName() : "Externo");
	    }
	    
	    return text.toString();
	}
    }
}
