/*
 * Created on Apr 27, 2006
 */
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public class InputMenuOptionListRendererWithPostback extends InputMenuOptionListRenderer {

    private final String HIDDEN_NAME = "postback";
    
    @Override
    public HtmlComponent render(Object object, Class type) {
        HtmlInlineContainer container = new HtmlInlineContainer();
        
        String prefix = ((MetaSlot) getInputContext().getMetaObject()).getName();
        
        HtmlHiddenField hidden = new HtmlHiddenField(prefix + HIDDEN_NAME, "");

        HtmlMenu menu = (HtmlMenu) super.render(object, type);
        menu.setOnChange("this.form." + prefix + HIDDEN_NAME + ".value='true';this.form.submit();");
        menu.setController(new PostBackController(hidden));

        container.addChild(hidden);
        container.addChild(menu);

        return container;
    }    
    
    private static class PostBackController extends HtmlController{

        private HtmlHiddenField hidden;

        public PostBackController(HtmlHiddenField hidden) {
            this.hidden = hidden;
        }

        @Override
        public void execute(IViewState viewState) {
            if(hidden.getValue() != null && hidden.getValue().length() != 0) {
                viewState.setCurrentDestination("postBack");
                viewState.setSkipValidation(true);
            }
            
        }
        
    }
}
