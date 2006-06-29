package net.sourceforge.fenixedu.renderers.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class RendererPropertyUtils {

    /**
     * Provides an alternative to the bean utils
     * {@link PropertyUtils#getPropertyDescriptor(java.lang.Object, java.lang.String)}. Nevertheless
     * only simple properties are supported.
     * 
     * TODO: cfgi, support complex properties
     */
    public static PropertyDescriptor getPropertyDescriptor(Class type, String name) {
        	PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(type);
        
        	for (int i = 0; i < descriptors.length; i++) {
        	    PropertyDescriptor descriptor = descriptors[i];
        
        	    if (descriptor.getName().equals(name)) {
        		return descriptor;
        	    }
        	}
        
        	return null;
    }

    /**
     * Provides an alternative to
     * {@link PropertyUtils#getPropertyType(java.lang.Object, java.lang.String)} were you don't need
     * an instance to get the property.
     * 
     * @return
     */
    static public Class getPropertyType(Class type, String name) {
        	String firstPart;
        	String remaining;
        
        	int index = name.indexOf(".");
        	if (index == -1) {
        	    firstPart = name;
        	    remaining = null;
        	} else {
        	    firstPart = name.substring(0, index);
        	    remaining = name.substring(index + 1);
        	}
        
        	PropertyDescriptor descriptor = getPropertyDescriptor(type, firstPart);
        	if (descriptor == null) {
        	    throw new RuntimeException("cound not find property '" + firstPart + "' in type " + type);
        	}
        
        	Class propertyType = descriptor.getPropertyType();
        	if (remaining == null) {
        	    return propertyType;
        	} else {
        	    return getPropertyType(propertyType, remaining);
        	}
    }

    private static Object getCreatedProperty(Object object, String name, boolean create)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	    InstantiationException {
        	Object property = getProperty(object, name, create);
        
        	if (property == null && create) {
        	    Class type = getPropertyType(object.getClass(), name);
        
        	    property = type.newInstance(); // ASSUMPTION: type is a complex value with a default
                                                        // constructor
        	    PropertyUtils.setProperty(object, name, property);
        	}
        
        	return property;
    }

    /**
     * Provides an alternative to
     * {@link PropertyUtils#getProperty(java.lang.Object, java.lang.String)} that throws an exception
     * for properties like <code>a.b</code> if the <code>a</code> is a null value. This method
     * allows you to choose if the value for <code>a</code> is created or if the method simply
     * returns <code>null</code>. Any exception thrown by {@link PropertyUtils} is wrapped in a
     * {@link RuntimeException}.
     * 
     * @param object
     *                the target object
     * @param name
     *                the property name
     * @param create
     *                true to create intermediary values
     * @return
     */
    static public Object getProperty(Object object, String name, boolean create) {
        	String message = "could not get property '" + name + "' for object '" + object + "'";
        
        	try {
        	    int index = name.indexOf(".");
        	    if (index == -1) {
        		return PropertyUtils.getProperty(object, name);
        	    }
        
        	    String firstPart = name.substring(0, index);
        	    String remaining = name.substring(index + 1);
        
        	    Object target = getCreatedProperty(object, firstPart, create);
        	    if (target == null) {
        		return null;
        	    } else {
        		return getProperty(target, remaining, create);
        	    }
        	} catch (IllegalAccessException e) {
        	    throw new RuntimeException(message, e);
        	} catch (InvocationTargetException e) {
        	    throw new RuntimeException(message, e);
        	} catch (NoSuchMethodException e) {
        	    throw new RuntimeException(message, e);
        	} catch (InstantiationException e) {
        	    throw new RuntimeException(message, e);
        	}
    }

    /**
     * Provides an alternative to
     * {@link PropertyUtils#setProperty(java.lang.Object, java.lang.String, java.lang.Object)} that
     * throws an exception for properties like <code>a.b</code> if the <code>a</code> is a null
     * value. This method allows you to choose if the value for <code>a</code> is created or if the
     * method simply returns without setting the value. Any exception thrown by {@link PropertyUtils}
     * is wrapped in a {@link RuntimeException}.
     * 
     */
    static public void setProperty(Object object, String name, Object value, boolean create) {
        	String message = "could not set property '" + name + "' for object '" + object
        		+ "' with value '" + value + "'";
        
        	try {
        	    int index = name.lastIndexOf(".");
        	    if (index == -1) {
        		PropertyUtils.setProperty(object, name, value);
        		return;
        	    }
        
        	    String firstPart = name.substring(0, index);
        	    String remaining = name.substring(index + 1);
        
        	    Object target = getCreatedProperty(object, firstPart, create);
        	    if (target != null) {
        		PropertyUtils.setProperty(target, remaining, value);
        	    }
        	} catch (IllegalAccessException e) {
        	    throw new RuntimeException(message, e);
        	} catch (InvocationTargetException e) {
        	    if (! (e.getCause() instanceof RuntimeException)) {
        		throw new RuntimeException(message, e.getTargetException());
        	    } else {
        		throw (RuntimeException) e.getTargetException();
        	    }
        	} catch (NoSuchMethodException e) {
        	    throw new RuntimeException(message, e);
        	} catch (InstantiationException e) {
        	    throw new RuntimeException(message, e);
        	}
    }
}
