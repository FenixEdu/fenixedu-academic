/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Jun 26, 2006
 */
package org.fenixedu.academic.dto.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PaymentsManagementDTO implements Serializable {

    private static final long serialVersionUID = 3591155631181117718L;

    private Person person;

    private List<EntryDTO> entryDTOs;

    private DateTime paymentDate;

    private PaymentMethod paymentMethod;

    private String paymentReference;

    public PaymentsManagementDTO(){
    }

    public PaymentsManagementDTO(Person person) {
        setPerson(person);
        setEntryDTOs(new ArrayList<>());
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

    public List<EntryDTO> getSelectedEntries() {
        return getEntryDTOs().stream().filter(EntryDTO::isSelected).collect(Collectors.toList());
    }

    public Money getSelectedTotalAmountToPay() {
        return getSelectedEntries().stream().map(EntryDTO::getAmountToPay).reduce(Money.ZERO, Money::add);
    }

    public Money getTotalAmountToPay() {
        return getEntryDTOs().stream().map(EntryDTO::getAmountToPay).reduce(Money.ZERO, Money::add);
    }

    public void select(List<String> entries) {
        getEntryDTOs().forEach(entryDTO -> entryDTO.setSelected(entries.contains(entryDTO.toString())));
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

}
