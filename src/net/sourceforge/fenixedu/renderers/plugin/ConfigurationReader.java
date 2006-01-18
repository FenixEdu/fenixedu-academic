package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConfigurationReader {
    private static final Logger logger = Logger.getLogger(ConfigurationReader.class);
    
    private static String schemas;
    
    private static String config;
    
    public String getSchemas() {
        return ConfigurationReader.schemas;
    }

    public void setSchemas(String schemas) {
        ConfigurationReader.schemas = schemas;
    }

    public String getConfig() {
        return ConfigurationReader.config;
    }

    public void setConfig(String config) {
        ConfigurationReader.config = config;
    }
    
    public ConfigurationReader() {
        super();
    }
    
    public ConfigurationReader(String config, String schemas) {
        super();
        
        setConfig(config);
        setSchemas(schemas);
    }

    public void resetConfiguration() {
        
    }
    
    public void readAll(ServletContext context) throws ServletException {
        readConfiguration(context);
        readSchemas(context);
    }
    
    public void readSchemas(ServletContext context) throws ServletException {
        Element root = readConfigRootElement(context, "schemas", getSchemas());

        if (root != null) {
            List schemaElements = root.getChildren("schema");

            for (Iterator schemaIterator = schemaElements.iterator(); schemaIterator.hasNext();) {
                Element schemaElement = (Element) schemaIterator.next();

                String schemaName = schemaElement.getAttributeValue("name");
                String typeName = schemaElement.getAttributeValue("type");

                Class type;
                try {
                    type = getClassForType(typeName);

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

                        Properties properties = getPropertiesFromElement(slotElement);
                        
                        Properties validatorProperties;
                        Element validatorElement = slotElement.getChild("validator");
                        if (validatorElement != null) {
                            validatorProperties = getPropertiesFromElement(validatorElement);
                            validatorName = validatorElement.getAttributeValue("class");
                        }
                        else {
                            validatorProperties = new Properties();
                        }

                        Class validator = null;
                        
                        if (validatorName != null) {
                            try {
                                validator = getClassForType(validatorName);
                            } catch (ClassNotFoundException e) {
                                logger.warn("specified validator '" + validatorName + "' does not exist");
                            }
                        }
                        
                        SchemaSlotDescription slotDescription = new SchemaSlotDescription(slotName);

                        slotDescription.setLayout(layout);
                        slotDescription.setKey(key);
                        slotDescription.setProperties(properties);
                        slotDescription.setSchema(slotSchema);
                        slotDescription.setValidator(validator);
                        slotDescription.setValidatorProperties(validatorProperties);
                        slotDescription.setDefaultValue(defaultValue);
                        
                        schema.addSlotDescription(slotDescription);
                    }

                    logger.debug("adding new schema: " + schema.getName());
                    RenderKit.getInstance().registerSchema(schema);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Properties getPropertiesFromElement(Element element) {
        Properties properties = new Properties();

        List propertyElements = element.getChildren("property");
        for (Iterator propertyIterator = propertyElements.iterator(); propertyIterator
                .hasNext();) {
            Element propertyElement = (Element) propertyIterator.next();

            String name = propertyElement.getAttributeValue("name");
            String value = propertyElement.getAttributeValue("value");

            properties.setProperty(name, value);
        }
        
        return properties;
    }

    public void readConfiguration(ServletContext context) throws ServletException {
        Element root = readConfigRootElement(context, "config", getConfig());

        if (root != null) {
            List renderers = root.getChildren();

            for (Iterator iter = renderers.iterator(); iter.hasNext();) {
                Element rendererElement = (Element) iter.next();

                String type = rendererElement.getAttributeValue("type");
                String layout = rendererElement.getAttributeValue("layout");
                String className = rendererElement.getAttributeValue("class");

                Properties rendererProperties = getPropertiesFromElement(rendererElement);

                try {
                    Class objectClass = getClassForType(type);
                    Class rendererClass = Class.forName(className);

                    String modeName = rendererElement.getAttributeValue("mode");
                    if (modeName == null) {
                        modeName = "output";
                    }
                    
                    RenderMode mode = RenderMode.getMode(modeName);
                    
                    logger.debug("[" + modeName + "] adding new renderer: " + objectClass + "/" + layout + "/"
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

    private Class getClassForType(String type) throws ClassNotFoundException {
        String[] primitiveTypesNames = { "void", "boolean", "byte", "short", "int", "long", "char", "float", "double" };
        Class[]  primitiveTypesClass = { Void.TYPE, Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, 
                                         Long.TYPE, Character.TYPE, Float.TYPE, Double.TYPE };
        
        for (int i = 0; i < primitiveTypesNames.length; i++) {
            if (type.equals(primitiveTypesNames[i])) {
                return primitiveTypesClass[i];
            }
        }
        
        return Class.forName(type);
    }
 
    private Element readConfigRootElement(final ServletContext context, String name, String configFile) throws ServletException {
        
        if (configFile != null) {
            InputStream input = context.getResourceAsStream(configFile);
            
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
                        
                        // TODO: understand the API better and use something less hackish
                        return new InputSource(context.getResourceAsStream(systemId.substring("file://".length())));
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
