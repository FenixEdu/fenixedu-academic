/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;

public class PaymentsManagementDTO implements Serializable {

    private DomainReference<Person> person;

    private List<EntryDTO> entryDTOs;

    private DateTime paymentDate;

    private boolean differedPayment;

    public PaymentsManagementDTO(Person person) {
        setPerson(person);
        setEntryDTOs(new ArrayList<EntryDTO>());
    }

    public Person getPerson() {
        return (this.person != null) ? this.person.getObject() : null;
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
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

    public BigDecimal getTotalAmountToPay() {
        BigDecimal result = new BigDecimal("0");
        for (final EntryDTO entryDTO : getSelectedEntries()) {
            result = result.add(entryDTO.getAmountToPay());
        }
        return result;
    }

}
