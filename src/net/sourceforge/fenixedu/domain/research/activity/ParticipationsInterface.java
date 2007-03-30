package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public interface ParticipationsInterface {

    public List<? extends Participation> getParticipationsFor(Party party);
    public List<? extends Participation> getParticipations();
    
}
