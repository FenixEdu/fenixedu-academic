package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class ThesisBean implements Serializable {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<Student> student;
    
    private PersonTarget targetType;
    private DomainReference<ThesisEvaluationParticipant> target;

    private DomainReference<Degree> degree;
    private boolean internal;
    private String rawPersonName;
    private DomainReference<PersonName> personName;
    private DomainReference<UnitName> unitName;
    private String rawUnitName;
    
    private MultiLanguageString title;
    private String comment;
    
    private String mark;
    private DateTime discussion;

    public ThesisBean() {
        super();
        
        this.degree     = new DomainReference<Degree>(null);
        this.student    = new DomainReference<Student>(null);
        this.personName = new DomainReference<PersonName>(null);
        this.unitName   = new DomainReference<UnitName>(null);
        this.target     = new DomainReference<ThesisEvaluationParticipant>(null);
        
        this.internal = true;
    }

    public Degree getDegree() {
        return this.degree.getObject();
    }

    public void setDegree(Degree degree) {
        this.degree = new DomainReference<Degree>(degree);
    }

    public Student getStudent() {
        return this.student.getObject();
    }

    public void setStudent(Student student) {
        this.student = new DomainReference<Student>(student);
    }

    public PersonTarget getTargetType() {
        return this.targetType;
    }

    public void setTargetType(PersonTarget target) {
        this.targetType = target;
    }
    
    public ThesisEvaluationParticipant getTarget() {
        return this.target.getObject();
    }

    public void setTarget(ThesisEvaluationParticipant target) {
        this.target = new DomainReference<ThesisEvaluationParticipant>(target);
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

    public DateTime getDiscussion() {
        return this.discussion;
    }

    public void setDiscussion(DateTime discussion) {
        this.discussion = discussion;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
