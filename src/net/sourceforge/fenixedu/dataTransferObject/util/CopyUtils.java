/*
 * Created on Oct 27, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Luis Cruz & João Luz
 *  
 */
public class CopyUtils {

    private static List interfacesNotToCopy = null;

    static {
        interfacesNotToCopy = new ArrayList();
        interfacesNotToCopy.add(Collection.class);
        interfacesNotToCopy.add(IDomainObject.class);
    }

    public static Object copyProperties(Object destinationBean, Object sourceBean)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        //		    BeanUtils.copyProperties(destinationBean, sourceBean);
        //		    return destinationBean;
        /**
         * FIXME: The code below have some problems with introspection. To see
         * the problem try to use this code on use case Criar Candidato on
         * post-graduation office...
         * 
         * @author jpvl
         */
        PropertyDescriptor[] fields = PropertyUtils.getPropertyDescriptors(sourceBean.getClass());
        for (int i = 0; i < fields.length; i++) {
            PropertyDescriptor propertyDescriptor = fields[i];
            Class fieldClass = propertyDescriptor.getPropertyType();
            Class[] interfaces = fieldClass.getInterfaces();
            List interfacesList = Arrays.asList(interfaces);

            if (CollectionUtils.intersection(interfacesNotToCopy, interfacesList).isEmpty()
                    && !propertyDescriptor.getName().equals("class")
                    && !propertyDescriptor.getName().equals("slideName")) {
                Object value = PropertyUtils.getProperty(sourceBean, propertyDescriptor.getName());
                // FIXME: Temporary workaround, because OJB RC5 (CVS_HEAD) does
                // not generate sequence
                // numbers if idInternal is equal to 0. Check if future versions
                // of OJB have the same
                // problem.

                if ((propertyDescriptor.getName().equals("idInternal"))
                        && ((value == null) || (((Integer) value).intValue() == 0))) {
                    //lets force the destination value to be null instead of 0
                    //BeanUtils.setProperty(destinationBean,
                    // propertyDescriptor.getName(), null);
                    String propertySetMethodName = "set"
                            + StringUtils.capitalize(propertyDescriptor.getName());
                    Class[] paramTypes = { Integer.class };
                    Method propertySetMethod = destinationBean.getClass().getMethod(
                            propertySetMethodName, paramTypes);
                    Object[] args = { null };
                    propertySetMethod.invoke(destinationBean, args);
                } else {
                    BeanUtils.copyProperty(destinationBean, propertyDescriptor.getName(), value);
                }
            }
        }
        return destinationBean;
    }

    /**
     * Copies all the properties of an object to a Bean object. Converts the null property values
     * to negative pseudo-infinity instead of zero and null property strings to empty string
     */
    public static Object copyPropertiesNullConvertion(Object destinationBean, Object sourceBean)
    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		//		    BeanUtils.copyProperties(destinationBean, sourceBean);
		//		    return destinationBean;
		/**
		 * FIXME: The code below have some problems with introspection. To see
		 * the problem try to use this code on use case Criar Candidato on
		 * post-graduation office...
		 * 
		 * @author João Fialho & Rita Ferreira
		 */
		PropertyDescriptor[] fields = PropertyUtils.getPropertyDescriptors(sourceBean.getClass());
		for (int i = 0; i < fields.length; i++) {
		    PropertyDescriptor propertyDescriptor = fields[i];
		    Class fieldClass = propertyDescriptor.getPropertyType();
		    Class[] interfaces = fieldClass.getInterfaces();
		    List interfacesList = Arrays.asList(interfaces);
		
		    if (CollectionUtils.intersection(interfacesNotToCopy, interfacesList).isEmpty()
		            && !propertyDescriptor.getName().equals("class")) {
		        Object value = PropertyUtils.getProperty(sourceBean, propertyDescriptor.getName());
		        // FIXME: Temporary workaround, because OJB RC5 (CVS_HEAD) does
		        // not generate sequence
		        // numbers if idInternal is equal to 0. Check if future versions
		        // of OJB have the same
		        // problem.
		
		        if ((propertyDescriptor.getName().equals("idInternal"))
		                && ((value == null) || (((Integer) value).intValue() == 0))) {
		            //lets force the destination value to be null instead of 0
		            //BeanUtils.setProperty(destinationBean,
		            // propertyDescriptor.getName(), null);
		            String propertySetMethodName = "set"
		                    + StringUtils.capitalize(propertyDescriptor.getName());
		            Class[] paramTypes = { Integer.class };
		            Method propertySetMethod = destinationBean.getClass().getMethod(
		                    propertySetMethodName, paramTypes);
		            Object[] args = { null };
		            propertySetMethod.invoke(destinationBean, args);
		        } else {
		            
		            if((value == null) && (propertyDescriptor.getPropertyType().getSuperclass().getName().equalsIgnoreCase("java.lang.Number"))) {
			            BeanUtils.copyProperty(destinationBean, propertyDescriptor.getName(), new Integer(Integer.MIN_VALUE));

		            } else if((value == null) && (propertyDescriptor.getPropertyType().getName().equalsIgnoreCase("java.lang.String"))) {
			            BeanUtils.copyProperty(destinationBean, propertyDescriptor.getName(), new String(""));

		            } else {
		                BeanUtils.copyProperty(destinationBean, propertyDescriptor.getName(), value);
		            }
		        }
		    }
		}
		return destinationBean;
	}


}