package net.sourceforge.fenixedu.domain.thesis;

import java.text.Collator;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ThesisEvaluationParticipant extends ThesisEvaluationParticipant_Base {

    public final static Comparator<ThesisEvaluationParticipant> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("idInternal"));
    }


    public  ThesisEvaluationParticipant(final Person person, final Thesis orientatedThesis,
	    final Thesis coorientatedThesis, final Thesis presidentThesis, final Thesis vowelThesis) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPerson(person);
        setOrientatedThesis(orientatedThesis);
        setCoorientatedThesis(coorientatedThesis);
        setPresidentThesis(presidentThesis);
        setVowelThesis(vowelThesis);
    }

    public void delete() {
	removePerson();
	removeOrientatedThesis();
	removeCoorientatedThesis();
	removePresidentThesis();
	removeVowelThesis();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
