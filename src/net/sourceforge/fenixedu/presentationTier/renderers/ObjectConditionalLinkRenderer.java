package net.sourceforge.fenixedu.presentationTier.renderers;

public class ObjectConditionalLinkRenderer extends ObjectLinkRenderer {

    private boolean visibleIf;

    public boolean isVisibleIf() {
        return visibleIf;
    }

    public void setVisibleIf(boolean visibleIf) {
        this.visibleIf = visibleIf;
    }

}
