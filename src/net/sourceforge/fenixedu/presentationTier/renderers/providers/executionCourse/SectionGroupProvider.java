package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

import org.apache.commons.collections.list.UnmodifiableList;

import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SectionGroupProvider extends SectionProvider {

    @Override
    public Object provideForContext(Site site, Section superiorSection, Section self) {
        return UnmodifiableList.decorate(site.getContextualPermissionGroups());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
