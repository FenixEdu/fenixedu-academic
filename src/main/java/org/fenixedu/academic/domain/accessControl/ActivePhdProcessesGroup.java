package org.fenixedu.academic.domain.accessControl;

import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@GroupOperator("activePhdProcessesGroup")
public class ActivePhdProcessesGroup extends GroupStrategy {

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.GROUP, "label.name.ActivePhdProcessesGroup");
    }

    @Override
    public Stream<User> getMembers() {
        return Bennu.getInstance().getPhdProgramsSet().stream()
                .flatMap(program -> program.getIndividualProgramProcessesSet().stream())
                .filter(PhdIndividualProgramProcess::isProcessActive).map(PhdIndividualProgramProcess::getPerson).map
                        (Person::getUser);
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson() == null) {
            return false;
        }

        return user.getPerson().getPhdIndividualProgramProcessesSet().stream()
                .anyMatch(PhdIndividualProgramProcess::isProcessActive);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }
}
