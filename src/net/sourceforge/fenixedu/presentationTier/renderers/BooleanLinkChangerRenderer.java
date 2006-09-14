package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.BooleanRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class BooleanLinkChangerRenderer extends BooleanRenderer {

    private String trueDestination;

    private String falseDestination;

    private String trueMessageKey;

    private String falseMessageKey;

    private String messageBundle;

    public String getFalseDestination() {
        return falseDestination;
    }

    public void setFalseDestination(String falseDestination) {
        this.falseDestination = falseDestination;
    }

    public String getFalseMessageKey() {
        return falseMessageKey;
    }

    public void setFalseMessageKey(String falseMessageKey) {
        this.falseMessageKey = falseMessageKey;
    }

    public String getMessageBundle() {
        return messageBundle;
    }

    public void setMessageBundle(String messageBundle) {
        this.messageBundle = messageBundle;
    }

    public String getTrueDestination() {
        return trueDestination;
    }

    public void setTrueDestination(String trueDestination) {
        this.trueDestination = trueDestination;
    }

    public String getTrueMessageKey() {
        return trueMessageKey;
    }

    public void setTrueMessageKey(String trueMessageKey) {
        this.trueMessageKey = trueMessageKey;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        final Layout layout = super.getLayout(object, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlComponent component = layout.createComponent(object, type);

                if (object == null) {
                    return component;
                }

                HtmlInlineContainer container = new HtmlInlineContainer();

                HtmlLink link = createLink((Boolean) object);

                container.addChild(component);
                container.addChild(new HtmlText("("));
                container.addChild(link);
                container.addChild(new HtmlText(")"));

                return container;
            }

            private HtmlLink createLink(Boolean value) {
                Object holder = getContext().getParentContext().getMetaObject().getObject();

                HtmlLink link = new HtmlLink();

                link.setModuleRelative(true);
                link.setUrl(RenderUtils.getFormattedProperties(getUrlPath(value), holder));

                link.setText(getLinkText(value));

                return link;
            }

            private String getLinkText(Boolean value) {
                String key = value ? getTrueMessageKey() : getFalseMessageKey();
                return RenderUtils.getResourceString(getMessageBundle(), key);
            }

            private String getUrlPath(Boolean value) {
                IViewState viewState = getContext().getViewState();

                String destinationName = value ? getTrueDestination() : getFalseDestination();
                ViewDestination destination = viewState.getDestination(destinationName);

                if (destination == null) {
                    return "";
                } else {
                    return destination.getPath();
                }
            }

        };
    }

}
