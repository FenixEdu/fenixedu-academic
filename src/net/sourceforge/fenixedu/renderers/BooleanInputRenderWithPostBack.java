package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class BooleanInputRenderWithPostBack extends BooleanInputRenderer {
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
    protected Layout getLayout(Object object, Class type) {
        final Layout layout = super.getLayout(object, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlCheckBox checkBox = (HtmlCheckBox) layout.createComponent(object, type);

                checkBox.setController(new PostBackController(getDestination()));
                checkBox.setOnChange("this.form.submit();");

                return checkBox;
            }

        };
    }

    private static class PostBackController extends HtmlController {

        private String destination;

        public PostBackController(String destination) {
            this.destination = destination;
        }

        @Override
        public void execute(IViewState viewState) {
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
