package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("allAlumni")
public class AllAlumniGroup extends GroupStrategy {

    private static final long serialVersionUID = -2926898164196025354L;

    @Override
    public String getPresentationName() {
        return "All Alumni";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu
                .getInstance()
                .getStudentsSet()
                .stream()
                .filter(student -> student.getAlumni() != null
                        || student.hasAnyRegistrationInState(RegistrationStateType.CONCLUDED)
                        || student.hasAnyRegistrationInState(RegistrationStateType.STUDYPLANCONCLUDED))
                .map(student -> student.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getStudent() != null
                && (user.getPerson().getStudent().hasAnyRegistrationInState(RegistrationStateType.CONCLUDED) || user.getPerson()
                        .getStudent().hasAnyRegistrationInState(RegistrationStateType.STUDYPLANCONCLUDED));
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
