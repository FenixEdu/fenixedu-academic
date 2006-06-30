package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class CreateReceiptBean implements Serializable {

    private DomainReference<Party> party;

    private DomainReference<Contributor> contributor;

    private List<SelectableEntryBean> entries;

    public CreateReceiptBean() {

    }

    public CreateReceiptBean(Party party, Contributor contributor, List<SelectableEntryBean> entries) {
        setParty(party);
        setContributor(contributor);
        setEntries(entries);
    }

    public Contributor getContributor() {
        return (this.contributor != null) ? this.contributor.getObject() : null;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = (contributor != null) ? new DomainReference<Contributor>(contributor) : null;
    }

    public void setParty(Party party) {
        this.party = (party != null) ? new DomainReference<Party>(party) : null;
    }

    public Party getParty() {
        return (this.party != null) ? this.party.getObject() : null;
    }

    public List<SelectableEntryBean> getEntries() {
        return entries;
    }

    public void setEntries(List<SelectableEntryBean> entries) {
        this.entries = entries;
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

}
