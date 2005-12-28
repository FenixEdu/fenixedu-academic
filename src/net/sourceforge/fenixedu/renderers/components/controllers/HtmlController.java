package net.sourceforge.fenixedu.renderers.components.controllers;

import java.io.Serializable;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

public abstract class HtmlController implements Serializable {
    private HtmlComponent controlledComponent;
    
    public HtmlComponent getControlledComponent() {
        return controlledComponent;
    }
    
    public void setControlledComponent(HtmlComponent controlledComponent) {
        this.controlledComponent = controlledComponent;
    }

    public abstract void execute(IViewState viewState);
}
