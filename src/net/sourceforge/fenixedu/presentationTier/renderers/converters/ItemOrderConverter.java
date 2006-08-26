package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.Item;

public class ItemOrderConverter extends DomainObjectKeyConverter {

    @Override
    public Object convert(Class type, Object value) {
	final Item item = (Item) super.convert(type, value);
	return item.getItemOrder();
    }  

}
