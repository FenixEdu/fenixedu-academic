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
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.ReadFinalDegreeWorkProposalAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.AccessFinalDegreeWorkProposalAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposal {

    protected InfoProposal run(String finalDegreeWorkProposalOID) throws FenixServiceException {
        return InfoProposal.newInfoFromDomain(FenixFramework.<Proposal> getDomainObject(finalDegreeWorkProposalOID));
    }

    // Service Invokers migrated from Berserk

    private static final ReadFinalDegreeWorkProposal serviceInstance = new ReadFinalDegreeWorkProposal();

    @Atomic
    public static InfoProposal runReadFinalDegreeWorkProposal(String DegreeWorkProposalOID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ReadFinalDegreeWorkProposalAuthorization.instance.execute(DegreeWorkProposalOID);
            return serviceInstance.run(DegreeWorkProposalOID);
        } catch (NotAuthorizedException ex1) {
            try {
                AccessFinalDegreeWorkProposalAuthorizationFilter.instance.execute(DegreeWorkProposalOID);
                return serviceInstance.run(DegreeWorkProposalOID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}