package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("guidingsAndAssistants")
public class PersistentGuidingsAndAssistantsOfPhdGroup extends PersistentGuidingsAndAssistantsOfPhdGroup_Base {
    protected PersistentGuidingsAndAssistantsOfPhdGroup(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        super();
        setPhdIndividualProgramProcess(phdIndividualProgramProcess);
    }

    @CustomGroupArgument
    public static Argument<PhdIndividualProgramProcess> phdIndividualProgramProcessArgument() {
        return new SimpleArgument<PhdIndividualProgramProcess, PersistentGuidingsAndAssistantsOfPhdGroup>() {
            @Override
            public PhdIndividualProgramProcess parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework
                        .<PhdIndividualProgramProcess> getDomainObject(argument);
            }

            @Override
            public Class<? extends PhdIndividualProgramProcess> getType() {
                return PhdIndividualProgramProcess.class;
            }

            @Override
            public String extract(PersistentGuidingsAndAssistantsOfPhdGroup group) {
                return group.getPhdIndividualProgramProcess() != null ? group.getPhdIndividualProgramProcess().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getPhdIndividualProgramProcess().getProcessNumber() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        for (PhdParticipant participant : getPhdIndividualProgramProcess().getGuidingsAndAssistantGuidings()) {
            if (participant.isInternal()) {
                User user = ((InternalPhdParticipant) participant).getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        for (InternalPhdParticipant participant : user.getPerson().getInternalParticipantsSet()) {
            if (participant.getIndividualProcess().equals(getPhdIndividualProgramProcess())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentGuidingsAndAssistantsOfPhdGroup getInstance(
            final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        PersistentGuidingsAndAssistantsOfPhdGroup instance = phdIndividualProgramProcess.getGuidingsAndAssistantsGroup();
        return instance != null ? instance : create(phdIndividualProgramProcess);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentGuidingsAndAssistantsOfPhdGroup create(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        PersistentGuidingsAndAssistantsOfPhdGroup instance = phdIndividualProgramProcess.getGuidingsAndAssistantsGroup();
        return instance != null ? instance : new PersistentGuidingsAndAssistantsOfPhdGroup(phdIndividualProgramProcess);
    }
}
