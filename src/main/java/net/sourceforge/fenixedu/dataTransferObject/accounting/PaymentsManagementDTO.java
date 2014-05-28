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
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class PaymentsManagementDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3591155631181117718L;

    private Person person;

    private List<EntryDTO> entryDTOs;

    private DateTime paymentDate;

    private boolean differedPayment;

    private Party contributorParty;

    private String contributorNumber;

    private String contributorName;

    private boolean usingContributorParty;

    public PaymentsManagementDTO(Person person) {
        setPerson(person);
        setContributorPartySocialSecurityNumber(person.getPartySocialSecurityNumber());
        setEntryDTOs(new ArrayList<EntryDTO>());
        setUsingContributorParty(true);
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void addEntryDTO(EntryDTO entryDTO) {
        this.entryDTOs.add(entryDTO);
    }

    public void addEntryDTOs(List<EntryDTO> entryDTOs) {
        this.entryDTOs.addAll(entryDTOs);
    }

    public List<EntryDTO> getEntryDTOs() {
        return entryDTOs;
    }

    public void setEntryDTOs(List<EntryDTO> entryDTOs) {
        this.entryDTOs = entryDTOs;
    }

    public DateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean isDifferedPayment() {
        return differedPayment;
    }

    public void setDifferedPayment(boolean differedPayment) {
        this.differedPayment = differedPayment;
    }

    public List<EntryDTO> getSelectedEntries() {
        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        for (final EntryDTO each : getEntryDTOs()) {
            if (each.isSelected()) {
                result.add(each);
            }
        }
        return result;
    }

    public Money getTotalAmountToPay() {
        Money result = Money.ZERO;
        for (final EntryDTO entryDTO : getSelectedEntries()) {
            result = result.add(entryDTO.getAmountToPay());
        }
        return result;
    }

    public Party getContributorParty() {
        return (this.contributorParty != null) ? this.contributorParty : StringUtils.isEmpty(this.contributorNumber) ? null : Party
                .readByContributorNumber(this.contributorNumber);
    }

    public void setContributorParty(Party contributorParty) {
        this.contributorParty = contributorParty;
    }

    public PartySocialSecurityNumber getContributorPartySocialSecurityNumber() {
        return (this.contributorParty != null) ? this.contributorParty.getPartySocialSecurityNumber() : null;
    }

    public void setContributorPartySocialSecurityNumber(PartySocialSecurityNumber partySocialSecurityNumber) {
        this.contributorParty = (partySocialSecurityNumber != null) ? partySocialSecurityNumber.getParty() : null;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getContributorNumber() {
        return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public boolean isUsingContributorParty() {
        return usingContributorParty;
    }

    public void setUsingContributorParty(boolean usingContributorParty) {
        this.usingContributorParty = usingContributorParty;
    }

}
