package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;

public class ItemOrderProvider extends ItemProvider {

    @Override
    public Object provideForContext(Section section, Item self) {
        Collection<Item> siblings = new ArrayList<Item>(section.getOrderedItems());
        siblings.remove(self);
        
        return siblings;
    }

}
