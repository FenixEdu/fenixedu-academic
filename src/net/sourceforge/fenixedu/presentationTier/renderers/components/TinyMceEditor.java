package net.sourceforge.fenixedu.presentationTier.renderers.components;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.log4j.Logger;

public class TinyMceEditor extends HtmlTextArea {

    private static final Logger logger = Logger.getLogger(TinyMceEditor.class);

    public static final String EDITOR_PATH = "/javaScript/tiny_mce/";
    public static final String CODE_PATH = "/javaScript/tiny_mce/tiny_mce.js";
    public static final String CONFIG_PATH = "/javaScript/tiny_mce/config/";

    private String config;
    private Integer width;
    private Integer height;
    
    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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
        
        HtmlLink link = new HtmlLink();
        link.setModuleRelative(false);
        link.setContextRelative(true);
        
        link.setUrl(TinyMceEditor.CODE_PATH);
        HtmlScript sourceScript = new HtmlScript("text/javascript", link.calculateUrl(), true);
        parentTag.addChild(sourceScript.getOwnTag(context));
        
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

        if (getHeight() != null) {
            properties.setProperty("height", getHeight().toString());
        }
        
        if (getWidth() != null) {
            properties.setProperty("width", getWidth().toString());
        }
        
        loadConfig(context, properties);
        
        properties.setProperty("mode", "exact");
        properties.setProperty("elements", getId());
        properties.setProperty("relative_urls", "false");
        properties.setProperty("remove_script_host", "false");
        properties.setProperty("convert_fonts_to_spans", "true");
        properties.setProperty("fix_list_elements", "true");
        
        Language language = LanguageUtils.getLanguage();
        properties.setProperty("language", language.toString());
        properties.setProperty("docs_language", "en"); // hardcoded because pt is not supported
        
        builder.append("tinyMCE.init({\n");
        
        int index = 1;
        for (Object key : properties.keySet()) {
            Object value = properties.getProperty((String) key);
            
            if (value.equals("true") || value.equals("false")) {
                builder.append(String.valueOf(key) + ": " + value);    
            }
            else if (((String) value).matches("\\p{Digit}+")) {
                builder.append(String.valueOf(key) + ": " + value);
            }
            else {
                builder.append(String.valueOf(key) + ": '" + value + "'");    
            }
            
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
