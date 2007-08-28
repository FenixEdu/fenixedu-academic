package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;

public class ComponentTag extends TagSupport {
    
    private static final Logger logger = Logger.getLogger(ComponentTag.class);
    
    @Override
    public int doEndTag() throws JspException {
        String id = getId();
        
        HtmlComponent component = RenderUtils.getRegisteredComponent(id);
        
        if (component == null) {
            if (LogLevel.WARN) {
                logger.warn("no component with id='" + id + "' was registered");
            }
        }
        
        try {
            component.draw(pageContext);
        } catch (IOException e) {
            throw new JspException("failed to draw component because of an I/O exception", e);
        }
        
        return EVAL_PAGE;
    }
    
}
