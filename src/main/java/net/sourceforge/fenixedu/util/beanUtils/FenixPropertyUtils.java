/*
 * Created on Jun 30, 2004
 *
 */
package net.sourceforge.fenixedu.util.beanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixframework.DomainObject;

/**
 * @author Luis Cruz
 * 
 */
public class FenixPropertyUtils extends PropertyUtils {

    public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        if (dest == null) {
            throw new IllegalArgumentException("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }

        PropertyDescriptor origDescriptors[] = getPropertyDescriptors(orig);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            String name = origDescriptor.getName();
            if (canBeCopied(name)) {
                if (isReadable(orig, name)) {
                    if (isWriteable(dest, name)) {
                        Object value = getSimpleProperty(orig, name);
                        if (notADomainObject(value)) {
                            setSimpleProperty(dest, name, value);
                        }
                    }
                }
            }
        }

    }

    private static boolean notADomainObject(Object value) {
        return !((value instanceof DomainObject) || value instanceof InfoObject);
    }

    public static boolean canBeCopied(String name) {
        if (name == null || name.equals("externalId") || name.equals("ackOptLock") || name.startsWith("key")) {
            return false;
        }
        return true;

    }

}