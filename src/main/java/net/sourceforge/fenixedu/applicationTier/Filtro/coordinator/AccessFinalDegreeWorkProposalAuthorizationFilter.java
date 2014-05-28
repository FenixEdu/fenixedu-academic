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
 * Created on 2004/03/12
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class AccessFinalDegreeWorkProposalAuthorizationFilter extends DomainObjectAuthorizationFilter {

    public static final AccessFinalDegreeWorkProposalAuthorizationFilter instance =
            new AccessFinalDegreeWorkProposalAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(User id, String objectId) {
        if (objectId == null) {
            return false;
        }

        final Proposal proposal = FenixFramework.getDomainObject(objectId);
        if (proposal == null) {
            return false;
        }

        final Person person = id.getPerson();
        if (person == proposal.getOrientator() || person == proposal.getCoorientator()) {
            return true;
        }

        for (final ExecutionDegree executionDegree : proposal.getScheduleing().getExecutionDegreesSet()) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                if (coordinator != null && person == coordinator.getPerson()) {
                    return true;
                }
            }
            for (final ScientificCommission scientificCommission : person.getScientificCommissionsSet()) {
                if (executionDegree == scientificCommission.getExecutionDegree()
                        || (executionDegree.getDegreeCurricularPlan() == scientificCommission.getExecutionDegree()
                                .getDegreeCurricularPlan() && executionDegree.getExecutionYear() == scientificCommission
                                .getExecutionDegree().getExecutionYear().getPreviousExecutionYear())) {
                    return true;
                }
            }
        }

        return false;
    }
}