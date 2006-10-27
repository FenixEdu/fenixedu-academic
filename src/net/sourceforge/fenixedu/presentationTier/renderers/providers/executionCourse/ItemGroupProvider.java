package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import org.apache.commons.collections.list.UnmodifiableList;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ItemGroupProvider extends ItemProvider {

    @Override
    public Object provideForContext(Section section, Item self) {
        return UnmodifiableList.decorate(section.getSite().getContextualPermissionGroups());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
