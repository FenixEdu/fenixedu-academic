package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ThesisBean implements Serializable {

    public static enum PersonTarget {
        orientator,
        coorientator,
        president,
        vowel
    };
    
    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    
    private DomainReference<Student> student;
    private DomainReference<Person> orientator;
    private DomainReference<Person> coorientator;
    private DomainReference<Person> president;
    private DomainListReference<Person> vowels;

    private PersonTarget targetType;
    private DomainReference<Person> target;

    private boolean internal;
    private String rawPersonName;
    private DomainReference<PersonName> personName;
    private DomainReference<UnitName> unitName;
    private String rawUnitName;
    
    private MultiLanguageString title;
    private String comment;
    
    public ThesisBean() {
        super();
        
        this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(null);
        this.student      = new DomainReference<Student>(null);
        this.orientator   = new DomainReference<Person>(null);
        this.coorientator = new DomainReference<Person>(null);
        this.president    = new DomainReference<Person>(null);
        this.vowels       = new DomainListReference<Person>();
        this.personName   = new DomainReference<PersonName>(null);
        this.unitName     = new DomainReference<UnitName>(null);
        
        this.internal = true;
    }

    public Student getStudent() {
        return this.student.getObject();
    }

    public void setStudent(Student student) {
        this.student = new DomainReference<Student>(student);
    }

    public Person getCoorientator() {
        return this.coorientator.getObject();
    }

    public void setCoorientator(Person coorientator) {
        this.coorientator = new DomainReference<Person>(coorientator);
    }

    public Person getOrientator() {
        return this.orientator.getObject();
    }

    public void setOrientator(Person orientator) {
        this.orientator = new DomainReference<Person>(orientator);
    }

    public Person getPresident() {
        return this.president.getObject();
    }

    public void setPresident(Person president) {
        this.president = new DomainReference<Person>(president);
    }

    public List<Person> getVowels() {
        return this.vowels;
    }

    public void setVowels(List<Person> vowels) {
        this.vowels = new DomainListReference<Person>(vowels);
    }

    public PersonTarget getTargetType() {
        return this.targetType;
    }

    public void setTargetType(PersonTarget target) {
        this.targetType = target;
    }
    
    public Person getTarget() {
        return this.target.getObject();
    }

    public void setTarget(Person target) {
        this.target = new DomainReference<Person>(target);
    }

    public void changePerson(Person person) {
        switch (getTargetType()) {
        case orientator:
            setOrientator(person);
            break;
        case coorientator:
            setCoorientator(person);
            break;
        case president:
            setPresident(person);
            break;
        case vowel:
            List<Person> vowels = getVowels();
            
            Person target = getTarget();
            if (target != null) {
                if (person != null) {
                    int index = vowels.indexOf(target);
                    vowels.set(index, person);
                }
                else {
                    vowels.remove(target);
                }
            }
            else {
                if (person != null) {
                    vowels.add(person);
                }
            }
            
            break;
        }
        
        setTarget(null);
        setTargetType(null);
    }

    public Person getPerson() {
        PersonName personName = getPersonName();
        
        if (personName == null) {
            return null;
        }
        else {
            return personName.getPerson();
        }
    }
    
    public PersonName getPersonName() {
        return this.personName.getObject();
    }

    public boolean isInternal() {
        return this.internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public void setPersonName(PersonName personName) {
        this.personName = new DomainReference<PersonName>(personName);
    }

    public Unit getUnit() {
        UnitName unitName = getUnitName();
        
        if (unitName == null) {
            return null;
        }
        else {
            return unitName.getUnit();
        }
    }
    
    public UnitName getUnitName() {
        return this.unitName.getObject();
    }

    public void setUnitName(UnitName unitName) {
        this.unitName = new DomainReference<UnitName>(unitName);
    }

    public String getRawPersonName() {
        return this.rawPersonName;
    }

    public void setRawPersonName(String rawPersonName) {
        this.rawPersonName = rawPersonName;
    }

    public String getRawUnitName() {
        return this.rawUnitName;
    }

    public void setRawUnitName(String rawUnitName) {
        this.rawUnitName = rawUnitName;
    }

    public MultiLanguageString getTitle() {
        return this.title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

    public Degree getDegree() {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        if (degreeCurricularPlan == null) {
            return null;
        }
        else {
            return degreeCurricularPlan.getDegree();
        }
    }
    
}
