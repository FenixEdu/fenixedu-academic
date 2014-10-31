package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("candidate")
public class CandidateGroup extends GroupStrategy {

    @Override
    public String getPresentationName() {
        return "Candidate";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getCandidaciesSet().stream().filter(candidacy -> {
            CandidacySituationType situation = candidacy.getActiveCandidacySituationType();
            return situation != null && situation.isActive() && !situation.equals(CandidacySituationType.REGISTERED);
        }).map(candidacy -> candidacy.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && hasActiveCandidacies(user.getPerson());
    }

    private boolean hasActiveCandidacies(Person person) {
        for (Candidacy candidacy : person.getCandidaciesSet()) {
            CandidacySituationType situation = candidacy.getActiveCandidacySituationType();
            if (situation != null && situation.isActive() && !situation.equals(CandidacySituationType.REGISTERED)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
