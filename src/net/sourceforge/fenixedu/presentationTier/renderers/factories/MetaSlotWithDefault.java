package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import pt.ist.fenixWebFramework.renderers.model.DefaultValues;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;

public class MetaSlotWithDefault extends pt.ist.fenixWebFramework.renderers.model.MetaSlotWithDefault {

    public MetaSlotWithDefault(MetaObject metaObject, String name) {
	super(metaObject, name);
    }

    @Override
    protected Object createDefault(Class type, String defaultValue) {
	DefaultValues instance = FenixDefaultValues.getInstance();
	return instance.createValue(type, defaultValue);
    }

}
