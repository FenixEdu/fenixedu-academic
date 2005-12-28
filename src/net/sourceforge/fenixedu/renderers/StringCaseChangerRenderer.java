package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlSubmitButton;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlSubmitButtonController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public class StringCaseChangerRenderer extends StringInputRenderer {
    
    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        HtmlInlineContainer container = new HtmlInlineContainer();

        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) super.createTextField(object, type);
        HtmlSubmitButton caseChangeButton = new HtmlSubmitButton("");
        HtmlSubmitButton capitalizeButton = new HtmlSubmitButton("Capitalize");
        
        nameButton(caseChangeButton, "case-button-name");
        nameButton(capitalizeButton, "capitalize-button-name");
        
        container.addChild(component);
        container.addChild(caseChangeButton);
        container.addChild(capitalizeButton);
        
        CaseChangeController controller = new CaseChangeController(component, caseChangeButton, capitalizeButton); 
        
        caseChangeButton.setController(controller);
        capitalizeButton.setController(new CapitalizeController(component));
        
        return container;
    }

    private void nameButton(HtmlSubmitButton caseChangeButton, String attribute) {
        String buttonName = (String) getInputContext().getViewState().getAttribute(attribute);
        
        if (buttonName == null) {
            getInputContext().getViewState().setAttribute(attribute, caseChangeButton.getName());
        }
        else {
            caseChangeButton.setName(buttonName);
        }
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TextFieldLayout() {

            @Override
            public void applyStyle(HtmlComponent component) {
                super.applyStyle(((HtmlInlineContainer)component).getChildren().get(0));
            }
        
            @Override
            protected void setContextSlot(HtmlComponent component, MetaSlotKey slotKey) {
                HtmlContainer container = (HtmlInlineContainer) component;
                HtmlSimpleValueComponent field = (HtmlSimpleValueComponent) container.getChildren().get(0);
                
                super.setContextSlot(field, slotKey);
            }
            
        };
    }
    
    class CaseChangeController extends HtmlSubmitButtonController {

        private HtmlSubmitButton button;
        private HtmlSubmitButton capitalize;
        private HtmlSimpleValueComponent input;
        
        public CaseChangeController(HtmlSimpleValueComponent component, HtmlSubmitButton button, HtmlSubmitButton capitalizeButton) {
            this.input = component;
            this.button = button;
            this.capitalize = capitalizeButton;
            
            setupButtons();
        }

        public boolean isUpperCase() {
            return getInputContext().getViewState().getAttribute("isUpperCase") == null ||
            ((Boolean) getInputContext().getViewState().getAttribute("isUpperCase")).booleanValue();
        }
        
        public void setUpperCase(boolean isUpperCase) {
            getInputContext().getViewState().setAttribute("isUpperCase", new Boolean(isUpperCase));
        }
        
        public void setupButtons() {
            this.button.setText(isUpperCase() ? "To Upper Case" : "To Lower Case");
            this.capitalize.setVisible(isUpperCase());
        }
        
        @Override
        protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
            String text = this.input.getValue();
            this.input.setValue(isUpperCase() ? text.toUpperCase() : text.toLowerCase());
            
            setUpperCase(!isUpperCase());
            setupButtons();
        }
    }

    class CapitalizeController extends HtmlSubmitButtonController {

        private HtmlSimpleValueComponent input;
        
        public CapitalizeController(HtmlSimpleValueComponent component) {
            this.input = component;
        }

        @Override
        protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
            String text = this.input.getValue();
            this.input.setValue(capitalize(text));
        }

        private String capitalize(String text) {
            StringBuffer buffer = new StringBuffer();
            char ch, prevCh;
            
            prevCh = ' ';
            for (int i = 0;  i < text.length();  i++) {
               ch = text.charAt(i);
               
               if (Character.isLetter(ch)  &&  !Character.isLetter(prevCh)) {
                   buffer.append(Character.toUpperCase(ch));
               }
               else {
                   buffer.append(ch);
               }
               
               prevCh = ch;
            }
            
            return buffer.toString();
        }
        
    }
}
