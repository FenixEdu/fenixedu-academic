package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;

public class ContactListRenderer extends AbstractContactRenderer {
    private String itemClasses;

    @Override
    protected Layout getLayout(Object unfiltered, Class type) {
	return new Layout() {
	    @Override
	    public HtmlComponent createComponent(Object unfiltered, Class type) {
		List<MetaObject> contacts = getFilteredContacts((Collection<PartyContact>) unfiltered);
		if (contacts.isEmpty())
		    return new HtmlText();
		HtmlList list = new HtmlList();
		list.setClasses(getClasses());
		for (MetaObject meta : contacts) {
		    HtmlListItem item = list.createItem();
		    item.setClasses(getItemClasses());
		    item.setBody(getValue((PartyContact) meta.getObject()));
		}
		return list;
	    }
	};
    }

    public String getItemClasses() {
	return itemClasses;
    }

    public void setItemClasses(String itemClasses) {
	this.itemClasses = itemClasses;
    }
}
