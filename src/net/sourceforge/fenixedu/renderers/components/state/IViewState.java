package net.sourceforge.fenixedu.renderers.components.state;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public interface IViewState extends Serializable {

    public String getId();

    public boolean isPostBack();

    public void invalidate();
    
    public void setPostBack(boolean isPostBack);

    public HtmlComponent getComponent();

    public void setComponent(HtmlComponent component);

    public void setValid(boolean isValid);

    public boolean skipUpdate();

    public void setSkipUpdate(boolean skipUpdate);

    public boolean isValid();

    public void setUpdateComponentTree(boolean updateTree);

    public boolean getUpdateComponentTree();

    public void addDestination(String name, ViewDestination destination);

    public ViewDestination getDestination(String name);

    public void setInputDestination(ViewDestination destination);

    public ViewDestination getInputDestination();

    public void setCurrentDestination(String name);

    public void setCurrentDestination(ViewDestination destination);

    public ViewDestination getCurrentDestination();

    public void setMetaObject(MetaObject object);
    
    public MetaObject getMetaObject();
    
    public HttpServletRequest getRequest();

    public void setRequest(HttpServletRequest request);

    public UserIdentity getUser();

    public void setUser(UserIdentity user);

    public String getLayout();

    public void setLayout(String layout);

    public Properties getProperties();

    public void setProperties(Properties properties);

    public void setContext(PresentationContext context);

    public PresentationContext getContext();

    public Class getContextClass();

    public void setContextClass(Class contextClass);

    public void setLocalAttribute(String name, Object value);
    
    public void setAttribute(String name, Object value);

    public Object getLocalAttribute(String name);
    
    public Object getAttribute(String name);

    public void removeLocalAttribute(String name);
    
    public void removeAttribute(String name);

    public void addHiddenSlot(HiddenSlot slot);

    public List<HiddenSlot> getHiddenSlots();
    
    public List<ViewStateMessage> setMessages(List<ViewStateMessage> messages);

    public List<ViewStateMessage> getMessages();
    
    public void addMessage(ViewStateMessage message);
}