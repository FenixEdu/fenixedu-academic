package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBoxList;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class CheckBoxOptionListRendererWithPostBack extends CheckBoxOptionListRenderer {
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
                HtmlCheckBoxList checkBoxList = (HtmlCheckBoxList) layout.createComponent(object, type);

                checkBoxList.setController(new PostBackController(getDestination()));
                for (HtmlCheckBox checkBox : checkBoxList.getCheckBoxes()) {
                    checkBox.setOnClick("this.form.submit();");
                }

                return checkBoxList;
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

            ViewDestination destination = viewState.getDestination(this.destination);

            if (destination != null) {
                viewState.setCurrentDestination(destination);
            } else {
                viewState.setCurrentDestination("postBack");
            }

            viewState.setSkipValidation(true);
        }

    }
}
