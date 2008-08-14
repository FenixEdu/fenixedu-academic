package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Entry;

public class SelectableEntryBean implements Serializable {

    private boolean selected;

    private DomainReference<Entry> entry;

    public SelectableEntryBean() {

    }

    public SelectableEntryBean(boolean selected, Entry entry) {
	setSelected(selected);
	setEntry(entry);
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public Entry getEntry() {
	return (this.entry != null) ? this.entry.getObject() : null;
    }

    public void setEntry(Entry entry) {
	this.entry = (entry != null) ? new DomainReference<Entry>(entry) : null;
    }

}
