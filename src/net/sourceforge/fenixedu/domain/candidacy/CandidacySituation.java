package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.util.IState;

import org.joda.time.DateTime;

public abstract class CandidacySituation extends CandidacySituation_Base implements IState{
    
    public  CandidacySituation() {
        super();
        setSituationDate(new DateTime());
    }
    
    public abstract void nextState();
    
    public abstract void checkConditionsToForward();
    
}
