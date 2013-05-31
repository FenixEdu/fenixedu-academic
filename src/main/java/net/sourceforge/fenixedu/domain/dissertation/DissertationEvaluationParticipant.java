package net.sourceforge.fenixedu.domain.dissertation;

import dml.runtime.RelationAdapter;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;


public class DissertationEvaluationParticipant extends DissertationEvaluationParticipant_Base {
    
    public  DissertationEvaluationParticipant() {
        super();
    }
    
    /*private static KeepParticipationNumberAdapter KEEP_PARTICIPATION_NUMBER_ADAPTER = new KeepParticipationNumberAdapter();
    static {
        DissertationHasParticipations.addListener(KEEP_PARTICIPATION_NUMBER_ADAPTER);
    }

    public final static Comparator<DissertationEvaluationParticipant> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(AbstractDomainObject.COMPARATOR_BY_ID);
    }

    public final static Comparator<DissertationEvaluationParticipant> COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator(
            "thesis.student.number");

    public DissertationEvaluationParticipant(Dissertation dissertation, Person person, DissertationParticipationType type) {
        super();

        setRootDomainObject(RootDomainObject.getInstance());

        setType(type);
        setDissertation(dissertation);
        setPerson(person);
        setPersonName(person.getName());
    }

    public String getPersonNameWithLogin() {
        Person person = getPerson();

        if (person == null || person.hasExternalContract()) {
            return getPersonName() + " (Externa)";
        } else {
            return getPersonName() + " (" + person.getIstUsername() + ")";
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

        if (teacher != null && teacher.getCurrentWorkingDepartment() != null) {
            if (teacher.getCategory() == null) {
                setCategory("-");
            } else {
                setCategory(teacher.getCategory().getName().getContent());
            }
            setAffiliation(teacher.getCurrentWorkingDepartment().getRealName());
        } else {
            Employee employee = person.getEmployee();
            if (employee != null) {
                Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
                if (currentWorkingPlace != null) {
                    setAffiliation(currentWorkingPlace.getNameWithAcronym());
                }
            } else {
                ExternalContract contract = person.getExternalContract();
                if (contract != null) {
                    setAffiliation(contract.getInstitutionUnit().getName());
                }
            }
        }
    }

    public double getParticipationCredits() {
        return Dissertation.getCredits() * getCreditsDistribution() / 100;
    }*/

    /*public double getCreditsDistribution() {
        Dissertation dissertation = getDissertation();

        if (!dissertation.hasCredits()) {
            return 0.0;
        }

        DissertationParticipationType type = this.getType();

        if (type.equals(DissertationParticipationType.ORIENTATOR)) {
            /*if (dissertation.getOrientatorCreditsDistribution() != null) {
                return dissertation.getOrientatorCreditsDistribution();
            }
        }

        if (type.equals(DissertationParticipationType.COORIENTATOR)) {
            if (dissertation.getCoorientatorCreditsDistribution() != null) {
                return dissertation.getCoorientatorCreditsDistribution();
            }
        }*/

      /*  return 0.0;
    }*/

    public void delete() {
        //removeRootDomainObject();
        //removePerson();
        removeDissertation();
        

        deleteDomainObject();
    }

    /*@Override
    public void setType(DissertationParticipationType type) {
        super.setType(type);

        KEEP_PARTICIPATION_NUMBER_ADAPTER.changedType(this);
    }*/

    public static class KeepParticipationNumberAdapter extends RelationAdapter<DissertationEvaluationParticipant, Dissertation> {

        public void beforeAdd(DissertationEvaluationParticipant o1, Dissertation o2) {
            super.beforeAdd(o1, o2);

            if (o1 != null && o2 != null) {
                keepTypeCount(o1, o2);
            }
        }

        public void changedType(DissertationEvaluationParticipant participant) {
            keepTypeCount(participant, participant.getDissertation());
        }

        private void keepTypeCount(DissertationEvaluationParticipant participant, Dissertation dissertation) {
            if (dissertation == null) {
                return;
            }

            DissertationParticipationType type = participant.getType();

            if (type == null) {
                return;
            }

            if (type.isSingle()) {
                DissertationEvaluationParticipant existing = dissertation.getParticipant(type);

                if (existing != null && existing != participant) {
                    existing.delete();
                }
            }
        }
    }
}