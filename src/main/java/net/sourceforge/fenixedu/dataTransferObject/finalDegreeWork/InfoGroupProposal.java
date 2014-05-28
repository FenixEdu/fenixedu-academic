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
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGroupProposal extends InfoObject {

    private final GroupProposal groupProposalDomainReference;

    public InfoGroupProposal(final GroupProposal groupProposal) {
        groupProposalDomainReference = groupProposal;
    }

    public static InfoGroupProposal newInfoFromDomain(final GroupProposal groupProposal) {
        return groupProposal == null ? null : new InfoGroupProposal(groupProposal);
    }

    private GroupProposal getGroupProposal() {
        return groupProposalDomainReference;
    }

    @Override
    public String getExternalId() {
        return getGroupProposal().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoGroupProposal && getGroupProposal() == ((InfoGroupProposal) obj).getGroupProposal();
    }

    @Override
    public int hashCode() {
        return getGroupProposal().hashCode();
    }

    @Override
    public String toString() {
        return getGroupProposal().toString();
    }

    /**
     * @return Returns the finalDegreeWorkProposal.
     */
    public InfoProposal getFinalDegreeWorkProposal() {
        return InfoProposal.newInfoFromDomain(getGroupProposal().getFinalDegreeWorkProposal());
    }

    /**
     * @return Returns the orderOfPreference.
     */
    public Integer getOrderOfPreference() {
        return getGroupProposal().getOrderOfPreference();
    }

    /**
     * @return Returns the infoGroup.
     */
    public InfoGroup getInfoGroup() {
        return InfoGroup.newInfoFromDomain(getGroupProposal().getFinalDegreeDegreeWorkGroup());
    }

}
