package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.contexts.OutputContext;

/**
 * The base renderer for every output renderer. 
 * 
 * @author cfgi
 */
public abstract class OutputRenderer extends Renderer {
    
    public OutputContext getOutputContext() {
        return (OutputContext) getContext();
    }
}
