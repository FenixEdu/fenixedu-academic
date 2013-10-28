package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

public class ActionServletWrapper extends ActionServlet {

    private static final Logger logger = LoggerFactory.getLogger(ActionServletWrapper.class);

    private static class ServletConfigWrapper implements ServletConfig {

        private final ServletConfig servletConfig;

        public static final Map<String, String> parameterMap = new HashMap<String, String>();
        static {
            parameterMap.put("config", "/WEB-INF/conf/struts-default.xml");
            parameterMap.put("application", "resources.ApplicationResources");

            try {
                Properties properties = PropertiesManager.loadProperties("/struts-auto-mapping.properties");
                String strutFiles = properties.getProperty("struts.files");

                for (String filename : strutFiles.split(",")) {
                    String mapping = filename.replace("struts-", "").replace(".xml", "");
                    parameterMap.put("config/" + mapping, "/WEB-INF/conf/" + filename);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            parameterMap.put("config/person/functionalities", "/WEB-INF/conf/struts-functionalities.xml");
            parameterMap.put("config/manager/functionalities", "/WEB-INF/conf/struts-functionalities.xml");

            parameterMap.put("debug", "3");
            parameterMap.put("detail", "3");
            parameterMap.put("validating", "true");
        }

        public ServletConfigWrapper(final ServletConfig servletConfig) {
            this.servletConfig = servletConfig;
        }

        @Override
        public String getInitParameter(final String name) {
            final String parameter = parameterMap.get(name);
            return parameter == null ? servletConfig == null ? null : servletConfig.getInitParameter(name) : parameter;
        }

        @Override
        public Enumeration getInitParameterNames() {
            return new Enumeration() {

                private final Enumeration enumeration = servletConfig == null ? null : servletConfig.getInitParameterNames();

                private final Iterator iterator = parameterMap.keySet().iterator();

                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext() || (enumeration != null && enumeration.hasMoreElements());
                }

                @Override
                public Object nextElement() {
                    return enumeration != null && enumeration.hasMoreElements() ? enumeration.nextElement() : iterator.next();
                }

            };
        }

        @Override
        public ServletContext getServletContext() {
            return servletConfig == null ? null : servletConfig.getServletContext();
        }

        @Override
        public String getServletName() {
            return servletConfig == null ? null : servletConfig.getServletName();
        }

    }

    public static ServletConfig servletConfig = null;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        final ServletConfigWrapper servletConfigWrapper = new ServletConfigWrapper(config);
        super.init(servletConfigWrapper);
        this.servletConfig = config;
    }

    @Override
    protected ModuleConfig initModuleConfig(String prefix, String paths) throws ServletException {
        logger.info("Initializing Struts Module '{}'", prefix);
        return super.initModuleConfig(prefix, paths);
    }

}
