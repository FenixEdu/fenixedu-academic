package net.sourceforge.fenixedu.renderers.components.state;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class ViewStateWrapper implements IViewState {

    private IViewState viewState;
    
    private String attributesPrefix;
    
    public ViewStateWrapper(IViewState viewState, String attributesPrefix) {
        this.viewState = viewState;
        this.attributesPrefix = attributesPrefix + "/";
    }

    public Object getAttribute(String name) {
        return this.viewState.getAttribute(name);
    }

    public void removeAttribute(String name) {
        viewState.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        this.viewState.setAttribute(name, value);
    }

    public void setLocalAttribute(String name, Object value) {
        viewState.setAttribute(attributesPrefix + name, value);
    }

    public Object getLocalAttribute(String name) {
        return viewState.getAttribute(attributesPrefix + name);
    }

    public void removeLocalAttribute(String name) {
        viewState.removeAttribute(attributesPrefix + name);
    }

    public String getId() {
        return viewState.getId();
    }

    public boolean isPostBack() {
        return this.viewState.isPostBack();
    }

    public void setPostBack(boolean isPostBack) {
        viewState.setPostBack(isPostBack);
    }

    public void invalidate() {
        this.viewState.invalidate();
    }

    public HtmlComponent getComponent() {
        return viewState.getComponent();
    }

    public void setComponent(HtmlComponent component) {
        viewState.setComponent(component);
    }

    public void setValid(boolean isValid) {
        viewState.setValid(isValid);
    }

    public boolean skipUpdate() {
        return viewState.skipUpdate();
    }

    public void setSkipUpdate(boolean skipUpdate) {
        viewState.setSkipUpdate(skipUpdate);
    }

    public void setSkipValidation(boolean skipValidation) {
        this.viewState.setSkipValidation(skipValidation);
    }

    public boolean skipValidation() {
        return this.viewState.skipValidation();
    }
    
    public boolean isValid() {
        return viewState.isValid();
    }

    public void setUpdateComponentTree(boolean updateTree) {
        viewState.setUpdateComponentTree(updateTree);
    }

    public boolean getUpdateComponentTree() {
        return viewState.getUpdateComponentTree();
    }

    public void addDestination(String name, ViewDestination destination) {
        viewState.addDestination(name, destination);
    }

    public ViewDestination getDestination(String name) {
        return viewState.getDestination(name);
    }

    public void setInputDestination(ViewDestination destination) {
        viewState.setInputDestination(destination);
    }

    public ViewDestination getInputDestination() {
        return viewState.getInputDestination();
    }

    public void setCurrentDestination(String name) {
        viewState.setCurrentDestination(name);
    }

    public void setCurrentDestination(ViewDestination destination) {
        viewState.setCurrentDestination(destination);
    }

    public ViewDestination getCurrentDestination() {
        return viewState.getCurrentDestination();
    }

    public HttpServletRequest getRequest() {
        return viewState.getRequest();
    }

    public void setRequest(HttpServletRequest request) {
        viewState.setRequest(request);
    }

    public UserIdentity getUser() {
        return viewState.getUser();
    }

    public void setUser(UserIdentity user) {
        viewState.setUser(user);
    }

    public String getLayout() {
        return viewState.getLayout();
    }

    public void setLayout(String layout) {
        viewState.setLayout(layout);
    }

    public Properties getProperties() {
        return viewState.getProperties();
    }

    public void setProperties(Properties properties) {
        viewState.setProperties(properties);
    }

    public Class getContextClass() {
        return viewState.getContextClass();
    }

    public void setContextClass(Class contextClass) {
        viewState.setContextClass(contextClass);
    }

    public void setMetaObject(MetaObject object) {
        viewState.setMetaObject(object);
    }

    public MetaObject getMetaObject() {
        return viewState.getMetaObject();
    }

    public void setContext(PresentationContext context) {
        this.viewState.setContext(context);
    }

    public PresentationContext getContext() {
        return this.viewState.getContext();
    }

    public void addHiddenSlot(HiddenSlot slot) {
        this.viewState.addHiddenSlot(slot);
    }

    public List<HiddenSlot> getHiddenSlots() {
        return this.viewState.getHiddenSlots();
    }

    public void addMessage(Message message) {
        this.viewState.addMessage(message);
    }

    public List<Message> getMessages() {
        return this.viewState.getMessages();
    }

    public List<Message> setMessages(List<Message> messages) {
        return this.viewState.setMessages(messages);
    }
}
