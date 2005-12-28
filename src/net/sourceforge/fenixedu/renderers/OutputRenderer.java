package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.contexts.OutputContext;

public abstract class OutputRenderer extends Renderer {
    
    public OutputContext getOutputContext() {
        return (OutputContext) getContext();
    }
}
