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
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranchEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GetBranchListByCandidateID {

    @Atomic
    public static List<InfoBranch> run(String candidateID) throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        List<InfoBranch> result = new ArrayList<InfoBranch>();

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);
        Collection<Branch> branches = masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getAreas();
        if (branches == null) {
            InfoBranchEditor infoBranch = new InfoBranchEditor();
            infoBranch.setName("Tronco Comum");
            result.add(infoBranch);
            return result;
        }

        for (Branch branch : branches) {
            InfoBranch infoBranch = InfoBranch.newInfoFromDomain(branch);
            result.add(infoBranch);
        }

        return result;
    }

}