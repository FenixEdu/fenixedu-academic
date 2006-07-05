package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.FloatNumberConverter;

/**
 * {@inheritDoc}
 * 
 * This renderer converts the value to a float with {@link Float#parseFloat(java.lang.String)}.
 * 
 * @author cfgi
 */
public class FloatInputRenderer extends NumberInputRenderer {

    @Override
    protected Converter getConverter() {
        return new FloatNumberConverter();
    }
    
}
