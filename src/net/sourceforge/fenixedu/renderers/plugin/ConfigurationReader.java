package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sourceforge.fenixedu.renderers.exceptions.NoRendererException;
import net.sourceforge.fenixedu.renderers.exceptions.NoSuchSchemaException;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import net.sourceforge.fenixedu.renderers.schemas.Signature;
import net.sourceforge.fenixedu.renderers.schemas.SignatureParameter;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

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

    public void readAll(ServletContext context) throws ServletException {
        RenderKit.reset();
        
        readConfiguration(context);
        readSchemas(context);
        
        logger.info("configuration read");
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
                String refinedSchemaName  = schemaElement.getAttributeValue("refines");
                String schemaBundle       = schemaElement.getAttributeValue("bundle");
                String constructor        = schemaElement.getAttributeValue("constructor");

                try {
                    RenderKit.getInstance().findSchema(schemaName);
                    logger.error("schema '" + schemaName + "' was already defined");
                    continue;
                }
                catch (NoSuchSchemaException e) {
                    // ok
                }
                
                Class type;
                try {
                    type = getClassForType(typeName, true);
                } catch (ClassNotFoundException e) {
                    logger.error("schema '" + schemaName + "' was defined for the undefined type '" + typeName + "'");
                    e.printStackTrace();
                    continue;
                }

                if (extendedSchemaName != null && refinedSchemaName != null) {
                    logger.error("schema '" + schemaName + "' cannot extend '" + extendedSchemaName + "' and refine '" + refinedSchemaName + "' at the same time");
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

                Schema refinedSchema;
                try {
                    refinedSchema = RenderKit.getInstance().findSchema(refinedSchemaName);
                } catch (NoSuchSchemaException e) {
                    logger.error("schema '" + schemaName + "' cannot refine '" + refinedSchemaName + "', schema not found");
                    e.printStackTrace();
                    continue;
                }
                
                if (extendedSchema != null && !extendedSchema.getType().isAssignableFrom(type)) {
                    logger.warn("schema '" + schemaName + "' is defined for type '" + typeName + "' that is not a subclass of the type '" + extendedSchema.getType().getName() + "' specified in the extended schema");
                }
                
                Schema schema;
                if (extendedSchema != null) {
                    schema = new Schema(schemaName, type, extendedSchema);
                }
                else if (refinedSchema != null) {
                    schema = refinedSchema;
                    schema.setType(type);
                }
                else {
                    schema = new Schema(schemaName, type);
                }

                List removeElements = schemaElement.getChildren("remove");
                if (extendedSchemaName == null && refinedSchema == null && removeElements.size() > 0) {
                    logger.warn("schema '" + schemaName + "' specifies slots to be removed but it does not extend or refine schema");
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

                    String slotName       = slotElement.getAttributeValue("name");
                    String layout         = slotElement.getAttributeValue("layout");
                    String key            = slotElement.getAttributeValue("key");
                    String bundle         = slotElement.getAttributeValue("bundle");
                    String slotSchema     = slotElement.getAttributeValue("schema");
                    String validatorName  = slotElement.getAttributeValue("validator");
                    String defaultValue   = slotElement.getAttributeValue("default");
                    String converterName  = slotElement.getAttributeValue("converter");
                    String readOnlyValue  = slotElement.getAttributeValue("read-only");
                    String hiddenValue    = slotElement.getAttributeValue("hidden");
                    String alwaysSetValue = slotElement.getAttributeValue("always-set");

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
                            validator = getClassForType(validatorName, true);
                        } catch (ClassNotFoundException e) {
                            logger.error("in schema '" + schemaName + "': validator '" + validatorName + "' was not found");
                            e.printStackTrace();
                            continue;
                        }
                    }
                    
                    Class converter = null;
                    if (converterName != null) {
                        try {
                            converter = getClassForType(converterName, true);
                        } catch (ClassNotFoundException e) {
                            logger.error("in schema '" + schemaName + "': converter '" + converterName + "' was not found");
                            e.printStackTrace();
                            continue;
                        }
                    }
                    
                    boolean readOnly = readOnlyValue == null ? false : Boolean.parseBoolean(readOnlyValue);
                    boolean hidden = hiddenValue == null ? false : Boolean.parseBoolean(hiddenValue);
                    
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
                    slotDescription.setHidden(hidden);
                    
                    schema.addSlotDescription(slotDescription);
                }

                Signature construtorSignature = null;
                if (constructor != null) {
                    construtorSignature = parseSignature(schema, constructor);
                    
                    if (construtorSignature != null) {
                        for (SignatureParameter parameter : construtorSignature.getParameters()) {
                            SchemaSlotDescription slotDescription = parameter.getSlotDescription();
                            
                            if (parameter.getSlotDescription() != null) {
                                slotDescription.setSetterIgnored(true);
                            }
                        }
                    }
                }

                schema.setConstructor(construtorSignature);
                
                List setterElements = schemaElement.getChildren("setter");
                
                if (! setterElements.isEmpty()) {
                    schema.getSpecialSetters().clear();
                }
                
                for (Iterator setterIterator = setterElements.iterator(); setterIterator.hasNext();) {
                    Element setterElement = (Element) setterIterator.next();

                    String signature = setterElement.getAttributeValue("signature");
                    
                    Signature setterSignature = parseSignature(schema, signature);
                    if (setterSignature != null) {
                        for (SignatureParameter parameter : setterSignature.getParameters()) {
                            parameter.getSlotDescription().setSetterIgnored(true);
                        }
                        
                        schema.addSpecialSetter(setterSignature);
                    }
                }
                
                if (refinedSchema != null) {
                    schema = new Schema(schemaName, type, refinedSchema);
                    schema.setConstructor(refinedSchema.getConstructor());
                }

                logger.debug("adding new schema: " + schema.getName());
                RenderKit.getInstance().registerSchema(schema);
            }
        }
    }

    private Signature parseSignature(Schema schema, String signature) {

        String name;
        String parameters;
        
        int indexOfStartParent = signature.indexOf("(");
        if (indexOfStartParent != -1) {
            name = signature.substring(0, indexOfStartParent).trim();
            
            int indexOfCloseParen = signature.indexOf(")", indexOfStartParent);
            
            if (indexOfCloseParen == -1) {
                logger.error("in schema " + schema.getName() + ": malformed signature '" + signature + "', missing ')'");
                return null;
            }
            
            parameters = signature.substring(indexOfStartParent + 1, indexOfCloseParen);
        }
        else {
            name = null;
            parameters = signature.trim();
        }

        Signature programmaticSignature = new Signature(name);
        if (parameters.trim().length() == 0) {
            return programmaticSignature;
        }
        
        String[] allParameters = parameters.split(",");
        for (int i = 0; i < allParameters.length; i++) {
            String singleParameter = allParameters[i].trim();
            
            String slotName;
            String typeName;
            
            int index = singleParameter.indexOf(":");
            if (index != -1) {
                slotName = singleParameter.substring(0, index).trim();
                typeName = singleParameter.substring(index + 1).trim();
            }
            else {
                slotName = singleParameter;
                typeName = null;
            }

            SchemaSlotDescription slotDescription = schema.getSlotDescription(slotName);
            if (slotDescription == null) {
                logger.error("in schema " + schema.getName() + ": malformed signature '" + signature + "', slot '" + slotName + "' is not defined");
            }
            
            Class slotType;
            
            if (typeName != null) {
                try {
                    slotType = getClassForType(typeName, false);
                } catch (ClassNotFoundException e) {
                    logger.error("in schema " + schema.getName() + ": malformed signature '" + signature + "', could not find type '" + typeName + "'");
                    return null;
                }
            }
            else {
                slotType = RendererPropertyUtils.getPropertyType(schema.getType(), slotName);
            }
            
            programmaticSignature.addParameter(slotDescription, slotType);
        }
        
        return programmaticSignature;
    }

    private Properties getPropertiesFromElement(Element element) {
        Properties properties = new Properties();

        List propertyElements = element.getChildren("property");
        for (Iterator propertyIterator = propertyElements.iterator(); propertyIterator.hasNext();) {
            Element propertyElement = (Element) propertyIterator.next();

            String name = propertyElement.getAttributeValue("name");
            String value = propertyElement.getAttributeValue("value");

            if (value == null && !propertyElement.getContent().isEmpty()) {
               value = propertyElement.getText();
            }
            
            if (value != null) {
                properties.setProperty(name, value);
            }
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
                    Class objectClass = getClassForType(type, true);
                    Class rendererClass = Class.forName(className);

                    String modeName = rendererElement.getAttributeValue("mode");
                    if (modeName == null) {
                        modeName = "output";
                    }
                    
                    RenderMode mode = RenderMode.getMode(modeName);
                    
                    if (hasRenderer(layout, objectClass, mode)) {
                        logger.warn(String.format("[%s] duplicated definition for type '%s' and layout '%s'", modeName, objectClass, layout));
                    }

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

    private boolean hasRenderer(String layout, Class objectClass, RenderMode mode) {
        try {
            return RenderKit.getInstance().getExactRendererDescription(mode, objectClass, layout) != null;
        }
        catch (NoRendererException e) {
            return false;
        }
    }

    private Class getClassForType(String type, boolean prefixedLangPackage) throws ClassNotFoundException {
        String[] primitiveTypesNames = { "void", "boolean", "byte", "short", "int", "long", "char", "float", "double" };
        Class[]  primitiveTypesClass = { Void.TYPE, Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, 
                                         Long.TYPE, Character.TYPE, Float.TYPE, Double.TYPE };
        
        for (int i = 0; i < primitiveTypesNames.length; i++) {
            if (type.equals(primitiveTypesNames[i])) {
                return primitiveTypesClass[i];
            }
        }
        
        if (! prefixedLangPackage && type.indexOf(".") == -1) {
            return Class.forName("java.lang." + type);
        }
        else {
            return Class.forName(type);
        }
    }
 
    private Element readConfigRootElement(final ServletContext context, String name, final String configFile) throws ServletException {
        
        if (configFile != null) {
            final String realPath = "file://" + context.getRealPath(configFile);

            if (realPath == null) {
                throw new ServletException("Could not load " + name + ": " + configFile);
            }

            try {
                SAXBuilder build = new SAXBuilder();
                build.setExpandEntities(true);
                build.setEntityResolver(new EntityResolver() {

                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                        if (systemId == null) {
                            return null;
                        }

                        final URL url = new URL(systemId);
                        final InputStream inputStream = url.openStream();
                        return new InputSource(inputStream);
                    }
                    
                });

                final URL url = new URL(realPath);
                final Document document = build.build(url);

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
