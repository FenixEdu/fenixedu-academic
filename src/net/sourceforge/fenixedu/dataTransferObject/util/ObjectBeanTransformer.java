/*
 * Created on 2003/08/27
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import java.beans.Beans;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 *  
 */
public abstract class ObjectBeanTransformer implements Transformer {

    protected Class fromClass;

    protected Class toClass;

    protected FastHashMap processedObjectsMap = null;

    /**
     *  
     */
    public ObjectBeanTransformer(Class toClass, Class fromClass) {
        super();
        processedObjectsMap = new FastHashMap();
        processedObjectsMap.setFast(true);

        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    public Object transform(Object arg0) {
        if (Beans.isInstanceOf(arg0, fromClass)) {
            Object destinationObject = destinationObjectConstructor(arg0);

            indicateProcessed(arg0, destinationObject);

            try {
                Method[] methods = arg0.getClass().getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().startsWith("get")) {
                        Object value = methods[i].invoke(arg0, new Object[0]);

                        if (value != null) {

                            Object transformedValue = null;
                            if (value instanceof List) {
                                transformedValue = copyObject(value);
                                copyProperty(destinationObject, methods[i], List.class, transformedValue);
                            } else if (Beans.isInstanceOf(value, fromClass)) {
                                if (alreadyProcessed(value)) {
                                    transformedValue = retrieveProcessedElement(value);
                                } else {
                                    transformedValue = copyObject(value);
                                }
                                copyProperty(destinationObject, methods[i], transformedValue.getClass(),
                                        transformedValue);
                            } else {
                                copyProperty(destinationObject, methods[i], value.getClass(), value);
                            }
                        }

                    }
                }

                return destinationObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return arg0;

    }

    /**
     * @param fromObject
     * @return
     */
    protected abstract Object getHashKey(Object fromObject);

    /**
     * @param object
     * @return
     */
    protected Object retrieveProcessedElement(Object fromObject) {
        return processedObjectsMap.get(getHashKey(fromObject));
    }

    /**
     * @param object
     * @return
     */
    protected boolean alreadyProcessed(Object fromObject) {
        return processedObjectsMap.containsKey(getHashKey(fromObject));
    }

    /**
     * @param object
     */
    protected void indicateProcessed(Object fromObject, Object toObject) {
        processedObjectsMap.put(getHashKey(fromObject), toObject);
    }

    /**
     * @param dataBean
     * @param method
     * @param class1
     * @param value
     */
    protected void copyProperty(Object bean, Method method, Class class1, Object value)
            throws IllegalArgumentException, SecurityException, IllegalAccessException,
            InvocationTargetException {
        Class[] argTypes = { class1 };
        Object[] setArgs = { value };
        try {
            invokeSetMethod(bean, method, argTypes, setArgs);
        } catch (NoSuchMethodException ex) {
        }
    }

    /**
     * @param dataBean
     * @param method
     * @param argTypes
     * @param setArgs
     */
    protected void invokeSetMethod(Object bean, Method method, Class[] argTypes, Object[] setArgs)
            throws IllegalArgumentException, SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        bean.getClass().getMethod(method.getName().replaceFirst("get", "set"), argTypes).invoke(bean,
                setArgs);
    }

    public Object copyObject(Object result) {
        if (result instanceof List) {
            return CollectionUtils.collect((List) result, this);
        }
        return transform(result);

    }

    protected abstract Object destinationObjectConstructor(Object fromObject);

    protected Object objectConstructor(String classToCreate) {
        try {
            return Class.forName(classToCreate).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}