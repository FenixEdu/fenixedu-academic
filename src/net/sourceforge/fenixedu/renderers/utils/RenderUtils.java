package net.sourceforge.fenixedu.renderers.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;

public class RenderUtils {
    private static Logger logger = Logger.getLogger(RenderUtils.class);
    
    public static String RESOURCE_LABEL_PREFIX = "label";
    
    public static String getSlotLabel(Class objectType, String slotName, String key) {
        String label = null;
        
        if (key != null) {
            label = RenderUtils.getResourceString(key);
        }
    
        if (label != null) {
            return label;
        } else if (key != null) {
            logger.warn("Key specified for slot '" + slotName + "' does not exist: " + key);
        }
    
        label = RenderUtils.getResourceString(RenderUtils.RESOURCE_LABEL_PREFIX + "." + objectType.getName() + "."
                + slotName);
    
        if (label != null) {
            return label;
        }
    
        label = RenderUtils.getResourceString(RenderUtils.RESOURCE_LABEL_PREFIX + "." + slotName);
        
        if (label != null) {
            return label;
        }
        
        label = RenderUtils.getResourceString(slotName);
    
        if (label != null) {
            return label;
        }
    
        if (slotName.contains(".")) {
            label = RenderUtils.getResourceString(slotName.substring(slotName.lastIndexOf(".") + 1));
        }
    
        if (label != null) {
            return label;
        }
        
        return slotName;
    }    
   
    public static String getResourceString(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.RendererResources");

            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public static String getResourceString(String key, String ... args) {
        String text = getResourceString(key);
        
        MessageFormat format = new MessageFormat(text);
        
        return format.format(args);
    }
    
    public static void setProperties(Object target, Properties properties) {
        String propertyName = null;

        for (Object property : properties.keySet()) {
            try {
                propertyName = (String) property;

                if (PropertyUtils.isWriteable(target, propertyName)) {
                    BeanUtils
                            .copyProperty(target, propertyName, properties.getProperty(propertyName));
                }
                else {
                    logger.warn("The object " + target + " does not support property '" + propertyName + "': Not writeable!");
                }
            } catch (Exception e) {
                logger.warn("The object " + target + " does not support property '" + propertyName + "': " + e);
            } // IllegalAccessException, InvocationTargetException, NoSuchMethodException
        }
    }
    
    public static  String getModuleRelativePath(HttpServletRequest request, String path) {
        ModuleConfig module = ModuleUtils.getInstance().getModuleConfig(request);

        String returnPath;
        
        if (module != null) {
            returnPath = module.getPrefix() + path;
        }
        else {
            returnPath = path;
        }
        
        return getContextRelativePath(request, returnPath);
    }
    
    public static String getContextRelativePath(HttpServletRequest request, String path) {
        String contextPath = request.getContextPath();
        
        return contextPath + path;
    }
}
