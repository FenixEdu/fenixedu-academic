package net.sourceforge.fenixedu.renderers.components.controllers;

import net.sourceforge.fenixedu.renderers.components.HtmlSubmitButton;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

public abstract class HtmlSubmitButtonController extends HtmlController {

    public HtmlSubmitButtonController() {
        super();
    }

    @Override
    public void execute(IViewState viewState) {
        HtmlSubmitButton button = (HtmlSubmitButton) getControlledComponent();
        
        if (button.isPressed()) {
            viewState.setSkipUpdate(true);
            
            buttonPressed(viewState, button);
        }
    }

    protected abstract void buttonPressed(IViewState viewState, HtmlSubmitButton button);
}
