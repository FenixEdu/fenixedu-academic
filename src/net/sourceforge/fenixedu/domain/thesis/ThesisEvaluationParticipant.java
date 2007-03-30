package net.sourceforge.fenixedu.domain.thesis;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import dml.runtime.RelationAdapter;

public class ThesisEvaluationParticipant extends ThesisEvaluationParticipant_Base {

    private static KeepParticipationNumberAdapter KEEP_PARTICIPATION_NUMBER_ADAPTER = new KeepParticipationNumberAdapter();
    static {
        ThesisHasParticipations.addListener(KEEP_PARTICIPATION_NUMBER_ADAPTER);
    }
    
    public final static Comparator<ThesisEvaluationParticipant> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("idInternal"));
    }
    
    public ThesisEvaluationParticipant(Thesis thesis, Person person, ThesisParticipationType type) {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
        
        setType(type);
        setThesis(thesis);
        setPerson(person);
        setPersonName(person.getName());
    }

    public String getPersonNameWithLogin() {
        Person person = getPerson();
        
        if (person.hasExternalPerson()) {
            return getPersonName() + " (Externa)";
        }
        else {
            return getPersonName() + " (" + person.getMostImportantAlias() + ")";
        }
    }

    @Override
    public void setPerson(Person person) {
        super.setPerson(person);
        
        if (person != null) { // consider remove
            updateParticipantInformation(person);
        }
    }

    protected void updateParticipantInformation(Person person) {
        Teacher teacher = person.getTeacher();
        
        if (teacher != null) {
            setCategory(teacher.getCategory().getName().getContent());
            setAffiliation(teacher.getCurrentWorkingDepartment().getRealName());
        }
        else {
            ExternalContract contract = person.getExternalPerson();
            if (contract != null) {
                setAffiliation(contract.getInstitutionUnit().getName());
            }
            else {
                Employee employee = person.getEmployee();
                if (employee != null) {
                    Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
                    
                    if (currentWorkingPlace != null) {
                        setAffiliation(currentWorkingPlace.getNameWithAcronym());
                    }
                }
            }
        }
    }

    public void delete() {
        removePerson();
        removeThesis();
        
        deleteDomainObject();
    }
 
    @Override
    public void setType(ThesisParticipationType type) {
        super.setType(type);
        
        KEEP_PARTICIPATION_NUMBER_ADAPTER.changedType(this);
    }
    
    public static class KeepParticipationNumberAdapter extends RelationAdapter<ThesisEvaluationParticipant, Thesis> {

        @Override
        public void beforeAdd(ThesisEvaluationParticipant o1, Thesis o2) {
            super.beforeAdd(o1, o2);
            
            if (o1 != null && o2 != null) {
                keepTypeCount(o1, o2);
            }
        }

        public void changedType(ThesisEvaluationParticipant participant) {
            keepTypeCount(participant, participant.getThesis());
        }
        
        private void keepTypeCount(ThesisEvaluationParticipant participant, Thesis thesis) {
            if (thesis == null) {
                return;
            }
            
            ThesisParticipationType type = participant.getType();
            
            if (type == null) {
                return;
            }
            
            if (type.isSingle()) {
                ThesisEvaluationParticipant existing = thesis.getParticipant(type);
                
                if (existing != null && existing != participant) {
                    existing.delete();
                }
            }
        }
        
    }

}
