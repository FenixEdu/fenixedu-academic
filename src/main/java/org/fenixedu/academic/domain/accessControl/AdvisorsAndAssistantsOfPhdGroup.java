/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("guidingsAndAssistants")
@Deprecated
public class AdvisorsAndAssistantsOfPhdGroup extends FenixGroup {
    private static final long serialVersionUID = -4915895868986948375L;

    @GroupArgument
    private PhdIndividualProgramProcess process;

    private AdvisorsAndAssistantsOfPhdGroup() {
        super();
    }

    private AdvisorsAndAssistantsOfPhdGroup(PhdIndividualProgramProcess process) {
        this();
        this.process = process;
    }

    public static AdvisorsAndAssistantsOfPhdGroup get(PhdIndividualProgramProcess process) {
        return new AdvisorsAndAssistantsOfPhdGroup(process);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { process.getProcessNumber() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        for (PhdParticipant participant : process.getGuidingsAndAssistantGuidings()) {
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
            if (participant.getIndividualProcess().equals(process)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentGuidingsAndAssistantsOfPhdGroup.getInstance(process);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AdvisorsAndAssistantsOfPhdGroup) {
            return Objects.equal(process, ((AdvisorsAndAssistantsOfPhdGroup) object).process);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(process);
    }
}
