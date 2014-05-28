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
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy {

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy() {
        super();
    }

    @Atomic
    public static Boolean run(FinalDegreeWorkGroup group, String groupProposalOID, Integer orderOfPreference) {
        check(RolePredicates.STUDENT_PREDICATE);
        GroupProposal groupProposal = FenixFramework.getDomainObject(groupProposalOID);
        if (group != null && groupProposal != null) {
            for (GroupProposal otherGroupProposal : group.getGroupProposalsSet()) {
                if (otherGroupProposal != null && !groupProposal.getExternalId().equals(otherGroupProposal.getExternalId())) {
                    int otherOrderOfPreference = otherGroupProposal.getOrderOfPreference().intValue();
                    if (orderOfPreference.intValue() <= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() > otherOrderOfPreference) {
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference + 1));
                    } else if (orderOfPreference.intValue() >= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() < otherOrderOfPreference) {
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference - 1));
                    }
                }
            }
            groupProposal.setOrderOfPreference(orderOfPreference);
        }
        return true;
    }

}