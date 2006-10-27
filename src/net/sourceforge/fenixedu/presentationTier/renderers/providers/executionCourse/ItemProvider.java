package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ItemCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public abstract class ItemProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Item self;
        Section section;
        
        if (source instanceof Item) {
            self = (Item) source;
            section = self.getSection();;
        }
        else if (source instanceof ItemCreator){
            ItemCreator creator = (ItemCreator) source;
            
            self = null;
            section = creator.getSection();
        }
        else {
            throw new RuntimeException("type not supported");
        }
        
        return provideForContext(section, self);
    }

    public abstract Object provideForContext(Section section, Item self);
    
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
