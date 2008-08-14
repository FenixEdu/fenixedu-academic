package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.domain.DomainObject;
import pt.ist.fenixWebFramework.renderers.model.DefaultValues;

public class FenixDefaultValues extends pt.ist.fenixWebFramework.renderers.model.DefaultValues {

    public static DefaultValues getInstance() {
	if (DefaultValues.instance == null) {
	    DefaultValues.instance = new FenixDefaultValues();
	}

	return DefaultValues.instance;
    }

    public DomainObject createValue(DomainObject o, Class type, String defaultValue) {
	return null;
    }
}
