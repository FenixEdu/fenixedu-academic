package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Item.ItemFactoryCreator;
import net.sourceforge.fenixedu.domain.Section.SectionFactoryCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.ItemOrderConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ItemOrderProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Section section;
	if (source instanceof ItemFactoryCreator) { 
	    final ItemFactoryCreator itemFactoryCreator = (ItemFactoryCreator) source;
	    section = itemFactoryCreator.getSection();
	} else if (source instanceof Item) {
	    final Item item = (Item) source;
	    section = item.getSection();
	} else {
	    throw new Error("Unknown section type: " + source);
	}
	return section.getOrderedItems();
    }

    public Converter getConverter() {
        return new ItemOrderConverter();
    }

}
