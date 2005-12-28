package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.SchemaFactory;
import net.sourceforge.fenixedu.renderers.model.UserIdentityFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RenderersPlugin implements PlugIn {
    private static Logger logger = Logger.getLogger(RenderersPlugin.class);

    // TODO: allow per module configuration, this includes factories
    private static boolean initialized = false;
    
    private String schemas;
    
    private String config;
    
    private String metaObjectFactory;

    private String userIdentityFactory;
    
    private String schemaFactory;
    
    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getMetaObjectFactory() {
        return metaObjectFactory;
    }

    public void setMetaObjectFactory(String metaObjectFactory) {
        this.metaObjectFactory = metaObjectFactory;
    }

    public String getSchemaFactory() {
        return schemaFactory;
    }

    public void setSchemaFactory(String schemaFactory) {
        this.schemaFactory = schemaFactory;
    }

    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    public String getUserIdentityFactory() {
        return userIdentityFactory;
    }

    public void setUserIdentityFactory(String userIdentityFactory) {
        this.userIdentityFactory = userIdentityFactory;
    }

    public void destroy() {
        setConfig(null);
        setSchemas(null);
        setUserIdentityFactory(null);
        setMetaObjectFactory(null);
        
        MetaObjectFactory.setCurrentFactory(MetaObjectFactory.DEFAULT_FACTORY);
        UserIdentityFactory.setCurrentFactory(UserIdentityFactory.DEFAULT_FACTORY);
        SchemaFactory.setCurrentFactory(SchemaFactory.DEFAULT_FACTORY);
    }

    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
        if (! initialized) {
            initialized = true;
            
            initConfiguration(servlet, config);
            initSchemas(servlet, config);
            initFactories(servlet, config);
        }
        
        initProcessor(servlet, config);
    }

    private void initProcessor(ActionServlet servlet, ModuleConfig config) throws ServletException {
        String ourProcessorClassname = RenderersRequestProcessor.class.getName();
        ControllerConfig controllerConfig = config.getControllerConfig();
        String configProcessorClassname = controllerConfig.getProcessorClass();

        // Check if specified classname exist
        Class configProcessorClass;
        try {
            configProcessorClass =
                RequestUtils.applicationClass(configProcessorClassname);
                
        } catch (ClassNotFoundException ex) {
            logger.fatal("Can't set RequestProcessor: bad class name '" + configProcessorClassname + "'.");
            throw new ServletException(ex);
        }

        if (configProcessorClassname.equals(RequestProcessor.class.getName())
            || configProcessorClassname.endsWith(ourProcessorClassname)) {
            
            controllerConfig.setProcessorClass(ourProcessorClassname);
            return;
        }

        // Check if specified request processor is compatible with ours.
        Class ourProcessorClass = RenderersRequestProcessor.class;
        if (!ourProcessorClass.isAssignableFrom(configProcessorClass)) {
            logger.fatal("Specified processor is incopatible with " + RequestProcessor.class.getName());
            throw new ServletException("invalid processor was specified");
        }
    }

    private void initFactories(ActionServlet servlet, ModuleConfig config) throws ServletException {
        if (getMetaObjectFactory() != null) {
            try {
                MetaObjectFactory factory = (MetaObjectFactory) RequestUtils.applicationInstance(getMetaObjectFactory());
                MetaObjectFactory.setCurrentFactory(factory);
            } catch (Exception e) {
                throw new ServletException("Could not create meta object factory", e);
            }
        }

        if (getUserIdentityFactory() != null) {
            try {
                UserIdentityFactory factory = (UserIdentityFactory) RequestUtils.applicationInstance(getUserIdentityFactory());
                UserIdentityFactory.setCurrentFactory(factory);
            } catch (Exception e) {
                throw new ServletException("Could not create user identity factory", e);
            }
        }

        if (getSchemaFactory() != null) {
            try {
                SchemaFactory factory = (SchemaFactory) RequestUtils.applicationInstance(getSchemaFactory());
                SchemaFactory.setCurrentFactory(factory);
            } catch (Exception e) {
                throw new ServletException("Could not create user identity factory", e);
            }
        }
    }

    private void initSchemas(ActionServlet servlet, ModuleConfig config) throws ServletException {
        Element root = readConfigRootElement(servlet, "schemas", getSchemas());

        if (root != null) {
            List schemaElements = root.getChildren("schema");

            for (Iterator schemaIterator = schemaElements.iterator(); schemaIterator.hasNext();) {
                Element schemaElement = (Element) schemaIterator.next();

                String schemaName = schemaElement.getAttributeValue("name");
                String typeName = schemaElement.getAttributeValue("type");

                Class type;
                try {
                    type = Class.forName(typeName);

                    Schema schema = new Schema(schemaName, type);

                    List slotElements = schemaElement.getChildren("slot");
                    for (Iterator slotIterator = slotElements.iterator(); slotIterator.hasNext();) {
                        Element slotElement = (Element) slotIterator.next();

                        String slotName      = slotElement.getAttributeValue("name");
                        String layout        = slotElement.getAttributeValue("layout");
                        String key           = slotElement.getAttributeValue("key");
                        String slotSchema    = slotElement.getAttributeValue("schema");
                        String validatorName = slotElement.getAttributeValue("validator");
                        String defaultValue  = slotElement.getAttributeValue("default");

                        Class validator = null;
                        
                        if (validatorName != null) {
                            try {
                                validator = Class.forName(validatorName);
                            } catch (ClassNotFoundException e) {
                                logger.warn("specified validator '" + validatorName + "' does not exist");
                            }
                        }
                        
                        Properties properties = new Properties();

                        List propertyElements = slotElement.getChildren("property");
                        for (Iterator propertyIterator = propertyElements.iterator(); propertyIterator
                                .hasNext();) {
                            Element propertyElement = (Element) propertyIterator.next();

                            String name = propertyElement.getAttributeValue("name");
                            String value = propertyElement.getAttributeValue("value");

                            properties.setProperty(name, value);
                        }

                        SchemaSlotDescription slotDescription = new SchemaSlotDescription(slotName);

                        slotDescription.setLayout(layout);
                        slotDescription.setKey(key);
                        slotDescription.setProperties(properties);
                        slotDescription.setSchema(slotSchema);
                        slotDescription.setValidator(validator);
                        slotDescription.setDefaultValue(defaultValue);
                        
                        schema.addSlotDescription(slotDescription);
                    }

                    logger.info("adding new schema: " + schema.getName());
                    RenderKit.getInstance().registerSchema(schema);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initConfiguration(ActionServlet servlet, ModuleConfig config) throws ServletException {
        Element root = readConfigRootElement(servlet, "config", getConfig());

        if (root != null) {
            List renderers = root.getChildren();

            for (Iterator iter = renderers.iterator(); iter.hasNext();) {
                Element rendererElement = (Element) iter.next();

                String type = rendererElement.getAttributeValue("type");
                String layout = rendererElement.getAttributeValue("layout");
                String className = rendererElement.getAttributeValue("class");

                Properties rendererProperties = new Properties();

                List properties = rendererElement.getChildren("property");
                for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
                    Element propertyElement = (Element) iterator.next();

                    String propertyName = propertyElement.getAttributeValue("name");
                    String propertyValue = propertyElement.getAttributeValue("value");

                    rendererProperties.setProperty(propertyName, propertyValue);
                }

                try {
                    Class objectClass = Class.forName(type);
                    Class rendererClass = Class.forName(className);

                    String modeName = rendererElement.getAttributeValue("mode");
                    if (modeName == null) {
                        modeName = "output";
                    }
                    
                    RenderMode mode = RenderMode.getMode(modeName);
                    
                    logger.info("[" + modeName + "] adding new renderer: " + objectClass + "/" + layout + "/"
                            + rendererClass + "/" + rendererProperties);
                    RenderKit.getInstance().registerRenderer(mode, objectClass, layout, rendererClass,
                            rendererProperties);
                } catch (ClassNotFoundException e) {
                    logger.error("could not register new renderer: " + e);
                    e.printStackTrace();
                }
            }
        }
    }
 
    private Element readConfigRootElement(final ActionServlet servlet, String name, String configFile) throws ServletException {
        
        if (configFile != null) {
            InputStream input = servlet.getServletContext().getResourceAsStream(configFile);
            
            if (input == null) {
                input = getClass().getResourceAsStream(configFile);
            }

            if (input == null) {
                throw new ServletException("Could not load " + name + ": " + configFile);
            }
            
            try {
                SAXBuilder build = new SAXBuilder();
                build.setEntityResolver(new EntityResolver() {

                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                        if (systemId == null) {
                            return null;
                        }
                        
                        // TODO: understand the API better and use somethin less hackish
                        return new InputSource(servlet.getServletContext().getResourceAsStream(systemId.substring("file://".length())));
                    }
                    
                });
                
                Document document = build.build(input);

                return document.getRootElement();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("parameter[" + name + "] no configuration file was provided");
        }

        return null;
    }
}
