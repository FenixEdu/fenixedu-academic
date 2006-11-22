package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class EnumInputRendererWithPostBack extends EnumInputRenderer {
    
    private final String HIDDEN_NAME = "postback";

    private String destination;

    public String getDestination() {
        return destination;
    }

    /**
     * Allows to choose the postback destination. If this property is not
     * specified the default "postback" destination is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    
    @Override
    public HtmlComponent render(Object object, Class type) {
        HtmlInlineContainer container = new HtmlInlineContainer();

        String prefix = ((MetaSlot) getInputContext().getMetaObject()).getName();

        HtmlHiddenField hidden = new HtmlHiddenField(prefix + HIDDEN_NAME, "");

        HtmlMenu menu = (HtmlMenu) super.render(object, type);
        menu.setOnChange("this.form." + prefix + HIDDEN_NAME + ".value='true';this.form.submit();");
        menu.setController(new PostBackController(hidden, getDestination()));

        container.addChild(hidden);
        container.addChild(menu);

        return container;
    }

    private static class PostBackController extends HtmlController {

        private HtmlHiddenField hidden;

        private String destination;

        public PostBackController(HtmlHiddenField hidden, String destination) {
            this.hidden = hidden;
            this.destination = destination;
        }

        @Override
        public void execute(IViewState viewState) {
            if (hidden.getValue() != null && hidden.getValue().length() != 0) {
                String destinationName = this.destination == null ? "postback" : this.destination;
                ViewDestination destination = viewState.getDestination(destinationName);

                if (destination != null) {
                    viewState.setCurrentDestination(destination);
                } else {
                    viewState.setCurrentDestination("postBack");
                }

                viewState.setSkipValidation(true);
            }

        }

    }
}
