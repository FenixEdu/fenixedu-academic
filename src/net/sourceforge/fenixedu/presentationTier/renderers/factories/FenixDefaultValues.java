package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.renderers.model.DefaultValues;

public class FenixDefaultValues  extends net.sourceforge.fenixedu.renderers.model.DefaultValues {
    
    @Override
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
