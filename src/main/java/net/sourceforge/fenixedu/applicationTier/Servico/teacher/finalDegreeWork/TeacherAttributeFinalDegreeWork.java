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
/*
 * Created on 2004/04/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class TeacherAttributeFinalDegreeWork {

    @Atomic
    public static Boolean run(String selectedGroupProposalOID) throws FenixServiceException {
        final GroupProposal groupProposal = FenixFramework.getDomainObject(selectedGroupProposalOID);

        if (groupProposal != null) {
            final Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
            final FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();

            if (proposal != null && group != null) {
                final Proposal proposalAttributedToGroup = group.getProposalAttributedByTeacher();
                if (proposalAttributedToGroup != null && proposalAttributedToGroup != proposal) {
                    throw new GroupAlreadyAttributed(proposalAttributedToGroup.getProposalNumber().toString());
                }

                if (proposal.getGroupAttributedByTeacher() == null || proposal.getGroupAttributedByTeacher() != group) {
                    proposal.setGroupAttributedByTeacher(group);
                    for (GroupProposal otherGroupProposal : group.getGroupProposalsSet()) {
                        Proposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                        if (otherProposal != proposal && group == otherProposal.getGroupAttributedByTeacher()) {
                            otherProposal.setGroupAttributedByTeacher(null);
                        }
                    }
                } else {
                    proposal.setGroupAttributedByTeacher(null);
                }
            }
        }
        return Boolean.TRUE;
    }

    public static class GroupAlreadyAttributed extends FenixServiceException {

        public GroupAlreadyAttributed() {
            super();
        }

        public GroupAlreadyAttributed(int errorType) {
            super(errorType);
        }

        public GroupAlreadyAttributed(String s) {
            super(s);
        }

        public GroupAlreadyAttributed(Throwable cause) {
            super(cause);
        }

        public GroupAlreadyAttributed(String message, Throwable cause) {
            super(message, cause);
        }

    }

}