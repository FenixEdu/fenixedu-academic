package net.sourceforge.fenixedu.presentationTier.renderers.components;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

import org.apache.log4j.Logger;

public class TinyMceEditor extends HtmlTextArea {

    private static final Logger logger = Logger.getLogger(TinyMceEditor.class);

    public static final String CODE_PATH = "/javaScript/tiny_mce/tiny_mce.js";
    public static final String CONFIG_PATH = "/javaScript/tiny_mce/config/";

    private String config;
    
    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public TinyMceEditor() {
        super();
    }

    public TinyMceEditor(String config) {
        this();
 
        this.config = config;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag parentTag = new HtmlTag(null);
        
        HtmlScript script = new HtmlScript();
        script.setContentType("text/javascript");
        script.setScript(generateScript(context));
        parentTag.addChild(script.getOwnTag(context));
        
        HtmlTag tag = super.getOwnTag(context);
        parentTag.addChild(tag);
        
        return parentTag;
    }

    private CharSequence generateScript(PageContext context) {
        StringBuilder builder = new StringBuilder();
        
        Properties properties = new Properties();
        loadConfig(context, properties);
        
        properties.setProperty("mode", "exact");
        properties.setProperty("elements", getId());
        
        builder.append("tinyMCE.init({\n");
        
        int index = 1;
        for (Object key : properties.keySet()) {
            Object value = properties.getProperty((String) key);
            
            builder.append(String.valueOf(key) + ": '" + value + "'");
            if (index < properties.size()) {
                builder.append(",\n");
            }
            
            index++;
        }
        
        builder.append("\n});");
        
        return builder;
    }

    private void loadConfig(PageContext context, Properties properties) {
        String config = getConfig();
        
        if (config == null) {
            return;
        }
        
        try {
            ServletContext servletContext = context.getServletContext();
            InputStream stream = servletContext.getResourceAsStream(CONFIG_PATH + getConfig() + ".properties");
            
            if (stream == null) {
                logger.warn("could not read TinyMCE configuration named '" + getConfig() + "'");
            }
            else {
                properties.load(stream);
            }
        } catch (IOException e) {
            logger.warn("exception thrown when reading TinyMCE configuration '" + getConfig() + "'" + e);
            e.printStackTrace();
        }
    }

}
