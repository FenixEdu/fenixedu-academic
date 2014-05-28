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
 * Created on 2004/03/13
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher {

    @Atomic
    public static List run(final Person person) throws FenixServiceException {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();
        for (final Proposal proposal : person.findFinalDegreeWorkProposals()) {
            final Scheduleing scheduleing = proposal.getScheduleing();

            for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegrees()) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

}