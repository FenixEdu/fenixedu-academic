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
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class CreateReceiptBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3609194839690577766L;

    private Person person;

    private Party contributorParty;

    private List<SelectableEntryBean> entries;

    private String contributorNumber;

    private Integer year;

    private String contributorName;

    private boolean usingContributorParty;

    public CreateReceiptBean() {
        setYear(new YearMonthDay().getYear());
        setUsingContributorParty(true);

    }

    public Party getContributorParty() {
        return (this.contributorParty != null) ? this.contributorParty : StringUtils.isEmpty(this.contributorNumber) ? null : Party
                .readByContributorNumber(this.contributorNumber);
    }

    public void setContributorParty(Party contributorParty) {
        this.contributorParty = contributorParty;
    }

    public boolean isUsingContributorParty() {
        return usingContributorParty;
    }

    public void setUsingContributorParty(boolean usingContributorParty) {
        this.usingContributorParty = usingContributorParty;
    }

    public PartySocialSecurityNumber getContributorPartySocialSecurityNumber() {
        return (this.contributorParty != null) ? this.contributorParty.getPartySocialSecurityNumber() : null;
    }

    public void setContributorPartySocialSecurityNumber(PartySocialSecurityNumber partySocialSecurityNumber) {
        this.contributorParty = (partySocialSecurityNumber != null) ? partySocialSecurityNumber.getParty() : null;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return this.person;
    }

    public List<SelectableEntryBean> getEntries() {
        return entries;
    }

    public void setEntries(List<SelectableEntryBean> entries) {
        this.entries = entries;
    }

    public String getContributorNumber() {
        return (StringUtils.isEmpty(contributorNumber) && person != null) ? person.getSocialSecurityNumber() : contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public List<Entry> getSelectedEntries() {
        final List<Entry> result = new ArrayList<Entry>();

        for (final SelectableEntryBean selectableEntryBean : getEntries()) {
            if (selectableEntryBean.isSelected()) {
                result.add(selectableEntryBean.getEntry());
            }
        }

        return result;
    }

    public Money getTotalAmount() {
        Money result = Money.ZERO;
        for (final Entry entry : getSelectedEntries()) {
            result = result.add(entry.getAmountWithAdjustment());
        }
        return result;
    }

}
