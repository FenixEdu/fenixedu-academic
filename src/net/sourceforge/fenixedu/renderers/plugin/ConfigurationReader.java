package net.sourceforge.fenixedu.renderers.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sourceforge.fenixedu.renderers.exceptions.NoSuchSchemaException;
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

                String schemaName         = schemaElement.getAttributeValue("name");
                String typeName           = schemaElement.getAttributeValue("type");
                String extendedSchemaName = schemaElement.getAttributeValue("extends");
                String schemaBundle       = schemaElement.getAttributeValue("bundle");

                Class type;
                try {
                    type = getClassForType(typeName);
                } catch (ClassNotFoundException e) {
                    logger.error("schema '" + schemaName + "' was defined for the undefined type '" + typeName + "'");
                    e.printStackTrace();
                    continue;
                }
                    
                Schema extendedSchema;
                try {
                    extendedSchema = RenderKit.getInstance().findSchema(extendedSchemaName);
                } catch (NoSuchSchemaException e) {
                    logger.error("schema '" + schemaName + "' cannot extend '" + extendedSchemaName + "', schema not found");
                    e.printStackTrace();
                    continue;
                }

                if (extendedSchema != null && !extendedSchema.getType().isAssignableFrom(type)) {
                    logger.warn("schema '" + schemaName + "' is defined for type '" + typeName + "' that is not a subclass of the type '" + extendedSchema.getType().getName() + "' specified in the extended schema");
                }
                
                Schema schema = new Schema(schemaName, type, extendedSchema);

                List removeElements = schemaElement.getChildren("remove");
                if (extendedSchemaName == null && removeElements.size() > 0) {
                    logger.warn("schema '" + schemaName + "' specifies slots to be removed but it does not extend a schema");
                }
                else {
                    for (Iterator removeIterator = removeElements.iterator(); removeIterator.hasNext();) {
                        Element removeElement = (Element) removeIterator.next();
                        
                        String name = removeElement.getAttributeValue("name");
        
                        SchemaSlotDescription slotDescription = schema.getSlotDescription(name);
                        if (slotDescription == null) {
                            logger.warn("schema '" + schemaName + "' specifies that slot '" + name + "' is to be removed but it is not defined in the extended schema");
                            continue;
                        }
                        
                        schema.removeSlotDescription(slotDescription);
                    }
                }
                
                List slotElements = schemaElement.getChildren("slot");
                for (Iterator slotIterator = slotElements.iterator(); slotIterator.hasNext();) {
                    Element slotElement = (Element) slotIterator.next();

                    String slotName      = slotElement.getAttributeValue("name");
                    String layout        = slotElement.getAttributeValue("layout");
                    String key           = slotElement.getAttributeValue("key");
                    String bundle        = slotElement.getAttributeValue("bundle");
                    String slotSchema    = slotElement.getAttributeValue("schema");
                    String validatorName = slotElement.getAttributeValue("validator");
                    String defaultValue  = slotElement.getAttributeValue("default");
                    String converterName = slotElement.getAttributeValue("converter");
                    String readOnlyValue = slotElement.getAttributeValue("read-only");

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
                            logger.error("in schema '" + schemaName + "': validator '" + validatorName + "' was not found");
                            e.printStackTrace();
                            continue;
                        }
                    }
                    
                    Class converter = null;
                    if (converterName != null) {
                        try {
                            converter = getClassForType(converterName);
                        } catch (ClassNotFoundException e) {
                            logger.error("in schema '" + schemaName + "': converter '" + converterName + "' was not found");
                            e.printStackTrace();
                            continue;
                        }
                    }
                    
                    boolean readOnly = readOnlyValue == null ? false : Boolean.parseBoolean(readOnlyValue);
                    
                    if (bundle == null) {
                        bundle = schemaBundle;
                    }
                    
                    SchemaSlotDescription slotDescription = new SchemaSlotDescription(slotName);

                    slotDescription.setLayout(layout);
                    slotDescription.setKey(key);
                    slotDescription.setBundle(bundle);
                    slotDescription.setProperties(properties);
                    slotDescription.setSchema(slotSchema);
                    slotDescription.setValidator(validator);
                    slotDescription.setConverter(converter);
                    slotDescription.setValidatorProperties(validatorProperties);
                    slotDescription.setDefaultValue(defaultValue);
                    slotDescription.setReadOnly(readOnly);
                    
                    schema.addSlotDescription(slotDescription);
                }

                logger.debug("adding new schema: " + schema.getName());
                RenderKit.getInstance().registerSchema(schema);
            }
        }
    }

    private Properties getPropertiesFromElement(Element element) {
        Properties properties = new Properties();

        List propertyElements = element.getChildren("property");
        for (Iterator propertyIterator = propertyElements.iterator(); propertyIterator.hasNext();) {
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
 
    private Element readConfigRootElement(final ServletContext context, String name, final String configFile) throws ServletException {
        
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
                build.setExpandEntities(true);
                build.setEntityResolver(new EntityResolver() {

                    // TODO: understand the API better and use something less hackish
                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                        if (systemId == null) {
                            return null;
                        }

                        String entityPath = systemId.substring("file://".length());
                        
                        // relative to configuration file
                        File file = new File(context.getRealPath(configFile));
                        
                        // remove home path automatically appended 
                        String currentPath = new File(System.getProperty("user.dir")).getCanonicalPath();
                        currentPath = currentPath.replace(" ", "%20");
                        
                        if (File.separatorChar != '/') {
                            currentPath = "/" + currentPath.replace(File.separatorChar, '/');
                        }
                        
                        if (currentPath != null && entityPath.startsWith(currentPath + "/")) {
                            entityPath = entityPath.substring(currentPath.length() + 1);
                        }
                        
                        File entityFile = new File(file.getParentFile(), entityPath);
                        FileInputStream fileInputStream = new FileInputStream(entityFile);
                        
                        return new InputSource(fileInputStream);
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
