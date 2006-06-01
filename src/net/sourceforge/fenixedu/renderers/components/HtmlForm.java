package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlSubmitButtonController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class HtmlForm extends HtmlComponent implements Controllable {

    public static String POST = "POST";
    public static String GET  = "GET";
    
    public static String URL_ENCODED = "application/x-www-form-urlencoded";
    public static String FORM_DATA   = "multipart/form-data";
    
    private String action;
    private String method;
    private String encoding;
    private HtmlComponent body;
    private List<HtmlHiddenField> hiddenFields;
    private HtmlSubmitButton submitButton;
    private HtmlSubmitButton cancelButton;
    
    private HtmlController controller;
    
    public HtmlForm() {
        super();
        
        this.hiddenFields = new ArrayList<HtmlHiddenField>();
        this.submitButton = new HtmlSubmitButton(RenderUtils.getResourceString("renderers.form.submit.name"));
        this.cancelButton = new HtmlSubmitButton(RenderUtils.getResourceString("renderers.form.cancel.name"));
     
        this.cancelButton.setName("$form.cancel"); // HACK: ?
        this.cancelButton.setController(new HtmlSubmitButtonController() {

            @Override
            protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
                viewState.setSkipUpdate(true);
                
                ViewDestination destination = viewState.getDestination("cancel");
                
                if (destination == null) {
                    destination = viewState.getInputDestination();
                }
            
                viewState.setCurrentDestination(destination);
                viewState.invalidate();
            }
            
        });
        
        setEncoding(URL_ENCODED);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public HtmlComponent getBody() {
        return this.body;
    }
    
    public void setBody(HtmlComponent component) {
        this.body = component;
    }
    
    public List<HtmlHiddenField> getHiddenFields() {
        return this.hiddenFields;
    }
    
    public void addHiddenField(HtmlHiddenField htmlHiddenField) {
        this.hiddenFields.add(htmlHiddenField);
    }
    
    public HtmlSubmitButton getCancelButton() {
        return this.cancelButton;
    }

    public void setCancelButton(HtmlSubmitButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    public HtmlSubmitButton getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(HtmlSubmitButton submitButton) {
        this.submitButton = submitButton;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        children.addAll(getHiddenFields());
        
        if (this.body != null) {
            children.add(this.body);
        }
        
        if (this.submitButton != null) {
            children.add(this.submitButton);
        }
        
        if (this.cancelButton != null) {
            children.add(this.cancelButton);
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("form");
        
        tag.setAttribute("action", this.action);
        tag.setAttribute("method", this.method);
        tag.setAttribute("enctype", this.encoding);
        
        for (HtmlHiddenField field : this.hiddenFields) {
            tag.addChild(field.getOwnTag(context));
        }
        
        if (this.body != null) {
            tag.addChild(this.body.getOwnTag(context));
        }
        
        if (this.submitButton != null) {
            tag.addChild(this.submitButton.getOwnTag(context));
        }
        
        if (this.cancelButton != null) {
            tag.addChild(this.cancelButton.getOwnTag(context));
        }

        return tag;
    }

    public boolean hasController() {
        return controller != null;
    }

    public HtmlController getController() {
        return controller;
    }

    public void setController(HtmlController controller) {
        this.controller = controller;
        this.controller.setControlledComponent(this);
    }
}
