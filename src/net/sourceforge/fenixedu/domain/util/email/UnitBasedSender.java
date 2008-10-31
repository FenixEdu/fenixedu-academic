package net.sourceforge.fenixedu.domain.util.email;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class UnitBasedSender extends UnitBasedSender_Base {

    public UnitBasedSender(final Unit unit, final String fromAddress, final Group members) {
        super();
        setUnit(unit);
	setFromAddress(fromAddress);
	setMembers(members);
    }

    @Override
    public void delete() {
	removeUnit();
	super.delete();
    }

    @Override
    public String getFromName() {
	final Unit unit = getUnit();
	return unit.getName();
    }

    @Override
    public void setFromName(final String fromName) {
	throw new Error("method.not.available.for.this.type.of.sender");
    }

    protected boolean hasRecipientWithToName(final String toName) {
	for (final Recipient recipient : super.getRecipientsSet()) {
	    if (recipient.getToName().equals(toName)) {
		return true;
	    }
	}
	return false;
    }

    private boolean hasPersistentGroup(final String toName) {
	final Unit unit = getUnit();
	for (final PersistentGroupMembers persistentGroupMembers : unit.getPersistentGroupsSet()) {
	    if (persistentGroupMembers.getName().equals(toName)) {
		return true;
	    }
	}
	return false;
    }

    private void updateRecipients() {
	final Unit unit = getUnit();
	if (unit != null) {
	    for (final PersistentGroupMembers persistentGroupMembers : unit.getPersistentGroupsSet()) {
		if (!hasRecipientWithToName(persistentGroupMembers.getName())) {
		    createRecipient(persistentGroupMembers);
		}
	    }
	    for (final Recipient recipient : super.getRecipientsSet()) {
		if (recipient.getMembers() instanceof PersistentGroup) {
		    if (!hasPersistentGroup(recipient.getToName())) {
			if (recipient.getMessagesSet().isEmpty()) {
			    recipient.delete();
			} else {
			    removeRecipients(recipient);
			}
		    }
		}
	    }
	}
    }

    @Override
    public List<Recipient> getRecipients() {
	updateRecipients();
	return super.getRecipients();
    }

    @Override
    public Set<Recipient> getRecipientsSet() {
	updateRecipients();
	return super.getRecipientsSet();
    }

    @Override
    public int getRecipientsCount() {
	updateRecipients();
	return super.getRecipientsCount();
    }

    @Override
    public Iterator<Recipient> getRecipientsIterator() {
	updateRecipients();
	return super.getRecipientsIterator();
    }

    @Service
    @Override
    public void addRecipients(final Recipient recipients) {
	super.addRecipients(recipients);
    }

    @Service
    @Override
    public void removeRecipients(final Recipient recipients) {
	super.removeRecipients(recipients);
    }

    @Service
    protected void createRecipient(final PersistentGroupMembers persistentGroupMembers) {
	addRecipients(new Recipient(null, new PersistentGroup(persistentGroupMembers)));
    }

}
