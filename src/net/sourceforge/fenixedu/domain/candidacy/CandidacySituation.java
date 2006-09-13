package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.IState;

import org.joda.time.DateTime;

public abstract class CandidacySituation extends CandidacySituation_Base implements IState {

    final public static Comparator<CandidacySituation> DATE_COMPARATOR = new Comparator<CandidacySituation>() {
        public int compare(CandidacySituation cs1, CandidacySituation cs2) {
            return cs1.getSituationDate().compareTo(cs2.getSituationDate());
        }
    };

    public CandidacySituation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSituationDate(new DateTime());
        setOjbConcreteClass(this.getClass().getName());
    }
    
    public abstract void nextState();

    public abstract void checkConditionsToForward();

    public abstract void nextState(String nextState);

    public abstract void checkConditionsToForward(String nextState);

    public abstract Set<String> getValidNextStates();

    public abstract CandidacySituationType getCandidacySituationType();

    public String getDescription() {
        return ResourceBundle.getBundle("resources.CandidateResources").getString(
                getCandidacySituationType().name());
    }

    public boolean canChangePersonalData() {
        return false;
    }
    
    public boolean getCanCandidacyDataBeValidated() {
        return false;
    }

    public boolean getCanGeneratePass() {
        return true;
    }
    
    public boolean getCanRegister() {
        return false;
    }
        
}
