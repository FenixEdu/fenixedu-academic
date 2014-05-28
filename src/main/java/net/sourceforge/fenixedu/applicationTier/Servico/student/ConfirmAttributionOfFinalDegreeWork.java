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
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    @Atomic
    public static Boolean run(String username, String selectedGroupProposalOID) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        GroupProposal groupProposal = FenixFramework.getDomainObject(selectedGroupProposalOID);

        if (groupProposal != null) {
            FinalDegreeWorkGroup groupAttributed = groupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher();

            if (groupAttributed == null) {
                throw new NoAttributionToConfirmException();
            }

            FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            if (group != null) {
                if (!group.getExternalId().equals(groupAttributed.getExternalId())) {
                    throw new NoAttributionToConfirmException();
                }

                Collection<GroupStudent> groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (GroupStudent groupStudent : groupStudents) {
                        if (groupStudent != null && groupStudent.getRegistration().getPerson().getUsername().equals(username)) {
                            groupStudent.setFinalDegreeWorkProposalConfirmation(groupProposal.getFinalDegreeWorkProposal());
                        }
                    }
                }
            }
        }

        return true;
    }

    public static class NoAttributionToConfirmException extends FenixServiceException {

        public NoAttributionToConfirmException() {
            super();
        }

        public NoAttributionToConfirmException(int errorType) {
            super(errorType);
        }

        public NoAttributionToConfirmException(String s) {
            super(s);
        }

        public NoAttributionToConfirmException(Throwable cause) {
            super(cause);
        }

        public NoAttributionToConfirmException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}