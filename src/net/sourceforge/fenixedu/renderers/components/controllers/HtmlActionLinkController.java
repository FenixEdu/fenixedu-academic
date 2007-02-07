package net.sourceforge.fenixedu.renderers.components.controllers;

import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

public abstract class HtmlActionLinkController extends HtmlController {

    @Override
    public void execute(IViewState viewState) {
	HtmlActionLink link = (HtmlActionLink) getControlledComponent();

	if (link.isActivated()) {
	    viewState.setSkipUpdate(isToSkipUpdate());

	    linkPressed(viewState, link);
	}
    }

    protected boolean isToSkipUpdate() {
	return true;
    }

    public abstract void linkPressed(IViewState viewState, HtmlActionLink link);
}
