package net.sourceforge.fenixedu.domain.thesis;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import dml.runtime.RelationAdapter;

public class ThesisEvaluationParticipant extends ThesisEvaluationParticipant_Base {

    static {
        ThesisHasParticipations.addListener(new KeepParticipationNumberAdapter());
    }
    
    public final static Comparator<ThesisEvaluationParticipant> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("idInternal"));
    }
    
    public ThesisEvaluationParticipant(Thesis thesis, Person person, ThesisParticipationType type) {
        super();
        
        setThesis(thesis);
        setPerson(person);
        setType(type);
    }

    @Override
    public void setPerson(Person person) {
        super.setPerson(person);
        
        Teacher teacher = person.getTeacher();
        if (teacher != null) {
            setCategory(teacher.getCategory());
            setAffiliation(teacher.getCurrentWorkingDepartment().getName());
        }
        else {
            ExternalContract contract = person.getExternalPerson();
            if (contract != null) {
                setAffiliation(contract.getInstitutionUnit().getName());
            }
        }
    }

    public void delete() {
        removePerson();
        removeThesis();
        
        deleteDomainObject();
    }
 
    public static class KeepParticipationNumberAdapter extends RelationAdapter<ThesisEvaluationParticipant, Thesis> {

        @Override
        public void beforeAdd(ThesisEvaluationParticipant o1, Thesis o2) {
            super.beforeAdd(o1, o2);
            
            if (o1 != null && o2 != null) {
                if (o1.getType().isSingle()) {
                    removeExisting(o2, o1.getType());
                }
            }
        }

        private void removeExisting(Thesis thesis, ThesisParticipationType type) {
            ThesisEvaluationParticipant participant = thesis.getParticipant(type);
            
            if (participant != null) {
                participant.delete();
            }
        }
        
    }
}
