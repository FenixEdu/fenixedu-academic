package net.sourceforge.fenixedu.domain.research.activity;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public interface ParticipationsInterface {

    public Collection<? extends Participation> getParticipationsFor(Party party);

    public Collection<? extends Participation> getParticipations();

    public void addUniqueParticipation(Participation participation);

    public boolean canBeEditedByUser(Person person);

    public boolean canBeEditedByCurrentUser();
}
