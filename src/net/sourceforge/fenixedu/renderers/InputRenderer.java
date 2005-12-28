package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.contexts.InputContext;

public abstract class InputRenderer extends Renderer {
    public InputContext getInputContext() {
        return (InputContext) getContext();
    }
}
