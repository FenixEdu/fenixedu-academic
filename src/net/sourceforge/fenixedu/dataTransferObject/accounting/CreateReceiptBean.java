package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;

public class CreateReceiptBean implements Serializable {

    private DomainReference<Person> person;

    private DomainReference<Contributor> contributor;

    private List<SelectableEntryBean> entries;

    private String contributorNumber;

    public CreateReceiptBean() {

    }

    public CreateReceiptBean(Person person, Contributor contributor, List<SelectableEntryBean> entries) {
        setPerson(person);
        setContributor(contributor);
        setEntries(entries);
    }

    public Contributor getContributor() {
        return (this.contributor != null) ? this.contributor.getObject() : null;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = (contributor != null) ? new DomainReference<Contributor>(contributor) : null;
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public Person getPerson() {
        return (this.person != null) ? this.person.getObject() : null;
    }

    public List<SelectableEntryBean> getEntries() {
        return entries;
    }

    public void setEntries(List<SelectableEntryBean> entries) {
        this.entries = entries;
    }

    public String getContributorNumber() {
        return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
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

    public BigDecimal getTotalAmount() {
        BigDecimal result = new BigDecimal("0");
        for (final Entry entry : getSelectedEntries()) {
            result = result.add(entry.getAmountWithAdjustment());
        }
        return result;
    }

}
