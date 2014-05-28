/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class RemoveProposalFromFinalDegreeWorkStudentGroup {

    @Atomic
    public static Boolean run(final FinalDegreeWorkGroup group, String groupProposalOID) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final GroupProposal groupProposal = findGroupProposal(group, groupProposalOID);
        if (groupProposal != null) {
            if (group.getProposalAttributed() == groupProposal.getFinalDegreeWorkProposal()) {
                throw new GroupProposalAttributedException();
            } else if (group.getProposalAttributedByTeacher() == groupProposal.getFinalDegreeWorkProposal()) {
                throw new GroupProposalAttributedByTeacherException();
            }

            for (GroupProposal otherGroupProposal : group.getGroupProposalsSet()) {
                if (!groupProposal.equals(otherGroupProposal)
                        && groupProposal.getOrderOfPreference().intValue() < otherGroupProposal.getOrderOfPreference().intValue()) {
                    otherGroupProposal
                            .setOrderOfPreference(new Integer(otherGroupProposal.getOrderOfPreference().intValue() - 1));
                }
            }

            groupProposal.delete();
            return true;
        }
        return false;

    }

    private static GroupProposal findGroupProposal(final FinalDegreeWorkGroup group, final String groupProposalOID) {
        for (final GroupProposal groupProposal : group.getGroupProposalsSet()) {
            if (groupProposal.getExternalId().equals(groupProposalOID)) {
                return groupProposal;
            }
        }
        return null;
    }

    public static class GroupProposalAttributedException extends FenixServiceException {
    }

    public static class GroupProposalAttributedByTeacherException extends FenixServiceException {
    }

}