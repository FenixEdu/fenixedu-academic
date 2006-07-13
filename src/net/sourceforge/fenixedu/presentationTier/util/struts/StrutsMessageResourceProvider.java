package net.sourceforge.fenixedu.presentationTier.util.struts;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.util.resources.AbstractMessageResourceProvider;

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

public class StrutsMessageResourceProvider extends AbstractMessageResourceProvider {

    private Locale locale;

    private HttpServletRequest request;

    private ServletContext servletContext;

    public StrutsMessageResourceProvider(Properties properties, Locale locale,
            ServletContext servletContext, HttpServletRequest request) {
        super(properties);
        this.locale = locale;
        this.servletContext = servletContext;
        this.request = request;
    }

    public StrutsMessageResourceProvider(Locale locale, ServletContext servletContext,
            HttpServletRequest request) {
        super();
        this.locale = locale;
        this.servletContext = servletContext;
        this.request = request;
    }

    private MessageResources getMessageResources(String bundleName) {
        // Identify the current module
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(this.request,
                this.servletContext);

        // Return the requested message resources instance
        return (MessageResources) this.servletContext
                .getAttribute(bundleName + moduleConfig.getPrefix());
    }

    public String getMessage(String key, String bundle) {
        return getMessageResources(getBundleMapping(bundle)).getMessage(this.locale, key);
    }
}
