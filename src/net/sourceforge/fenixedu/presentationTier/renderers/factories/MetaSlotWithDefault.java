package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.DefaultValues;
import net.sourceforge.fenixedu.renderers.model.MetaObject;

public class MetaSlotWithDefault extends net.sourceforge.fenixedu.renderers.model.MetaSlotWithDefault {

    public MetaSlotWithDefault(MetaObject metaObject, String name) {
        super(metaObject, name);
    }

    @Override
    protected Object createDefault(Class type, String defaultValue) {
        DefaultValues instance = FenixDefaultValues.getInstance();
        return instance.createValue(type, defaultValue);
    }

}
